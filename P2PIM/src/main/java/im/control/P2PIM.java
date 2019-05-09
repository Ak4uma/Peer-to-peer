/*
 * Decompiled with CFR 0.137.
 */
package im.control;

import im.control.Control;
import java.util.StringTokenizer;

public class P2PIM {
    public static Control mainControl;
    public static final String RELEASE_ID = "P2PIM.release.id";

    public static void main(String[] arrstring) {
        String string = System.getProperty("java.class.path");
        StringTokenizer stringTokenizer = new StringTokenizer(string, ";");
        String string2 = stringTokenizer.nextToken();
        while (string2.indexOf("P2PIM") < 0 && stringTokenizer.hasMoreTokens()) {
            string2 = stringTokenizer.nextToken();
        }
        String string3 = string2.indexOf("P2PIM") == -1 ? "DEBUG" : string2.substring(string2.indexOf("-") + 1, string2.lastIndexOf("."));
        System.setProperty(RELEASE_ID, string3);
        mainControl = new Control();
    }
}

