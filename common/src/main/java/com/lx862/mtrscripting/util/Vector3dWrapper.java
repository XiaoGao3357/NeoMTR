package com.lx862.mtrscripting.util;

import cn.zbx1425.sowcer.math.Vector3f;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

public class Vector3dWrapper {
    private Vec3 impl;

    public Vector3dWrapper(Vec3 impl) {
        this.impl = impl;
    }

    public Vector3dWrapper(float x, float y, float z) {
        this(new Vec3(x, y, z));
    }

    public Vector3dWrapper(Vector3f impl) {
        this(new Vec3(impl.x(), impl.y(), impl.z()));
    }

    public Vector3dWrapper(BlockPos blockPos) {
        this(blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }

    public double x() { return impl.x; }
    public double y() { return impl.y; }
    public double z() { return impl.z; }

    public Vector3dWrapper copy() {
        return new Vector3dWrapper(new Vec3(x(), y(), z()));
    }

    public void normalize() {
        impl = impl.normalize();
    }

    public void add(float x, float y, float z) {
        impl = impl.add(x, y, z);
    }

    public void add(Vector3dWrapper other) {
        impl = impl.add(other.impl);
    }

    public void sub(Vector3dWrapper other) {
        impl = impl.subtract(other.impl);
    }

    public void mul(float x, float y, float z) {
        impl = impl.multiply(x, y, z);
    }

    public void mul(float n) {
        impl = impl.scale(n);
    }

    public void rotX(float rad) {
        impl = impl.xRot(rad);
    }

    public void rotY(float rad) {
        impl = impl.yRot(rad);
    }

    public void rotZ(float rad) {
        impl = impl.zRot(rad);
    }

    public void cross(Vector3dWrapper other) {
        impl = impl.cross(other.impl);
    }

    public double distance(Vector3dWrapper other) {
        double dx = x() - other.x();
        double dy = y() - other.y();
        double dz = z() - other.z();
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    public double distanceSq(Vector3dWrapper other) {
        double dx = x() - other.x();
        double dy = y() - other.y();
        double dz = z() - other.z();
        return dx * dx + dy * dy + dz * dz;
    }

    public BlockPos rawBlockPos() {
        return new BlockPos((int) x(), (int) y(), (int) z());
    }

    public Vec3 rawVector3d() {
        return copy().impl;
    }

    public Vector3f rawVector3f() {
        return new Vector3f((float) x(), (float) y(), (float) z());
    }

    @Override
    public int hashCode() {
        return impl.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Vector3dWrapper that = (Vector3dWrapper) o;
        return impl.equals(that.impl);
    }

    @Override
    public String toString() {
        return "Vector3dWrapper[" + "x=" + x() + ", y=" + y() + ", z=" + z() + "]";
    }

    public static final Vector3dWrapper ZERO = new Vector3dWrapper(0, 0, 0);
    public static final Vector3dWrapper XP = new Vector3dWrapper(1, 0, 0);
    public static final Vector3dWrapper YP = new Vector3dWrapper(0, 1, 0);
    public static final Vector3dWrapper ZP = new Vector3dWrapper(0, 0, 1);
}
