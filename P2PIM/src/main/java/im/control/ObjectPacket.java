/*
 * Decompiled with CFR 0.137.
 */
package im.control;

import java.io.Serializable;

public class ObjectPacket
implements Serializable {
    String header;
    Object data;

    public ObjectPacket() {
        this.header = null;
        this.data = null;
    }

    public ObjectPacket(String string, Object object) {
        this.header = string;
        this.data = object;
    }

    public String getHeader() {
        return this.header;
    }

    public Object getObject() {
        return this.data;
    }
}

