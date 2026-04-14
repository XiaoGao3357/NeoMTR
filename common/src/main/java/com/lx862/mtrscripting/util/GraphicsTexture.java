package com.lx862.mtrscripting.util;

import cn.zbx1425.mtrsteamloco.mixin.NativeImageAccessor;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.system.MemoryUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.Closeable;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.UUID;

@SuppressWarnings("unused")
public class GraphicsTexture implements Closeable {
    private final DynamicTexture dynamicTexture;
    public final ResourceLocation identifier;

    public final BufferedImage bufferedImage;
    public final Graphics2D graphics;

    public final int width, height;

    public GraphicsTexture(int width, int height) {
        this.width = width;
        this.height = height;
        dynamicTexture = new DynamicTexture(new NativeImage(width, height, false));
        identifier = ResourceLocation.fromNamespaceAndPath("mtrscripting", String.format("dynamic/graphics/%s", UUID.randomUUID()));
        Minecraft.getInstance().execute(() -> {
            Minecraft.getInstance().getTextureManager().register(identifier, dynamicTexture);
        });
        bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        graphics = bufferedImage.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
    }

    public static BufferedImage createArgbBufferedImage(BufferedImage src) {
        BufferedImage newImage = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = newImage.createGraphics();
        graphics.drawImage(src, 0, 0, null);
        graphics.dispose();
        return newImage;
    }

    public void upload() {
        int[] imgData = ((DataBufferInt) bufferedImage.getData().getDataBuffer()).getData();
        IntBuffer imgBuffer = IntBuffer.wrap(imgData);
        long nativeImagePointer = ((NativeImageAccessor) (Object) dynamicTexture.getPixels()).getPixels();
        ByteBuffer target = MemoryUtil.memByteBuffer(nativeImagePointer, width * height * 4);
        for (int i = 0; i < width * height; i++) {
            int pixel = imgBuffer.get();
            target.put((byte) ((pixel >> 16) & 0xFF));
            target.put((byte) ((pixel >> 8) & 0xFF));
            target.put((byte) (pixel & 0xFF));
            target.put((byte) ((pixel >> 24) & 0xFF));
        }
        RenderSystem.recordRenderCall(dynamicTexture::upload);
    }

    private static int toAbgr(int rgb) {
        int a = (rgb >> 24) & 255;
        int r = (rgb >> 16) & 255;
        int g = (rgb >> 8) & 255;
        int b = (rgb) & 255;
        return a << 24 | b << 16 | g << 8 | r;
    }

    @Override
    public void close() {
        Minecraft.getInstance().execute(() -> {
            Minecraft.getInstance().getTextureManager().release(identifier);
        });
        graphics.dispose();
    }
}
