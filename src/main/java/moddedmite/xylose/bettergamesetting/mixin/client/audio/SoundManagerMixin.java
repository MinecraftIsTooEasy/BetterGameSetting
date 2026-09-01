package moddedmite.xylose.bettergamesetting.mixin.client.audio;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import moddedmite.xylose.bettergamesetting.api.ISoundManager;
import moddedmite.xylose.bettergamesetting.client.audio.ISound;
import moddedmite.xylose.bettergamesetting.client.audio.ITickableSound;
import moddedmite.xylose.bettergamesetting.client.audio.SoundCategory;
import moddedmite.xylose.bettergamesetting.client.audio.SoundEvent;
import moddedmite.xylose.bettergamesetting.client.audio.SoundEventAccessorComposite;
import moddedmite.xylose.bettergamesetting.client.audio.SoundHandler;
import net.minecraft.GameSettings;
import net.minecraft.MathHelper;
import net.minecraft.Minecraft;
import net.minecraft.ResourceLocation;
import net.minecraft.SoundManager;
import net.minecraft.SoundPoolEntry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulscode.sound.SoundSystem;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Mixin(value = SoundManager.class, priority = 999)
public abstract class SoundManagerMixin implements ISoundManager {
	@Shadow private boolean loaded;
	@Shadow public SoundSystem sndSystem;
	@Shadow @Final private Set<String> playingSounds;
	@Shadow @Final private GameSettings options;
	@Shadow private int latestSoundID;

	@Unique private int playTime = 0;
	@Unique private final BiMap<String, ISound> playingSoundsMap = HashBiMap.create();
	@Unique private Map<ISound, String> invPlayingSounds;
	@Unique private final Map<ISound, SoundPoolEntry> playingSoundPoolEntries = Maps.newHashMap();
	@Unique private final Multimap<SoundCategory, String> categorySounds = HashMultimap.create();
	@Unique private final List<ITickableSound> tickableSounds = Lists.newArrayList();
	@Unique private final Map<ISound, Integer> delayedSounds = Maps.newHashMap();
	@Unique private final Map<String, Integer> playingSoundsStopTime = Maps.newHashMap();

	@Inject(method = "<init>", at = @At("TAIL"))
	private void init(CallbackInfo info) {
		this.invPlayingSounds = this.playingSoundsMap.inverse();
	}

	@WrapOperation(method = "playSound", at = @At(value = "INVOKE", target = "Lpaulscode/sound/SoundSystem;setVolume(Ljava/lang/String;F)V"))
	private void applyCategoryVolume(SoundSystem instance, String sourcename, float value, Operation<Void> original, @Local(argsOnly = true) String par1Str) {
		instance.setVolume(sourcename, value * this.getSoundCategoryVolume(this.getCategoryForSoundPath(par1Str)));
	}

	@WrapOperation(method = "playEntitySound", at = @At(value = "INVOKE", target = "Lpaulscode/sound/SoundSystem;setVolume(Ljava/lang/String;F)V"))
	private void applyEntityCategoryVolume(SoundSystem instance, String sourcename, float value, Operation<Void> original, @Local(argsOnly = true) String par1Str) {
		instance.setVolume(sourcename, value * this.getSoundCategoryVolume(this.getCategoryForSoundPath(par1Str)));
	}

	@WrapOperation(method = "playLongDistanceSound", at = @At(value = "INVOKE", target = "Lpaulscode/sound/SoundSystem;setVolume(Ljava/lang/String;F)V"))
	private void applyLongDistanceCategoryVolume(SoundSystem instance, String sourcename, float value, Operation<Void> original, @Local(argsOnly = true) String par1Str) {
		instance.setVolume(sourcename, value * this.getSoundCategoryVolume(this.getCategoryForSoundPath(par1Str)));
	}

	@WrapOperation(method = "playStreaming", at = @At(value = "INVOKE", target = "Lpaulscode/sound/SoundSystem;setVolume(Ljava/lang/String;F)V"))
	private void applyRecordCategoryVolume(SoundSystem instance, String sourcename, float value, Operation<Void> original) {
		instance.setVolume(sourcename, value * this.getSoundCategoryVolume(SoundCategory.RECORDS));
	}
	
