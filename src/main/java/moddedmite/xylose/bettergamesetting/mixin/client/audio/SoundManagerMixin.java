package moddedmite.xylose.bettergamesetting.mixin.client.audio;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import moddedmite.xylose.bettergamesetting.api.ISoundManager;
import moddedmite.xylose.bettergamesetting.client.audio.ISound;
import moddedmite.xylose.bettergamesetting.client.audio.ISoundEventListener;
import moddedmite.xylose.bettergamesetting.client.audio.ITickableSound;
import moddedmite.xylose.bettergamesetting.client.audio.Sound;
import moddedmite.xylose.bettergamesetting.util.OpenALOutputLibrary;
import moddedmite.xylose.bettergamesetting.client.audio.SoundCategory;
import moddedmite.xylose.bettergamesetting.client.audio.SoundEvent;
import moddedmite.xylose.bettergamesetting.client.audio.SoundEventAccessor;
import moddedmite.xylose.bettergamesetting.client.audio.SoundHandler;
import moddedmite.xylose.bettergamesetting.init.BGSClient;
import moddedmite.xylose.bettergamesetting.util.Mth;
import net.minecraft.Entity;
import net.minecraft.EntityPlayer;
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
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.SoundSystemException;
import paulscode.sound.SoundSystemLogger;
import paulscode.sound.Source;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@Mixin(value = SoundManager.class, priority = 999)
public abstract class SoundManagerMixin implements ISoundManager {
	@Shadow private boolean loaded;
	@Shadow public SoundSystem sndSystem;
	@Shadow @Final private Set<String> playingSounds;
	@Shadow @Final private GameSettings options;

	@Unique private int playTime = 0;
	@Unique private final BiMap<String, ISound> playingSoundsMap = HashBiMap.create();
	@Unique private Map<ISound, String> invPlayingSounds;
	@Unique private final Map<ISound, SoundPoolEntry> playingSoundPoolEntries = Maps.newHashMap();
	@Unique private final Multimap<SoundCategory, String> categorySounds = HashMultimap.create();
	@Unique private final List<ITickableSound> tickableSounds = Lists.newArrayList();
	@Unique private final Map<ISound, Integer> delayedSounds = Maps.newHashMap();
	@Unique private final Map<String, Integer> playingSoundsStopTime = Maps.newHashMap();
	@Unique private static final Set<ResourceLocation> UNABLE_TO_PLAY = Sets.<ResourceLocation>newHashSet();
	@Unique private List<ISoundEventListener> listeners;
	@Unique private boolean loadingSoundSystem = false;

	@Inject(method = "<init>", at = @At("TAIL"))
	private void init(CallbackInfo info) {
		this.invPlayingSounds = this.playingSoundsMap.inverse();
		this.listeners = Lists.<ISoundEventListener>newArrayList();
	}

	@WrapOperation(method = "playSound", at = @At(value = "INVOKE", target = "Lpaulscode/sound/SoundSystem;setVolume(Ljava/lang/String;F)V"))
	private void applyCategoryVolume(SoundSystem instance, String sourcename, float value, Operation<Void> original, @Local(argsOnly = true) String par1Str) {
		instance.setVolume(sourcename, value * this.getVolume(this.getCategoryForSoundPath(par1Str)));
	}

	@WrapOperation(method = "playEntitySound", at = @At(value = "INVOKE", target = "Lpaulscode/sound/SoundSystem;setVolume(Ljava/lang/String;F)V"))
	private void applyEntityCategoryVolume(SoundSystem instance, String sourcename, float value, Operation<Void> original, @Local(argsOnly = true) String par1Str) {
		instance.setVolume(sourcename, value * this.getVolume(this.getCategoryForSoundPath(par1Str)));
	}

	@WrapOperation(method = "playLongDistanceSound", at = @At(value = "INVOKE", target = "Lpaulscode/sound/SoundSystem;setVolume(Ljava/lang/String;F)V"))
	private void applyLongDistanceCategoryVolume(SoundSystem instance, String sourcename, float value, Operation<Void> original, @Local(argsOnly = true) String par1Str) {
		instance.setVolume(sourcename, value * this.getVolume(this.getCategoryForSoundPath(par1Str)));
	}

	@WrapOperation(method = "playStreaming", at = @At(value = "INVOKE", target = "Lpaulscode/sound/SoundSystem;setVolume(Ljava/lang/String;F)V"))
	private void applyRecordCategoryVolume(SoundSystem instance, String sourcename, float value, Operation<Void> original) {
		instance.setVolume(sourcename, value * this.getVolume(SoundCategory.RECORDS));
	}
	
