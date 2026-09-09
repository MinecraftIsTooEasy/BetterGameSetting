package moddedmite.xylose.bettergamesetting.util;

import moddedmite.xylose.bettergamesetting.client.audio.SoundCategory;
import moddedmite.xylose.bettergamesetting.client.audio.SoundEvent;
import net.minecraft.ResourceLocation;

public class SoundHelper {
	public static SoundCategory getCategoryForSoundPath(String s) {
		if (s == null) return SoundCategory.MASTER;
		SoundEvent soundevent = (SoundEvent) SoundEvent.REGISTRY.getObject(new ResourceLocation(s));
		if (soundevent != null) {
			return soundevent.getSoundCategory();
		}
		if (s.startsWith("music.")) {
			return SoundCategory.MUSIC;
		}
		if (s.startsWith("record.") || s.startsWith("records.")) {
			return SoundCategory.RECORDS;
		}
		if (s.startsWith("weather.")) {
			return SoundCategory.WEATHER;
		}
		if (s.startsWith("ambient.")) {
			return SoundCategory.AMBIENT;
		}
		if (s.startsWith("mob.")) {
			String str = s.substring(4);
			if (str.startsWith("cow.") || str.startsWith("pig.") || str.startsWith("chicken.") || str.startsWith("sheep.")
					|| str.startsWith("bat.") || str.startsWith("squid.") || str.startsWith("horse.") || str.startsWith("ocelot.")
					|| str.startsWith("wolf.") || str.startsWith("villager.") || str.startsWith("rabbit.") || str.startsWith("cat.")) {
				return SoundCategory.ANIMALS;
			}
			return SoundCategory.MOBS;
		}
		if (s.startsWith("dig.") || s.startsWith("step.") || s.startsWith("fire.") || s.startsWith("liquid.")
				|| s.startsWith("note.") || s.startsWith("portal.") || s.startsWith("tile.")) {
			return SoundCategory.BLOCKS;
		}
		if (s.startsWith("minecart.")) {
			return SoundCategory.ANIMALS;
		}
		if (s.startsWith("random.") || s.startsWith("game.")) {
			return SoundCategory.PLAYERS;
		}
		if (s.contains("click.")) {
			return SoundCategory.UI;
		}
		return SoundCategory.MASTER;
	}
}
