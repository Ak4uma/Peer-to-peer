/*
 * Decompiled with CFR 0.137.
 */
package im.control.user;

import im.control.user.User;
import java.io.PrintStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.net.InetAddress;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class UserList
implements Serializable {
    List users = Collections.synchronizedList(new LinkedList());

    public InetAddress getIPAddress(String string) {
        for (int i = 0; i < this.users.size(); ++i) {
            User user = (User)this.users.get(i);
            if (!string.equals(user.getName())) continue;
            return user.getAddress();
        }
        return null;
    }

    public String getUsername(InetAddress inetAddress) {
        for (int i = 0; i < this.users.size(); ++i) {
            User user = (User)this.users.get(i);
            if (!inetAddress.equals(user.getAddress())) continue;
            return user.getName();
        }
        return null;
    }

    public String getUsername(BigInteger bigInteger) {
        for (int i = 0; i < this.users.size(); ++i) {
            User user = (User)this.users.get(i);
            if (!bigInteger.equals(user.getID())) continue;
            return user.getName();
        }
        return null;
    }

    public String getProfile(String string) {
        for (int i = 0; i < this.users.size(); ++i) {
            User user = (User)this.users.get(i);
            if (!user.getName().equals(string)) continue;
            return user.getProfile();
        }
        return null;
    }

    public void addUser(String string, InetAddress inetAddress, BigInteger bigInteger, String string2) {
        string = string.trim();
        User user = new User(string, inetAddress, bigInteger, string2);
        this.users.add(user);
    }

    public void addUser(User user) {
        this.users.add(user);
    }

    public void removeUserByAddress(InetAddress inetAddress) {
        for (int i = 0; i < this.users.size(); ++i) {
            User user = (User)this.users.get(i);
            if (!inetAddress.equals(user.getAddress())) continue;
            this.users.remove(user);
            return;
        }
    }

    public void removeUserByID(BigInteger bigInteger) {
        for (int i = 0; i < this.users.size(); ++i) {
            User user = (User)this.users.get(i);
            if (!bigInteger.equals(user.getID())) continue;
            this.users.remove(user);
            return;
        }
    }

    public boolean contains(String string) {
        for (int i = 0; i < this.users.size(); ++i) {
            User user = (User)this.users.get(i);
            if (!user.getName().equals(string)) continue;
            return true;
        }
        return false;
    }

    public boolean contains(BigInteger bigInteger) {
        for (int i = 0; i < this.users.size(); ++i) {
            User user = (User)this.users.get(i);
            if (!user.getID().equals(bigInteger)) continue;
            return true;
        }
        return false;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public UserList mergeList(UserList userList, String[] arrstring, InetAddress inetAddress) {
        int n;
        boolean bl;
        int n2;
        User user = null;
        User user2 = null;
        int n3 = this.users.size();
        boolean bl2 = false;
        UserList userList2 = new UserList();
        List list = userList.getRawList();
        List list2 = this.users;
        synchronized (list2) {
            for (n2 = 0; n2 < list.size(); ++n2) {
                user2 = (User)list.get(n2);
                bl = false;
                for (n = 0; n < n3; ++n) {
                    user = (User)this.users.get(n);
                    if (!user2.getID().equals(user.getID())) continue;
                    bl = true;
                }
                try {
                    if (bl) continue;
                    if (user2.getAddress() == null) {
                        User user3 = new User(user2.getName(), inetAddress, user2.getID(), user2.getProfile());
                        this.users.add(user3);
                        continue;
                    }
                    this.users.add(user2);
                    continue;
                }
                catch (Exception exception) {
                    System.out.println(exception);
                }
            }
            for (n2 = n3; n2 < this.users.size(); ++n2) {
                arrstring[n2 - n3] = ((User)this.users.get(n2)).getName();
            }
            arrstring[n2 - n3] = null;
        }
        for (n2 = 0; n2 < this.users.size(); ++n2) {
            user = (User)this.users.get(n2);
            bl = false;
            for (n = 0; n < list.size(); ++n) {
                user2 = (User)list.get(n);
                if (!user.getID().equals(user2.getID())) continue;
                bl = true;
            }
            if (bl) continue;
            userList2.addUser(user);
            bl2 = true;
        }
        if (bl2) {
            return userList2;
        }
        return null;
    }

    protected List getRawList() {
        return this.users;
    }
}

