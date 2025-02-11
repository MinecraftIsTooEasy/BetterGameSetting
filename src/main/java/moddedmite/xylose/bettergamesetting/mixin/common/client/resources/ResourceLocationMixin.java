package moddedmite.xylose.bettergamesetting.mixin.common.client.resources;

import net.minecraft.Minecraft;
import net.minecraft.ResourceLocation;
import net.xiaoyu233.fml.util.ReflectHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ResourceLocation.class)
public class ResourceLocationMixin {
    /**
     * @author Xy_Lose
     * @reason disable MITE Resource Pack
     */
    @Overwrite
    public boolean exists() {
        if (Minecraft.theMinecraft == null) {
            Minecraft.setErrorMessage("ResourceLocation.exists: theMinecraft==null, checking too early");
            return false;
        } else if (Minecraft.theMinecraft.mcDefaultResourcePack == null) {
            Minecraft.setErrorMessage("ResourceLocation.exists: mcDefaultResourcePack==null, checking too early");
            return false;
        } else {
            return Minecraft.theMinecraft.mcDefaultResourcePack.resourceExists(ReflectHelper.dyCast(this));
        }
    }
}
