/*
 * Decompiled with CFR 0.137.
 */
package im.control.group;

import java.io.PrintStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class GroupList
implements Serializable {
    List users = Collections.synchronizedList(new LinkedList());

    public void addUser(String string) {
        string = string.trim();
        this.users.add(string);
    }

    public void removeUser(String string) {
        for (int i = 0; i < this.users.size(); ++i) {
            String string2 = (String)this.users.get(i);
            if (!string.equals(string2)) continue;
            this.users.remove(string2);
            return;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public GroupList mergeList(GroupList groupList, String[] arrstring) {
        int n = this.users.size();
        List list = groupList.getRawList();
        List list2 = this.users;
        synchronized (list2) {
            int n2;
            for (n2 = 0; n2 < list.size(); ++n2) {
                String string = (String)list.get(n2);
                boolean bl = false;
                for (int i = 0; i < n; ++i) {
                    String string2 = (String)this.users.get(i);
                    if (!string.equals(string2)) continue;
                    bl = true;
                }
                try {
                    if (bl) continue;
                    this.users.add(string);
                    continue;
                }
                catch (Exception exception) {
                    System.out.println(exception);
                }
            }
            for (n2 = n; n2 < this.users.size(); ++n2) {
                arrstring[n2 - n] = (String)this.users.get(n2);
            }
            arrstring[n2 - n] = null;
        }
        return null;
    }

    protected List getRawList() {
        return this.users;
    }
}

