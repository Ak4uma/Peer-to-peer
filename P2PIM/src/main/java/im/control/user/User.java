/*
 * Decompiled with CFR 0.137.
 */
package im.control.user;

import java.io.Serializable;
import java.math.BigInteger;
import java.net.InetAddress;

public class User
implements Serializable {
    String name;
    InetAddress address;
    String profile;
    BigInteger userID;

    public User(String string, InetAddress inetAddress, BigInteger bigInteger, String string2) {
        this.name = string;
        this.address = inetAddress;
        this.userID = bigInteger;
        this.profile = string2;
    }

    public String getName() {
        return this.name;
    }

    public InetAddress getAddress() {
        return this.address;
    }

    public BigInteger getID() {
        return this.userID;
    }

    public String getProfile() {
        return this.profile;
    }
}

