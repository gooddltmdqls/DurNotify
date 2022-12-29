package kr.icetang0123.durnotify.client;

import com.google.gson.Gson;
import kr.icetang0123.durnotify.client.config.ConfigSerializer;
import kr.icetang0123.durnotify.client.utils.Util;
import kr.icetang0123.durnotify.client.utils.Version;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class DurNotifyClient implements ClientModInitializer {
    public static final String LATEST_RELEASE;

    public static final String CURRENT_VERSION = "1.1.0";

    static {
        try {
            LATEST_RELEASE = getLatestRelease();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static final String MODRINTH_API_ROUTE = "https://api.modrinth.com/v2/";
    private static final String MOD_SLUG = "dur-notify";
    public static final Logger LOGGER = LoggerFactory.getLogger(DurNotifyClient.class);

    public final ConfigSerializer serializer = new ConfigSerializer();

    @Override
    public void onInitializeClient() {
        final boolean[] updateNotified = {false};

        LOGGER.info("DurNotify have been initialized");

        ClientTickEvents.END_CLIENT_TICK.register(e -> {
            if (e.world == null) return;

            assert e.player != null;
            ItemStack item = e.player.getInventory().getMainHandStack();
            List<ItemStack> armor = e.player.getInventory().armor;
            Map<String, Object> config;

            String latestRelease = DurNotifyClient.LATEST_RELEASE;

            if (latestRelease != null && !updateNotified[0] && (new Version(DurNotifyClient.CURRENT_VERSION).compareTo(new Version(latestRelease))) < 0) {
                e.player.sendMessage(Text.translatable("title.durnotify.new_ver", Text.of("https://modrinth.com/project/dur-notify").copy().setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://modrinth.com/project/dur-notify")).withUnderline(true))));

                updateNotified[0] = true;
            }

            NbtCompound nbt = item.getNbt();

            if (item.isDamageable() && nbt != null && (item.getItem().getMaxDamage() - item.getDamage()) <= getLowDurValue(item) && !nbt.getBoolean("notified")) {
                config = serializer.load();

                e.player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_PLING.value(), SoundCategory.MASTER, 1, 2);

                e.player.sendMessage(Text.of((String) config.get("warn_message")).copy().formatted(Formatting.RED), (Boolean) config.get("warn_into_actionbar"));

                nbt.putBoolean("notified", true);

                item.setNbt(nbt);
            } else if (item.isDamageable() && nbt != null && (item.getItem().getMaxDamage() - item.getDamage()) > getLowDurValue(item)) {
                nbt.putBoolean("notified", false);

                item.setNbt(nbt);
            }

            boolean notified = false;

            for (ItemStack itemStack : armor) {
                NbtCompound nbtArm = itemStack.getNbt();

                if (!itemStack.isEmpty() && itemStack.isDamageable() && nbtArm != null && (itemStack.getItem().getMaxDamage() - itemStack.getDamage()) <= getLowDurValue(itemStack) && !nbtArm.getBoolean("notified")) {
                    if (!notified) {
                        config = serializer.load();

                        e.player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_PLING.value(), SoundCategory.MASTER, 1, 2);

                        e.player.sendMessage(Text.of((String) config.get("warn_armor_message")).copy().formatted(Formatting.RED), (Boolean) config.get("warn_into_actionbar"));
                    }

                    nbtArm.putBoolean("notified", true);

                    itemStack.setNbt(nbtArm);

                    notified = true;
                } else if (itemStack.isDamageable() && nbtArm != null && (itemStack.getItem().getMaxDamage() - itemStack.getDamage()) > getLowDurValue(itemStack)) {
                    nbtArm.putBoolean("notified", false);

                    itemStack.setNbt(nbtArm);
                }
            }
        });
    }

    private int getLowDurValue(ItemStack stack) {
        Map<String, Object> config = serializer.load();
        int maxDamage = stack.getItem().getMaxDamage();

        if (maxDamage < 50) return Util.integerOrDouble(config.get("warn_max_under_50"));

        else if (maxDamage < 100) return Util.integerOrDouble(config.get("warn_max_under_100"));

        else if (maxDamage < 200) return Util.integerOrDouble(config.get("warn_max_under_200"));

        else return Util.integerOrDouble(config.get("warn_max_above_200"));
    }

    private static String getLatestRelease() throws IOException {
        URL url = new URL(MODRINTH_API_ROUTE + "project/" + MOD_SLUG + "/version");
        String releaseType;

        releaseType = "release";

        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);

        BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;

        while((line = br.readLine()) != null) {
            sb.append(line);
        }

        if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
            Gson gson = new Gson();

             java.util.List map = gson.fromJson(sb.toString(), java.util.List.class);

            for (Object ver : map) {
                if (ver instanceof Map version) {
                    if (version.get("version_type").equals(releaseType)) return (String) version.get("version_number");
                }
            }
        }

        return null;
    }
}