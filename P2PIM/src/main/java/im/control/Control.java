/*
 * Decompiled with CFR 0.137.
 */
package im.control;

import im.control.ObjectPacket;
import im.control.group.GroupList;
import im.control.user.UserList;
import im.network.Network;
import im.prefs.Preferences;
import im.ui.Client;
import java.io.File;
import java.io.PrintStream;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.StringTokenizer;

public class Control {
    Client ui;
    Network network;
    UserList users;
    GroupList groupusers;
    InetAddress groupAddress;
    boolean loggedIn = false;
    boolean loggingIn = false;
    BigInteger myUserID;
    Preferences mainPrefs;

    public Control() {
        File file = new File(Preferences.findPrefsDirectory().getPath() + "/p2pim.properties");
        this.mainPrefs = new Preferences(file);
        this.myUserID = this.mainPrefs.getUserID();
        if (this.myUserID == null) {
            this.myUserID = new BigInteger(32, new Random());
            this.mainPrefs.setUserID(this.myUserID);
        }
        this.ui = new Client(this);
        this.users = new UserList();
        this.groupusers = new GroupList();
    }

    public Preferences getMainPrefs() {
        return this.mainPrefs;
    }

    public String getProfile(String string) {
        return this.users.getProfile(string);
    }

    public void login(String string) {
        try {
            this.network = new Network(this, InetAddress.getByName("239.192.0.1"));
        }
        catch (UnknownHostException unknownHostException) {
            System.out.println(unknownHostException);
            return;
        }
        String string2 = new String("HELO;").concat(string).concat(";").concat(this.myUserID.toString());
        this.users = new UserList();
        try {
            this.users.addUser(string, null, this.myUserID, this.mainPrefs.getProfile());
        }
        catch (Exception exception) {
            // empty catch block
        }
        this.network.sendMulticast(string2);
        this.loggingIn = true;
    }

    public void logout() {
        if (this.network != null) {
            String string = new String("GBYE;");
            this.network.sendMulticast(string);
            this.network.stopThreads();
            this.network = null;
        }
        this.users = new UserList();
        this.loggedIn = false;
    }

    public void sendText(String string, String string2) {
        InetAddress inetAddress = this.users.getIPAddress(string);
        string2 = new String("TEXT;").concat(string2);
        this.network.sendText(inetAddress, string2);
    }

    public void sendGroupText(String string, String string2) {
        string2 = new String("MTXT;").concat(string2);
        this.network.sendMulticast(string2);
    }

    public void sendFormattedText(String string, Object object) {
        InetAddress inetAddress = this.users.getIPAddress(string);
        ObjectPacket objectPacket = new ObjectPacket("FTXT;", object);
        this.network.sendObject(inetAddress, objectPacket);
    }

    public void joinGroup() {
        String string = null;
        string = this.users.getUsername(this.myUserID);
        this.groupusers = new GroupList();
        this.groupusers.addUser(string);
        this.ui.addGroupMember(string);
        this.network.sendMulticast("JGRP;" + string);
    }

    public void leaveGroup() {
        String string = null;
        string = this.users.getUsername(this.myUserID);
        this.groupusers = new GroupList();
        this.network.sendMulticast("LGRP;" + string);
    }

