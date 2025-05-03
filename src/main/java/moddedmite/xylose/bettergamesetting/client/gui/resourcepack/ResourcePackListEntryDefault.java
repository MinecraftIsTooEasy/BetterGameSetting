package moddedmite.xylose.bettergamesetting.client.gui.resourcepack;

import com.google.gson.JsonParseException;
import net.minecraft.*;

import java.util.logging.Logger;

public class ResourcePackListEntryDefault extends ResourcePackListEntry {
    private static final Logger logger = Logger.getLogger("");
    private final ResourcePack resourcePack;
    private final ResourceLocation resourcePackIcon;

    public ResourcePackListEntryDefault(GuiScreenResourcePacks resourcePacksGUIIn) {
        super(resourcePacksGUIIn);
        this.resourcePack = this.mc.getResourcePackRepository().rprDefaultResourcePack;
        DynamicTexture dynamictexture;

        dynamictexture = new DynamicTexture(this.resourcePack.getPackImage());
        this.resourcePackIcon = this.mc.getTextureManager().getDynamicTextureLocation("texturepackicon", dynamictexture);
    }

    protected int getPackFormat() {
        return 1;
    }

    protected String getPackDescription() {
        try {
            PackMetadataSection packmetadatasection = (PackMetadataSection) this.resourcePack.getPackMetadata(this.mc.getResourcePackRepository().rprMetadataSerializer, "pack");

            if (packmetadatasection != null) {
                return packmetadatasection.getPackDescription();
            }
        } catch (JsonParseException jsonparseexception) {
            logger.severe("Couldn\'t load metadata info");
            logger.severe(jsonparseexception.getMessage());
        }

        return EnumChatFormatting.RED + "Missing " + "pack.mcmeta" + " :(";
    }

    protected boolean hasNotPackEntry() {
        return false;
    }

    protected boolean hasPackEntry() {
        return false;
    }

    protected boolean func_148314_g() {
        return false;
    }

    protected boolean func_148307_h() {
        return false;
    }

    protected String getPackName() {
        return "Default";
    }

    protected void getPackIcon() {
        this.mc.getTextureManager().bindTexture(this.resourcePackIcon);
    }

    protected boolean func_148310_d() {
        return false;
    }
}
