package com.spideramulet;

import net.fabricmc.api.ClientModInitializer;

public class SpiderAmuletModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // No custom entities are registered by this mod, so no renderers are needed.
        // This client initializer exists to satisfy the client entrypoint declaration.
    }
}