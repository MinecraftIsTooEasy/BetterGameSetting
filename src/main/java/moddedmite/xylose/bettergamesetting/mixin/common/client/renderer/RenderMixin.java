package moddedmite.xylose.bettergamesetting.mixin.common.client.renderer;

import net.minecraft.Minecraft;
import net.minecraft.Render;
import net.minecraft.ResourceLocation;
import net.minecraft.ResourcePackRepositoryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;

@Mixin(Render.class)
public class RenderMixin {
    @Shadow protected ResourceLocation[] textures_glowing = new ResourceLocation[16];

    @Inject(method = "setTexture(ILjava/lang/String;Ljava/lang/String;)V", at = @At("TAIL"))
    protected void setTexture(int index, String path, String glow_path, CallbackInfo ci) {
        ResourceLocation texture_glowing = new ResourceLocation(glow_path + "_glow.png", false);
        Iterator var2 = Minecraft.getMinecraft().mcResourcePackRepository.getRepositoryEntries().iterator();
        ResourcePackRepositoryEntry var3 = null;
        while (var2.hasNext()) {
            var3 = (ResourcePackRepositoryEntry) var2.next();
        }
        if (var3 != null && var3.getResourcePack().resourceExists(texture_glowing)) {
            this.textures_glowing[index] = texture_glowing;
        }
    }
}
