package com.terret.casinokeepvillagers.mixin;

import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.terret.casinokeepvillagers.CasinoKeepVillagersMod.KEEP_TAG;

/**
 * Rewrites commands that target minecraft:villager so they exclude tag=!casino_keep.
 * This is designed to keep Casino Rocket villagers alive in modpacks that remove villagers via repeating commands.
 */
@Mixin(CommandManager.class)
public class CommandManagerMixin {

    @Inject(method = "executeWithPrefix", at = @At("HEAD"), cancellable = true)
    private void casinokeepvillagers$rewriteVillagerKill(ServerCommandSource source, String command, CallbackInfo ci) {
        if (command == null) return;
        if (!command.contains("minecraft:villager")) return;

        String rewritten = rewrite(command);

        if (!rewritten.equals(command)) {
            source.getServer().getCommandManager().executeWithPrefix(source, rewritten);
            ci.cancel();
        }
    }

    private static String rewrite(String cmd) {
        if (cmd.contains("tag=!" + KEEP_TAG)) return cmd;

        String out = cmd;

        // Inject tag filter into selectors
        out = out.replace("type=minecraft:villager]", "type=minecraft:villager,tag=!" + KEEP_TAG + "]");
        out = out.replace("type=minecraft:villager,", "type=minecraft:villager,tag=!" + KEEP_TAG + ",");

        return out;
    }
}
