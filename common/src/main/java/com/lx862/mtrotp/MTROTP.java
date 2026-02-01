package com.lx862.mtrotp;

import com.lx862.mtrotp.config.ServerConfig;
import mtr.loader.MTRRegistry;
import mtr.registry.MTRAddonRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MTROTP {
    public static final Logger LOGGER = LoggerFactory.getLogger("MTR-OTP");
    public static final MTRAddonRegistry.MTRAddon ADDON = new MTRAddonRegistry.MTRAddon("mtrotp", "MTR-OTP", "1.1.3");

    public static void init() {
        TickManager.initialize();
        MTRAddonRegistry.registerAddon(ADDON);
        MTRRegistry.registerServerStartingEvent(server -> ServerConfig.load(server.getServerDirectory().resolve("config")));
        LOGGER.info("[MTR-OTP] MTR-OTP initialized \\(＾▽＾)/");
    }
}
