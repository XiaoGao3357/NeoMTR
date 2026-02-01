package com.lx862.logregator;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;

public class LogregatorFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Logregator.init("1.0.0", FabricLoader.getInstance().getConfigDir(), cb -> {
            CommandRegistrationCallback.EVENT.register((dispatcher, dedicated, third) -> cb.accept(dispatcher));
        });
    }
}
