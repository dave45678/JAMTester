/*
 * Decompiled with CFR 0_102.
 */
package jamtester;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.image.ImageObserver;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JDialog;

public class GradingToolAboutDialog
extends JDialog {
    Image img = null;
    public static final String versionID = "v1.30.011306";
    ImageObserver imgobs;

    public GradingToolAboutDialog(Frame frame) {
        super(frame, "About JAM*Tester v1.30.011306...");
        this.imgobs = this;
        try {
            this.img = ImageIO.read(new File("jamt.jpg"));
        }
        catch (Exception var2_2) {
            // empty catch block
        }
        this.setSize(this.img.getWidth(this), this.img.getHeight(this) + 25);
        this.setModal(true);
        this.setResizable(false);
        GradingToolAboutDialog.centerWindow(this);
        this.getContentPane().setSize(this.img.getHeight(this.imgobs), this.img.getWidth(this.imgobs));
        this.show();
    }

    private void prepImg(Graphics graphics) {
        graphics.setColor(Color.black);
        graphics.drawString("v1.30.011306", 20, 20);
    }

    public static void centerWindow(Window window) {
        int n = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2 - window.getWidth() / 2;
        int n2 = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2 - window.getHeight() / 2;
        window.setLocation(n, n2);
    }

    public void paint(Graphics graphics) {
        this.getContentPane().getGraphics().drawImage(this.img, 0, 0, this.imgobs);
    }
}

