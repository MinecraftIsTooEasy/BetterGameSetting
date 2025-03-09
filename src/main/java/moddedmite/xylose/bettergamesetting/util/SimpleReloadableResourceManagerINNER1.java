package moddedmite.xylose.bettergamesetting.util;

import com.google.common.base.Function;
import net.minecraft.ResourcePack;
import net.minecraft.SimpleReloadableResourceManager;

public class SimpleReloadableResourceManagerINNER1 implements Function {
    final SimpleReloadableResourceManager theSimpleReloadableResourceManager;

    public SimpleReloadableResourceManagerINNER1(SimpleReloadableResourceManager par1SimpleReloadableResourceManager) {
        this.theSimpleReloadableResourceManager = par1SimpleReloadableResourceManager;
    }

    public String apply(ResourcePack par1ResourcePack) {
        return par1ResourcePack.getPackName();
    }

    public Object apply(Object par1Obj) {
        return this.apply((ResourcePack) par1Obj);
    }
}
