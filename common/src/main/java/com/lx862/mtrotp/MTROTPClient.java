package com.lx862.mtrotp;

import com.lx862.mtrotp.config.ClientConfig;

import net.minecraft.client.Minecraft;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MTROTPClient {
    public static final Logger LOGGER = LoggerFactory.getLogger("MTR-OTP Client");

    public static void init() {
        ClientConfig.load(Minecraft.getInstance().gameDirectory.toPath().resolve("config"));
    }
}
