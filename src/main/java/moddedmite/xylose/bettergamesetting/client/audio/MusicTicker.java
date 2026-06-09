//package moddedmite.xylose.bettergamesetting.client.audio;
//
//import net.minecraft.BossStatus;
//import net.minecraft.GuiWinGame;
//import net.minecraft.Minecraft;
//import net.minecraft.IUpdatePlayerListBox;
//import net.minecraft.MathHelper;
//import net.minecraft.ResourceLocation;
//import net.minecraft.WorldProviderEnd;
//import net.minecraft.WorldProviderHell;
//
//import java.util.Random;
//
//public class MusicTicker implements IUpdatePlayerListBox {
//    private final Random rand = new Random();
//    private final Minecraft client;
//    private ISound sound;
//    private int delay = 100;
//
//    public MusicTicker(Minecraft client) {
//        this.client = client;
//    }
//
//    /**
//     * Updates the JList with a new model.
//     */
//    public void update() {
//        MusicType musictype = this.getCurrentMusicType(this.client);
//
//        if (this.sound != null) {
//            if (!musictype.getMusicTickerLocation().equals(this.sound.getPositionedSoundLocation())) {
//                this.client.getSoundHandler().stopSound(this.sound);
//                this.delay = MathHelper.getRandomIntegerInRange(this.rand, 0, musictype.getMinDelay() / 2);
//            }
//
//            if (!this.client.getSoundHandler().isSoundPlaying(this.sound)) {
//                this.sound = null;
//                this.delay = Math.min(MathHelper.getRandomIntegerInRange(this.rand, musictype.getMinDelay(), musictype.getMaxDelay()), this.delay);
//            }
//        }
//
//        if (this.sound == null && this.delay-- <= 0) {
//            this.sound = PositionedSoundRecord.func_147673_a(musictype.getMusicTickerLocation());
//            this.client.getSoundHandler().playSound(this.sound);
//            this.delay = Integer.MAX_VALUE;
//        }
//    }
//
//	public MusicTicker.MusicType getCurrentMusicType(Minecraft client) {
//		if (client.currentScreen instanceof GuiWinGame) {
//			return MusicTicker.MusicType.CREDITS;
//		}
//
//		if (client.thePlayer == null) {
//			return MusicTicker.MusicType.MENU;
//		}
//
//		if (client.thePlayer.worldObj.provider instanceof WorldProviderHell) {
//			return MusicTicker.MusicType.NETHER;
//		}
//
//		if (client.thePlayer.worldObj.provider instanceof WorldProviderEnd) {
//			if (BossStatus.bossName != null && BossStatus.statusBarLength > 0) {
//				return MusicTicker.MusicType.END_BOSS;
//			}
//			return MusicTicker.MusicType.END;
//		}
//
//		if (client.thePlayer.capabilities.isCreativeMode && client.thePlayer.capabilities.allowFlying) {
//			return MusicTicker.MusicType.CREATIVE;
//		}
//
//		return MusicTicker.MusicType.GAME;
//	}
//
//    public static enum MusicType {
//        MENU(new ResourceLocation("minecraft:music.menu"), 20, 600),
//        GAME(new ResourceLocation("minecraft:music.game"), 12000, 24000),
//        CREATIVE(new ResourceLocation("minecraft:music.game.creative"), 1200, 3600),
//        CREDITS(new ResourceLocation("minecraft:music.game.end.credits"), Integer.MAX_VALUE, Integer.MAX_VALUE),
//        NETHER(new ResourceLocation("minecraft:music.game.nether"), 1200, 3600),
//        END_BOSS(new ResourceLocation("minecraft:music.game.end.dragon"), 0, 0),
//        END(new ResourceLocation("minecraft:music.game.end"), 6000, 24000);
//        private final ResourceLocation identity;
//        private final int minDelay;
//        private final int maxDelay;
//
//
//        private MusicType(ResourceLocation p_i45111_3_, int p_i45111_4_, int p_i45111_5_) {
//            this.identity = p_i45111_3_;
//            this.minDelay = p_i45111_4_;
//            this.maxDelay = p_i45111_5_;
//        }
//
//        public ResourceLocation getMusicTickerLocation() {
//            return this.identity;
//        }
//
//        public int getMinDelay() {
//            return this.minDelay;
//        }
//
//        public int getMaxDelay() {
//            return this.maxDelay;
//        }
//    }
//}