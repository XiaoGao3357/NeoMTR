package com.lx862.mtrotp.config;

import com.google.gson.*;
import com.lx862.mtrotp.MTROTPClient;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;

public class ClientConfig {
    private static Path configPath;
    public static boolean cullTrain = true;
    public static boolean dashboardLazyRender = true;

    public static void load(Path configDir) {
        configPath = configDir.resolve("mtrotp_server.json");
        if (!Files.exists(configPath)) {
            MTROTPClient.LOGGER.info("[MTR-OTP] Client config not found, generating one...");
            writeConfig();
            return;
        }

        MTROTPClient.LOGGER.info("[MTR-OTP] Reading client config...");
        try {
            final JsonObject jsonConfig = JsonParser.parseString(Files.readString(configPath)).getAsJsonObject();
            try {
                cullTrain = jsonConfig.get("cullTrain").getAsBoolean();
            } catch (Exception ignored) {}

            try {
                dashboardLazyRender = jsonConfig.get("dashboardLazyRender").getAsBoolean();
            } catch (Exception ignored) {}

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeConfig() {
        MTROTPClient.LOGGER.info("[MTR-OTP] Writing client config...");
        final JsonObject jsonConfig = new JsonObject();
        jsonConfig.addProperty("cullTrain", cullTrain);
        jsonConfig.addProperty("dashboardLazyRender", dashboardLazyRender);

        try {
            Files.write(configPath, Collections.singleton(new GsonBuilder().setPrettyPrinting().create().toJson(jsonConfig)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
