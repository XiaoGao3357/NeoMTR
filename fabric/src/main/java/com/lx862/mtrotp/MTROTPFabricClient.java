package com.lx862.mtrotp;

import net.fabricmc.api.ClientModInitializer;

public class MTROTPFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        MTROTPClient.init();
    }
}
