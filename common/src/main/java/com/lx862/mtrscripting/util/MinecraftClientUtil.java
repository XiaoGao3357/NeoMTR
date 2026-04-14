package com.lx862.mtrscripting.util;

/* From https://github.com/zbx1425/mtr-nte/blob/master/common/src/main/java/cn/zbx1425/mtrsteamloco/render/scripting/util/MinecraftClientUtil.java */

import com.mojang.text2speech.Narrator;
import mtr.mappings.Text;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.LightLayer;
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

    public static boolean worldIsRainingAt(Vector3dWrapper pos) {
        return Minecraft.getInstance().level != null
                && Minecraft.getInstance().level.isRainingAt(pos.rawBlockPos());
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

    public static int blockLightAt(Vector3dWrapper pos) {
        return Minecraft.getInstance().level.getBrightness(LightLayer.BLOCK, pos.rawBlockPos());
    }

    public static int skyLightAt(Vector3dWrapper pos) {
        return Minecraft.getInstance().level.getBrightness(LightLayer.SKY, pos.rawBlockPos());
    }

    public static int lightLevelAt(Vector3dWrapper pos) {
        return Math.min(blockLightAt(pos), skyLightAt(pos));
    }

    public static Vector3dWrapper playerPos() {
        return new Vector3dWrapper((float) Minecraft.getInstance().player.position().x, (float) Minecraft.getInstance().player.position().y, (float) Minecraft.getInstance().player.position().z);
    }

    public static Vector3dWrapper playerBlockPos() {
        return new Vector3dWrapper(Minecraft.getInstance().player.blockPosition());
    }

    public static String playerName() {
        return Minecraft.getInstance().player.getGameProfile().getName();
    }

    public static boolean isHoldingItem(String id) {
        ResourceLocation itemId = ResourceLocation.tryParse(id);
        Optional<Item> itm = itemId == null ? Optional.empty() : BuiltInRegistries.ITEM.getOptional(itemId);
        return itm.map(item -> Minecraft.getInstance().player.isHolding(item)).orElse(false);
    }

    public static Integer getScoreboardScore(String objectiveName, String playerName) {
        Objective objective = Minecraft.getInstance().level.getScoreboard().getObjective(objectiveName);
        ScoreAccess score = objective == null ? null : Minecraft.getInstance().level.getScoreboard().getOrCreatePlayerScore(ScoreHolder.forNameOnly(playerName), objective);
        return score == null ? null : score.get();
    }

    public static boolean gamePaused() {
        return Minecraft.getInstance().isPaused();
    }

    public static ItemStackWrapper mainHandItem() {
        ItemStackWrapper itemStackWrapper = new ItemStackWrapper(Minecraft.getInstance().player.getMainHandItem());
        return itemStackWrapper.empty() ? null : itemStackWrapper;
    }

    public static ItemStackWrapper offHandItem() {
        ItemStackWrapper itemStackWrapper = new ItemStackWrapper(Minecraft.getInstance().player.getOffhandItem());
        return itemStackWrapper.empty() ? null : itemStackWrapper;
    }

    public static ItemStackWrapper itemHeld() {
        ItemStackWrapper mainHandItem = mainHandItem();
        if (mainHandItem != null) return mainHandItem;

        ItemStackWrapper offHandItem = offHandItem();
        if (offHandItem != null) return offHandItem;

        return null;
    }

    public static void displayMessage(String message, boolean actionBar) {
        displayMessage(Text.literal(message), actionBar);
    }

    public static void displayMessage(VanillaTextWrapper vanillaTextWrapper, boolean actionBar) {
        displayMessage(vanillaTextWrapper.impl(), actionBar);
    }

    private static void displayMessage(MutableComponent text, boolean actionBar) {
        final Player player = Minecraft.getInstance().player;
        if (player != null) {
            Minecraft.getInstance().execute(() -> {
                player.displayClientMessage(text, actionBar);
            });
        }
    }
}
