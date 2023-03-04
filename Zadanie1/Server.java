import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Server {
    private ArrayList<SocketAddress> udpClientAddresses = new ArrayList<>();
    private ArrayList<TCPClientHandler> tcpClientHandlers = new ArrayList<>();
    private DatagramSocket datagramSocket;
    private ServerSocket serverSocket;
    private final int port;
    public DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm:ss");

    public static void main(String[] args) {
        int port = 12345;

        Server chatServer = new Server(port);
        chatServer.start();
    }

    public Server(int port) {
        this.port = port;
    }

    public void start() {
        Thread tcpThread = new Thread() {
            public void run() {
                try {
                    serverSocket = new ServerSocket(port);

                    while (true) {
                        Socket clientSocket = serverSocket.accept();
                        TCPClientHandler clientHandler = new TCPClientHandler(clientSocket, Server.this);
                        clientHandler.start();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread udpThread = new Thread() {
            public void run() {
                try {
                    datagramSocket = new DatagramSocket(port);

                    while (true) {
                        byte[] receiveBuffer = new byte[1024];

                        DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                        datagramSocket.receive(receivePacket);

                        String message = new String(receivePacket.getData());
                        InetAddress address = receivePacket.getAddress();
                        int port = receivePacket.getPort();

                        if (message.contains("@REGISTERUDPCLIENT")) {
                            addUDPClient(address, port);
                        } else {
                            logMessage("UDP - " + message);
                            sendBroadcastUDP(message, address, port);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        tcpThread.start();
        udpThread.start();

        System.out.println("Server is listening on port :" + port);
    }

    public void sendBroadcastTCP(String message, TCPClientHandler sender) {
        for (TCPClientHandler clientHandler : tcpClientHandlers) {
            if (!sender.equals(clientHandler)) {
                clientHandler.sendToClient(message);
            }
        }
    }

    private void sendBroadcastUDP(String message, InetAddress address, int port) {
        byte[] sendBuffer = message.getBytes();
        SocketAddress senderSocketAddress = new SocketAddress(address, port);

        try {
            for (SocketAddress socketAddress : udpClientAddresses) {
                if (!socketAddress.equals(senderSocketAddress)) {
                    DatagramPacket packet = new DatagramPacket(sendBuffer, sendBuffer.length,
                            socketAddress.getInetAddress(), socketAddress.getPort());
                    datagramSocket.send(packet);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addUDPClient(InetAddress address, int port) {
        SocketAddress socketAddress = new SocketAddress(address, port);
        udpClientAddresses.add(socketAddress);
    }

    public void addTCPClientHandler(TCPClientHandler clientHandler) {
        tcpClientHandlers.add(clientHandler);
        sendBroadcastTCP(clientHandler.getClientName() + " has joined the chat.", clientHandler);
    }

    public void removeClient(TCPClientHandler clientHandler) {
        tcpClientHandlers.remove(clientHandler);
        logMessage(clientHandler.getClientName() + " has left the chat.");
        sendBroadcastTCP(clientHandler.getClientName() + " has left the chat.", clientHandler);
    }

    public void logMessage(String message) {
        System.out.println("[SERVER - " + LocalDateTime.now().format(formatter) + " ] " + message);
    }
}