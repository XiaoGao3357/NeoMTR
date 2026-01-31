package com.lx862.logregator;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;

public class LogregatorFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Logregator.modVersion = FabricLoader.getInstance()
                .getModContainer("logregator")
                .map(container -> container.getMetadata().getVersion().getFriendlyString())
                .orElse(null);
        Logregator.initialize(FabricLoader.getInstance().getConfigDir(), cb -> {
            CommandRegistrationCallback.EVENT.register((dispatcher, dedicated, third) -> cb.accept(dispatcher));
        });
    }
}
