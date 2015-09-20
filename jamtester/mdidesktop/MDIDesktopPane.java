/*
 * Decompiled with CFR 0_102.
 */
package jamtester.mdidesktop;

import jamtester.mdidesktop.MDIDesktopManager;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.beans.PropertyVetoException;
import javax.swing.DesktopManager;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;

public class MDIDesktopPane
extends JDesktopPane {
    private static int FRAME_OFFSET = 20;
    private MDIDesktopManager manager;

    public MDIDesktopPane() {
        this.manager = new MDIDesktopManager(this);
        this.setDesktopManager(this.manager);
        this.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
    }

    public void setBounds(int n, int n2, int n3, int n4) {
        super.setBounds(n, n2, n3, n4);
        this.checkDesktopSize();
    }

    public Component add(JInternalFrame jInternalFrame) {
        Point point;
        JInternalFrame[] arrjInternalFrame = this.getAllFrames();
        Component component = super.add(jInternalFrame);
        this.checkDesktopSize();
        if (arrjInternalFrame.length > 0) {
            point = arrjInternalFrame[0].getLocation();
            point.x+=FRAME_OFFSET;
            point.y+=FRAME_OFFSET;
        } else {
            point = new Point(0, 0);
        }
        jInternalFrame.setLocation(point.x, point.y);
        if (jInternalFrame.isResizable()) {
            int n = this.getWidth() - this.getWidth() / 3;
            int n2 = this.getHeight() - this.getHeight() / 3;
            if ((double)n < jInternalFrame.getMinimumSize().getWidth()) {
                n = (int)jInternalFrame.getMinimumSize().getWidth();
            }
            if ((double)n2 < jInternalFrame.getMinimumSize().getHeight()) {
                n2 = (int)jInternalFrame.getMinimumSize().getHeight();
            }
            jInternalFrame.setSize(n, n2);
        }
        this.moveToFront(jInternalFrame);
        jInternalFrame.setVisible(true);
        try {
            jInternalFrame.setSelected(true);
        }
        catch (PropertyVetoException var7_7) {
            jInternalFrame.toBack();
        }
        return component;
    }

    public void remove(Component component) {
        super.remove(component);
        this.checkDesktopSize();
    }

    public void cascadeFrames() {
        int n = 0;
        int n2 = 0;
        JInternalFrame[] arrjInternalFrame = this.getAllFrames();
        this.manager.setNormalSize();
        int n3 = this.getBounds().height - 5 - arrjInternalFrame.length * FRAME_OFFSET;
        int n4 = this.getBounds().width - 5 - arrjInternalFrame.length * FRAME_OFFSET;
        for (int i = arrjInternalFrame.length - 1; i >= 0; --i) {
            arrjInternalFrame[i].setSize(n4, n3);
            arrjInternalFrame[i].setLocation(n, n2);
            n+=FRAME_OFFSET;
            n2+=FRAME_OFFSET;
        }
    }

    public void tileFrames() {
        JInternalFrame[] arrjInternalFrame = this.getAllFrames();
        this.manager.setNormalSize();
        int n = this.getBounds().height / arrjInternalFrame.length;
        int n2 = 0;
        for (int i = 0; i < arrjInternalFrame.length; ++i) {
            arrjInternalFrame[i].setSize(this.getBounds().width, n);
            arrjInternalFrame[i].setLocation(0, n2);
            n2+=n;
        }
    }

    public void setAllSize(Dimension dimension) {
        this.setMinimumSize(dimension);
        this.setMaximumSize(dimension);
        this.setPreferredSize(dimension);
    }

    public void setAllSize(int n, int n2) {
        this.setAllSize(new Dimension(n, n2));
    }

    private void checkDesktopSize() {
        if (this.getParent() != null && this.isVisible()) {
            this.manager.resizeDesktop();
        }
    }
}

