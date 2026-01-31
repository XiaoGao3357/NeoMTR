package com.lx862.mtrotp.mixin;

import mtr.data.Rail;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = Rail.class, remap = false)
public interface RailAccessorMixin {
    @Invoker("getPositionXZ")
    static Vec3 positionXZ(double h, double k, double r, double t, double radiusOffset, boolean isStraight) {
        return null;
    }

    @Invoker("getPositionY")
    double positionY(double value);

    @Accessor
    double getTStart1();

    @Accessor
    double getTEnd1();

    @Accessor
    double getTStart2();

    @Accessor
    double getTEnd2();

    @Accessor
    boolean getReverseT2();

    @Accessor
    boolean getReverseT1();

    @Accessor
    boolean getIsStraight2();

    @Accessor
    boolean getIsStraight1();

    @Accessor("h1")
    double getH1();

    @Accessor("k1")
    double getK1();

    @Accessor("r1")
    double getR1();

    @Accessor("h2")
    double getH2();

    @Accessor("k2")
    double getK2();

    @Accessor("r2")
    double getR2();
}
