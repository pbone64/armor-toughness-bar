package dev.pbone.ArmorToughnessBar.mixin;

import dev.pbone.ArmorToughnessBar.ToughnessBarHud;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Inject(method = "renderStatusBars", at = @At("TAIL"))
    private void renderStatusBars(DrawContext drawContext, CallbackInfo ci) {
        ToughnessBarHud.render(drawContext);
    }
}
