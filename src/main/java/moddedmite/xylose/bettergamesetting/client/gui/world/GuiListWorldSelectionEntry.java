package moddedmite.xylose.bettergamesetting.client.gui.world;

import moddedmite.rustedironcore.internal.unsafe.MiteReleaseAccess;
import moddedmite.xylose.bettergamesetting.api.IGuiSelectWorld;
import moddedmite.xylose.bettergamesetting.client.audio.PositionedSoundRecord;
import moddedmite.xylose.bettergamesetting.client.audio.SoundEvents;
import moddedmite.xylose.bettergamesetting.client.gui.base.GuiListExtended;
import moddedmite.xylose.bettergamesetting.client.gui.base.GuiScreenWorking;
import moddedmite.xylose.bettergamesetting.client.gui.base.GuiYesNoModern;
import moddedmite.xylose.bettergamesetting.init.BGSClient;
import moddedmite.xylose.bettergamesetting.util.ScreenUtil;
import net.minecraft.DynamicTexture;
import net.minecraft.EnumChatFormatting;
import net.minecraft.FontRenderer;
import net.minecraft.Gui;
import net.minecraft.GuiCreateWorld;
import net.minecraft.GuiRenameWorld;
import net.minecraft.GuiScreen;
import net.minecraft.I18n;
import net.minecraft.ISaveFormat;
import net.minecraft.ISaveHandler;
import net.minecraft.Minecraft;
import net.minecraft.ResourceLocation;
import net.minecraft.SaveFormatComparator;
import net.minecraft.TextureObject;
import net.minecraft.WorldInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GuiListWorldSelectionEntry implements GuiListExtended.IGuiListEntry {
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat();
    private static final ResourceLocation ICON_MISSING = new ResourceLocation("textures/misc/unknown_pack.png");
    private static final ResourceLocation ICON_OVERLAY_LOCATION = new ResourceLocation(BGSClient.resourceId, "textures/gui/world_selection.png");

    private final Minecraft client;
    private final GuiScreen worldSelScreen;
    private final IGuiSelectWorld worldSelInterface;
    private final SaveFormatComparator worldSummary;
    private final ResourceLocation iconLocation;
    private final GuiListWorldSelection containingListSel;
    private final String versionName;
    private final boolean markVersionInList;
    private final boolean askToOpenWorld;
    private File iconFile;
    private DynamicTexture icon;
    private long lastClickTime;

    public GuiListWorldSelectionEntry(GuiListWorldSelection listWorldSelIn, SaveFormatComparator worldSummaryIn) {
        this.containingListSel = listWorldSelIn;
        this.worldSelScreen = listWorldSelIn.getParentScreen();
        this.worldSelInterface = listWorldSelIn.getGuiWorldSelection();
        this.worldSummary = worldSummaryIn;
        this.client = Minecraft.getMinecraft();
        this.iconLocation = new ResourceLocation("worlds/" + worldSummaryIn.getFileName() + "/icon");
        this.iconFile = GuiListWorldSelection.getIconFile(worldSummaryIn.getFileName());

        if (!this.iconFile.isFile()) {
            this.iconFile = null;
        }

        TextureObject existingIcon = this.client.getTextureManager().getTexture(this.iconLocation);
        this.icon = existingIcon instanceof DynamicTexture ? (DynamicTexture) existingIcon : null;

        WorldInfo worldInfo = this.client.getSaveLoader().getSaveLoader(worldSummaryIn.getFileName(), false).loadWorldInfo();
        int version = getClientMiteRelease();
        if (worldInfo == null) {
            this.versionName = null;
            this.markVersionInList = false;
            this.askToOpenWorld = false;
        } else {
            int earliest = worldInfo.getEarliestMITEReleaseRunIn();
            int latest = worldInfo.getLatestMITEReleaseRunIn();
            this.versionName = earliest == latest ? "R" + earliest : "R" + earliest + " - R" + latest;
            this.markVersionInList = earliest != version || latest != version;
            this.askToOpenWorld = latest > version;
        }

        this.loadServerIcon();
    }

    private static int getClientMiteRelease() {
        try {
            Field field = MiteReleaseAccess.class.getDeclaredField("miteRelease");
            field.setAccessible(true);
            return field.getInt(null);
        } catch (Exception e) {
            return Minecraft.MITE_release_number;
        }
    }

    @Override
    public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected) {
        String s = this.worldSummary.getDisplayName();
        String s1 = this.worldSummary.getFileName() + " (" + DATE_FORMAT.format(new Date(this.worldSummary.getLastTimePlayed())) + ")";
        String s2;

        if (StringUtils.isEmpty(s)) {
            s = I18n.getString("selectWorld.world") + " " + (slotIndex + 1);
        }

        if (!this.worldSummary.passed_validation) {
            s = s + EnumChatFormatting.DARK_GRAY + " | " + EnumChatFormatting.RED + (this.worldSummary.failed_validation_reason == null ? "INVALID WORLD" : this.worldSummary.failed_validation_reason);
        }

        if (this.worldSummary.requiresConversion()) {
            s2 = I18n.getString("selectWorld.conversion") + " ";
        } else {
            s2 = I18n.getString("gameMode." + this.worldSummary.getEnumGameType().getName());

            if (this.worldSummary.isHardcoreModeEnabled()) {
                s2 = EnumChatFormatting.DARK_RED + I18n.getString("gameMode.hardcore") + EnumChatFormatting.RESET;
            }

            if (this.worldSummary.areSkillsEnabled()) {
                s2 = s2 + ", " + I18n.getString("selectWorld.skills");
            }

            if (this.worldSummary.getCheatsEnabled()) {
                s2 = s2 + ", " + I18n.getString("selectWorld.cheats");
            }
            
            if (this.versionName != null) {
                if (this.markVersionInList) {
                    if (this.askToOpenWorld) {
                        s2 = s2 + ", " + I18n.getString("selectWorld.version") + " " + EnumChatFormatting.RED + this.versionName + EnumChatFormatting.RESET;
                    } else {
                        s2 = s2 + ", " + I18n.getString("selectWorld.version") + " " + EnumChatFormatting.ITALIC + this.versionName + EnumChatFormatting.RESET;
                    }
                } else {
                    s2 = s2 + ", " + I18n.getString("selectWorld.version") + " " + this.versionName;
                }
            }
        }

        this.client.fontRenderer.drawStringWithShadow(trimToWidth(this.client.fontRenderer, s, getTextWidth(listWidth)), x + 36, y + 1, 16777215);
        this.client.fontRenderer.drawStringWithShadow(trimToWidth(this.client.fontRenderer, s1, getTextWidth(listWidth)), x + 36, y + 12, 8421504);
        this.client.fontRenderer.drawStringWithShadow(trimToWidth(this.client.fontRenderer, s2, getTextWidth(listWidth)), x + 36, y + 22, 8421504);

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.client.getTextureManager().bindTexture(this.icon != null ? this.iconLocation : ICON_MISSING);
        GL11.glEnable(GL11.GL_BLEND);
        ScreenUtil.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 0.0F, 32, 32, 32.0F, 32.0F);
        GL11.glDisable(GL11.GL_BLEND);

        if (this.client.gameSettings.touchscreen || isSelected) {
            this.client.getTextureManager().bindTexture(ICON_OVERLAY_LOCATION);
            Gui.drawRect(x, y, x + 32, y + 32, -1601138544);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            int j = mouseX - x;
            int i = j < 32 ? 32 : 0;

            if (!this.worldSummary.passed_validation) {
                ScreenUtil.drawModalRectWithCustomSizedTexture(x, y, 32.0F, (float) i, 32, 32, 256.0F, 256.0F);
                ScreenUtil.drawModalRectWithCustomSizedTexture(x, y, 96.0F, (float) i, 32, 32, 256.0F, 256.0F);

                if (j < 32 && this.worldSummary.failed_validation_reason != null) {
                    this.worldSelInterface.setVersionTooltip(EnumChatFormatting.RED + this.worldSummary.failed_validation_reason);
                }
            } else {
                ScreenUtil.drawModalRectWithCustomSizedTexture(x, y, 0.0F, (float) i, 32, 32, 256.0F, 256.0F);
            }
            if (this.client.fontRenderer.getStringWidth(s1) > getTextWidth(listWidth)
                    && mouseY >= y + 11 && mouseY < y + this.client.fontRenderer.FONT_HEIGHT + 13
                    && mouseX >= x + 36 && mouseX < x + listWidth) {
                this.worldSelInterface.setVersionTooltip(s1);
            }
        }
    }

    private static int getTextWidth(int listWidth) {
        return listWidth - 36 - 2;
    }

    private static String trimToWidth(FontRenderer font, String text, int maxWidth) {
        if (font.getStringWidth(text) <= maxWidth) return text;
        int width = maxWidth - font.getStringWidth("...");
        return font.trimStringToWidth(text, width) + "...";
    }

    @Override
    public boolean mousePressed(int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX, int relativeY) {
        this.containingListSel.selectWorld(slotIndex);

        if (relativeX < 32) {
            this.joinWorld();
            return true;
        } else if (Minecraft.getSystemTime() - this.lastClickTime < 250L) {
            this.joinWorld();
            return true;
        } else {
            this.lastClickTime = Minecraft.getSystemTime();
            return false;
        }
    }

    public void joinWorld() {
        if (this.worldSummary.passed_validation) {
            this.loadWorld();
        }
    }

    public void deleteWorld() {
        this.client.displayGuiScreen(new GuiYesNoModern((result, id) -> {
            if (result) {
                GuiListWorldSelectionEntry.this.client.displayGuiScreen(new GuiScreenWorking());
                ISaveFormat isaveformat = GuiListWorldSelectionEntry.this.client.getSaveLoader();
                isaveformat.flushCache();
                isaveformat.deleteWorldDirectory(GuiListWorldSelectionEntry.this.worldSummary.getFileName());
                GuiListWorldSelectionEntry.this.containingListSel.refreshList();
            }

            GuiListWorldSelectionEntry.this.client.displayGuiScreen(GuiListWorldSelectionEntry.this.worldSelScreen);
        }, I18n.getString("selectWorld.deleteQuestion"), "'" + this.worldSummary.getDisplayName() + "' " + I18n.getString("selectWorld.deleteWarning"), I18n.getString("selectWorld.deleteButton"), I18n.getString("gui.cancel"), 0));
    }

    public void editWorld() {
        this.client.displayGuiScreen(new GuiRenameWorld(this.worldSelScreen, this.worldSummary.getFileName()));
    }

    public void recreateWorld() {
        this.client.displayGuiScreen(new GuiScreenWorking());
        GuiCreateWorld guicreateworld = new GuiCreateWorld(this.worldSelScreen);
        ISaveHandler isavehandler = this.client.getSaveLoader().getSaveLoader(this.worldSummary.getFileName(), false);
        WorldInfo worldinfo = isavehandler.loadWorldInfo();
        isavehandler.flush();

        if (worldinfo != null) {
            guicreateworld.func_82286_a(worldinfo);
            this.client.displayGuiScreen(guicreateworld);
        }
    }

    private void loadWorld() {
        this.client.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));

        if (this.client.getSaveLoader().canLoadWorld(this.worldSummary.getFileName())) {
            this.worldSelInterface.loadWorld(this.worldSummary.getFileName());
        }
    }

    private void loadServerIcon() {
        boolean flag = this.iconFile != null && this.iconFile.isFile();

        if (flag) {
            BufferedImage bufferedimage;

            try {
                bufferedimage = ImageIO.read(this.iconFile);
                Validate.validState(bufferedimage.getWidth() == 64, "Must be 64 pixels wide");
                Validate.validState(bufferedimage.getHeight() == 64, "Must be 64 pixels high");
            } catch (Throwable throwable) {
                this.iconFile = null;
                this.freeIcon();
                return;
            }

            if (this.icon == null) {
                this.icon = new DynamicTexture(bufferedimage.getWidth(), bufferedimage.getHeight());
                this.client.getTextureManager().loadTexture(this.iconLocation, this.icon);
            }

            bufferedimage.getRGB(0, 0, bufferedimage.getWidth(), bufferedimage.getHeight(), this.icon.getTextureData(), 0, bufferedimage.getWidth());
            this.icon.updateDynamicTexture();
        } else {
            this.freeIcon();
        }
    }

    private void freeIcon() {
        if (this.icon != null) {
            ScreenUtil.deleteTexture(this.iconLocation, this.icon.getGlTextureId());
            this.icon = null;
        }
    }

    public boolean isPassedValidation() {
        return this.worldSummary.passed_validation;
    }

    @Override
    public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {
    }

    @Override
    public void keyTyped(int slotIndex, char typedChar, int keyCode) {
    }

    @Override
    public void setSelected(int slotIndex, int mouseX, int mouseY) {
    }
}
