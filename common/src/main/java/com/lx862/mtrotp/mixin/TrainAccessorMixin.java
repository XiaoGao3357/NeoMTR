package com.lx862.mtrotp.mixin;

import mtr.data.Train;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(value = Train.class, remap = false)
public interface TrainAccessorMixin {
    @Accessor
    List<Double> getDistances();

    @Invoker("getModelZOffset")
    float modelZOffset();

    @Invoker("getRoutePosition")
    Vec3 routePosition(double offset);
}
