package com.lx862.mtrotp;

import net.fabricmc.api.ModInitializer;

public class MTROTPFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        MTROTP.init();
    }
}
