package com.axiom.client.module.visual;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import com.axiom.client.event.RenderWorldEvent;
import com.axiom.client.event.TickEvent;
import com.axiom.client.module.Category;
import com.axiom.client.module.Module;
import com.axiom.client.module.settings.BooleanSetting;
import com.axiom.client.module.settings.NumberSetting;
import com.axiom.client.util.EntityUtil;
import com.axiom.client.util.RenderUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Visual Detection System (VDS).
 * Three independent toggles:
 *  - Player Tracker
 *  - Mob Radar
 *  - Item Highlighter (valuable drops only)
 */
public class VisualDetectionSystem extends Module {
    private final BooleanSetting players = addSetting(new BooleanSetting("Players", true));
    private final BooleanSetting mobs = addSetting(new BooleanSetting("Mobs", true));
    private final BooleanSetting items = addSetting(new BooleanSetting("ValuableItems", true));
    private final NumberSetting radius = addSetting(new NumberSetting("Radius", 64.0, 8.0, 256.0, 4.0));

    private final List<LabelEntry> labels = new ArrayList<>();
    private Matrix4f lastPositionMatrix = new Matrix4f();
    private Matrix4f lastProjectionMatrix = new Matrix4f();

    private static final Set<Item> VALUABLE = Set.of(
            Items.DIAMOND,
            Items.EMERALD,
            Items.GOLD_INGOT,
            Items.IRON_INGOT,
            Items.COAL,
            Items.NETHERITE_INGOT,
            Items.NETHERITE_SCRAP
    );

    public VisualDetectionSystem() {
        super("VDS", "Visual detection overlays", Category.VISUAL);
        setKey(72); // H
    }

    @Override
    public void onTick(TickEvent event) {
        // Nothing here; work is done during the render loop where matrices are available.
    }

    @Override
    public void onRenderWorld(RenderWorldEvent event) {
        labels.clear();
        MinecraftClient client = event.client;
        if (client.player == null || client.world == null) return;

        Camera camera = event.camera;
        Vec3d camPos = camera.getPos();
        MatrixStack matrices = event.matrices;
        matrices.push();

        RenderSystemSnapshot snapshot = setupRender();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
        Matrix4f matrix = matrices.peek().getPositionMatrix();

        double r = radius.getValue();

        // Capture matrices for the HUD pass.
        lastPositionMatrix.set(matrix);
        float fov = client.options.getFov().getValue();
        lastProjectionMatrix.set(client.gameRenderer.getBasicProjectionMatrix(fov));

        for (Entity entity : client.world.getEntities()) {
            if (entity.isRemoved() || !entity.isAlive()) continue;
            double dist = entity.squaredDistanceTo(camPos);
            if (dist > r * r) continue;

            if (entity instanceof PlayerEntity player && players.getValue() && player != client.player) {
                drawTracer(buffer, matrix, camPos, entity, 0xFF00FFFF);
                addLabel(entity, player.getName().getString(), EntityUtil.getHealth(player), EntityUtil.getMaxHealth(player), 0xFF00FFFF);
            } else if (entity instanceof MobEntity mob && mobs.getValue()) {
                drawTracer(buffer, matrix, camPos, entity, 0xFF00FF00);
                String name = mob.getName().getString();
                addLabel(entity, name, EntityUtil.getHealth(mob), EntityUtil.getMaxHealth(mob), 0xFF00FF00);
            } else if (entity instanceof ItemEntity itemEntity && items.getValue()) {
                Item drop = itemEntity.getStack().getItem();
                if (VALUABLE.contains(drop)) {
                    drawTracer(buffer, matrix, camPos, entity, 0xFFFFFF00);
                    String label = drop.getName().getString() + " x" + itemEntity.getStack().getCount();
                    addLabel(entity, label, -1, -1, 0xFFFFFF00);
                }
            }
        }

        BufferRenderer.drawWithGlobalProgram(buffer.end());
        restoreRender(snapshot);
        matrices.pop();
    }

    @Override
    public void onRenderHud(DrawContext context, RenderTickCounter tickCounter, int screenWidth, int screenHeight) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        for (LabelEntry label : labels) {
            Optional<Vec3d> screen = RenderUtil.worldToScreen(label.worldPos, lastPositionMatrix, lastProjectionMatrix, screenWidth, screenHeight);
            if (screen.isEmpty()) continue;
            Vec3d s = screen.get();
            int x = (int) s.x;
            int y = (int) s.y;

            RenderUtil.drawLabel(context, client.textRenderer, label.text, x, y, label.color);

            if (label.maxHealth > 0) {
                int barWidth = 50;
                int filled = MathHelper.clamp((int) ((label.health / label.maxHealth) * barWidth), 0, barWidth);
                int bx = x - barWidth / 2;
                int by = y + client.textRenderer.fontHeight + 2;
                context.fill(bx, by, bx + barWidth, by + 4, 0xFF000000);
                context.fill(bx, by, bx + filled, by + 4, 0xFFFF0000);
            }
        }
    }

    private void drawTracer(BufferBuilder buffer, Matrix4f matrix, Vec3d camPos, Entity entity, int color) {
        Vec3d rel = entity.getBoundingBox().getCenter().subtract(camPos);
        vertex(buffer, matrix, 0, 0, 0, color);
        vertex(buffer, matrix, rel.x, rel.y, rel.z, color);
    }

    private void vertex(BufferBuilder buffer, Matrix4f matrix, double x, double y, double z, int color) {
        // Đã xóa .next() vì trong 1.21 VertexConsumer không còn hàm này
        buffer.vertex(matrix, (float) x, (float) y, (float) z).color(color);
    }

    private void addLabel(Entity entity, String name, float health, float maxHealth, int color) {
        MinecraftClient client = MinecraftClient.getInstance();
        double dist = entity.distanceTo(client.player);
        String text = name + " [" + String.format("%.1f", dist) + "m]";
        if (maxHealth > 0) {
            text += " " + String.format("%.1f", health) + "/" + String.format("%.1f", maxHealth);
        }
        labels.add(new LabelEntry(entity.getBoundingBox().getCenter(), text, health, maxHealth, color));
    }

    private RenderSystemSnapshot setupRender() {
        RenderSystemSnapshot s = new RenderSystemSnapshot();
        // Đã đổi sang dùng RenderSystem của Mojang thay vì Minecraft
        RenderSystem.disableTexture();
        RenderSystem.disableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        return s;
    }

    private void restoreRender(RenderSystemSnapshot s) {
        RenderSystem.enableTexture();
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
        RenderSystem.enableCull();
    }

    private static class LabelEntry {
        final Vec3d worldPos;
        final String text;
        final float health;
        final float maxHealth;
        final int color;

        LabelEntry(Vec3d worldPos, String text, float health, float maxHealth, int color) {
            this.worldPos = worldPos;
            this.text = text;
            this.health = health;
            this.maxHealth = maxHealth;
            this.color = color;
        }
    }

    private static class RenderSystemSnapshot {
        // Placeholder to make setup/restore symmetric; actual state is handled by RenderSystem.
    }
}
