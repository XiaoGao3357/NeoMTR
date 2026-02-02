package cn.zbx1425.mtrsteamloco.gui;

import cn.zbx1425.mtrsteamloco.ClientConfig;
import cn.zbx1425.mtrsteamloco.render.scripting.AbstractScriptContext;
import cn.zbx1425.mtrsteamloco.render.scripting.ScriptContextManager;
import cn.zbx1425.mtrsteamloco.render.scripting.ScriptHolder;
import cn.zbx1425.mtrsteamloco.render.scripting.util.GraphicsTexture;
import com.google.common.base.Splitter;
import com.lx862.jcm.mod.scripting.jcm.JCMScripting;
import com.lx862.mtrscripting.core.ScriptInstance;
import com.lx862.mtrscripting.data.UniqueKey;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScriptDebugOverlay {
    private static final double IDEAL_FRAMERATE = 60;
    private static final int COLOR_RED = 0xFFFF0000;
    private static final int COLOR_BLUE = 0xFFCCCCFF;
    private static final int COLOR_YELLOW = 0xFFFFFF00;

    public static void render(GuiGraphics vdStuff) {
        PoseStack matrices = vdStuff.pose();
        if (!ClientConfig.enableScriptDebugOverlay) return;
        if (Minecraft.getInstance().screen != null) return;

        matrices.pushPose();
        matrices.translate(10, 10, 0);

        Map<ScriptHolderWrapper, List<ScriptContextWrapper>> contexts = new HashMap<>();
        for (Map.Entry<AbstractScriptContext, ScriptHolder> entry : ScriptContextManager.livingContexts.entrySet()) {
            AbstractScriptContext k = entry.getKey();
            ScriptHolder v = entry.getValue();
            contexts.computeIfAbsent(new ScriptHolderWrapper() {
                @Override
                public boolean duringFailCooldown() {
                    return v.failTime > 0;
                }

                @Override
                public String getName() {
                    return v.name;
                }

                @Override
                public Exception getException() {
                    return v.failException;
                }
            }, ka -> new java.util.ArrayList<>()).add(new ScriptContextWrapper() {
                @Override
                public double getLastExecutionMs() {
                    return k.lastExecuteDurationMovingAverage / 1000000.0;
                }

                @Override
                public Map<String, Object> getDebugInfo() {
                    return k.debugInfo;
                }
            });
        }

        for(Map.Entry<UniqueKey, ScriptInstance> entry : JCMScripting.getScriptManager().getInstanceManager().getInstances().entrySet()) {
            ScriptInstance<?> v = entry.getValue();
            contexts.computeIfAbsent(new ScriptHolderWrapper() {
                @Override
                public boolean duringFailCooldown() {
                    return v.getScript().duringFailCooldown();
                }

                @Override
                public String getName() {
                    return v.getScript().getDisplayName();
                }

                @Override
                public Exception getException() {
                    return null; // TODO
                }
            }, k -> new java.util.ArrayList<>()).add(new ScriptContextWrapper() {
                @Override
                public double getLastExecutionMs() {
                    return v.lastExecuteTime;
                }

                @Override
                public Map<String, Object> getDebugInfo() {
                    Map<String, Object> map = new HashMap<>();
                    for(Map.Entry<String, Object> vs : v.getScriptContext().getDebugInfo()) {
                        map.put(vs.getKey(), vs.getValue());
                    }
                    return map;
                }
            });
        }

        int y = 0;
        Font font = Minecraft.getInstance().font;
        int lineHeight = Mth.ceil(font.lineHeight * 1.2f);
        for (Map.Entry<ScriptHolderWrapper, List<ScriptContextWrapper>> entry : contexts.entrySet()) {
            ScriptHolderWrapper holder = entry.getKey();
            synchronized (holder) {
                // Fail time
                // name
                // exception
                if (holder.duringFailCooldown()) {
                    drawText(vdStuff, font, holder.getName() + " FAILED", 0, y, COLOR_RED);
                    y += lineHeight;
                    for (String msgLine : Splitter.fixedLength(60).split(holder.getException().getMessage())) {
                        drawText(vdStuff, font, msgLine, 5, y, 0xFFFF8888);
                        y += lineHeight;
                    }
                } else {
                    drawText(vdStuff, font, holder.getName(), 0, y, COLOR_BLUE);
                    y += lineHeight;
                }
            }

            for (ScriptContextWrapper context : entry.getValue()) {
                drawText(vdStuff, font,
                        String.format("#%08X (%.2f ms)", context.hashCode(), context.getLastExecutionMs()),
                        10, y,  getColor(context.getLastExecutionMs()));
                y += lineHeight;
                for (Map.Entry<String, Object> debugInfo : context.getDebugInfo().entrySet()) {
                    Object value = debugInfo.getValue();
                    if (value instanceof GraphicsTexture) {
                        GraphicsTexture texture = (GraphicsTexture) value;
                        float scale = (Minecraft.getInstance().getWindow().getGuiScaledWidth() - 40) / (float) texture.width;
                        blit(vdStuff, texture.identifier, 20, y, (int)(texture.width * scale), (int)(texture.height * scale));
                        drawText(vdStuff, font, debugInfo.getKey() + ": GraphicsTexture", 20, y, 0xFFFFFFFF);
                        y += (int)(texture.height * scale) + lineHeight / 2;
                    } else {
                        drawText(vdStuff, font, debugInfo.getKey() + ": " + debugInfo.getValue(), 20, y, 0xFFFFFFFF);
                        y += lineHeight;
                    }
                }
            }
        }

        matrices.popPose();
    }

    private static int getColor(double executionMs) {
        if(executionMs > (1000/(IDEAL_FRAMERATE/2))) {
            return COLOR_RED;
        } else if(executionMs > (1000/IDEAL_FRAMERATE)) {
            return COLOR_YELLOW;
        } else {
            return COLOR_BLUE;
        }
    }

    private static void drawText(GuiGraphics guiGraphics, Font font, String text, int x, int y, int color) {
        guiGraphics.drawString(font, text, x, y, color);
    }
    private static void blit(GuiGraphics guiGraphics, ResourceLocation texture, int x, int y, int width, int height) {
        guiGraphics.blit(texture, x, y, width, height, 0, 0, 1, 1, 1, 1);
    }

    interface ScriptHolderWrapper {
        boolean duringFailCooldown();
        String getName();
        Exception getException();
    }

    interface ScriptContextWrapper {
        double getLastExecutionMs();
        Map<String, Object> getDebugInfo();
        int hashCode();
    }
}