	@WrapOperation(method = "playSoundFX", at = @At(value = "INVOKE", target = "Lpaulscode/sound/SoundSystem;setVolume(Ljava/lang/String;F)V"))
	public void applyFXVolume(SoundSystem instance, String sourcename, float value, Operation<Void> original) {
		instance.setVolume(sourcename, value * this.getVolume(SoundCategory.UI));
	}
	
	public void reloadSoundSystem() {
		UNABLE_TO_PLAY.clear();
		
		for (SoundEvent soundevent : SoundEvent.getRegisteredSounds()) {
			ResourceLocation resourcelocation = soundevent.soundName();

			if (this.getSoundHandler().getAccessor(resourcelocation) == null) {
				BGSClient.logger.warn("Missing sound for event: {}", soundevent);
				UNABLE_TO_PLAY.add(resourcelocation);
			}
		}
		
		this.unloadSoundSystem();
		this.loadSoundSystem();
	}

	@WrapOperation(method = "onResourceManagerReload", at = @At(value = "INVOKE", target = "Lnet/minecraft/SoundManager;stopAllSounds()V"))
	private void removeReload_1(SoundManager instance, Operation<Void> original) {
	}
	
	@WrapOperation(method = "onResourceManagerReload", at = @At(value = "INVOKE", target = "Lnet/minecraft/SoundManager;cleanup()V"))
	private void removeReload_2(SoundManager instance, Operation<Void> original) {
	}
	
	@WrapOperation(method = "onResourceManagerReload", at = @At(value = "INVOKE", target = "Lnet/minecraft/SoundManager;tryToSetLibraryAndCodecs()V"))
	private void removeReload_3(SoundManager instance, Operation<Void> original) {
	}
	