	@WrapOperation(method = "playSoundFX", at = @At(value = "INVOKE", target = "Lpaulscode/sound/SoundSystem;setVolume(Ljava/lang/String;F)V"))
	public void applyFXVolume(SoundSystem instance, String sourcename, float value, Operation<Void> original) {
		instance.setVolume(sourcename, value * this.getSoundCategoryVolume(SoundCategory.UI));
	}
		
		
		/**
		 * @author Xy_Lose
		 * @reason also stop the 1.7.10 style sounds
		 */
	@Overwrite
	public void stopAllSounds() {
		if (this.loaded) {
			for (String o : this.playingSounds) {
				this.sndSystem.stop(o);
			}
			this.playingSounds.clear();
			for (ISound sound : this.playingSoundsMap.values()) {
				String s = this.invPlayingSounds.get(sound);
				if (s != null) {
					this.sndSystem.stop(s);
				}
			}
		}
		this.playingSoundsMap.clear();
		this.categorySounds.clear();
		this.tickableSounds.clear();
		this.delayedSounds.clear();
		this.playingSoundsStopTime.clear();
		this.playingSoundPoolEntries.clear();
	}

	/**
	 * @author Xy_Lose
	 * @reason disable MITE's native background music: 1.7.10's SoundManager has no native music,
	 * music is exclusively driven by MusicTicker -> SoundHandler.playSound(ISound). Without this,
	 * MITE's playRandomMusicIfReady() (called every tick from PlayerControllerMP.updateController)
	 * plays a random track on the "BgMusic" channel alongside the port's music -> double music in any scene.
	 */
	@Overwrite
	public void playRandomMusicIfReady() {
	}

	/**
	 * @author Xy_Lose
	 * @reason disable MITE's native BgMusic/streaming volume handling: 1.7.10 applies volume per-category
	 * via setSoundCategoryVolume. MITE's onSoundOptionsChanged() directly set the "BgMusic"/"streaming"
	 * channel volumes from musicVolume, which would fight the per-category system (e.g. the music slider
	 * overriding the records/streaming channel volume).
	 */
	@Overwrite
	public void onSoundOptionsChanged() {
	}

	public void playSound(ISound sound) {
		if (this.loaded) {
			if (sound.canRepeat()) {
				this.playTime += 10;
			}
			SoundPoolEntry soundpoolentry = this.getURLForSoundResource(sound.getSoundLocation());
			if (soundpoolentry != SoundHandler.MISSING_SOUND) {
				float f = sound.getVolume();
				float f1 = 16.0F;
				if (f > 1.0F) {
					f1 *= f;
				}
				String s1 = "sound_" + this.latestSoundID;
				this.latestSoundID = (this.latestSoundID + 1) % 256;
				this.playingSoundsStopTime.put(s1, this.playTime + (soundpoolentry.isStreaming() ? 100 : 20));
				this.playingSoundPoolEntries.put(sound, soundpoolentry);
				if (soundpoolentry.isStreaming()) {
					this.sndSystem.newStreamingSource(sound.getAttenuationType() == ISound.AttenuationType.LINEAR, s1, soundpoolentry.getSoundUrl(), soundpoolentry.getSoundName(), false, sound.getXPosF(), sound.getYPosF(), sound.getZPosF(), sound.getAttenuationType().getTypeInt(), f1);
				} else {
					this.sndSystem.newSource(sound.getAttenuationType() == ISound.AttenuationType.LINEAR, s1, soundpoolentry.getSoundUrl(), soundpoolentry.getSoundName(), false, sound.getXPosF(), sound.getYPosF(), sound.getZPosF(), sound.getAttenuationType().getTypeInt(), f1);
				}
				if (f > 1.0F) {
					f = 1.0F;
				}
				this.sndSystem.setPitch(s1, this.getNormalizedPitch(sound, soundpoolentry));
				SoundCategory soundcategory = this.getSoundCategory(sound.getSoundLocation());
				this.sndSystem.setVolume(s1, this.getNormalizedVolume(sound, soundpoolentry, soundcategory));
				this.sndSystem.play(s1);
				this.playingSoundsMap.put(s1, sound);
				this.categorySounds.put(soundcategory, s1);
				if (sound instanceof ITickableSound tickableSound) {
					this.tickableSounds.add(tickableSound);
				}
			}
		}
	}

