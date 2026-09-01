package moddedmite.xylose.bettergamesetting.client.audio;

import net.minecraft.BossStatus;
import net.minecraft.GuiWinGame;
import net.minecraft.Minecraft;
import net.minecraft.IUpdatePlayerListBox;
import net.minecraft.MathHelper;
import net.minecraft.ResourceLocation;
import net.minecraft.WorldProviderEnd;
import net.minecraft.WorldProviderHell;
import net.minecraft.WorldProviderUnderworld;

import java.util.Random;

public class MusicTicker implements IUpdatePlayerListBox {
    private final Random rand = new Random();
    private final Minecraft client;
    private ISound sound;
    private int delay = 100;

    public MusicTicker(Minecraft client) {
        this.client = client;
    }

    /**
     * Updates the JList with a new model.
     */
    public void update() {
        MusicType musictype = this.getCurrentMusicType(this.client);

        if (this.sound != null) {
            if (!musictype.getMusicTickerLocation().equals(this.sound.getSoundLocation())) {
                this.client.getSoundHandler().stopSound(this.sound);
                this.delay = MathHelper.getRandomIntegerInRange(this.rand, 0, musictype.getMinDelay() / 2);
            }

            if (!this.client.getSoundHandler().isSoundPlaying(this.sound)) {
                this.sound = null;
                this.delay = Math.min(MathHelper.getRandomIntegerInRange(this.rand, musictype.getMinDelay(), musictype.getMaxDelay()), this.delay);
            }
        }

        if (musictype == MusicType.CREDITS && this.sound == null) {
            this.delay = 0;
        }

        if (this.sound == null && this.delay-- <= 0) {
            this.sound = PositionedSoundRecord.of(musictype.getMusicTickerLocation());
            this.client.getSoundHandler().playSound(this.sound);
            this.delay = Integer.MAX_VALUE;
        }
    }

	public MusicType getCurrentMusicType(Minecraft client) {
		if (client.currentScreen instanceof GuiWinGame) {
			return MusicType.CREDITS;
		}

		if (client.thePlayer == null) {
			return MusicType.MENU;
		}

		if (client.thePlayer.worldObj.provider instanceof WorldProviderUnderworld) {
			return MusicType.UNDERWORLD;
		}

		if (client.thePlayer.worldObj.provider instanceof WorldProviderHell) {
			return MusicType.NETHER;
		}

		if (client.thePlayer.worldObj.provider instanceof WorldProviderEnd) {
			if (BossStatus.bossName != null && BossStatus.statusBarLength > 0) {
				return MusicType.END_BOSS;
			}
			return MusicType.END;
		}

		if (client.thePlayer.capabilities.isCreativeMode && client.thePlayer.capabilities.allowFlying) {
			return MusicType.CREATIVE;
		}

		return MusicType.GAME;
	}

    public static enum MusicType {
        MENU(new ResourceLocation("minecraft:music.menu"), 20, 600),
        GAME(new ResourceLocation("minecraft:music.game"), 12000, 24000),
        CREATIVE(new ResourceLocation("minecraft:music.game.creative"), 1200, 3600),
        CREDITS(new ResourceLocation("minecraft:music.game.end.credits"), Integer.MAX_VALUE, Integer.MAX_VALUE),
        UNDERWORLD(new ResourceLocation("bgs:music.game.underworld"), 1200, 3600),
        NETHER(new ResourceLocation("minecraft:music.game.nether"), 1200, 3600),
        END_BOSS(new ResourceLocation("minecraft:music.game.end.dragon"), 0, 0),
        END(new ResourceLocation("minecraft:music.game.end"), 6000, 24000);

        private final ResourceLocation identity;
        private final int minDelay;
        private final int maxDelay;


        private MusicType(ResourceLocation id, int minDelay, int maxDelay) {
            this.identity = id;
            this.minDelay = minDelay;
            this.maxDelay = maxDelay;
        }

        public ResourceLocation getMusicTickerLocation() {
            return this.identity;
        }

        public int getMinDelay() {
            return this.minDelay;
        }

        public int getMaxDelay() {
            return this.maxDelay;
        }
    }
}
