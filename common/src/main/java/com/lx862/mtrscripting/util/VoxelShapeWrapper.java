package com.lx862.mtrscripting.util;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class VoxelShapeWrapper {
    private final VoxelShape impl;

    public VoxelShapeWrapper(VoxelShape impl) {
        this.impl = impl;
    }

    public static VoxelShapeWrapper empty() {
        return new VoxelShapeWrapper(Shapes.empty());
    }

    public static VoxelShapeWrapper fullCube() {
        return new VoxelShapeWrapper(Shapes.block());
    }

    public static VoxelShapeWrapper create(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        return new VoxelShapeWrapper(Block.box(minX, minY, minZ, maxX, maxY, maxZ));
    }

    public VoxelShapeWrapper combine(VoxelShapeWrapper other) {
        return new VoxelShapeWrapper(Shapes.or(this.impl, other.impl));
    }

    public VoxelShape impl() {
        return impl;
    }
}
