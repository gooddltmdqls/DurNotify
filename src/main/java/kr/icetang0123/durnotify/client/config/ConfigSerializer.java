package kr.icetang0123.durnotify.client.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.client.MinecraftClient;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class ConfigSerializer {
    public Map<String, Object> load() {
        try {
            File configFile = new File(Paths.get(MinecraftClient.getInstance().runDirectory.getAbsolutePath(), "config", "dur_notify.json").toUri());
            Gson gson = new Gson();

            if (!configFile.exists()) {
                if (configFile.createNewFile())
                    Files.writeString(configFile.toPath(), gson.toJson(getDefaultConfig()));
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(configFile), StandardCharsets.UTF_8));
            StringBuilder file = new StringBuilder();

            String line = null;
            do {
                if (line == null) continue;
                file.append("\n");
                file.append(line);
            } while ((line = br.readLine()) != null);

            br.close();

            return gson.fromJson(file.toString(), Map.class);
        } catch (IOException ignored) {}

        return getDefaultConfig();
    }

    public void save(ModMenuIntegration values) {
        Map<String, Object> file = new HashMap<>();
        GsonBuilder gson = new GsonBuilder();

        file.put("warn_message", values.warnMessageValue.get());
        file.put("warn_armor_message", values.warnArmorMessageValue.get());
        file.put("warn_into_actionbar", values.warnActionBarValue.get());
        file.put("warn_max_under_50", values.warnMaxUnder50.get());
        file.put("warn_max_under_100", values.warnMaxUnder100.get());
        file.put("warn_max_under_200", values.warnMaxUnder200.get());
        file.put("warn_max_above_200", values.warnMaxAbove200.get());

        try {
            BufferedWriter br = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(Paths.get(MinecraftClient.getInstance().runDirectory.getAbsolutePath(), "config", "dur_notify.json").toUri())), StandardCharsets.UTF_8));

            br.write(gson.setPrettyPrinting().create().toJson(file).toCharArray());

            br.close();
        } catch (IOException ignored) {}
    }

    private Map<String, Object> getDefaultConfig() {
        Map<String, Object> config = new java.util.HashMap<>(Map.of());

        config.put("warn_message", "Low dur!");
        config.put("warn_armor_message", "Low armor dur!");
        config.put("warn_into_actionbar", true);
        config.put("warn_max_under_50", 15);
        config.put("warn_max_under_100", 50);
        config.put("warn_max_under_200", 65);
        config.put("warn_max_above_200", 75);

        return config;
    }
}
