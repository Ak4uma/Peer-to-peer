/*
 * Decompiled with CFR 0.137.
 */
package im.network;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

class TxFileThread
extends Thread {
    final int STRINGPORT = 1101;
    final int OBJECTPORT = 1102;
    final int FILEPORT = 1103;
    public boolean runThread = true;
    byte[] data;
    InetAddress userAddress;
    File toSend;

    TxFileThread(byte[] arrby, InetAddress inetAddress, File file) {
        this.data = arrby;
        this.userAddress = inetAddress;
        this.toSend = file;
    }

    public void run() {
        try {
            Socket socket = new Socket(this.userAddress, 1103);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());
            ((OutputStream)bufferedOutputStream).write(this.data);
            ((OutputStream)bufferedOutputStream).flush();
        }
        catch (IOException iOException) {}
    }
}

