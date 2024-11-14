package moddedmite.xylose.bettergamesetting.mixin.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moddedmite.xylose.bettergamesetting.api.IGameSetting;
import net.minecraft.*;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EntityRenderer.class, priority = 9999)
public abstract class EntityRenderMixin {
    @Shadow private float farPlaneDistance;
    @Shadow private Minecraft mc;
    @Shadow protected abstract void setupFog(int par1, float par2);
    @Shadow private float fogColorRed;
    @Shadow private float fogColorGreen;
    @Shadow private float fogColorBlue;
    @Shadow protected abstract double getInterpolatedPosYForVoidFog(EntityLivingBase viewer, float partial_tick);
    @Shadow private float field_82831_U;
    @Shadow private float field_82832_V;
    @Shadow private boolean cloudFog;
    @Shadow public int debugViewDirection;
    @Shadow private float fovModifierHandPrev;
    @Shadow private float fovModifierHand;
    @Shadow private float prevDebugCamFOV;
    @Shadow private float debugCamFOV;

    @Inject(method = "setupCameraTransform", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glLoadIdentity()V", shift = At.Shift.AFTER))
    private void setupCameraTransform0(float par1, int par2, boolean extend_far_clipping_plane, CallbackInfo ci) {
        this.farPlaneDistance = this.mc.gameSettings.getRenderDistance() * 16;
    }

    @Inject(method = "renderWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/ClippingHelperImpl;getInstance()Lnet/minecraft/ClippingHelper;"))
    private void renderWorld(float par1, long par2, CallbackInfo ci) {
        RenderGlobal renderGlobal = this.mc.renderGlobal;
        if ((this.mc.gameSettings.getRenderDistance() >= 4)) {
            setupFog(-1, par1);
            this.mc.mcProfiler.endStartSection("sky");
            renderGlobal.renderSky(par1);
        }
    }

//    @Inject(method = "updateFogColor", at = @At(value = "INVOKE", target = "Lnet/minecraft/WorldClient;getRainStrength(F)F"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
//    private void updateFogColor(float par1, CallbackInfo ci, WorldClient var2, EntityLivingBase var3, float var4, Vec3 var5, float var6, float var7, float var8) {
//        var4 = 1.0f - ((float) Math.pow(0.25f + ((0.75f * this.mc.gameSettings.getRenderDistance()) / 32.0f), 0.25d));
//        this.fogColorRed += (var6 - this.fogColorRed) * var4;
//        this.fogColorGreen += (var7 - this.fogColorGreen) * var4;
//        this.fogColorBlue += (var8 - this.fogColorBlue) * var4;
//    }

//    @Inject(method = "updateFogColor", at = @At(value = "INVOKE", target = "Lnet/minecraft/WorldClient;getFogColor(FLnet/minecraft/EntityLivingBase;)Lnet/minecraft/Vec3;", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
//    private void updateFogColor_1(float par1, CallbackInfo ci, WorldClient var2, EntityLivingBase var3, float var4, Vec3 var5, float var6, float var7, float var8) {
//        float var10;
//        if (this.mc.gameSettings.getRenderDistance() >= 4) {
//            Vec3 var11 = MathHelper.sin(var2.getCelestialAngleRadians(par1)) > 0.0F ? var2.getWorldVec3Pool().getVecFromPool(-1.0D, 0.0D, 0.0D) : var2.getWorldVec3Pool().getVecFromPool(1.0D, 0.0D, 0.0D);
//            var10 = (float) var3.getLook(par1).dotProduct(var11);
//
//            if (var10 < 0.0F) {
//                var10 = 0.0F;
//            }
//
//            if (var10 > 0.0F) {
//                float[] var12 = var2.provider.calcSunriseSunsetColors(var2.getCelestialAngle(par1), par1);
//
//                if (var12 != null) {
//                    var10 *= var12[3];
//                    this.fogColorRed = this.fogColorRed * (1.0F - var10) + var12[0] * var10;
//                    this.fogColorGreen = this.fogColorGreen * (1.0F - var10) + var12[1] * var10;
//                    this.fogColorBlue = this.fogColorBlue * (1.0F - var10) + var12[2] * var10;
//                }
//            }
//        }
//    }
    @Overwrite
    private void updateFogColor(float par1) {
        WorldClient var2 = this.mc.theWorld;
        EntityLivingBase var3 = this.mc.renderViewEntity;
        float var4 = 1.0f - ((float) Math.pow(0.25f + ((0.75f * this.mc.gameSettings.getRenderDistance()) / 32.0f), 0.25d));
        Vec3 var5 = var2.getSkyColor(this.mc.renderViewEntity, par1);
        float var6 = (float) var5.xCoord;
        float var7 = (float) var5.yCoord;
        float var8 = (float) var5.zCoord;
        Vec3 var9 = var2.getFogColor(par1, var3);
        this.fogColorRed = (float) var9.xCoord;
        this.fogColorGreen = (float) var9.yCoord;
        this.fogColorBlue = (float) var9.zCoord;
        float var10;

        if (this.mc.gameSettings.getRenderDistance() >= 4) {
            Vec3 var11 = MathHelper.sin(var2.getCelestialAngleRadians(par1)) > 0.0F ? var2.getWorldVec3Pool().getVecFromPool(-1.0D, 0.0D, 0.0D) : var2.getWorldVec3Pool().getVecFromPool(1.0D, 0.0D, 0.0D);
            var10 = (float) var3.getLook(par1).dotProduct(var11);

            if (var10 < 0.0F) {
                var10 = 0.0F;
            }

            if (var10 > 0.0F) {
                float[] var12 = var2.provider.calcSunriseSunsetColors(var2.getCelestialAngle(par1), par1);

                if (var12 != null) {
                    var10 *= var12[3];
                    this.fogColorRed = this.fogColorRed * (1.0F - var10) + var12[0] * var10;
                    this.fogColorGreen = this.fogColorGreen * (1.0F - var10) + var12[1] * var10;
                    this.fogColorBlue = this.fogColorBlue * (1.0F - var10) + var12[2] * var10;
                }
            }
        }

        this.fogColorRed += (var6 - this.fogColorRed) * var4;
        this.fogColorGreen += (var7 - this.fogColorGreen) * var4;
        this.fogColorBlue += (var8 - this.fogColorBlue) * var4;
        float var20 = var2.getRainStrength(par1);
        float var21;

        if (var20 > 0.0F) {
            var10 = 1.0F - var20 * 0.5F;
            var21 = 1.0F - var20 * 0.4F;
            this.fogColorRed *= var10;
            this.fogColorGreen *= var10;
            this.fogColorBlue *= var21;
        }

        var10 = var2.getWeightedThunderStrength(par1);

        if (var10 > 0.0F) {
            var21 = 1.0F - var10 * 0.5F;
            this.fogColorRed *= var21;
            this.fogColorGreen *= var21;
            this.fogColorBlue *= var21;
        }

        int var13 = ActiveRenderInfo.getBlockIdAtEntityViewpoint(this.mc.theWorld, var3, par1);

        if (this.cloudFog) {
            Vec3 var15 = var2.getCloudColour(par1);
            this.fogColorRed = (float) var15.xCoord;
            this.fogColorGreen = (float) var15.yCoord;
            this.fogColorBlue = (float) var15.zCoord;
        } else if (var13 != 0 && Block.blocksList[var13].blockMaterial == Material.water) {
            float var14 = (float) EnchantmentHelper.getRespiration(var3) * 0.2F;
            this.fogColorRed = 0.02F + var14;
            this.fogColorGreen = 0.02F + var14;
            this.fogColorBlue = 0.2F + var14;
        } else if (var13 != 0 && Block.blocksList[var13].blockMaterial == Material.lava) {
            this.fogColorRed = 0.6F;
            this.fogColorGreen = 0.1F;
            this.fogColorBlue = 0.0F;
        }

        double var22 = this.mc.theWorld.hasSkylight() ? this.getInterpolatedPosYForVoidFog(var3, par1) * var2.provider.getVoidFogYFactor() : 1.0D;

        if (var3.isPotionActive(Potion.blindness)) {
            int var17 = var3.getActivePotionEffect(Potion.blindness).getDuration();

            if (var17 < 20) {
                var22 *= (double) (1.0F - (float) var17 / 20.0F);
            } else {
                var22 = 0.0D;
            }
        }

        if (var22 < 1.0D) {
            if (var22 < 0.0D) {
                var22 = 0.0D;
            }

            var22 *= var22;
            this.fogColorRed = (float) ((double) this.fogColorRed * var22);
            this.fogColorGreen = (float) ((double) this.fogColorGreen * var22);
            this.fogColorBlue = (float) ((double) this.fogColorBlue * var22);
        }

        float var23;

        if (this.field_82831_U > 0.0F) {
            var23 = this.field_82832_V + (this.field_82831_U - this.field_82832_V) * par1;
            this.fogColorRed = this.fogColorRed * (1.0F - var23) + this.fogColorRed * 0.7F * var23;
            this.fogColorGreen = this.fogColorGreen * (1.0F - var23) + this.fogColorGreen * 0.6F * var23;
            this.fogColorBlue = this.fogColorBlue * (1.0F - var23) + this.fogColorBlue * 0.6F * var23;
        }

        if (this.mc.gameSettings.anaglyph) {
            var23 = (this.fogColorRed * 30.0F + this.fogColorGreen * 59.0F + this.fogColorBlue * 11.0F) / 100.0F;
            float var18 = (this.fogColorRed * 30.0F + this.fogColorGreen * 70.0F) / 100.0F;
            float var19 = (this.fogColorRed * 30.0F + this.fogColorBlue * 70.0F) / 100.0F;
            this.fogColorRed = var23;
            this.fogColorGreen = var18;
            this.fogColorBlue = var19;
        }

        GL11.glClearColor(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 0.0F);
    }

