package com.lx862.logregator;

import com.lx862.logregator.config.LogregatorConfig;
import com.lx862.logregator.data.MTRLoggingManager;
import com.mojang.brigadier.CommandDispatcher;
import mtr.registry.MTRAddonRegistry;
import net.minecraft.commands.CommandSourceStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.util.function.Consumer;

public class Logregator {
    public static final Logger LOGGER = LogManager.getLogger("Logregator");
    public static MTRLoggingManager mtrLoggingManager;

    public static void init(String modVersion, Path configDirectory, Consumer<Consumer<CommandDispatcher<CommandSourceStack>>> registerCommand) {
        if(modVersion == null) throw new IllegalArgumentException("Version is not detected, this should not happen.");
        LOGGER.info("[Logregator] Version {}", modVersion);
        MTRAddonRegistry.MTRAddon ADDON = new MTRAddonRegistry.MTRAddon("logregator", "Logregator", modVersion);
        MTRAddonRegistry.registerAddon(ADDON);

        LogregatorConfig.load(configDirectory);
        mtrLoggingManager = new MTRLoggingManager();

        registerCommand.accept(CommandHandler::registerCommands);
    }
}
