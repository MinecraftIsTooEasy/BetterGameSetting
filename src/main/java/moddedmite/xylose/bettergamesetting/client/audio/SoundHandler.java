package moddedmite.xylose.bettergamesetting.client.audio;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import java.util.Set;

import moddedmite.xylose.bettergamesetting.init.BGSClient;
import moddedmite.xylose.bettergamesetting.util.JsonUtils;
import net.minecraft.*;
import org.apache.commons.io.IOUtils;

import javax.annotation.Nullable;

public class SoundHandler implements ResourceManagerReloadListener, IUpdatePlayerListBox {
	public static final Sound MISSING_SOUND = new Sound("meta:missing_sound", 1.0F, 1.0F, 1, Sound.Type.FILE, false);
	private static final Gson GSON = (new GsonBuilder()).registerTypeAdapter(SoundList.class, new SoundListSerializer()).create();
	private static final ParameterizedType TYPE = new ParameterizedType() {
		
		public Type[] getActualTypeArguments() {
			return new Type[]{String.class, SoundList.class};
		}

		public Type getRawType() {
			return Map.class;
		}

		public Type getOwnerType() {
			return null;
		}
	};
	private final SoundRegistry soundRegistry = new SoundRegistry();
	private final SoundManager sndManager;
	private final ResourceManager resourceManager;
	
	public SoundHandler(ResourceManager resourceManager, SoundManager sndManager) {
		this.resourceManager = resourceManager;
		this.sndManager = sndManager;
	}

	public static SoundHandler formClient(Minecraft client) {
		return new SoundHandler(client.getResourceManager(), client.sndManager);
	}

	public void onResourceManagerReload(ResourceManager manager) {
		this.soundRegistry.clear();

		for (String s : (Set<String>) manager.getResourceDomains()) {
			try {
				List<Resource> list = manager.getAllResources(new ResourceLocation(s, "sounds.json"));
				for (Resource iresource : list.stream().filter(Objects::nonNull).toList()) {
					try {
						Map<String, SoundList> map = this.getSoundMap(iresource.getInputStream());
						for (Entry<String, SoundList> entry : map.entrySet()) {
							this.loadSoundResource(new ResourceLocation(s, entry.getKey()), entry.getValue());
						}
					} catch (RuntimeException runtimeexception) {
						BGSClient.logger.warn("Invalid sounds.json", runtimeexception);
					}
				}
			} catch (Exception exception) {
				if (exception instanceof FileNotFoundException) {
					BGSClient.logger.debug("Skipped missing sounds.json for domain {}", new Object[]{s});
				} else {
					BGSClient.logger.warn("Could not load sounds.json for domain {}", new Object[]{s}, exception);
				}
			}
		}

		for (ResourceLocation resourcelocation : this.soundRegistry.getKeys()) {
			SoundEventAccessor soundeventaccessor = (SoundEventAccessor) this.soundRegistry.getObject(resourcelocation);

			if (soundeventaccessor.getSubtitle() instanceof ChatMessageComponent) {
				String s1 = soundeventaccessor.getSubtitle().getTranslationKey();

				if (!Objects.equals(I18n.getString(s1), s1)) {
					BGSClient.logger.debug("Missing subtitle {} for event: {}", s1, resourcelocation);
				}
			}
		}

		for (ResourceLocation resourcelocation1 : this.soundRegistry.getKeys()) {
			if (SoundEvent.REGISTRY.getObject(resourcelocation1) == null) {
				BGSClient.logger.debug("Not having sound event for: {}", resourcelocation1);
			}
		}

		this.sndManager.reloadSoundSystem();
	}

	@Nullable
	protected Map<String, SoundList> getSoundMap(InputStream stream) {
		Map map;
		try {
			map = JsonUtils.fromJson(GSON, new InputStreamReader(stream, StandardCharsets.UTF_8), TYPE);
		} finally {
			IOUtils.closeQuietly(stream);
		}
		return map;
	}

