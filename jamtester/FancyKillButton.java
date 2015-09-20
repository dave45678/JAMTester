/*
 * Decompiled with CFR 0_102.
 */
package jamtester;

import jamtester.FancyKillButton;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.PrintStream;
import java.util.Timer;
import java.util.TimerTask;
import javax.imageio.ImageIO;
import javax.swing.JButton;

public class FancyKillButton
extends JButton {
    private static Image aliveImg;
    private static Image deadImg;
    private static Image mouseOver;
    private Image myAlive;
    private boolean alive;

    public FancyKillButton() {
        this.setSize(aliveImg.getWidth(this), aliveImg.getHeight(this));
        this.enableEvents(16);
        this.setRolloverEnabled(true);
        this.myAlive = aliveImg;
        this.alive = true;
        this.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent actionEvent) {
                FancyKillButton.this.alive = false;
                FancyKillButton.this.invalidate();
                Timer timer = new Timer();
                timer.schedule(new TimerTask(this){
                    private final /* synthetic */  this$1;

                    public void run() {
                        FancyKillButton.access$002(.access$100(this.this$1), true);
                        .access$100(this.this$1).repaint();
                    }
                }, 500);
            }

            static /* synthetic */ FancyKillButton access$100( var0) {
                return var0.FancyKillButton.this;
            }
        });
        this.addMouseListener(new MouseAdapter(){

            public void mouseEntered(MouseEvent mouseEvent) {
                FancyKillButton.this.myAlive = mouseOver;
                FancyKillButton.this.repaint();
            }

            public void mouseExited(MouseEvent mouseEvent) {
                FancyKillButton.this.myAlive = aliveImg;
                FancyKillButton.this.repaint();
            }
        });
        this.setCursor(new Cursor(12));
    }

    public void paint(Graphics graphics) {
        Image image = this.myAlive;
        if (!this.alive) {
            image = deadImg;
        }
        graphics.drawImage(image, 0, 0, (int)graphics.getClipRect().getWidth(), (int)graphics.getClipRect().getHeight(), this);
    }

    static {
        System.out.println(new File("Bug.bmp").getAbsolutePath());
        try {
            aliveImg = ImageIO.read(new File("Bug.jpg"));
            deadImg = ImageIO.read(new File("SquishedBug.jpg"));
            mouseOver = ImageIO.read(new File("BugMouseOver.jpg"));
        }
        catch (Exception var0) {
            // empty catch block
        }
    }

}

