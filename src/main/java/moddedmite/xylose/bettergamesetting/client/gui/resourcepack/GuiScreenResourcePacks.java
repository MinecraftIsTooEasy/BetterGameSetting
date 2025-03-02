package moddedmite.xylose.bettergamesetting.client.gui.resourcepack;

import com.google.common.collect.Lists;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import moddedmite.xylose.bettergamesetting.api.IGameSetting;
import moddedmite.xylose.bettergamesetting.api.IGuiSlot;
import moddedmite.xylose.bettergamesetting.api.IResourcePackRepository;
import net.minecraft.*;
import net.xiaoyu233.fml.FishModLoader;
import org.lwjgl.Sys;

public class GuiScreenResourcePacks extends GuiScreen {
    private GuiScreen parentScreen;
    private List availableResourcePacks;
    private List selectedResourcePacks;
    private GuiResourcePackAvailable availableResourcePacksList;
    private GuiResourcePackSelected selectedResourcePacksList;

    public GuiScreenResourcePacks(GuiScreen p_i45050_1_) {
        this.parentScreen = p_i45050_1_;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui() {
        this.buttonList.add(new GuiButton(2, this.width / 2 - 154, this.height - 48, 150, 20, I18n.getString("resourcePack.openFolder")));
        this.buttonList.add(new GuiButton(1, this.width / 2 + 4, this.height - 48, 150, 20, I18n.getString("gui.done")));
        this.availableResourcePacks = new ArrayList();
        this.selectedResourcePacks = new ArrayList();
        ResourcePackRepository resourcepackrepository = this.mc.getResourcePackRepository();
        resourcepackrepository.updateRepositoryEntriesAll();
        ArrayList arraylist = Lists.newArrayList(resourcepackrepository.getRepositoryEntriesAll());
        arraylist.removeAll(resourcepackrepository.getRepositoryEntries());
        Iterator iterator = arraylist.iterator();
        ResourcePackRepositoryEntry entry;

        while (iterator.hasNext()) {
            entry = (ResourcePackRepositoryEntry) iterator.next();
            this.availableResourcePacks.add(new ResourcePackListEntryFound(this, entry));
        }

        iterator = Lists.reverse(resourcepackrepository.getRepositoryEntries()).iterator();

        while (iterator.hasNext()) {
            entry = (ResourcePackRepositoryEntry) iterator.next();
            this.selectedResourcePacks.add(new ResourcePackListEntryFound(this, entry));
        }

        this.selectedResourcePacks.add(new ResourcePackListEntryDefault(this));
        this.availableResourcePacksList = new GuiResourcePackAvailable(this.mc, 200, this.height, this.availableResourcePacks);
        ((IGuiSlot) this.availableResourcePacksList).setSlotXBoundsFromLeft(this.width / 2 - 4 - 200);
        this.availableResourcePacksList.registerScrollButtons(7, 8);
        this.selectedResourcePacksList = new GuiResourcePackSelected(this.mc, 200, this.height, this.selectedResourcePacks);
        ((IGuiSlot) this.selectedResourcePacksList).setSlotXBoundsFromLeft(this.width / 2 + 4);
        this.selectedResourcePacksList.registerScrollButtons(7, 8);
    }

    public boolean hasResourcePackEntry(ResourcePackListEntry resourcePackListEntry) {
        return this.selectedResourcePacks.contains(resourcePackListEntry);
    }

    public List<ResourcePackListEntry> getListContaining(ResourcePackListEntry resourcePackListEntry) {
        return this.hasResourcePackEntry(resourcePackListEntry) ? this.selectedResourcePacks : this.availableResourcePacks;
    }

    public List<ResourcePackListEntry> getAvailableResourcePacks() {
        return this.availableResourcePacks;
    }

    public List<ResourcePackListEntry> getSelectedResourcePacks() {
        return this.selectedResourcePacks;
    }

    protected void actionPerformed(GuiButton button) {
        if (button.enabled) {
            if (button.id == 2) {
                File file1 = this.mc.getResourcePackRepository().getDirResourcepacks();
                String s = file1.getAbsolutePath();

                if (Util.getOSType() == EnumOS.MACOS) {
                    try {
                        FishModLoader.LOGGER.info(s);
                        Runtime.getRuntime().exec(new String[]{"/usr/bin/open", s});
                        return;
                    } catch (IOException ioexception1) {
                        FishModLoader.LOGGER.error("Couldn\'t open file", ioexception1);
                    }
                } else if (Util.getOSType() == EnumOS.WINDOWS) {
                    String s1 = String.format("cmd.exe /C start \"Open file\" \"%s\"", new Object[]{s});

                    try {
                        Runtime.getRuntime().exec(s1);
                        return;
                    } catch (IOException ioexception) {
                        FishModLoader.LOGGER.error("Couldn\'t open file", ioexception);
                    }
                }

                boolean flag = false;

                try {
                    Class oclass = Class.forName("java.awt.Desktop");
                    Object object = oclass.getMethod("getDesktop", new Class[0]).invoke(null, new Object[0]);
                    oclass.getMethod("browse", new Class[]{URI.class}).invoke(object, new Object[]{file1.toURI()});
                } catch (Throwable throwable) {
                    FishModLoader.LOGGER.error("Couldn\'t open link", throwable);
                    flag = true;
                }

                if (flag) {
                    FishModLoader.LOGGER.info("Opening via system class!");
                    Sys.openURL("file://" + s);
                }
            } else if (button.id == 1) {
                ArrayList arraylist = Lists.newArrayList();
                Iterator iterator = this.selectedResourcePacks.iterator();

                while (iterator.hasNext()) {
                    ResourcePackListEntry resourcepacklistentry = (ResourcePackListEntry) iterator.next();

                    if (resourcepacklistentry instanceof ResourcePackListEntryFound) {
                        arraylist.add(((ResourcePackListEntryFound) resourcepacklistentry).func_148318_i());
                    }
                }

                Collections.reverse(arraylist);
                ((IResourcePackRepository) this.mc.getResourcePackRepository()).func_148527_a(arraylist);
                ((IGameSetting) this.mc.gameSettings).getResourcePacks().clear();
                iterator = arraylist.iterator();

                while (iterator.hasNext()) {
                    ResourcePackRepositoryEntry entry = (ResourcePackRepositoryEntry) iterator.next();
                    ((IGameSetting) this.mc.gameSettings).getResourcePacks().add(entry.getResourcePackName());
                }

                this.mc.gameSettings.saveOptions();
                this.mc.refreshResources();
                this.mc.displayGuiScreen(this.parentScreen);
            }
        }
    }

    /**
     * Called when the mouse is clicked.
     */
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.availableResourcePacksList.mouseClicked(mouseX, mouseY, mouseButton);
        this.selectedResourcePacksList.mouseClicked(mouseX, mouseY, mouseButton);
    }

    /**
     * Called when the mouse is moved or a mouse button is released.  Signature: (mouseX, mouseY, which) which==-1 is
     * mouseMove, which==0 or which==1 is mouseUp
     */
    protected void mouseMovedOrUp(int mouseX, int mouseY, int state) {
        super.mouseMovedOrUp(mouseX, mouseY, state);
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawBackground(0);
        this.availableResourcePacksList.drawScreen(mouseX, mouseY, partialTicks);
        this.selectedResourcePacksList.drawScreen(mouseX, mouseY, partialTicks);
        this.drawCenteredString(this.fontRenderer, I18n.getString("resourcePack.title"), this.width / 2, 16, 16777215);
        this.drawCenteredString(this.fontRenderer, I18n.getString("resourcePack.folderInfo"), this.width / 2 - 77, this.height - 26, 8421504);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