	private synchronized void loadSoundSystem() {
		if (!this.loaded && !this.loadingSoundSystem) {
			this.loadingSoundSystem = true;
			try {
				(new Thread(() -> {
					try {
						SoundSystemConfig.setLogger(new SoundSystemLogger() {
							public void message(String p_message_1_, int p_message_2_) {
								if (!p_message_1_.isEmpty()) {
									BGSClient.logger.info(p_message_1_);
								}
							}
							
							public void importantMessage(String p_importantMessage_1_, int p_importantMessage_2_) {
								if (!p_importantMessage_1_.isEmpty()) {
									BGSClient.logger.warn(p_importantMessage_1_);
								}
							}
							
							public void errorMessage(String p_errorMessage_1_, String p_errorMessage_2_, int p_errorMessage_3_) {
								if (!p_errorMessage_2_.isEmpty()) {
									BGSClient.logger.error("Error in class '{}'", p_errorMessage_1_);
									BGSClient.logger.error(p_errorMessage_2_);
								}
							}
						});
						try {
							Thread.sleep(400L);
						} catch (InterruptedException interrupted) {
							Thread.currentThread().interrupt();
						}
						OpenALOutputLibrary.setRequestedDevice(this.options.getSoundDevice());
						this.sndSystem = new SoundSystemStarterThread(OpenALOutputLibrary.class);
						this.loaded = true;
						this.sndSystem.setMasterVolume(this.options.getSoundLevel(SoundCategory.MASTER));
						BGSClient.logger.info("Sound engine started");
					} catch (SoundSystemException e) {
						throw new RuntimeException(e);
					} finally {
						this.loadingSoundSystem = false;
					}
				}, "Sound Library Loader")).start();
			} catch (RuntimeException runtimeexception) {
				this.loadingSoundSystem = false;
				BGSClient.logger.error("Error starting SoundSystem. Turning off sounds & music", runtimeexception);
				this.options.setSoundLevel(SoundCategory.MASTER, 0.0F);
				this.options.saveOptions();
			}
		}
	}
		
		
	/**
	 * @author Xy_Lose
	 * @reason
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

	@Inject(method = "playRandomMusicIfReady", at = @At("HEAD"), cancellable = true)
	private void removeRandomMusic(CallbackInfo ci) {
		ci.cancel();
	}

	@Inject(method = "onSoundOptionsChanged", at = @At("HEAD"), cancellable = true)
	private void removeSoundOptionsChanged(CallbackInfo ci) {
		ci.cancel();
	}

	public void playSound(ISound p_sound) {
		if (this.loaded) {
			SoundEventAccessor soundeventaccessor = p_sound.createAccessor(this.getSoundHandler());
			ResourceLocation resourcelocation = p_sound.getSoundLocation();
			
			if (soundeventaccessor == null) {
				if (UNABLE_TO_PLAY.add(resourcelocation)) {
					BGSClient.logger.warn("Unable to play unknown soundEvent: {}", resourcelocation);
				}
			} else {
				if (!this.listeners.isEmpty()) {
					for (ISoundEventListener isoundeventlistener : this.listeners) {
						isoundeventlistener.soundPlay(p_sound, soundeventaccessor);
					}
				}
				
				if (this.sndSystem.getMasterVolume() <= 0.0F) {
					BGSClient.logger.debug("Skipped playing soundEvent: {}, master volume was zero", (Object) resourcelocation);
				} else {
					Sound sound = p_sound.getSound();
					
					if (sound == SoundHandler.MISSING_SOUND) {
						if (UNABLE_TO_PLAY.add(resourcelocation)) {
							BGSClient.logger.warn("Unable to play empty soundEvent: {}", (Object) resourcelocation);
						}
					} else {
						float f3 = p_sound.getVolume();
						float f = 16.0F;
						
						if (f3 > 1.0F) {
							f *= f3;
						}
						
						SoundCategory soundcategory = p_sound.getCategory();
						float f1 = this.getClampedVolume(p_sound);
						float f2 = this.getClampedPitch(p_sound);
						
						if (f1 == 0.0F) {
							BGSClient.logger.debug("Skipped playing sound {}, volume was zero.", (Object) sound.getSoundLocation());
						} else {
							boolean flag = p_sound.canRepeat() && p_sound.getRepeatDelay() == 0;
							String s = Mth.getRandomUUID(ThreadLocalRandom.current()).toString();
							ResourceLocation resourcelocation1 = sound.getSoundAsOggLocation();
							
							if (sound.isStreaming()) {
								this.sndSystem.newStreamingSource(false, s, BGSClient.getURLForSoundResource(resourcelocation1), resourcelocation1.toString(), flag, p_sound.getXPosF(), p_sound.getYPosF(), p_sound.getZPosF(), p_sound.getAttenuationType().getTypeInt(), f);
							} else {
								this.sndSystem.newSource(false, s, BGSClient.getURLForSoundResource(resourcelocation1), resourcelocation1.toString(), flag, p_sound.getXPosF(), p_sound.getYPosF(), p_sound.getZPosF(), p_sound.getAttenuationType().getTypeInt(), f);
							}
							
							BGSClient.logger.debug("Playing sound {} for event {} as channel {}", sound.getSoundLocation(), resourcelocation, s);
							this.sndSystem.setPitch(s, f2);
							this.sndSystem.setVolume(s, f1);
							this.sndSystem.play(s);
							this.playingSoundsStopTime.put(s, Integer.valueOf(this.playTime + 20));
							this.playingSoundsMap.put(s, p_sound);
							this.categorySounds.put(soundcategory, s);
							
							if (p_sound instanceof ITickableSound) {
								this.tickableSounds.add((ITickableSound) p_sound);
							}
						}
					}
				}
			}
		}
	}
	
	public void updateAllSounds() {
		++this.playTime;
		
		for (ITickableSound itickablesound : this.tickableSounds) {
			itickablesound.update();
			
			if (itickablesound.isDonePlaying()) {
				this.stopSound(itickablesound);
			} else {
				String s = this.invPlayingSounds.get(itickablesound);
				this.sndSystem.setVolume(s, this.getClampedVolume(itickablesound));
				this.sndSystem.setPitch(s, this.getClampedPitch(itickablesound));
				this.sndSystem.setPosition(s, itickablesound.getXPosF(), itickablesound.getYPosF(), itickablesound.getZPosF());
			}
		}
		
		Iterator<Map.Entry<String, ISound>> iterator = this.playingSoundsMap.entrySet().iterator();
		
		while (iterator.hasNext()) {
			Map.Entry<String, ISound> entry = (Map.Entry) iterator.next();
			String s1 = entry.getKey();
			ISound isound = entry.getValue();
			
			if (!this.sndSystem.playing(s1)) {
				int i = ((Integer) this.playingSoundsStopTime.get(s1)).intValue();
				
				if (i <= this.playTime) {
					int j = isound.getRepeatDelay();
					
					if (isound.canRepeat() && j > 0) {
						this.delayedSounds.put(isound, Integer.valueOf(this.playTime + j));
					}
					
					iterator.remove();
					BGSClient.logger.debug("Removed channel {} because it's not playing anymore", (Object) s1);
					this.sndSystem.removeSource(s1);
					this.playingSoundsStopTime.remove(s1);
					
					try {
						this.categorySounds.remove(isound.getCategory(), s1);
					} catch (RuntimeException var8) {
						;
					}
					
					if (isound instanceof ITickableSound) {
						this.tickableSounds.remove(isound);
					}
				}
			}
		}
		
		Iterator<Map.Entry<ISound, Integer>> iterator1 = this.delayedSounds.entrySet().iterator();
		
		while (iterator1.hasNext()) {
			Map.Entry<ISound, Integer> entry1 = (Map.Entry) iterator1.next();
			
			if (this.playTime >= ((Integer) entry1.getValue()).intValue()) {
				ISound isound1 = entry1.getKey();
				
				if (isound1 instanceof ITickableSound) {
					((ITickableSound) isound1).update();
				}
				
				this.playSound(isound1);
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

	public void setVolume(SoundCategory category, float volume) {
		if (this.loaded) {
			if (category == SoundCategory.MASTER) {
				this.sndSystem.setMasterVolume(volume);
			} else {
				for (String s : this.categorySounds.get(category)) {
					ISound isound = this.playingSoundsMap.get(s);
					float f = this.getClampedVolume(isound);
					if (f <= 0.0F) {
						this.stopSound(isound);
					} else {
						this.sndSystem.setVolume(s, f);
					}
				}
			}
		}
	}

	public float getVolume(SoundCategory category) {
		return category != null && category != SoundCategory.MASTER ? this.options.getSoundLevel(category) : 1.0F;
	}
	
	public void setListener(EntityPlayer player, float multiplier) {
		setListener((Entity) player, multiplier);
	}
	
	public void setListener(Entity player, float multiplier) {
		if (this.loaded && player != null) {
			float f = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * multiplier;
			float f1 = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * multiplier;
			double d0 = player.prevPosX + (player.posX - player.prevPosX) * (double) multiplier;
			double d1 = player.prevPosY + (player.posY - player.prevPosY) * (double) multiplier + (double) player.getEyeHeight();
			double d2 = player.prevPosZ + (player.posZ - player.prevPosZ) * (double) multiplier;
			float f2 = MathHelper.cos((f1 + 90.0F) * 0.017453292F);
			float f3 = MathHelper.sin((f1 + 90.0F) * 0.017453292F);
			float f4 = MathHelper.cos(-f * 0.017453292F);
			float f5 = MathHelper.sin(-f * 0.017453292F);
			float f6 = MathHelper.cos((-f + 90.0F) * 0.017453292F);
			float f7 = MathHelper.sin((-f + 90.0F) * 0.017453292F);
			float f8 = f2 * f4;
			float f9 = f3 * f4;
			float f10 = f2 * f6;
			float f11 = f3 * f6;
			this.sndSystem.setListenerPosition((float) d0, (float) d1, (float) d2);
			this.sndSystem.setListenerOrientation(f8, f5, f9, f10, f7, f11);
		}
	}
	
	public void stop(String soundId, SoundCategory category) {
		if (category != null) {
			for (String s : this.categorySounds.get(category)) {
				ISound isound = this.playingSoundsMap.get(s);
				
				if (soundId.isEmpty()) {
					this.stopSound(isound);
				} else if (isound.getSoundLocation().equals(new ResourceLocation(soundId))) {
					this.stopSound(isound);
				}
			}
		} else if (soundId.isEmpty()) {
			this.stopAllSounds();
		} else {
			for (ISound isound1 : this.playingSoundsMap.values()) {
				if (isound1.getSoundLocation().equals(new ResourceLocation(soundId))) {
					this.stopSound(isound1);
				}
			}
		}
	}
	
	public void addListener(ISoundEventListener listener) {
		this.listeners.add(listener);
	}

	public void removeListener(ISoundEventListener listener) {
		this.listeners.remove(listener);
	}

	@Unique
	private SoundHandler getSoundHandler() {
		return Minecraft.getMinecraft().getSoundHandler();
	}
	
	@Unique
	private float getClampedPitch(ISound soundIn) {
		return MathHelper.clamp_float(soundIn.getPitch(), 0.5F, 2.0F);
	}

	@Unique
	private float getClampedVolume(ISound soundIn) {
		return MathHelper.clamp_float(soundIn.getVolume() * this.getVolume(soundIn.getCategory()), 0.0F, 1.0F);
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
	
	static class SoundSystemStarterThread extends SoundSystem {
		private SoundSystemStarterThread(Class libraryClass) throws SoundSystemException {
			super(libraryClass);
		}
		
		public boolean playing(String p_playing_1_) {
			synchronized (SoundSystemConfig.THREAD_SYNC) {
				if (this.soundLibrary == null) {
					return false;
				} else {
					Source source = this.soundLibrary.getSources().get(p_playing_1_);
					
					if (source == null) {
						return false;
					} else {
						return source.playing() || source.paused() || source.preLoad;
					}
				}
			}
		}
	}
}