	public void updateAllSounds() {
		++this.playTime;
		Iterator<ITickableSound> iterator = this.tickableSounds.iterator();
		String s;
		while (iterator.hasNext()) {
			ITickableSound itickablesound = iterator.next();
			itickablesound.update();
			if (itickablesound.isDonePlaying()) {
				this.stopSound(itickablesound);
			} else {
				s = this.invPlayingSounds.get(itickablesound);
				if (s != null) {
					this.sndSystem.setVolume(s, this.getNormalizedVolume(itickablesound, this.playingSoundPoolEntries.get(itickablesound), this.getSoundCategory(itickablesound.getSoundLocation())));
					this.sndSystem.setPitch(s, this.getNormalizedPitch(itickablesound, this.playingSoundPoolEntries.get(itickablesound)));
					this.sndSystem.setPosition(s, itickablesound.getXPosF(), itickablesound.getYPosF(), itickablesound.getZPosF());
				}
			}
		}

		Iterator iterator2 = this.playingSoundsMap.entrySet().iterator();
		ISound isound;
		while (iterator2.hasNext()) {
			Map.Entry entry = (Map.Entry) iterator2.next();
			s = (String) entry.getKey();
			isound = (ISound) entry.getValue();
			if (!this.sndSystem.playing(s)) {
				Integer integer = this.playingSoundsStopTime.get(s);
				if (integer != null && integer <= this.playTime) {
					int j = isound.getRepeatDelay();
					if (isound.canRepeat() && j > 0) {
						this.delayedSounds.put(isound, this.playTime + j);
					}
					iterator2.remove();
					this.sndSystem.removeSource(s);
					this.playingSoundsStopTime.remove(s);
					this.playingSoundPoolEntries.remove(isound);
					this.categorySounds.remove(this.getSoundCategory(isound.getSoundLocation()), s);
					if (isound instanceof ITickableSound) {
						this.tickableSounds.remove(isound);
					}
				}
			}
		}

		Iterator iterator1 = this.delayedSounds.entrySet().iterator();
		while (iterator1.hasNext()) {
			Map.Entry entry1 = (Map.Entry) iterator1.next();
			if (this.playTime >= (Integer) entry1.getValue()) {
				isound = (ISound) entry1.getKey();
				if (isound instanceof ITickableSound) {
					((ITickableSound) isound).update();
				}
				this.playSound(isound);
				iterator1.remove();
			}
		}
	}

	public void unloadSoundSystem() {
		if (this.loaded) {
			this.stopAllSounds();
			this.sndSystem.cleanup();
			this.loaded = false;
		}
	}

	public void stopSound(ISound sound) {
		if (this.loaded) {
			String s = this.invPlayingSounds.get(sound);
			if (s != null) {
				this.sndSystem.stop(s);
			}
		}
	}

	public boolean isSoundPlaying(ISound sound) {
		if (!this.loaded) {
			return false;
		}
		String s = this.invPlayingSounds.get(sound);
		return s != null && (this.sndSystem.playing(s) || this.playingSoundsStopTime.containsKey(s));
	}

	public void addDelayedSound(ISound sound, int delay) {
		this.delayedSounds.put(sound, this.playTime + delay);
	}

	public void setSoundCategoryVolume(SoundCategory category, float volume) {
		if (category == SoundCategory.MASTER) {
			if (volume <= 0.0F) {
				this.stopAllSounds();
			}
			return;
		}
		if (this.loaded) {
			for (String s : this.categorySounds.get(category)) {
				ISound isound = this.playingSoundsMap.get(s);
				if (isound != null) {
					this.sndSystem.setVolume(s, this.getNormalizedVolume(isound, this.playingSoundPoolEntries.get(isound), category));
				}
			}
		}
	}

