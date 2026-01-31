package com.lx862.logregator;

import com.lx862.logregator.config.LogregatorConfig;
import com.lx862.logregator.data.MTRLoggingManager;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.util.function.Consumer;

public class Logregator {
    public static final Logger LOGGER = LogManager.getLogger("Logregator");
    public static MTRLoggingManager mtrLoggingManager;
    public static String modVersion = null;

    public static void initialize(Path configDirectory, Consumer<Consumer<CommandDispatcher<CommandSourceStack>>> registerCommand) {
        if (modVersion != null) {
            LOGGER.info("[Logregator] Version {}", modVersion);
        }

        LogregatorConfig.load(configDirectory);
        mtrLoggingManager = new MTRLoggingManager();

        registerCommand.accept(CommandHandler::registerCommands);
    }
}
