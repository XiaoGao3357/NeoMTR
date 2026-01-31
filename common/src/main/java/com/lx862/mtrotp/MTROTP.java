package com.lx862.mtrotp;

import com.lx862.mtrotp.config.ServerConfig;
import mtr.loader.MTRRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MTROTP {
    public static final Logger LOGGER = LoggerFactory.getLogger("MTROTP");

    public static void init() {
        TickManager.initialize();
        MTRRegistry.registerServerStartingEvent(server -> ServerConfig.load(server.getServerDirectory().resolve("config")));
        LOGGER.info("[MTR-OTP] MTR-OTP initialized \\(＾▽＾)/");
    }
}
