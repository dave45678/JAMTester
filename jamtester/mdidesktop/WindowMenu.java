/*
 * Decompiled with CFR 0_102.
 */
package jamtester.mdidesktop;

import jamtester.mdidesktop.MDIDesktopPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import javax.swing.Icon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

public class WindowMenu
extends JMenu {
    private MDIDesktopPane desktop;
    private JMenuItem cascade = new JMenuItem("Cascade");
    private JMenuItem tile = new JMenuItem("Tile");

    public WindowMenu(MDIDesktopPane mDIDesktopPane) {
        this.desktop = mDIDesktopPane;
        this.setText("Window");
        this.cascade.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent actionEvent) {
                WindowMenu.this.desktop.cascadeFrames();
            }
        });
        this.tile.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent actionEvent) {
                WindowMenu.this.desktop.tileFrames();
            }
        });
        this.addMenuListener(new MenuListener(){

            public void menuCanceled(MenuEvent menuEvent) {
            }

            public void menuDeselected(MenuEvent menuEvent) {
                WindowMenu.this.removeAll();
            }

            public void menuSelected(MenuEvent menuEvent) {
                WindowMenu.this.buildChildMenus();
            }
        });
    }

    private void buildChildMenus() {
        JInternalFrame[] arrjInternalFrame = this.desktop.getAllFrames();
        this.add(this.cascade);
        this.add(this.tile);
        if (arrjInternalFrame.length > 0) {
            this.addSeparator();
        }
        this.cascade.setEnabled(arrjInternalFrame.length > 0);
        this.tile.setEnabled(arrjInternalFrame.length > 0);
        for (int i = 0; i < arrjInternalFrame.length; ++i) {
            ChildMenuItem childMenuItem = new ChildMenuItem(arrjInternalFrame[i]);
            childMenuItem.setState(i == 0);
            childMenuItem.addActionListener(new ActionListener(){

                public void actionPerformed(ActionEvent actionEvent) {
                    JInternalFrame jInternalFrame = ((ChildMenuItem)actionEvent.getSource()).getFrame();
                    jInternalFrame.moveToFront();
                    try {
                        jInternalFrame.setSelected(true);
                    }
                    catch (PropertyVetoException var3_3) {
                        var3_3.printStackTrace();
                    }
                }
            });
            childMenuItem.setIcon(arrjInternalFrame[i].getFrameIcon());
            this.add(childMenuItem);
        }
    }

    class ChildMenuItem
    extends JCheckBoxMenuItem {
        private JInternalFrame frame;

        public ChildMenuItem(JInternalFrame jInternalFrame) {
            super(jInternalFrame.getTitle());
            this.frame = jInternalFrame;
        }

        public JInternalFrame getFrame() {
            return this.frame;
        }
    }

}