	public float getSoundCategoryVolume(SoundCategory category) {
		return category != null && category != SoundCategory.MASTER ? this.options.getSoundLevel(category) : 1.0F;
	}

	@Unique
	private SoundCategory getSoundCategory(ResourceLocation location) {
		SoundEventAccessorComposite soundeventaccessorcomposite = this.getSoundHandler().getSound(location);
		return soundeventaccessorcomposite != null ? soundeventaccessorcomposite.getSoundCategory() : SoundCategory.MASTER;
	}

	@Unique
	private SoundPoolEntry getURLForSoundResource(ResourceLocation location) {
		SoundEventAccessorComposite soundeventaccessorcomposite = this.getSoundHandler().getSound(location);
		return soundeventaccessorcomposite != null ? soundeventaccessorcomposite.func_148720_g() : SoundHandler.MISSING_SOUND;
	}

	@Unique
	private float getNormalizedPitch(ISound sound, SoundPoolEntry entry) {
		return (float) MathHelper.clamp_double((double) sound.getPitch() * entry.getPitch(), 0.5D, 2.0D);
	}

	@Unique
	private float getNormalizedVolume(ISound sound, SoundPoolEntry entry, SoundCategory category) {
		return (float) MathHelper.clamp_double((double) sound.getVolume() * entry.getVolume() * (double) this.getSoundCategoryVolume(category), 0.0D, 1.0D);
	}

	@Unique
	private SoundHandler getSoundHandler() {
		return Minecraft.getMinecraft().getSoundHandler();
	}

	@Unique
	private SoundCategory getCategoryForSoundPath(String s) {
		if (s != null) {
			SoundEvent soundevent = (SoundEvent) SoundEvent.REGISTRY.getObject(new ResourceLocation(s));
			if (soundevent != null) {
				return soundevent.getSoundCategory();
			}
		}
		SoundEvent soundevent = (SoundEvent) SoundEvent.REGISTRY.getObject(s);
		if (soundevent != null) {
			return soundevent.getSoundCategory();
		}
//		if (s == null) {
//			return SoundCategory.MASTER;
//		}
//		if (s.startsWith("music.")) {
//			return SoundCategory.MUSIC;
//		}
//		if (s.startsWith("record.") || s.startsWith("records.")) {
//			return SoundCategory.RECORDS;
//		}
//		if (s.startsWith("weather.")) {
//			return SoundCategory.WEATHER;
//		}
//		if (s.startsWith("ambient.")) {
//			return SoundCategory.AMBIENT;
//		}
//		if (s.startsWith("mob.")) {
//			String str = s.substring(4);
//			if (str.startsWith("cow.") || str.startsWith("pig.") || str.startsWith("chicken.") || str.startsWith("sheep.")
//					|| str.startsWith("bat.") || str.startsWith("squid.") || str.startsWith("horse.") || str.startsWith("ocelot.")
//					|| str.startsWith("wolf.") || str.startsWith("villager.") || str.startsWith("rabbit.") || str.startsWith("cat.")) {
//				return SoundCategory.ANIMALS;
//			}
//			return SoundCategory.MOBS;
//		}
//		if (s.startsWith("dig.") || s.startsWith("step.") || s.startsWith("fire.") || s.startsWith("liquid.")
//				|| s.startsWith("note.") || s.startsWith("portal.") || s.startsWith("tile.")) {
//			return SoundCategory.BLOCKS;
//		}
//		if (s.startsWith("minecart.")) {
//			return SoundCategory.ANIMALS;
//		}
//		if (s.startsWith("random.") || s.startsWith("game.")) {
//			return SoundCategory.PLAYERS;
//		}
//		if (s.contains("click.")) {
//			return SoundCategory.UI;
//		}
		return SoundCategory.MASTER;
	}
}
