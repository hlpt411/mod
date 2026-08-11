package com.axiom.client.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Vector4f;

import java.util.Optional;

/**
 * Collection of rendering helpers for 2D GUI and 3D world overlays.
 */
public class RenderUtil {

    /**
     * Projects a world position to screen space using the current camera matrices.
     * Returns Optional.empty() if the point is behind the camera.
     */
    public static Optional<Vec3d> worldToScreen(Vec3d world, Matrix4f positionMatrix, Matrix4f projectionMatrix, int screenWidth, int screenHeight) {
        Vector4f clip = new Vector4f((float) (world.x - 0), (float) (world.y - 0), (float) (world.z - 0), 1.0f);
        clip.mul(positionMatrix);
        clip.mul(projectionMatrix);

        if (clip.w <= 0.0f) return Optional.empty();

        float ndcX = clip.x / clip.w;
        float ndcY = clip.y / clip.w;

        double screenX = (ndcX + 1.0) / 2.0 * screenWidth;
        double screenY = (1.0 - ndcY) / 2.0 * screenHeight;
        return Optional.of(new Vec3d(screenX, screenY, clip.w));
    }

    /**
     * Draws a shadowed text label on screen with a dark backing.
     */
    public static void drawLabel(DrawContext context, TextRenderer textRenderer, String text, int x, int y, int color) {
        context.fill(x - 2, y - 1, x + textRenderer.getWidth(text) + 2, y + textRenderer.fontHeight + 1, 0xAA000000);
        context.drawTextWithShadow(textRenderer, text, x, y, color);
    }

    /**
     * Draws a single 3D line in world space.
     * Assumes the matrix stack is already transformed to camera-relative space.
     */
    public static void drawLine(MatrixStack matrices, Vec3d start, Vec3d end, int color) {
        // Implementation lives in the VisualDetectionSystem to keep RenderUtil dependency-free.
        // This is a stub for modules that want a shared helper signature.
    }
}
