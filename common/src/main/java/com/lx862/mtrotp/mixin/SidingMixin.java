package com.lx862.mtrotp.mixin;

import com.lx862.mtrotp.TickManager;
import mtr.data.Siding;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(value = Siding.class, remap = false)
public class SidingMixin {
    @Shadow private Level level;

    @ModifyArg(method = "simulateTrain", at = @At(value = "INVOKE", target = "Lmtr/data/TrainServer;simulateTrain(Lnet/minecraft/world/level/Level;FLmtr/data/Depot;Lmtr/data/DataCache;Ljava/util/List;Ljava/util/Map;Ljava/util/Map;Ljava/util/Map;)Z"), index = 1)
    public float getTicksElapsed(float ticksElapsed) {
        return level != null && level.isClientSide() ? 1 : TickManager.getNextTickTime();
    }
}
