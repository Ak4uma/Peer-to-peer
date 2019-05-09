/*
 * Decompiled with CFR 0.137.
 */
package im.network;

import im.control.Control;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

class ServerThread
extends Thread {
    final int STRINGPORT = 1101;
    final int OBJECTPORT = 1102;
    final int FILEPORT = 1103;
    final int FILEOBJECTPORT = 1104;
    final int MCASTPORT = 1105;
    public File recievingFile;
    static String receiveFile = "";
    public InetAddress mcastaddress;
    Control mainControl;
    private boolean runThread = true;
    private int port;

    ServerThread() {
        this.port = 1101;
    }

    public void endThread() {
        this.runThread = false;
    }

    ServerThread(int n, Control control, InetAddress inetAddress) {
        this.port = n;
        this.mainControl = control;
        this.recievingFile = new File("hi");
        this.mcastaddress = inetAddress;
    }

    public void run() {
        block34 : {
            block37 : {
                block36 : {
                    block35 : {
                        block33 : {
                            if (this.port != 1101) break block33;
                            try {
                                ServerSocket serverSocket = new ServerSocket(1101);
                                serverSocket.setSoTimeout(1000);
                                while (this.runThread) {
                                    try {
                                        int n;
                                        Socket socket = serverSocket.accept();
                                        InputStream inputStream = socket.getInputStream();
                                        StringBuffer stringBuffer = new StringBuffer();
                                        InetAddress inetAddress = socket.getInetAddress();
                                        while ((n = inputStream.read()) != -1) {
                                            stringBuffer.append((char)n);
                                        }
                                        socket.close();
                                        String string = stringBuffer.toString().trim();
                                        this.mainControl.handleText(inetAddress, string);
                                    }
                                    catch (SocketTimeoutException socketTimeoutException) {
                                    }
                                    catch (IOException iOException) {
                                        System.out.println("IOException");
                                        iOException.printStackTrace();
                                    }
                                }
                                serverSocket.close();
                            }
                            catch (IOException iOException) {
                                System.out.println("ioexception");
                                iOException.printStackTrace();
                            }
                            break block34;
                        }
                        if (this.port != 1102) break block35;
                        try {
                            ServerSocket serverSocket = new ServerSocket(1102);
                            serverSocket.setSoTimeout(1000);
                            while (this.runThread) {
                                try {
                                    Socket socket = serverSocket.accept();
                                    InputStream inputStream = socket.getInputStream();
                                    ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
                                    Object object = objectInputStream.readObject();
                                    InetAddress inetAddress = socket.getInetAddress();
                                    socket.close();
                                    this.mainControl.handleObject(inetAddress, object);
                                }
                                catch (SocketTimeoutException socketTimeoutException) {
                                }
                                catch (IOException iOException) {
                                    System.out.println("object IOException");
                                    iOException.printStackTrace();
                                }
                                catch (ClassNotFoundException classNotFoundException) {
                                    System.out.println("Class not found exception");
                                }
                            }
                            serverSocket.close();
                        }
                        catch (IOException iOException) {
                            System.out.println("object ioexception");
                            iOException.printStackTrace();
                        }
                        break block34;
                    }
                    if (this.port != 1103) break block36;
                    try {
                        ServerSocket serverSocket = new ServerSocket(1103);
                        serverSocket.setSoTimeout(1000);
                        while (this.runThread) {
                            try {
                                int n;
                                Socket socket = serverSocket.accept();
                                InputStream inputStream = socket.getInputStream();
                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                while ((n = inputStream.read()) != -1) {
                                    byteArrayOutputStream.write(n);
                                }
                                byte[] arrby = byteArrayOutputStream.toByteArray();
                                File file = new File(receiveFile);
                                FileOutputStream fileOutputStream = new FileOutputStream(file);
                                System.out.flush();
                                fileOutputStream.write(arrby);
                                fileOutputStream.close();
                                socket.close();
                            }
                            catch (SocketTimeoutException socketTimeoutException) {
                            }
                            catch (IOException iOException) {
                                System.out.println("object IOException");
                            }
                        }
                        serverSocket.close();
                    }
                    catch (IOException iOException) {
                        System.out.println("object ioexception");
                        iOException.printStackTrace();
                    }
                    break block34;
                }
                if (this.port != 1104) break block37;
                try {
                    ServerSocket serverSocket = new ServerSocket(1104);
                    serverSocket.setSoTimeout(1000);
                    while (this.runThread) {
                        try {
                            Socket socket = serverSocket.accept();
                            InputStream inputStream = socket.getInputStream();
                            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
                            this.recievingFile = (File)objectInputStream.readObject();
                            receiveFile = this.recievingFile.getName();
                            System.out.flush();
                            socket.close();
                        }
                        catch (SocketTimeoutException socketTimeoutException) {
                        }
                        catch (IOException iOException) {
                            System.out.println("object IOException");
                            iOException.printStackTrace();
                        }
                        catch (ClassNotFoundException classNotFoundException) {
                            System.out.println("Class not found exception");
                        }
                    }
                    serverSocket.close();
                }
                catch (IOException iOException) {
                    System.out.println("object ioexception");
                    iOException.printStackTrace();
                }
                break block34;
            }
            if (this.port != 1105) break block34;
            try {
                MulticastSocket multicastSocket = new MulticastSocket(1105);
                multicastSocket.setSoTimeout(1000);
                multicastSocket.joinGroup(this.mcastaddress);
                byte[] arrby = new byte[8192];
                while (this.runThread) {
                    arrby = new byte[8192];
                    try {
                        DatagramPacket datagramPacket = new DatagramPacket(arrby, arrby.length);
                        multicastSocket.receive(datagramPacket);
                        String string = new String(datagramPacket.getData(), "8859_1");
                        InetAddress inetAddress = datagramPacket.getAddress();
                        this.mainControl.handleText(inetAddress, string);
                    }
                    catch (SocketTimeoutException socketTimeoutException) {
                        // empty catch block
                    }
                }
                multicastSocket.leaveGroup(this.mcastaddress);
            }
            catch (IOException iOException) {
                System.out.println(iOException);
                iOException.printStackTrace();
            }
        }
    }
}

