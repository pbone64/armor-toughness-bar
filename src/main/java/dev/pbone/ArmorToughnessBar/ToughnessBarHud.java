package dev.pbone.ArmorToughnessBar;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.ClientModInitializer;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.Identifier;

public class ToughnessBarHud {
	public static final String MOD_ID = "armor-toughness-bar";

	public static void render(DrawContext drawContext) {
		MinecraftClient client = MinecraftClient.getInstance();

		if (client.options.hudHidden
				|| client.interactionManager == null
				|| !client.interactionManager.hasStatusBars()
				|| !(client.cameraEntity instanceof PlayerEntity player)
		) {
			return;
		}

		int toughness = (int) player.getAttributeValue(EntityAttributes.GENERIC_ARMOR_TOUGHNESS);
		if (toughness <= 0) {
			return;
		}

		client.getProfiler().push("armor_toughness_bar");

		int windowWidth = drawContext.getScaledWindowWidth();
		int windowHeight = drawContext.getScaledWindowHeight();

		boolean missingAir = player.getAir() < player.getMaxAir();
		boolean submergedInWater = player.isSubmergedIn(FluidTags.WATER);
		boolean areBubblesVisible = missingAir || submergedInWater;

		int anchorX = windowWidth / 2 + 82;
		int anchorY = windowHeight - 49;

		if (areBubblesVisible) {
			anchorY -= 10;
		}

		RenderSystem.enableBlend();

		final int NUM_ICONS = 10;
		for (int i = 0; i < NUM_ICONS; i++) {
            ToughnessIcon icon;

			if (toughness >= 2) {
				icon = ToughnessIcon.FULL;
				toughness -= 2;
			} else if (toughness == 1) {
				icon = ToughnessIcon.HALF;
				toughness -= 1;
			} else {
				icon = ToughnessIcon.EMPTY;
			}

			icon.draw(
					drawContext,
					anchorX - i * 8,
					anchorY
			);
		}

		RenderSystem.disableBlend();

		client.getProfiler().pop();
    }

	public enum ToughnessIcon {
		FULL("full"),
		HALF("half"),
		EMPTY("empty");

		public static final int WIDTH = 9;
		public static final int HEIGHT = 9;

		private final Identifier texture;

		ToughnessIcon(String suffix) {
			this.texture = Identifier.of(MOD_ID, "textures/gui/toughness_" + suffix + ".png");
		}

		public void draw(DrawContext context, int x, int y) {
			context.drawTexture(
					texture,
					x,
					y,
					0,
					0,
					0,
					WIDTH,
					HEIGHT,
					WIDTH,
					HEIGHT
			);
		}
	}
}