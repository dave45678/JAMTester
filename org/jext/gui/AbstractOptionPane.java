/*
 * Decompiled with CFR 0_102.
 */
package org.jext.gui;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import org.jext.gui.OptionPane;

public class AbstractOptionPane
extends JPanel
implements OptionPane {
    protected int y = 0;
    protected GridBagLayout gridBag;
    private String name;

    public boolean isCacheable() {
        return false;
    }

    public void load() {
    }

    protected void addComponent(String string, Component component) {
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridy = this.y++;
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = 1;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = 17;
        JLabel jLabel = new JLabel(string, 2);
        this.gridBag.setConstraints(jLabel, gridBagConstraints);
        this.add(jLabel);
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.anchor = 13;
        this.gridBag.setConstraints(component, gridBagConstraints);
        this.add(component);
    }

    protected void addComponent(Component component) {
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridy = this.y++;
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.gridwidth = 0;
        gridBagConstraints.fill = 0;
        gridBagConstraints.anchor = 17;
        gridBagConstraints.weightx = 1.0;
        this.gridBag.setConstraints(component, gridBagConstraints);
        this.add(component);
    }

    public AbstractOptionPane(String string) {
        this.name = string;
        this.gridBag = new GridBagLayout();
        this.setLayout(this.gridBag);
        this.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
    }

    public Component getComponent() {
        return this;
    }

    public String getName() {
        return this.name;
    }

    public void save() {
    }

    protected void finalize() throws Throwable {
        super.finalize();
        this.gridBag = null;
        this.name = null;
    }
}

