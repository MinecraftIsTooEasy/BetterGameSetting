package moddedmite.xylose.bettergamesetting.client.audio;

import moddedmite.xylose.bettergamesetting.api.IWorldProvider;
import net.minecraft.BossStatus;
import net.minecraft.GuiWinGame;
import net.minecraft.Minecraft;
import net.minecraft.IUpdatePlayerListBox;
import net.minecraft.MathHelper;
import net.minecraft.WorldProviderEnd;
import net.minecraft.WorldProviderHell;
import net.minecraft.WorldProviderUnderworld;

import java.util.Random;

public class MusicTicker implements IUpdatePlayerListBox {
	private final Random rand = new Random();
	private final Minecraft client;
	private ISound currentMusic;
	private int timeUntilNextMusic = 100;
	
	public MusicTicker(Minecraft client) {
		this.client = client;
	}
	
	/**
	 * Like the old updateEntity(), except more generic.
	 */
	public void update() {
		MusicType musictype = this.getAmbientMusicType();
		
		if (this.currentMusic != null) {
			if (!musictype.getMusicLocation().soundName().equals(this.currentMusic.getSoundLocation())) {
				this.client.getSoundHandler().stopSound(this.currentMusic);
				this.timeUntilNextMusic = MathHelper.getRandomIntegerInRange(this.rand, 0, musictype.getMinDelay() / 2);
			}
			
			if (!this.client.getSoundHandler().isSoundPlaying(this.currentMusic)) {
				this.currentMusic = null;
				this.timeUntilNextMusic = Math.min(MathHelper.getRandomIntegerInRange(this.rand, musictype.getMinDelay(), musictype.getMaxDelay()), this.timeUntilNextMusic);
			}
		}
		
		this.timeUntilNextMusic = Math.min(this.timeUntilNextMusic, musictype.getMaxDelay());
		
		if (this.currentMusic == null && this.timeUntilNextMusic-- <= 0) {
			this.playMusic(musictype);
		}
	}
	
	public MusicType getAmbientMusicType() {
		if (this.client.currentScreen instanceof GuiWinGame) {
			return MusicType.CREDITS;
		} else if (this.client.thePlayer != null) {
			MusicType type = ((IWorldProvider) this.client.theWorld.provider).getMusicType();
			if (type != null) return type;
			if (this.client.thePlayer.worldObj.provider instanceof WorldProviderHell) {
				return MusicType.NETHER;
			} else if (this.client.thePlayer.worldObj.provider instanceof WorldProviderUnderworld) {
				return MusicType.UNDERWORLD;
			} else if (this.client.thePlayer.worldObj.provider instanceof WorldProviderEnd) {
				return BossStatus.bossName != null && BossStatus.statusBarLength > 0 ? MusicType.END_BOSS : MusicType.END;
			} else {
				return this.client.thePlayer.capabilities.isCreativeMode && this.client.thePlayer.capabilities.allowFlying ? MusicType.CREATIVE : MusicType.GAME;
			}
		} else {
			return MusicType.MENU;
		}
	}
	
	public void playMusic(MusicTicker.MusicType requestedMusicType) {
		this.currentMusic = PositionedSoundRecord.getMusicRecord(requestedMusicType.getMusicLocation());
		this.client.getSoundHandler().playSound(this.currentMusic);
		this.timeUntilNextMusic = Integer.MAX_VALUE;
	}
	
	public static enum MusicType {
		MENU(SoundEvents.MUSIC_MENU, 20, 600),
		GAME(SoundEvents.MUSIC_GAME, 12000, 24000),
		CREATIVE(SoundEvents.MUSIC_CREATIVE, 1200, 3600),
		CREDITS(SoundEvents.MUSIC_CREDITS, 0, 0),
		UNDERWORLD(SoundEvents.MUSIC_UNDERWORLD, 1200, 3600),
		NETHER(SoundEvents.MUSIC_NETHER, 1200, 3600),
		END_BOSS(SoundEvents.MUSIC_DRAGON, 0, 0),
		END(SoundEvents.MUSIC_END, 6000, 24000);
		
		private final SoundEvent sound;
		private final int minDelay;
		private final int maxDelay;
		
		private MusicType(SoundEvent sound, int minDelay, int maxDelay) {
			this.sound = sound;
			this.minDelay = minDelay;
			this.maxDelay = maxDelay;
		}
		
		public SoundEvent getMusicLocation() {
			return this.sound;
		}
		
		public int getMinDelay() {
			return this.minDelay;
		}
		
		public int getMaxDelay() {
			return this.maxDelay;
		}
	}
}