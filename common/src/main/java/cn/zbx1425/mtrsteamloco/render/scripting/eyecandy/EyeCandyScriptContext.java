package cn.zbx1425.mtrsteamloco.render.scripting.eyecandy;

import cn.zbx1425.mtrsteamloco.block.BlockEyeCandy;
import cn.zbx1425.mtrsteamloco.render.scripting.AbstractScriptContext;
import cn.zbx1425.mtrsteamloco.render.scripting.util.DynamicModelHolder;
import com.lx862.mtrscripting.util.VoxelShapeWrapper;
import cn.zbx1425.sowcer.math.Matrices;
import cn.zbx1425.sowcer.math.Matrix4f;
import cn.zbx1425.sowcerext.model.ModelCluster;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.phys.shapes.VoxelShape;

public class EyeCandyScriptContext extends AbstractScriptContext {

    public BlockEyeCandy.BlockEntityEyeCandy entity;
    private final EyecandyEvents events;
    private VoxelShapeWrapper outlineShape;
    private VoxelShapeWrapper collisionShape;

    public EyeCandyDrawCalls scriptResult;
    private EyeCandyDrawCalls scriptResultWriting;

    public EyeCandyScriptContext(BlockEyeCandy.BlockEntityEyeCandy entity) {
        scriptResult = new EyeCandyDrawCalls();
        scriptResultWriting = new EyeCandyDrawCalls();
        this.entity = entity;
        this.events = new EyecandyEvents();
        this.outlineShape = null;
        this.collisionShape = null;
    }

    public EyecandyEvents events() {
        return events;
    }

    public VoxelShape getOutlineShape() {
        return outlineShape == null ? null : outlineShape.impl();
    }

    public VoxelShape getCollisionShape() {
        return collisionShape == null ? null : collisionShape.impl();
    }

    public void setOutlineShape(VoxelShapeWrapper outlineShape) {
        this.outlineShape = outlineShape;
    }

    public void setCollisionShape(VoxelShapeWrapper collisionShape) {
        if (collisionShape.impl().bounds().maxY > 1.5) {
            throw new IllegalStateException("Collision shape must not be larger than 1.5 blocks (24 unit)!");
        }
        this.collisionShape = collisionShape;
    }

    @Override
    public void renderFunctionFinished() {
        synchronized (this) {
            EyeCandyDrawCalls temp = scriptResultWriting;
            scriptResultWriting = scriptResult;
            scriptResult = temp;
            scriptResultWriting.reset();
        }
    }

    @Override
    public Object getWrapperObject() {
        return entity;
    }

    // Something more graceful?
    public boolean disposeForReload = false;

    @Override
    public boolean isBearerAlive() {
        return !disposeForReload && !entity.isRemoved();
    }

    public void drawModel(ModelCluster model, Matrices poseStack) {
        scriptResultWriting.addModel(model, poseStack == null ? Matrix4f.IDENTITY : poseStack.last().copy());
    }

    public void drawModel(DynamicModelHolder model, Matrices poseStack) {
        scriptResultWriting.addModel(model, poseStack == null ? Matrix4f.IDENTITY : poseStack.last().copy());
    }

    public void playSound(ResourceLocation sound, float volume, float pitch) {
        scriptResultWriting.addSound(
                SoundEvent.createVariableRangeEvent(sound),
                volume, pitch
        );
    }
}
