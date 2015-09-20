/*
 * Decompiled with CFR 0_102.
 */
package jamtester.mdidesktop;

import jamtester.mdidesktop.MDIDesktopPane;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Rectangle;
import javax.swing.DefaultDesktopManager;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.border.Border;

class MDIDesktopManager
extends DefaultDesktopManager {
    private MDIDesktopPane desktop;

    public MDIDesktopManager(MDIDesktopPane mDIDesktopPane) {
        this.desktop = mDIDesktopPane;
    }

    public void endResizingFrame(JComponent jComponent) {
        super.endResizingFrame(jComponent);
        this.resizeDesktop();
    }

    public void endDraggingFrame(JComponent jComponent) {
        super.endDraggingFrame(jComponent);
        this.resizeDesktop();
    }

    public void setNormalSize() {
        JScrollPane jScrollPane = this.getScrollPane();
        int n = 0;
        int n2 = 0;
        Insets insets = this.getScrollPaneInsets();
        if (jScrollPane != null) {
            Dimension dimension = jScrollPane.getVisibleRect().getSize();
            if (jScrollPane.getBorder() != null) {
                dimension.setSize(dimension.getWidth() - (double)insets.left - (double)insets.right, dimension.getHeight() - (double)insets.top - (double)insets.bottom);
            }
            dimension.setSize(dimension.getWidth() - 20.0, dimension.getHeight() - 20.0);
            this.desktop.setAllSize(n, n2);
            jScrollPane.invalidate();
            jScrollPane.validate();
        }
    }

    private Insets getScrollPaneInsets() {
        JScrollPane jScrollPane = this.getScrollPane();
        if (jScrollPane == null) {
            return new Insets(0, 0, 0, 0);
        }
        return this.getScrollPane().getBorder().getBorderInsets(jScrollPane);
    }

    private JScrollPane getScrollPane() {
        JViewport jViewport;
        if (this.desktop.getParent() instanceof JViewport && (jViewport = (JViewport)this.desktop.getParent()).getParent() instanceof JScrollPane) {
            return (JScrollPane)jViewport.getParent();
        }
        return null;
    }

    protected void resizeDesktop() {
        int n = 0;
        int n2 = 0;
        JScrollPane jScrollPane = this.getScrollPane();
        Insets insets = this.getScrollPaneInsets();
        if (jScrollPane != null) {
            JInternalFrame[] arrjInternalFrame = this.desktop.getAllFrames();
            for (int i = 0; i < arrjInternalFrame.length; ++i) {
                if (arrjInternalFrame[i].getX() + arrjInternalFrame[i].getWidth() > n) {
                    n = arrjInternalFrame[i].getX() + arrjInternalFrame[i].getWidth();
                }
                if (arrjInternalFrame[i].getY() + arrjInternalFrame[i].getHeight() <= n2) continue;
                n2 = arrjInternalFrame[i].getY() + arrjInternalFrame[i].getHeight();
            }
            Dimension dimension = jScrollPane.getVisibleRect().getSize();
            if (jScrollPane.getBorder() != null) {
                dimension.setSize(dimension.getWidth() - (double)insets.left - (double)insets.right, dimension.getHeight() - (double)insets.top - (double)insets.bottom);
            }
            if ((double)n <= dimension.getWidth()) {
                n = (int)dimension.getWidth() - 20;
            }
            if ((double)n2 <= dimension.getHeight()) {
                n2 = (int)dimension.getHeight() - 20;
            }
            this.desktop.setAllSize(n, n2);
            jScrollPane.invalidate();
            jScrollPane.validate();
        }
    }
}

