package moddedmite.xylose.bettergamesetting.client.audio;

import javax.annotation.Nullable;

public record SoundType(float volume, float pitch, SoundEvent breakSound, SoundEvent stepSound, SoundEvent placeSound, @Nullable SoundEvent hitSound, @Nullable SoundEvent fallSound) {
	public static final SoundType WOOD = new SoundType(1.0F, 1.0F, SoundEvents.BLOCK_WOOD_BREAK, SoundEvents.BLOCK_WOOD_STEP, SoundEvents.BLOCK_WOOD_BREAK);
	public static final SoundType GROUND = new SoundType(1.0F, 1.0F, SoundEvents.BLOCK_GRAVEL_BREAK, SoundEvents.BLOCK_GRAVEL_STEP, SoundEvents.BLOCK_GRAVEL_BREAK);
	public static final SoundType PLANT = new SoundType(1.0F, 1.0F, SoundEvents.BLOCK_GRASS_BREAK, SoundEvents.BLOCK_GRASS_STEP, SoundEvents.BLOCK_GRASS_BREAK);
	public static final SoundType STONE = new SoundType(1.0F, 1.0F, SoundEvents.BLOCK_STONE_BREAK, SoundEvents.BLOCK_STONE_STEP, SoundEvents.BLOCK_STONE_BREAK);
	public static final SoundType METAL = new SoundType(1.0F, 1.5F, SoundEvents.BLOCK_STONE_BREAK, SoundEvents.BLOCK_METAL_STEP, SoundEvents.BLOCK_STONE_BREAK);
	public static final SoundType GLASS = new SoundType(1.0F, 1.0F, SoundEvents.BLOCK_GLASS_BREAK, SoundEvents.BLOCK_GLASS_STEP, SoundEvents.BLOCK_GLASS_BREAK);
	public static final SoundType CLOTH = new SoundType(1.0F, 1.0F, SoundEvents.BLOCK_CLOTH_BREAK, SoundEvents.BLOCK_CLOTH_STEP, SoundEvents.BLOCK_CLOTH_BREAK);
	public static final SoundType SAND = new SoundType(1.0F, 1.0F, SoundEvents.BLOCK_SAND_BREAK, SoundEvents.BLOCK_SAND_STEP, SoundEvents.BLOCK_SAND_BREAK);
	public static final SoundType SNOW = new SoundType(1.0F, 1.0F, SoundEvents.BLOCK_SNOW_BREAK, SoundEvents.BLOCK_SNOW_STEP, SoundEvents.BLOCK_SNOW_BREAK);
	public static final SoundType LADDER = new SoundType(1.0F, 1.0F, SoundEvents.BLOCK_WOOD_BREAK, SoundEvents.BLOCK_LADDER_STEP, SoundEvents.BLOCK_WOOD_BREAK);
	public static final SoundType ANVIL = new SoundType(0.3F, 1.0F, SoundEvents.BLOCK_ANVIL_BREAK, SoundEvents.BLOCK_STONE_STEP, SoundEvents.BLOCK_ANVIL_BREAK);
//	public static final SoundType SLIME = new SoundType(1.0F, 1.0F, SoundEvents.BLOCK_SLIME_BREAK, SoundEvents.BLOCK_SLIME_STEP, SoundEvents.BLOCK_SLIME_BREAK, SoundEvents.BLOCK_SLIME_HIT, SoundEvents.BLOCK_SLIME_FALL);
	
	public SoundType(float volumeIn, float pitchIn, SoundEvent breakSoundIn, SoundEvent stepSoundIn, SoundEvent placeSoundIn) {
		this(volumeIn, pitchIn, breakSoundIn, stepSoundIn, placeSoundIn, null, null);
	}
	
	public SoundType(float volumeIn, float pitchIn, SoundEvent breakSoundIn, SoundEvent stepSoundIn) {
		this(volumeIn, pitchIn, breakSoundIn, stepSoundIn, breakSoundIn, null, null);
	}
}