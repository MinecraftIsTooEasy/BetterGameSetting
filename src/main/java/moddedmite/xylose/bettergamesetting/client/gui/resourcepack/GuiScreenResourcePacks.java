package moddedmite.xylose.bettergamesetting.client.gui.resourcepack;

import com.google.common.collect.Lists;
import moddedmite.xylose.bettergamesetting.api.IGameSetting;
import moddedmite.xylose.bettergamesetting.api.IResourcePackRepository;
import moddedmite.xylose.bettergamesetting.client.gui.button.GuiOptionButton;
import moddedmite.xylose.bettergamesetting.util.ScreenUtil;
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
    private List<ResourcePackListEntry> availableResourcePacks;
    private List<ResourcePackListEntry> selectedResourcePacks;
    public GuiResourcePackAvailable availableResourcePacksList;
    public GuiResourcePackSelected selectedResourcePacksList;
    private boolean changed = false;
    private GuiTextField searchField;
    private String lastSearchText = "";

    public GuiScreenResourcePacks(GuiScreen parentScreenIn) {
        this.parentScreen = parentScreenIn;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui() {
        this.searchField = new GuiTextField(this.fontRenderer, this.width / 2 - 100, 14, 200, 14);
        this.searchField.setMaxStringLength(256);
        this.searchField.setFocused(true);
        this.searchField.setHint(I18n.getString("options.search"));

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

            for (ResourcePackRepositoryEntry resourcepackrepository$entry1 : (List<ResourcePackRepositoryEntry>) Lists.reverse(resourcepackrepository.getRepositoryEntries())) {
                this.selectedResourcePacks.add(new ResourcePackListEntryFound(this, resourcepackrepository$entry1));
            }

            this.selectedResourcePacks.add(new ResourcePackListEntryDefault(this));
        }

        this.availableResourcePacksList = new GuiResourcePackAvailable(this.mc, 200, this.height, this.availableResourcePacks);
        this.availableResourcePacksList.setSlotXBoundsFromLeft(this.width / 2 - 15 - 200);
        this.availableResourcePacksList.registerScrollButtons(7, 8);
        this.selectedResourcePacksList = new GuiResourcePackSelected(this.mc, 200, this.height, this.selectedResourcePacks);
        this.selectedResourcePacksList.setSlotXBoundsFromLeft(this.width / 2 + 15);
        this.selectedResourcePacksList.registerScrollButtons(7, 8);
    }

    private void filterResourcePacks() {
        String searchText = this.searchField.getText().toLowerCase();
        if (searchText.equals(this.lastSearchText)) {
            return;
        }
        this.lastSearchText = searchText;

        this.availableResourcePacks.clear();
        this.selectedResourcePacks.clear();

        ResourcePackRepository resourcepackrepository = this.mc.getResourcePackRepository();
        resourcepackrepository.updateRepositoryEntriesAll();
        List<ResourcePackRepositoryEntry> list = Lists.newArrayList(resourcepackrepository.getRepositoryEntriesAll());
        List<ResourcePackRepositoryEntry> selectedEntries = Lists.newArrayList(resourcepackrepository.getRepositoryEntries());
        list.removeAll(selectedEntries);

        for (ResourcePackRepositoryEntry resourcepackrepository$entry : list) {
            ResourcePackListEntry entry = new ResourcePackListEntryFound(this, resourcepackrepository$entry);
            if (entry.getPackName().toLowerCase().contains(searchText)) {
                this.availableResourcePacks.add(entry);
            }
        }

        for (ResourcePackRepositoryEntry resourcepackrepository$entry1 : Lists.reverse(selectedEntries)) {
            ResourcePackListEntry entry = new ResourcePackListEntryFound(this, resourcepackrepository$entry1);
            if (entry.getPackName().toLowerCase().contains(searchText)) {
                this.selectedResourcePacks.add(entry);
            }
        }

        ResourcePackListEntryDefault defaultEntry = new ResourcePackListEntryDefault(this);
        if (searchText.isEmpty() || defaultEntry.getPackName().toLowerCase().contains(searchText)) {
            this.selectedResourcePacks.add(defaultEntry);
        }
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
                        logger.severe("Couldn't open file");
                        logger.severe(ioexception1.getMessage());
                    }
                } else if (Util.getOSType() == EnumOS.WINDOWS) {
                    String s1 = String.format("cmd.exe /C start \"Open file\" \"%s\"", s);

                    try {
                        Runtime.getRuntime().exec(s1);
                        return;
                    } catch (IOException ioexception) {
                        logger.severe("Couldn't open file");
                        logger.severe(ioexception.getMessage());
                    }
                }

                boolean flag = false;

                try {
                    Class oclass = Class.forName("java.awt.Desktop");
                    Object object = oclass.getMethod("getDesktop").invoke(null);
                    oclass.getMethod("browse", URI.class).invoke(object, file1.toURI());
                } catch (Throwable throwable) {
                    logger.severe("Couldn't open link");
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

                    for (ResourcePackListEntry resourcepacklistentry : this.selectedResourcePacks) {
                        if (resourcepacklistentry instanceof ResourcePackListEntryFound) {
                            list.add(((ResourcePackListEntryFound) resourcepacklistentry).func_148318_i());
                        }
                    }

                    Collections.reverse(list);
                    ((IResourcePackRepository) this.mc.getResourcePackRepository()).setRepositories(list);
                    this.mc.gameSettings.getResourcePacks().clear();
                    this.mc.gameSettings.getIncompatibleResourcePacks().clear();

                    for (ResourcePackRepositoryEntry resourcepackrepository$entry : list) {
                        this.mc.gameSettings.getResourcePacks().add(resourcepackrepository$entry.getResourcePackName());

                        if (((IResourcePackRepository) resourcepackrepository$entry).getPackFormat() != 1) {
                            this.mc.gameSettings.getIncompatibleResourcePacks().add(resourcepackrepository$entry.getResourcePackName());
                        }
                    }

                    this.mc.gameSettings.saveOptions();
                    this.mc.refreshResources();
                }

                this.mc.displayGuiScreen(this.parentScreen);
            }
        }
    }

    protected void keyTyped(char typedChar, int keyCode) {
        super.keyTyped(typedChar, keyCode);

        if (this.searchField.textboxKeyTyped(typedChar, keyCode)) {
            this.filterResourcePacks();
        }
    }

    /**
     * Called when the mouse is clicked.
     */
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.searchField.mouseClicked(mouseX, mouseY, mouseButton);
        this.availableResourcePacksList.mouseClicked(mouseX, mouseY, mouseButton);
        this.selectedResourcePacksList.mouseClicked(mouseX, mouseY, mouseButton);
        this.filterResourcePacks();
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
        if (this.mc.gameSettings.isTransparentBackground()) this.drawDefaultBackground();
        else this.drawBackground(0);
        this.availableResourcePacksList.drawScreen(mouseX, mouseY, partialTicks);
        this.selectedResourcePacksList.drawScreen(mouseX, mouseY, partialTicks);
        this.drawCenteredString(this.fontRenderer, I18n.getString("resourcePack.title"), this.width / 2, 4, 16777215);
        this.searchField.drawTextBox();
        GuiButton buttonFolder = ScreenUtil.getInstance().getButtonById(2, this.buttonList);
        if (buttonFolder != null && buttonFolder.func_82252_a()) {
            ScreenUtil.getInstance().drawButtonTooltip(I18n.getString("resourcePack.folderInfo"), mouseX, mouseY);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public void markChanged() {
        this.changed = true;
    }
}
