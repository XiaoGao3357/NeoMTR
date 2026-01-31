package com.lx862.logregator.mixin;

import com.lx862.logregator.EventAction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLevel.class)
public class WorldMixin {
    @Inject(method= "gameEvent", at = @At("HEAD"))
    public void emitGameEvent(Holder<GameEvent> gameEvent, Vec3 pos, GameEvent.Context context, CallbackInfo ci) {
        EventAction.onWorldEvent(context.sourceEntity(), gameEvent.value(), BlockPos.containing((int)pos.x, pos.y, pos.z));
    }
}
