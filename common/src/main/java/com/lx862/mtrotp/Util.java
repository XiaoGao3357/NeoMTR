package com.lx862.mtrotp;

import com.lx862.mtrotp.mixin.RailAccessorMixin;
import com.lx862.mtrotp.mixin.TrainAccessorMixin;
import mtr.data.Rail;
import mtr.data.Train;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class Util {
    private static final int MAX_Y = 384;
    private static final double CULLING_MARGIN = 2;

    public static AABB getTrainBoundingBox(Train train, int car, int trainSpacing) {
        final double halfWidth = train.width / 2.0;
        Vec3 a1 = getRoutePosition(train, train.isReversed() ? train.trainCars - car : car, trainSpacing, halfWidth);
        Vec3 a2 = getRoutePosition(train, train.isReversed() ? (train.trainCars - car) - 1 : car + 1, trainSpacing, -halfWidth);
        Vec3 a3 = getRoutePosition(train, train.isReversed() ? train.trainCars - car : car, trainSpacing, -halfWidth);
        Vec3 a4 = getRoutePosition(train, train.isReversed() ? (train.trainCars - car) - 1 : car + 1, trainSpacing, halfWidth);
        AABB box1 = new AABB(a1.x, Math.min(a1.y, a2.y), a1.z, a2.x, MAX_Y, a2.z);
        AABB box2 = new AABB(a3.x, Math.min(a3.y, a4.y), a3.z, a4.x, MAX_Y, a4.z);
        return box1.minmax(box2).inflate(CULLING_MARGIN);
    }

    // All these methods are simply copied from MTR with a radiusOffset, so we know the actual bounding box area
    // Would be much cleaner if MTR has an override to be able to get a route position plus the radiusOffset
    // But the MTR codebase is painfully slow to develop anything...

    public static Vec3 getRailPosition(Rail rail, double rawValue, double radiusOffset) {
        final double tEnd1 = ((RailAccessorMixin)rail).getTEnd1();
        final double tStart1 = ((RailAccessorMixin)rail).getTStart1();
        final double tEnd2 = ((RailAccessorMixin)rail).getTEnd2();
        final double tStart2 = ((RailAccessorMixin)rail).getTStart2();
        final boolean reverseT1 = ((RailAccessorMixin)rail).getReverseT1();
        final boolean reverseT2 = ((RailAccessorMixin)rail).getReverseT2();
        final boolean isStraight1 = ((RailAccessorMixin)rail).getIsStraight1();
        final boolean isStraight2 = ((RailAccessorMixin)rail).getIsStraight2();
        final double h1 = ((RailAccessorMixin)rail).getH1();
        final double k1 = ((RailAccessorMixin)rail).getK1();
        final double r1 = ((RailAccessorMixin)rail).getR1();
        final double h2 = ((RailAccessorMixin)rail).getH2();
        final double k2 = ((RailAccessorMixin)rail).getK2();
        final double r2 = ((RailAccessorMixin)rail).getR2();

        final double count1 = Math.abs(tEnd1 - tStart1);
        final double count2 = Math.abs(tEnd2 - tStart2);
        final double value = Mth.clamp(rawValue, 0, count1 + count2);
        final double y = ((RailAccessorMixin)rail).positionY(value);

        if (value <= count1) {
            return RailAccessorMixin.positionXZ(h1, k1, r1, (reverseT1 ? -1 : 1) * value + tStart1, radiusOffset, isStraight1).add(0, y, 0);
        } else {
            return RailAccessorMixin.positionXZ(h2, k2, r2, (reverseT2 ? -1 : 1) * (value - count1) + tStart2, radiusOffset, isStraight2).add(0, y, 0);
        }
    }

    private static Vec3 getRoutePosition(Train train, int car, double trainSpacing, double radiusOffset) {
        final double tempRailProgress = Math.max(getRailProgress(train, car, trainSpacing) - ((TrainAccessorMixin)train).modelZOffset(), 0);
        final int index = train.getIndex(tempRailProgress, false);
        final Rail rail = train.path.get(index).rail;
        return getRailPosition(rail, tempRailProgress - (index == 0 ? 0 : ((TrainAccessorMixin)train).getDistances().get(index - 1)), radiusOffset).add(0, train.transportMode.railOffset, 0);
    }

    private static double getRailProgress(Train train, int car, double trainSpacing) {
        return train.getRailProgress() - car * trainSpacing;
    }
}