	private void loadSoundResource(ResourceLocation location, SoundList sounds) {
		SoundEventAccessor soundeventaccessor = (SoundEventAccessor) this.soundRegistry.getObject(location);
		boolean flag = soundeventaccessor == null;

		if (flag || sounds.canReplaceExisting()) {
			if (!flag) {
				BGSClient.logger.debug("Replaced sound event location {}", (Object) location);
			}

			soundeventaccessor = new SoundEventAccessor(location, sounds.getSubtitle());
			this.soundRegistry.registerSound(soundeventaccessor);
		}

		for (final Sound sound : sounds.getSounds()) {
			final ResourceLocation resourcelocation = sound.getSoundLocation();
			ISoundEventAccessor<Sound> isoundeventaccessor;

			switch (sound.getType()) {
				case FILE:

					if (!this.validateSoundResource(sound, location)) {
						continue;
					}

					isoundeventaccessor = sound;
					break;
				case SOUND_EVENT:
					isoundeventaccessor = new ISoundEventAccessor<Sound>() {
						public int getWeight() {
							SoundEventAccessor soundeventaccessor1 = (SoundEventAccessor) SoundHandler.this.soundRegistry.getObject(resourcelocation);
							return soundeventaccessor1 == null ? 0 : soundeventaccessor1.getWeight();
						}

						public Sound cloneEntry() {
							SoundEventAccessor soundeventaccessor1 = (SoundEventAccessor) SoundHandler.this.soundRegistry.getObject(resourcelocation);

							if (soundeventaccessor1 == null) {
								return SoundHandler.MISSING_SOUND;
							} else {
								Sound sound1 = soundeventaccessor1.cloneEntry();
								return new Sound(sound1.getSoundLocation().toString(), sound1.getVolume() * sound.getVolume(), sound1.getPitch() * sound.getPitch(), sound.getWeight(), Sound.Type.FILE, sound1.isStreaming() || sound.isStreaming());
							}
						}
					};
					break;
				default:
					throw new IllegalStateException("Unknown SoundEventRegistration type: " + sound.getType());
			}

			soundeventaccessor.addSound(isoundeventaccessor);
		}
	}

	private boolean validateSoundResource(Sound p_184401_1_, ResourceLocation p_184401_2_) {
		ResourceLocation resourcelocation = p_184401_1_.getSoundAsOggLocation();

		try {
			Resource iresource = this.resourceManager.getResource(resourcelocation);
			IOUtils.closeQuietly(iresource.getInputStream());
			return true;
//		} catch (FileNotFoundException var11) {
//			BGSClient.logger.warn("File {} does not exist, cannot add it to event {}", resourcelocation, p_184401_2_);
//		} catch (IOException ioexception) {
//			BGSClient.logger.warn("Could not load sound file {}, cannot add it to event {}", resourcelocation, p_184401_2_, ioexception);
		} catch (Exception e) {
			return false;
		}
	}

	@Nullable
	public SoundEventAccessor getAccessor(ResourceLocation location) {
		return (SoundEventAccessor) this.soundRegistry.getObject(location);
	}

	/**
	 * Play a sound
	 */
	public void playSound(ISound sound) {
		this.sndManager.playSound(sound);
	}

	/**
	 * Plays the sound in n ticks
	 */
	public void playDelayedSound(ISound sound, int delay) {
		this.sndManager.addDelayedSound(sound, delay);
	}

	public void setListener(EntityPlayer player, float p_147691_2_) {
		this.sndManager.setListener(player, p_147691_2_);
	}

	public void setListener(Entity entity, float partialTicks) {
		this.sndManager.setListener(entity, partialTicks);
	}

	public void pauseSounds() {
		this.sndManager.pauseAllSounds();
	}

	public void stopSounds() {
		this.sndManager.stopAllSounds();
	}

	public void unloadSounds() {
		this.sndManager.unloadSoundSystem();
	}

	/**
	 * Like the old updateEntity(), except more generic.
	 */
	public void update() {
		this.sndManager.updateAllSounds();
	}

	public void resumeSounds() {
		this.sndManager.resumeAllSounds();
	}

	public void setSoundLevel(SoundCategory category, float volume) {
		if (category == SoundCategory.MASTER && volume <= 0.0F) {
			this.stopSounds();
		}

		this.sndManager.setVolume(category, volume);
	}

	public void stopSound(ISound soundIn) {
		this.sndManager.stopSound(soundIn);
	}

	public boolean isSoundPlaying(ISound sound) {
		return this.sndManager.isSoundPlaying(sound);
	}

	public void addListener(ISoundEventListener listener) {
		this.sndManager.addListener(listener);
	}

	public void removeListener(ISoundEventListener listener) {
		this.sndManager.removeListener(listener);
	}

	public void stop(String p_189520_1_, SoundCategory p_189520_2_) {
		this.sndManager.stop(p_189520_1_, p_189520_2_);
	}
}