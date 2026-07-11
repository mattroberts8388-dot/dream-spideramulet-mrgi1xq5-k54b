package com.spideramulet;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SpiderAmuletMod implements ModInitializer {
    public static final String MOD_ID = "spideramulet";

    public static final Identifier LEAP_SOUND_ID = new Identifier(MOD_ID, "leap");
    public static final SoundEvent LEAP_SOUND_EVENT = SoundEvent.of(LEAP_SOUND_ID);

    public static final SpiderAmuletItem SPIDER_AMULET = new SpiderAmuletItem(
            new FabricItemSettings().maxCount(1));

    // Tracks previous "on ground" state per player for leap detection.
    private static final Map<UUID, Boolean> WAS_ON_GROUND = new HashMap<>();
    // Cooldown ticks remaining before another leap boost can trigger.
    private static final Map<UUID, Integer> LEAP_COOLDOWN = new HashMap<>();

    private static final int LEAP_COOLDOWN_TICKS = 40;

    @Override
    public void onInitialize() {
        Registry.register(Registries.SOUND_EVENT, LEAP_SOUND_ID, LEAP_SOUND_EVENT);

        Registry.register(Registries.ITEM, new Identifier(MOD_ID, "spider_amulet"), SPIDER_AMULET);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> entries.add(SPIDER_AMULET));

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                tickPlayer(player);
            }
        });
    }

    private void tickPlayer(ServerPlayerEntity player) {
        UUID id = player.getUuid();

        boolean wearing = isWearingAmulet(player);

        // Reduce cooldown regardless.
        int cd = LEAP_COOLDOWN.getOrDefault(id, 0);
        if (cd > 0) {
            LEAP_COOLDOWN.put(id, cd - 1);
        }

        if (!wearing) {
            WAS_ON_GROUND.put(id, player.isOnGround());
            return;
        }

        // Wall climbing: if the player is horizontally colliding with a wall and holding
        // toward it, slowly climb upward like a spider.
        if (player.horizontalCollision && !player.isOnGround() && !player.isSneaking()) {
            Vec3d vel = player.getVelocity();
            // Slow controlled climb.
            player.setVelocity(vel.x, 0.16, vel.z);
            player.velocityModified = true;
            player.fallDistance = 0.0f;
        }

        // Leap boost: sprinting off a ledge (was on ground last tick, now in the air,
        // moving horizontally, and sprinting) gives a brief burst of forward speed.
        boolean wasOnGround = WAS_ON_GROUND.getOrDefault(id, true);
        boolean onGround = player.isOnGround();

        if (wasOnGround && !onGround && player.isSprinting()) {
            Vec3d vel = player.getVelocity();
            double horizontalSpeedSq = vel.x * vel.x + vel.z * vel.z;
            if (horizontalSpeedSq > 0.0025 && LEAP_COOLDOWN.getOrDefault(id, 0) <= 0) {
                // Add a forward burst in the direction of current horizontal motion.
                double len = Math.sqrt(horizontalSpeedSq);
                double boost = 0.55;
                double newX = vel.x + (vel.x / len) * boost;
                double newZ = vel.z + (vel.z / len) * boost;
                double newY = Math.max(vel.y, 0.32);
                player.setVelocity(newX, newY, newZ);
                player.velocityModified = true;

                LEAP_COOLDOWN.put(id, LEAP_COOLDOWN_TICKS);

                player.getWorld().playSound(
                        null,
                        player.getX(), player.getY(), player.getZ(),
                        LEAP_SOUND_EVENT,
                        SoundCategory.PLAYERS,
                        1.0f, 1.0f);
            }
        }

        WAS_ON_GROUND.put(id, onGround);
    }

    private boolean isWearingAmulet(ServerPlayerEntity player) {
        // "Worn" = held in main hand or off hand, or present in the head armor slot.
        if (player.getMainHandStack().getItem() == SPIDER_AMULET) return true;
        if (player.getOffHandStack().getItem() == SPIDER_AMULET) return true;
        ItemStack head = player.getInventory().getArmorStack(3);
        return head != null && head.getItem() == SPIDER_AMULET;
    }
}