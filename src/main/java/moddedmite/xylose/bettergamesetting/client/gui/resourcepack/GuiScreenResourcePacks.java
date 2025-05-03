package moddedmite.xylose.bettergamesetting.client.gui.resourcepack;

import com.google.common.collect.Lists;
import moddedmite.xylose.bettergamesetting.api.IGameSetting;
import moddedmite.xylose.bettergamesetting.api.IGuiSlot;
import moddedmite.xylose.bettergamesetting.api.IResourcePackRepository;
import moddedmite.xylose.bettergamesetting.client.gui.button.GuiOptionButton;
import net.minecraft.*;
import org.lwjgl.Sys;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class GuiScreenResourcePacks extends GuiScreen {
    private static final Logger logger = Logger.getLogger("");
    private final GuiScreen parentScreen;
    private List availableResourcePacks;
    private List selectedResourcePacks;
    private GuiResourcePackAvailable availableResourcePacksList;
    private GuiResourcePackSelected selectedResourcePacksList;
    private boolean changed = false;

    public GuiScreenResourcePacks(GuiScreen parentScreenIn) {
        this.parentScreen = parentScreenIn;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui() {
        this.buttonList.add(new GuiOptionButton(2, this.width / 2 - 154, this.height - 48, I18n.getString("resourcePack.openFolder")));
        this.buttonList.add(new GuiOptionButton(1, this.width / 2 + 4, this.height - 48, I18n.getString("gui.done")));

        if (!this.changed) {
            this.availableResourcePacks = Lists.<ResourcePackListEntry>newArrayList();
            this.selectedResourcePacks = Lists.<ResourcePackListEntry>newArrayList();
            ResourcePackRepository resourcepackrepository = this.mc.getResourcePackRepository();
            resourcepackrepository.updateRepositoryEntriesAll();
            List<ResourcePackRepositoryEntry> list = Lists.newArrayList(resourcepackrepository.getRepositoryEntriesAll());
            list.removeAll(resourcepackrepository.getRepositoryEntries());

            for (ResourcePackRepositoryEntry resourcepackrepository$entry : list) {
                this.availableResourcePacks.add(new ResourcePackListEntryFound(this, resourcepackrepository$entry));
            }

            for (Object o : Lists.reverse(resourcepackrepository.getRepositoryEntries())) {
                ResourcePackRepositoryEntry resourcepackrepository$entry1 = (ResourcePackRepositoryEntry) o;
                this.selectedResourcePacks.add(new ResourcePackListEntryFound(this, resourcepackrepository$entry1));
            }

//            this.selectedResourcePacks.add(new ResourcePackListEntryDefault(this));
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
                        logger.info(s);
                        Runtime.getRuntime().exec(new String[]{"/usr/bin/open", s});
                        return;
                    } catch (IOException ioexception1) {
                        logger.severe("Couldn\'t open file");
                        logger.severe(ioexception1.getMessage());
                    }
                } else if (Util.getOSType() == EnumOS.WINDOWS) {
                    String s1 = String.format("cmd.exe /C start \"Open file\" \"%s\"", new Object[]{s});

                    try {
                        Runtime.getRuntime().exec(s1);
                        return;
                    } catch (IOException ioexception) {
                        logger.severe("Couldn\'t open file");
                        logger.severe(ioexception.getMessage());
                    }
                }

                boolean flag = false;

                try {
                    Class oclass = Class.forName("java.awt.Desktop");
                    Object object = oclass.getMethod("getDesktop", new Class[0]).invoke(null, new Object[0]);
                    oclass.getMethod("browse", new Class[]{URI.class}).invoke(object, new Object[]{file1.toURI()});
                } catch (Throwable throwable) {
                    logger.severe("Couldn\'t open link");
                    logger.severe(throwable.getMessage());
                    flag = true;
                }

                if (flag) {
                    logger.info("Opening via system class!");
                    Sys.openURL("file://" + s);
                }
            } else if (button.id == 1) {
                if (this.changed) {
                    List<ResourcePackRepositoryEntry> list = Lists.newArrayList();

                    for (Object o : this.selectedResourcePacks) {
                        ResourcePackListEntry resourcepacklistentry = (ResourcePackListEntry) o;
                        if (resourcepacklistentry instanceof ResourcePackListEntryFound) {
                            list.add(((ResourcePackListEntryFound) resourcepacklistentry).func_148318_i());
                        }
                    }

                    Collections.reverse(list);
                    ((IResourcePackRepository) this.mc.getResourcePackRepository()).setRepositories(list);
                    ((IGameSetting) this.mc.gameSettings).getResourcePacks().clear();
                    ((IGameSetting) this.mc.gameSettings).getIncompatibleResourcePacks().clear();

                    for (ResourcePackRepositoryEntry resourcepackrepository$entry : list) {
                        ((IGameSetting) this.mc.gameSettings).getResourcePacks().add(resourcepackrepository$entry.getResourcePackName());

                        if (((IResourcePackRepository) resourcepackrepository$entry).getPackFormat() != 1) {
                            ((IGameSetting) this.mc.gameSettings).getIncompatibleResourcePacks().add(resourcepackrepository$entry.getResourcePackName());
                        }
                    }

                    this.mc.gameSettings.saveOptions();
                    this.mc.refreshResources();
                }

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

    public void markChanged() {
        this.changed = true;
    }
}
