package moddedmite.xylose.bettergamesetting.client.gui.resourcepack;

import net.minecraft.I18n;
import net.minecraft.Minecraft;

import java.util.List;

public class GuiResourcePackAvailable extends GuiResourcePackList {

    public GuiResourcePackAvailable(Minecraft mcIn, int widthIn, int heightIn, List<ResourcePackListEntry> resourcePackList) {
        super(mcIn, widthIn, heightIn, resourcePackList);
    }

    protected String getListHeader() {
        return I18n.getString("resourcePack.available.title");
    }
}
