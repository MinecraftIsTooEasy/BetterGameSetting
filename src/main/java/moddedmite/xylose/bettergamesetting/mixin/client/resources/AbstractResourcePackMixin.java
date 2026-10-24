package moddedmite.xylose.bettergamesetting.mixin.client.resources;

import moddedmite.xylose.bettergamesetting.api.ResourcePackSource;
import net.minecraft.AbstractResourcePack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.io.File;

@Mixin(AbstractResourcePack.class)
public interface AbstractResourcePackMixin extends ResourcePackSource {

    @Override
    @Accessor("resourcePackFile")
    File bgs$getSourceFile();
}
