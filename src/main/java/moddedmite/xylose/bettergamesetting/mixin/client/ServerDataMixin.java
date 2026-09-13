package moddedmite.xylose.bettergamesetting.mixin.client;

import moddedmite.xylose.bettergamesetting.api.IServerData;
import net.minecraft.NBTTagCompound;
import net.minecraft.ServerData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerData.class)
public class ServerDataMixin implements IServerData {
    @Unique private String serverIcon;

    @Override
    public String getBase64EncodedIconData() {
        return this.serverIcon;
    }

    @Override
    public void setBase64EncodedIconData(String icon) {
        this.serverIcon = icon;
    }

    @Inject(method = "getNBTCompound", at = @At("TAIL"))
    private void writeIcon(CallbackInfoReturnable<NBTTagCompound> cir) {
        if (this.serverIcon != null) {
            cir.getReturnValue().setString("icon", this.serverIcon);
        }
    }

    @Inject(method = "getServerDataFromNBTCompound", at = @At("TAIL"))
    private static void readIcon(NBTTagCompound par0NBTTagCompound, CallbackInfoReturnable<ServerData> cir) {
        if (par0NBTTagCompound.hasKey("icon")) {
            ((IServerData) cir.getReturnValue()).setBase64EncodedIconData(par0NBTTagCompound.getString("icon"));
        }
    }
}
