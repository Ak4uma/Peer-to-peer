/*
 * Decompiled with CFR 0.137.
 */
package im.network;

import im.control.Control;
import im.network.ServerThread;
import im.network.TxFileThread;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;

public class Network {
    static final int STRINGPORT = 1101;
    static final int OBJECTPORT = 1102;
    static final int FILEPORT = 1103;
    static final int FILEOBJECTPORT = 1104;
    static final int MCASTPORT = 1105;
    private InetAddress mcastaddress;
    ServerThread textThread;
    ServerThread objectThread;
    ServerThread fileThread;
    ServerThread fileObjectThread;
    ServerThread multicastThread;

    public Network(Control control, InetAddress inetAddress) {
        this.mcastaddress = inetAddress;
        this.textThread = new ServerThread(1101, control, this.mcastaddress);
        this.textThread.start();
        this.objectThread = new ServerThread(1102, control, this.mcastaddress);
        this.objectThread.start();
        this.fileThread = new ServerThread(1103, control, this.mcastaddress);
        this.fileThread.start();
        this.fileObjectThread = new ServerThread(1104, control, this.mcastaddress);
        this.fileObjectThread.start();
        this.multicastThread = new ServerThread(1105, control, this.mcastaddress);
        this.multicastThread.start();
    }

    public void stopThreads() {
        this.textThread.endThread();
        this.objectThread.endThread();
        this.fileThread.endThread();
        this.fileObjectThread.endThread();
        this.multicastThread.endThread();
    }

    public void sendObject(InetAddress inetAddress, Object object, int n) {
        try {
            Socket socket = new Socket(inetAddress, n);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(object);
        }
        catch (IOException iOException) {
            System.err.println(iOException);
        }
    }

    public void sendObject(InetAddress inetAddress, Object object) {
        try {
            Socket socket = new Socket(inetAddress, 1102);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(object);
        }
        catch (IOException iOException) {
            System.err.println(iOException);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public void sendText(InetAddress inetAddress, String string) {
        outputStreamWriter = null;
        try {
            try {
                socket = new Socket(inetAddress, 1101);
                outputStream = socket.getOutputStream();
                bufferedOutputStream = new BufferedOutputStream(outputStream);
                outputStreamWriter = new OutputStreamWriter((OutputStream)bufferedOutputStream, "ASCII");
                outputStreamWriter.write(string);
            }
            catch (IOException iOException) {
                System.err.println(iOException);
                var8_9 = null;
                try {}
                catch (Exception exception) {
                    return;
                }
                outputStreamWriter.close();
                return;
            }
            var8_8 = null;
            outputStreamWriter.close();
            return;
            catch (Exception exception) {
                return;
            }
        }
        catch (Throwable throwable) {
            var8_10 = null;
            ** try [egrp 2[TRYBLOCK] [4 : 82->89)] { 
lbl29: // 1 sources:
            outputStreamWriter.close();
            throw throwable;
lbl31: // 1 sources:
            catch (Exception exception) {
                // empty catch block
            }
            throw throwable;
        }
    }

    public boolean sendFile(InetAddress inetAddress, File file) {
        try {
            int n;
            this.sendObject(inetAddress, file, 1104);
            FileInputStream fileInputStream = new FileInputStream(file);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            while ((n = ((InputStream)fileInputStream).read()) != -1) {
                byteArrayOutputStream.write(n);
            }
            byte[] arrby = byteArrayOutputStream.toByteArray();
            TxFileThread txFileThread = new TxFileThread(arrby, inetAddress, file);
            txFileThread.start();
        }
        catch (IOException iOException) {}
        return true;
    }

    public void sendMulticast(String string) {
        try {
            byte[] arrby = string.getBytes();
            DatagramPacket datagramPacket = new DatagramPacket(arrby, arrby.length, this.mcastaddress, 1105);
            MulticastSocket multicastSocket = new MulticastSocket();
            multicastSocket.send(datagramPacket);
        }
        catch (IOException iOException) {
            System.err.println(iOException);
        }
    }
}

