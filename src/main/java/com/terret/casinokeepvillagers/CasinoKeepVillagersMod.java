package com.terret.casinokeepvillagers;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.text.Text;

import java.util.Set;

public class CasinoKeepVillagersMod implements ModInitializer {
    public static final String MOD_ID = "casino-keep-villagers";
    public static final String KEEP_TAG = "casino_keep";

    // Types used by Casino Rocket (1.0.0)
    private static final Set<String> ALLOWED_NAMES = Set.of(
            "Chip Dealer",
            "Cashier",
            "Prize Dealer",
            "TM Instructor",
            "Battle Gear",
            "Snackmaster",
            "Mr. Lucky",
            "Banker"
    );

    @Override
    public void onInitialize() {
        // Whenever a villager loads on the server, tag it if it looks like a Casino Rocket NPC.
        ServerEntityEvents.ENTITY_LOAD.register((Entity entity, net.minecraft.server.world.ServerWorld world) -> {
            if (!(entity instanceof VillagerEntity villager)) return;

            Text cn = villager.getCustomName();
            if (cn == null) return;

            String name = cn.getString();
            if (!ALLOWED_NAMES.contains(name)) return;

            villager.addScoreboardTag(KEEP_TAG);
            if (villager instanceof MobEntity mob) {
                mob.setPersistent(); // prevents natural despawn
            }
        });
    }
}
