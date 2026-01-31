package com.lx862.mtrotp.mixin;

import com.lx862.mtrotp.config.ServerConfig;
import mtr.data.TrainServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(value = TrainServer.class, remap = false)
public class TrainServerMixin {
    @ModifyArg(method = "handlePositions", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/AABB;inflate(D)Lnet/minecraft/world/phys/AABB;"))
    public double fixTrainUpdateDistance(double d) {
        return ServerConfig.trainUpdateDistance;
    }
}