    public void handleText(InetAddress inetAddress, String string) {
        Object object;
        string = string.trim();
        String string2 = string.substring(0, 5);
        String string3 = this.users.getUsername(inetAddress);
        if (!this.loggedIn && !this.loggingIn) {
            return;
        }
        try {
            if (inetAddress.equals(InetAddress.getLocalHost())) {
                return;
            }
        }
        catch (Exception exception) {
            System.out.println(exception);
        }
        try {
            if (inetAddress.equals(InetAddress.getLocalHost())) {
                return;
            }
        }
        catch (Exception exception) {
            System.out.println("Control.handleText() - " + exception);
        }
        if (string2.equals("TEXT;")) {
            this.ui.receivedMessage(string3, string.substring(5));
        } else if (string2.equals("JGRP;")) {
            this.groupusers.addUser(string.substring(5));
            this.ui.addGroupMember(string.substring(5));
            object = new ObjectPacket("GLST;", this.groupusers);
            this.network.sendObject(inetAddress, object);
        } else if (string2.equals("LGRP;")) {
            if (string.substring(5) == null) {
                return;
            }
            this.groupusers.removeUser(string.substring(5));
            this.ui.removeGroupMember(string.substring(5));
        } else if (!string2.equals("FSRQ;")) {
            if (string2.equals("RCRQ;")) {
                int n = Integer.parseInt(string.substring(5));
            } else if (string2.equals("MTXT;")) {
                if (string3 == null) {
                    return;
                }
                this.ui.receivedGroupMessage(string3, string.substring(5));
            } else if (string2.equals("FAKE;")) {
                if (!this.loggingIn) {
                    this.loggingIn = false;
                    this.ui.logout("There is already a user with that username");
                }
            } else if (string2.equals("HELO;")) {
                StringTokenizer stringTokenizer;
                object = null;
                BigInteger bigInteger = null;
                if (this.loggingIn) {
                    this.loggedIn = true;
                    this.loggingIn = false;
                }
                if ((stringTokenizer = new StringTokenizer(string.substring(5), ";")).countTokens() != 2) {
                    System.out.println("Invalid HELO packet: " + string.substring(5));
                    return;
                }
                object = stringTokenizer.nextToken();
                try {
                    bigInteger = new BigInteger(stringTokenizer.nextToken());
                }
                catch (NumberFormatException numberFormatException) {
                    System.out.println("Invalid data in HELO packet: " + numberFormatException);
                    return;
                }
                if (bigInteger.equals(this.myUserID)) {
                    return;
                }
                if (this.users.contains(bigInteger)) {
                    this.users.removeUserByID(bigInteger);
                }
                if (this.users.contains((String)object)) {
                    if (object.equals(this.users.getUsername(this.myUserID))) {
                        this.network.sendText(inetAddress, "FAKE;");
                    }
                    return;
                }
                ObjectPacket objectPacket = new ObjectPacket("ULST;", this.users);
                this.network.sendObject(inetAddress, objectPacket);
            } else if (string2.equals("GBYE;")) {
                this.groupusers.removeUser(this.users.getUsername(inetAddress));
                this.ui.removeGroupMember(this.users.getUsername(inetAddress));
                this.ui.removeContact(this.users.getUsername(inetAddress));
                this.users.removeUserByAddress(inetAddress);
            } else {
                System.out.println("Don't know how to handle \"" + string + "\"");
            }
        }
    }

    public void handleObject(InetAddress inetAddress, Object object) {
        String string = ((ObjectPacket)object).getHeader();
        String string2 = this.users.getUsername(inetAddress);
        Object object2 = ((ObjectPacket)object).getObject();
        if (!this.loggedIn && !this.loggingIn) {
            return;
        }
        try {
            if (inetAddress.equals(InetAddress.getLocalHost())) {
                return;
            }
        }
        catch (Exception exception) {
            System.out.println(exception);
        }
        if (string.equals("FTXT;")) {
            this.ui.receivedMessage(string2, object2);
        } else if (string.equals("GLST;")) {
            String[] arrstring = new String[100];
            GroupList groupList = this.groupusers.mergeList((GroupList)object2, arrstring);
            int n = 0;
            while (arrstring[n] != null) {
                this.ui.addGroupMember(arrstring[n]);
                ++n;
            }
        } else if (string.equals("ULST;")) {
            String[] arrstring = new String[100];
            if (this.loggingIn) {
                this.loggedIn = true;
                this.loggingIn = false;
            }
            UserList userList = this.users.mergeList((UserList)object2, arrstring, inetAddress);
            int n = 0;
            while (arrstring[n] != null) {
                this.ui.addContact(arrstring[n]);
                ++n;
            }
            if (userList != null) {
                ObjectPacket objectPacket = new ObjectPacket("MLST;", this.users);
                this.network.sendObject(inetAddress, objectPacket);
            }
        } else if (string.equals("MLST;")) {
            String[] arrstring = new String[100];
            this.users.mergeList((UserList)object2, arrstring, inetAddress);
            int n = 0;
            while (arrstring[n] != null) {
                this.ui.addContact(arrstring[n]);
                ++n;
            }
        } else {
            System.out.println("Don't know how to handle object with header \"" + string + "\"");
        }
    }
}