    @Overwrite
    public static int performanceToFps(int par0) {
        if (!(Minecraft.getMinecraft().gameSettings.limitFramerate >= 260)) {
            return Minecraft.getMinecraft().gameSettings.limitFramerate;
        }
        return 9999;
    }

    @WrapOperation(method = "addRainParticles", at = @At(value = "INVOKE", target = "Lnet/minecraft/WorldClient;playSound(DDDLjava/lang/String;FFZ)V"))
    private void modifyRainSound(WorldClient instance, double v, double par1, double par3, String par5, float par7Str, float par8, boolean par9, Operation<Void> original) {
        this.mc.theWorld.playSound(v, par1, par3, par5, ((IGameSetting) Minecraft.getMinecraft().gameSettings).getWeatherVolume() * par7Str, par8, par9);
    }

//    @ModifyConstant(method = "getFOVModifier", constant = @Constant(floatValue = 70.0F))
//    private float getFOVModifier(float constant) {
//        return 30.0F;
//    }

//    @Inject(method = "updateFovModifierHand", at = @At("HEAD"), cancellable = true)
//    private void updateFovModifierHand(CallbackInfo ci) {
//        ci.cancel();
//    }

//    @Overwrite
//    private float getFOVModifier(float par1, boolean par2) {
//        if (this.debugViewDirection > 0) {
//            return 90.0F;
//        } else {
//            EntityPlayer var3 = (EntityPlayer) this.mc.renderViewEntity;
//            float var4 = 70.0F;
//
//            if (par2) {
//                var4 *= this.mc.gameSettings.fovSetting;
//                var4 *= this.fovModifierHandPrev + (this.fovModifierHand - this.fovModifierHandPrev) * par1;
//            }
//
//            if (var3.getHealth() <= 0.0F) {
//                float var5 = (float) var3.deathTime + par1;
//                var4 /= (1.0F - 500.0F / (var5 + 500.0F)) * 2.0F + 1.0F;
//            }
//
//            int var6 = ActiveRenderInfo.getBlockIdAtEntityViewpoint(this.mc.theWorld, var3, par1);
//
//            if (var6 != 0 && Block.blocksList[var6].blockMaterial == Material.water) {
//                var4 = var4 * 60.0F / 70.0F;
//            }
//
//            return var4 + this.prevDebugCamFOV + (this.debugCamFOV - this.prevDebugCamFOV) * par1;
//        }
//    }
}
