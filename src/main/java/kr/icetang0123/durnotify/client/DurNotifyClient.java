package kr.icetang0123.durnotify.client;

import com.google.gson.Gson;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class DurNotifyClient implements ClientModInitializer {
    public static final String LATEST_RELEASE;
    public static final String LATEST_BETA;
    public static final String LATEST_ALPHA;

    public static final String CURRENT_VERSION = "1.0.2";

    static {
        try {
            LATEST_RELEASE = getLatestRelease();
            LATEST_BETA = getLatestBeta();
            LATEST_ALPHA = getLatestAlpha();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static final String MODRINTH_API_ROUTE = "https://api.modrinth.com/v2/";
    private static final String MOD_SLUG = "dur-notify";
    public static final Logger LOGGER = LoggerFactory.getLogger(DurNotifyClient.class);
    @Override
    public void onInitializeClient() {
        LOGGER.info("DurNotify have been initialized");

        ClientTickEvents.END_CLIENT_TICK.register(e -> {
            if (e.world == null) return;

            assert e.player != null;
            ItemStack item = e.player.getInventory().getMainHandStack();
            ItemStack arm1 = e.player.getInventory().armor.get(0);
            ItemStack arm2 = e.player.getInventory().armor.get(1);
            ItemStack arm3 = e.player.getInventory().armor.get(2);
            ItemStack arm4 = e.player.getInventory().armor.get(3);

            NbtCompound nbt = item.getNbt();
            NbtCompound nbt1 = arm1.getNbt();
            NbtCompound nbt2 = arm2.getNbt();
            NbtCompound nbt3 = arm3.getNbt();
            NbtCompound nbt4 = arm4.getNbt();

            if (item.isDamageable() && nbt != null && (item.getItem().getMaxDamage() - item.getDamage()) <= getLowDurValue(item) && !nbt.getBoolean("notified")) {
                e.player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_PLING, SoundCategory.MASTER, 1, 2);

                e.player.sendMessage(Text.translatable("title.durnotify.warn").formatted(Formatting.RED), true);

                nbt.putBoolean("notified", true);

                item.setNbt(nbt);
            } else if (item.isDamageable() && nbt != null && (item.getItem().getMaxDamage() - item.getDamage()) > getLowDurValue(item)) {
                nbt.putBoolean("notified", false);

                item.setNbt(nbt);
            }

            if (arm1.isDamageable() && nbt1 != null && (arm1.getItem().getMaxDamage() - arm1.getDamage()) <= getLowDurValue(arm1) && !nbt1.getBoolean("notified")) {
                e.player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_PLING, SoundCategory.MASTER, 1, 2);

                e.player.sendMessage(Text.translatable("title.durnotify.warn_armor").formatted(Formatting.RED), true);

                nbt1.putBoolean("notified", true);

                arm1.setNbt(nbt1);
            } else if (arm1.isDamageable() && nbt1 != null && (arm1.getItem().getMaxDamage() - arm1.getDamage()) > getLowDurValue(arm1)) {
                nbt1.putBoolean("notified", false);

                arm1.setNbt(nbt1);
            }

            if (arm2.isDamageable() && nbt2 != null && (arm2.getItem().getMaxDamage() - arm2.getDamage()) <= getLowDurValue(arm2) && !nbt2.getBoolean("notified")) {
                e.player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_PLING, SoundCategory.MASTER, 1, 2);

                e.player.sendMessage(Text.translatable("title.durnotify.warn_armor").formatted(Formatting.RED), true);

                nbt2.putBoolean("notified", true);

                arm2.setNbt(nbt2);
            } else if (arm2.isDamageable() && nbt2 != null &&(arm2.getItem().getMaxDamage() - arm2.getDamage()) > getLowDurValue(arm2)) {
                nbt2.putBoolean("notified", false);

                arm2.setNbt(nbt2);
            }

            if (arm3.isDamageable() && nbt3 != null && (arm3.getItem().getMaxDamage() - arm3.getDamage()) <= getLowDurValue(arm3) && !nbt3.getBoolean("notified")) {
                e.player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_PLING, SoundCategory.MASTER, 1, 2);

                e.player.sendMessage(Text.translatable("title.durnotify.warn_armor").formatted(Formatting.RED), true);

                nbt3.putBoolean("notified", true);

                arm3.setNbt(nbt3);
            } else if (arm3.isDamageable() && nbt3 != null && (arm3.getItem().getMaxDamage() - arm3.getDamage()) > getLowDurValue(arm3)) {
                nbt3.putBoolean("notified", false);

                arm3.setNbt(nbt3);
            }

            if (arm4.isDamageable() && nbt4 != null && (arm4.getItem().getMaxDamage() - arm4.getDamage()) <= getLowDurValue(arm4) && !nbt4.getBoolean("notified")) {
                e.player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_PLING, SoundCategory.MASTER, 1, 2);

                e.player.sendMessage(Text.translatable("title.durnotify.warn_armor").formatted(Formatting.RED), true);

                nbt4.putBoolean("notified", true);

                arm4.setNbt(nbt4);
            } else if (arm4.isDamageable() && nbt4 != null && (arm4.getItem().getMaxDamage() - arm4.getDamage()) > getLowDurValue(arm4)) {
                nbt4.putBoolean("notified", false);

                arm4.setNbt(nbt4);
            }
        });
    }

    private int getLowDurValue(ItemStack stack) {
        int maxDamage = stack.getItem().getMaxDamage();

        if (maxDamage < 50) return 15;
        if (maxDamage < 100) return 50;
        if (maxDamage < 200) return 100;
        else return 150;
    }

    private static String getLatestRelease() throws IOException {
        URL url = new URL(MODRINTH_API_ROUTE + "project/" + MOD_SLUG + "/version");

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
                    if (version.get("version_type").equals("release")) return (String) version.get("version_number");
                }
            }
        }

        return null;
    }

    private static String getLatestBeta() throws IOException {
        URL url = new URL(MODRINTH_API_ROUTE + "project/" + MOD_SLUG + "/version");

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
                    if (version.get("version_type").equals("beta")) return (String) version.get("version_number");
                }
            }
        }

        return null;
    }

    private static String getLatestAlpha() throws IOException {
        URL url = new URL(MODRINTH_API_ROUTE + "project/" + MOD_SLUG + "/version");

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
                    if (version.get("version_type").equals("alpha")) return (String) version.get("version_number");
                }
            }
        }

        return null;
    }
}