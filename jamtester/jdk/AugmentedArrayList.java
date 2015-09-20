/*
 * Decompiled with CFR 0_102.
 */
package jamtester.jdk;

import java.util.ArrayList;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

public class AugmentedArrayList
extends ArrayList
implements ListModel {
    private ArrayList dataListeners = new ArrayList();
    private static int CHANGED = 0;
    private static int ADDED = 1;
    private static int REMOVED = 2;

    public void addListDataListener(ListDataListener listDataListener) {
        this.dataListeners.add(listDataListener);
    }

    public Object getElementAt(int n) {
        return this.get(n);
    }

    public int getSize() {
        return this.size();
    }

    public void removeListDataListener(ListDataListener listDataListener) {
        this.dataListeners.remove(listDataListener);
    }

    public void add(int n, Object object) {
        super.add(n, object);
        this.notifyListeners(n, n, ADDED);
    }

    public boolean add(Object object) {
        boolean bl = super.add(object);
        if (bl) {
            this.notifyListeners(this.size() - 1, this.size() - 1, ADDED);
        }
        return bl;
    }

    public Object set(int n, Object object) {
        Object object2 = this.set(n, object);
        this.notifyListeners(n, n, CHANGED);
        return object2;
    }

    public void clear() {
        int n = this.size();
        super.clear();
        this.notifyListeners(0, n, REMOVED);
    }

    private void notifyListeners(int n, int n2, int n3) {
        for (int i = 0; i < this.dataListeners.size(); ++i) {
            ((ListDataListener)this.dataListeners.get(i)).intervalAdded(new ListDataEvent(this, n3, n, n2));
        }
    }
}

