package moddedmite.xylose.bettergamesetting.util;

import net.xiaoyu233.fml.FishModLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class BGSMixinConfigCommon implements IMixinConfigPlugin {
    @Override
    public void onLoad(String s) {

    }

    @Override
    public String getRefMapperConfig() {
        return "";
    }

    @Override
    public boolean shouldApplyMixin(String s, String s1) {
        if (BGSConfig.freeDevAllowCheat.get()) {
            if (s1.contains("EnumGameTypeMixin$OhMyCommandsCompatMixin")) return !FishModLoader.hasMod("ohmycommands");
            if (s1.contains("ItemInWorldManagerMixin$OhMyCommandsCompatMixin")) return !FishModLoader.hasMod("ohmycommands");
        }
        if (s1.contains("widecheat")) return BGSConfig.freeDevAllowCheat.get();
        return true;
    }

    @Override
    public void acceptTargets(Set<String> set, Set<String> set1) {

    }

    @Override
    public List<String> getMixins() {
        return List.of();
    }

    @Override
    public void preApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {

    }

    @Override
    public void postApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {

    }
}
