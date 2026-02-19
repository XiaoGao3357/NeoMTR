package com.lx862.mtrscripting.util;

/* From https://github.com/zbx1425/mtr-nte/blob/master/common/src/main/java/cn/zbx1425/mtrsteamloco/render/scripting/util/MinecraftClientUtil.java */

import cn.zbx1425.sowcer.math.Vector3f;
import com.lx862.jcm.mod.util.JCMUtil;
import com.mojang.text2speech.Narrator;
import mtr.mappings.Text;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.ScoreAccess;
import net.minecraft.world.scores.ScoreHolder;

import java.util.Optional;

@SuppressWarnings("unused")
public class MinecraftClientUtil {
    public static boolean worldIsRaining() {
        return Minecraft.getInstance().level != null
                && Minecraft.getInstance().level.isRaining();
    }

    public static boolean worldIsThundering() {
        return Minecraft.getInstance().level != null
                && Minecraft.getInstance().level.isThundering();
    }

    public static boolean worldIsRainingAt(Vector3f pos) {
        return Minecraft.getInstance().level != null
                && Minecraft.getInstance().level.isRainingAt(pos.toBlockPos());
    }

    public static int worldDayTime() {
        return Minecraft.getInstance().level != null
                ? (int) Minecraft.getInstance().level.getDayTime() : 0;
    }

    public static void narrate(String message) {
        Minecraft.getInstance().execute(() -> {
            Narrator.getNarrator().say(message, true);
        });
    }

    public static int blockLightAt(Vector3f pos) {
        return Minecraft.getInstance().level.getBrightness(LightLayer.BLOCK, JCMUtil.vector3fToBlockPos(pos));
    }

    public static int skyLightAt(Vector3f pos) {
        return Minecraft.getInstance().level.getBrightness(LightLayer.SKY, JCMUtil.vector3fToBlockPos(pos));
    }

    public static int lightLevelAt(Vector3f pos) {
        return Math.min(blockLightAt(pos), skyLightAt(pos));
    }

    public static Vector3f playerPos() {
        Vec3 pos = Minecraft.getInstance().player.position();
        return new Vector3f((float)pos.x, (float)pos.y, (float)pos.z);
    }

    public static Vector3f playerBlockPos() {
        return JCMUtil.blockPosToVector3f(Minecraft.getInstance().player.blockPosition());
    }

    public static boolean isHoldingItem(String id) {
        ResourceLocation itemId = ResourceLocation.tryParse(id);
        Optional<Item> itm = itemId == null ? Optional.empty() : BuiltInRegistries.ITEM.getOptional(itemId);
        return itm.map(item -> Minecraft.getInstance().player.isHolding(item)).orElse(false);
    }

    public static Integer getScoreboardScore(String objectiveName) {
        Objective objective = Minecraft.getInstance().level.getScoreboard().getObjective(objectiveName);
        ScoreAccess score = objective == null ? null : Minecraft.getInstance().level.getScoreboard().getOrCreatePlayerScore(ScoreHolder.forNameOnly(Minecraft.getInstance().player.getGameProfile().getName()), objective);
        return score == null ? null : score.get();
    }

    public static void displayMessage(String message, boolean actionBar) {
        final Player player = Minecraft.getInstance().player;
        if (player != null) {
            Minecraft.getInstance().execute(() -> {
                player.displayClientMessage(Text.literal(message), actionBar);
            });
        }
    }
}
