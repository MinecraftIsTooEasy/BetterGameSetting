package moddedmite.xylose.bettergamesetting.mixin.client.renderer;

import net.minecraft.AxisAlignedBB;
import net.minecraft.RenderItem;
import net.minecraft.WorldRenderer;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {
	@Shadow private int glRenderList;
	@Shadow public int posXClip;
	@Shadow public int posYClip;
	@Shadow public int posZClip;
	@Unique private boolean occlusionQueryListDirty = true;

	@Redirect(method = "setPosition", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glNewList(II)V"))
	private void skipOcclusionQueryListBegin(int list, int mode) {
	}

	@Redirect(method = "setPosition", at = @At(value = "INVOKE", target = "Lnet/minecraft/RenderItem;renderAABB(Lnet/minecraft/AxisAlignedBB;)V"))
	private void markOcclusionQueryListDirty(AxisAlignedBB boundingBox) {
		this.occlusionQueryListDirty = true;
	}

	@Redirect(method = "setPosition", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glEndList()V"))
	private void skipOcclusionQueryListEnd() {
	}

	@Inject(method = "callOcclusionQueryList", at = @At("HEAD"), cancellable = true)
	private void callOcclusionQueryList(CallbackInfo ci) {
		if (this.occlusionQueryListDirty) {
			float var1 = 6.0F;
			GL11.glNewList(this.glRenderList + 2, 4864);
			RenderItem.renderAABB(AxisAlignedBB.getAABBPool().getAABB((float) this.posXClip - var1, (float) this.posYClip - var1, (float) this.posZClip - var1, (float) (this.posXClip + 16) + var1, (float) (this.posYClip + 16) + var1, (float) (this.posZClip + 16) + var1));
			GL11.glEndList();
			this.occlusionQueryListDirty = false;
		}
		GL11.glCallList(this.glRenderList + 2);
		ci.cancel();
	}
}
