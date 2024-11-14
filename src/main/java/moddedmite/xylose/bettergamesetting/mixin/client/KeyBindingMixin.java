package moddedmite.xylose.bettergamesetting.mixin.client;

import moddedmite.xylose.bettergamesetting.api.IKeyBinding;
import net.minecraft.KeyBinding;
import net.minecraft.I18n;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(KeyBinding.class)
public class KeyBindingMixin implements IKeyBinding {
    @Shadow
    public String keyDescription;
    @Shadow
    public int keyCode;
    @Unique
    private final String keyCategory;

    public KeyBindingMixin(String keyCategory) {
        this.keyCategory = keyCategory;
    }

    @Override
    public String getKeyCategory() {
        return this.keyCategory;
    }

    @Override
    public int compareTo(KeyBinding p_compareTo_1_) {
        int i = I18n.getString(this.keyCategory).compareTo(I18n.getString(this.keyCategory));

        if (i == 0) {
            i = I18n.getString(this.keyDescription).compareTo(I18n.getString(p_compareTo_1_.keyDescription));
        }

        return i;
    }

    @Override
    public int getKeyCode() {
        return this.keyCode;
    }

    @Override
    public void setKeyCode(int keyCode) {
        this.keyCode = keyCode;
    }
}
