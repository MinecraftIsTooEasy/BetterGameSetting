package moddedmite.xylose.bettergamesetting.client.gui.world;

import moddedmite.xylose.bettergamesetting.api.IGuiSelectWorld;
import moddedmite.xylose.bettergamesetting.client.gui.base.GuiListExtended;
import moddedmite.xylose.bettergamesetting.util.ScreenUtil;
import net.minecraft.DynamicTexture;
import net.minecraft.GuiScreen;
import net.minecraft.Minecraft;
import net.minecraft.ResourceLocation;
import net.minecraft.SaveFormatComparator;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GuiListWorldSelection extends GuiListExtended {
    private final GuiScreen parent;
    private final IGuiSelectWorld screen;
    private final List<GuiListExtended.IGuiListEntry> entries = new ArrayList<>();
    private static final Map<String, ResourceLocation> worldIcons = new HashMap<>();
    private static final ResourceLocation ICON_MISSING = new ResourceLocation("textures/misc/unknown_pack.png");

    public GuiListWorldSelection(GuiScreen parent, Minecraft mc, int width, int height, int top, int bottom, int slotHeight) {
        super(mc, width, height, top, bottom, slotHeight);
        this.parent = parent;
        this.screen = (IGuiSelectWorld) parent;
        this.centerListVertically = false;
        this.refreshList();
    }

    public void refreshList() {
        this.entries.clear();
        List<SaveFormatComparator> saves = this.screen.getSaveList();
        for (int i = 0; i < saves.size(); ++i) {
            this.entries.add(new GuiListWorldSelectionEntry(i, saves.get(i)));
        }
    }

    @Override
    protected int getSize() {
        return this.entries.size();
    }

    @Override
    public GuiListExtended.IGuiListEntry getListEntry(int index) {
        return this.entries.get(index);
    }

    @Override
    protected void drawBackground() {
        this.parent.drawDefaultBackground();
    }

    @Override
    protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {
        this.screen.onElementClicked(slotIndex, isDoubleClick);
    }

    @Override
    protected boolean isSelected(int slotIndex) {
        return slotIndex == this.screen.getSelectedIndex();
    }
    
    @Override
    public int getListWidth() {
        return 250;
    }
    
    @Override
    protected int getScrollBarX() {
        return super.getScrollBarX() + 10;
    }

    @Override
    protected void drawTooltip(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY) {
        GuiListExtended.IGuiListEntry entry = this.getListEntry(slotIndex);
        if (entry instanceof GuiListWorldSelectionEntry worldEntry) {
            worldEntry.drawHoverTooltip(x, y, listWidth, slotHeight, mouseX, mouseY);
        }
    }

    public static File getIconFile(String folderName) {
        return new File(new File(Minecraft.getMinecraft().saves_dir_MITE, folderName), "icon.png");
    }

    private static ResourceLocation getIcon(String folderName) {
        ResourceLocation cached = worldIcons.get(folderName);
        if (cached != null) return cached;
        File file = getIconFile(folderName);
        if (!file.isFile()) return null;
        try {
            BufferedImage image = ImageIO.read(file);
            if (image != null) {
                DynamicTexture texture = new DynamicTexture(image);
                ResourceLocation location = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation("worldicon_" + folderName, texture);
                worldIcons.put(folderName, location);
                return location;
            }
        } catch (Throwable throwable) {
            return null;
        }
        return null;
    }

    public static void drawIcon(String folderName, int x, int y) {
        ResourceLocation location = getIcon(folderName);
        if (location == null) location = ICON_MISSING;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft().getTextureManager().bindTexture(location);
        ScreenUtil.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 0.0F, 32, 32, 32F, 32F);
    }
}
