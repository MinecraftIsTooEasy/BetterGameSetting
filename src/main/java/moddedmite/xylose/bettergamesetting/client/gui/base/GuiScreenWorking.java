package moddedmite.xylose.bettergamesetting.client.gui.base;

import net.minecraft.GuiScreen;
import net.minecraft.IProgressUpdate;

public class GuiScreenWorking extends GuiScreen implements IProgressUpdate {
    private String message = "";
    private String message1 = "";
    private int progress;
    private boolean doneWorking;

    /**
     * "Saving level", or the loading,or downloading equivelent
     */
    public void displayProgressMessage(String message) {
        this.resetProgressAndMessage(message);
    }

    /**
     * this string, followed by "working..." and then the "% complete" are the 3 lines shown. This resets progress to 0,
     * and the WorkingString to "working...".
     */
    public void resetProgressAndMessage(String message) {
        this.message = message;
        this.resetProgresAndWorkingMessage("Working...");
    }

    /**
     * This is called with "Working..." by resetProgressAndMessage
     */
    public void resetProgresAndWorkingMessage(String message) {
        this.message1 = message;
        this.setLoadingProgress(0);
    }

    /**
     * Updates the progress bar on the loading screen to the specified amount. Args: loadProgress
     */
    public void setLoadingProgress(int progress) {
        this.progress = progress;
    }

    public void setDoneWorking() {
        this.doneWorking = true;
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (this.doneWorking) {
            this.mc.displayGuiScreen((GuiScreen) null);
        } else {
            this.drawDefaultBackground();
            this.drawCenteredString(this.fontRenderer, this.message, this.width / 2, 70, 16777215);
            this.drawCenteredString(this.fontRenderer, this.message1 + " " + this.progress + "%", this.width / 2, 90, 16777215);
            super.drawScreen(mouseX, mouseY, partialTicks);
        }
    }
}
