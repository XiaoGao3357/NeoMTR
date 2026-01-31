package com.lx862.mtrotp.config;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lx862.mtrotp.MTROTPClient;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;

public class ServerConfig {
    private static Path configPath;
    public static int trainUpdateDistance = 128;

    public static void load(Path configDir) {
        configPath = configDir.resolve("mtrotp_server.json");
        if (!Files.exists(configPath)) {
            MTROTPClient.LOGGER.info("[MTR-OTP] Server config not found, generating one...");
            writeConfig();
            return;
        }

        MTROTPClient.LOGGER.info("[MTR-OTP] Reading server config...");
        try {
            final JsonObject jsonConfig = JsonParser.parseString(Files.readString(configPath)).getAsJsonObject();
            try {
                trainUpdateDistance = jsonConfig.get("trainUpdateDistance").getAsInt();
            } catch (Exception ignored) {}
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeConfig() {
        MTROTPClient.LOGGER.info("[MTR-OTP] Writing server config...");
        final JsonObject jsonConfig = new JsonObject();
        jsonConfig.addProperty("trainUpdateDistance", trainUpdateDistance);

        try {
            Files.write(configPath, Collections.singleton(new GsonBuilder().setPrettyPrinting().create().toJson(jsonConfig)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
