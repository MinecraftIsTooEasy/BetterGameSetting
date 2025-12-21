package moddedmite.xylose.bettergamesetting.mixin.common;

import net.minecraft.GameRules;
import net.minecraft.GameRuleValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.TreeMap;

@Mixin(GameRules.class)
public class GameRulesMixin {
    @Shadow private TreeMap theGameRules;

    /**
     * @author Xy_Lose
     * @reason Allows free modification of game rules
     */
    @Overwrite
    public boolean getGameRuleBooleanValue(String p_82766_1_) {
        GameRuleValue value = (GameRuleValue) this.theGameRules.get(p_82766_1_);
        return value != null ? value.getGameRuleBooleanValue() : false;
    }
}
