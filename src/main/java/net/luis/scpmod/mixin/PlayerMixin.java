package net.luis.scpmod.mixin;

import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin {

    @Inject(method = "stopSleeping", at = @At("HEAD"), cancellable = true)
    private void preventEarlyWakeup(CallbackInfo ci) {
        Player player = (Player) (Object) this;

        if (player.getPersistentData().getBoolean("scp2295_force_sleep")) {
            ci.cancel(); // Prevent waking up too soon
        }
    }
}
