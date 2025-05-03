package moddedmite.xylose.bettergamesetting.mixin.common.client.resources;

import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import moddedmite.xylose.bettergamesetting.util.SimpleReloadableResourceManagerINNER1;
import net.minecraft.Minecraft;
import net.minecraft.ResourcePack;
import net.minecraft.SimpleReloadableResourceManager;
import net.xiaoyu233.fml.util.ReflectHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(SimpleReloadableResourceManager.class)
public class SimpleReloadableResourceManagerMixin {
    @Shadow private static final Joiner joinerResourcePacks = Joiner.on(", ");
    @Shadow public void reloadResourcePack(ResourcePack par1ResourcePack) {}
    @Shadow private void clearResources() {}
    @Shadow private void notifyReloadListeners() {}

    /**
     * @author Xy_Lose
     * @reason disable MITE Resource Pack
     */
    @Overwrite
    public void reloadResources(List par1List) {
        this.clearResources();
        Minecraft.getMinecraft().getLogAgent().logInfo("Reloading ResourceManager: " + joinerResourcePacks.join(Iterables.transform(par1List, new SimpleReloadableResourceManagerINNER1(ReflectHelper.dyCast(this)))));

        for (Object o : par1List) {
            ResourcePack resourcePack = (ResourcePack) o;
            this.reloadResourcePack(resourcePack);
        }

        this.notifyReloadListeners();
    }
}
