package moddedmite.xylose.bettergamesetting.client.gui;

import com.google.common.collect.Lists;
import moddedmite.xylose.bettergamesetting.client.audio.ISound;
import moddedmite.xylose.bettergamesetting.client.audio.ISoundEventListener;
import moddedmite.xylose.bettergamesetting.client.audio.SoundEventAccessor;
import moddedmite.xylose.bettergamesetting.util.Mth;
import net.minecraft.Gui;
import net.minecraft.MathHelper;
import net.minecraft.Minecraft;
import net.minecraft.ScaledResolution;
import net.minecraft.Vec3;
import org.lwjgl.opengl.GL11;

import java.util.Iterator;
import java.util.List;

public class GuiSubtitleOverlay extends Gui implements ISoundEventListener {
    private final Minecraft client;
    private final List<GuiSubtitleOverlay.Subtitle> subtitles = Lists.<GuiSubtitleOverlay.Subtitle>newArrayList();
    private boolean enabled;

    public GuiSubtitleOverlay(Minecraft clientIn) {
        this.client = clientIn;
    }

    public void renderSubtitles(ScaledResolution resolution) {
        if (!this.enabled && this.client.gameSettings.isShowSubtitles()) {
            this.client.getSoundHandler().addListener(this);
            this.enabled = true;
        } else if (this.enabled && !this.client.gameSettings.isShowSubtitles()) {
            this.client.getSoundHandler().removeListener(this);
            this.enabled = false;
        }

        if (this.enabled && this.client.thePlayer != null && !this.subtitles.isEmpty()) {
            GL11.glPushMatrix();
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            Vec3 vec3 = Vec3.createVectorHelper(this.client.thePlayer.posX, this.client.thePlayer.posY + (double) this.client.thePlayer.getEyeHeight(), this.client.thePlayer.posZ);
            Vec3 vec3_1 = Vec3.createVectorHelper(0.0D, 0.0D, -1.0D);
            vec3_1.rotateAroundX(-this.client.thePlayer.rotationPitch * 0.017453292F);
            vec3_1.rotateAroundY(-this.client.thePlayer.rotationYaw * 0.017453292F);
            Vec3 vec3_2 = Vec3.createVectorHelper(0.0D, 1.0D, 0.0D);
            vec3_2.rotateAroundX(-this.client.thePlayer.rotationPitch * 0.017453292F);
            vec3_2.rotateAroundY(-this.client.thePlayer.rotationYaw * 0.017453292F);
            Vec3 vec3_3 = vec3_1.crossProduct(vec3_2);
            int i = 0;
            int j = 0;
            Iterator<GuiSubtitleOverlay.Subtitle> iterator = this.subtitles.iterator();

            while (iterator.hasNext()) {
                GuiSubtitleOverlay.Subtitle subtitle = iterator.next();
                if (subtitle.getStartTime() + 3000L <= Minecraft.getSystemTime()) {
                    iterator.remove();
                } else {
                    j = Math.max(j, this.client.fontRenderer.getStringWidth(subtitle.getString()));
                }
            }

            j = j + this.client.fontRenderer.getStringWidth("<") + this.client.fontRenderer.getStringWidth(" ") + this.client.fontRenderer.getStringWidth(">") + this.client.fontRenderer.getStringWidth(" ");

            for (GuiSubtitleOverlay.Subtitle subtitle1 : this.subtitles) {
                String s = subtitle1.getString();
                Vec3 vec3_4 = subtitle1.getLocation().subtract(vec3).normalize();
                double d0 = -vec3_3.dotProduct(vec3_4);
                double d1 = -vec3_1.dotProduct(vec3_4);
                boolean flag = d1 > 0.5D;
                int l = j / 2;
                int i1 = this.client.fontRenderer.FONT_HEIGHT;
                int j1 = i1 / 2;
                int k1 = this.client.fontRenderer.getStringWidth(s);
                double frac = (double) (Minecraft.getSystemTime() - subtitle1.getStartTime()) / 3000.0D;
                int l1 = MathHelper.floor_double(Mth.clamp(Mth.lerp(frac, 255.0D, 75.0D), 75.0D, 255.0D));
                int i2 = l1 << 16 | l1 << 8 | l1;
                GL11.glPushMatrix();
                GL11.glTranslatef((float) resolution.getScaledWidth() - (float) l - 2.0F, (float) (resolution.getScaledHeight() - 30) - (float) (i * (i1 + 1)), 0.0F);
                this.drawRect(-l - 1, -j1 - 1, l + 1, j1 + 1, -872415232);
                GL11.glEnable(GL11.GL_BLEND);

                if (!flag) {
                    if (d0 > 0.0D) {
                        this.client.fontRenderer.drawString(">", l - this.client.fontRenderer.getStringWidth(">"), -j1, i2 + -16777216);
                    } else if (d0 < 0.0D) {
                        this.client.fontRenderer.drawString("<", -l, -j1, i2 + -16777216);
                    }
                }

                this.client.fontRenderer.drawString(s, -k1 / 2, -j1, i2 + -16777216);
                GL11.glPopMatrix();
                ++i;
            }

            GL11.glDisable(GL11.GL_BLEND);
            GL11.glPopMatrix();
        }
    }

    public void soundPlay(ISound soundIn, SoundEventAccessor accessor) {
        if (accessor != null && accessor.getSubtitle() != null) {
            String s = accessor.getSubtitle().toString();

            if (!this.subtitles.isEmpty()) {
                for (GuiSubtitleOverlay.Subtitle subtitle : this.subtitles) {
                    if (subtitle.getString().equals(s)) {
                        subtitle.refresh(Vec3.createVectorHelper((double) soundIn.getXPosF(), (double) soundIn.getYPosF(), (double) soundIn.getZPosF()));
                        return;
                    }
                }
            }

            this.subtitles.add(new GuiSubtitleOverlay.Subtitle(s, Vec3.createVectorHelper((double) soundIn.getXPosF(), (double) soundIn.getYPosF(), (double) soundIn.getZPosF())));
        }
    }

    public class Subtitle {
        private final String subtitle;
        private long startTime;
        private Vec3 location;

        public Subtitle(String subtitleIn, Vec3 locationIn) {
            this.subtitle = subtitleIn;
            this.location = locationIn;
            this.startTime = Minecraft.getSystemTime();
        }

        public String getString() {
            return this.subtitle;
        }

        public long getStartTime() {
            return this.startTime;
        }

        public Vec3 getLocation() {
            return this.location;
        }

        public void refresh(Vec3 locationIn) {
            this.location = locationIn;
            this.startTime = Minecraft.getSystemTime();
        }
    }
}
