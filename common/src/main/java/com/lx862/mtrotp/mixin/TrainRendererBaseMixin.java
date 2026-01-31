package com.lx862.mtrotp.mixin;

import mtr.MTRClient;
import mtr.render.TrainRendererBase;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(value = TrainRendererBase.class, remap = false)
public class TrainRendererBaseMixin {
    @ModifyArg(method = "renderRidingPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/EntityRenderDispatcher;render(Lnet/minecraft/world/entity/Entity;DDDFFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V"), index = 5)
    private static float fixTrainPlayerModelStutter(float tickDelta) {
        if (Minecraft.getInstance().isPaused()) return 0;
        return MTRClient.getLastFrameDuration();
    }
}
