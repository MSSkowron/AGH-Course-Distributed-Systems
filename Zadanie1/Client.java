import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

public class Client {
    // TCP
    private Socket socket;
    private InetAddress serverAddress;
    private int serverPort;
    private PrintWriter out;
    private BufferedReader in;
    // Multicast
    private MulticastSocket multicastSocket;
    private InetAddress multicastAddress;
    private int multicastPort;
    // UDP
    private DatagramSocket datagramSocket;
    // Othres
    private Scanner scanner;
    private String name;
    private ReadingThreadTcp readingThreadTcp;
    private ReadingThreadUdp readingThreadUdp;
    private ReadingThreadMulticast readingThreadMulticast;
    private WritingThread writingThread;

    public static void main(String[] args) throws IOException {
        String serverAddress = "127.0.0.1";
        int serverPort = 12345;
        String multicastAddress = "230.0.0.0";
        int multicastPort = 8888;

        Client chatClient = new Client(serverAddress, serverPort, multicastAddress, multicastPort);
        chatClient.start();
    }

    public Client(String serverAddress, int serverPort, String multicastAddress, int multicastPort) {
        try {
            this.serverAddress = InetAddress.getByName(serverAddress);
            this.serverPort = serverPort;
            this.multicastAddress = InetAddress.getByName(multicastAddress);
            this.multicastPort = multicastPort;

            // TCP
            socket = new Socket(this.serverAddress, this.serverPort);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Multicast
            multicastSocket = new MulticastSocket(this.multicastPort);
            multicastSocket.joinGroup(this.multicastAddress);

            // UDP
            datagramSocket = new DatagramSocket();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void start() {
        readingThreadTcp = new ReadingThreadTcp();
        readingThreadUdp = new ReadingThreadUdp();
        readingThreadMulticast = new ReadingThreadMulticast();
        writingThread = new WritingThread();
        readingThreadTcp.start();
        readingThreadUdp.start();
        readingThreadMulticast.start();
        writingThread.start();
    }

    private class ReadingThreadTcp extends Thread {
        private final AtomicBoolean running = new AtomicBoolean(false);

        public void run() {
            try {
                running.set(true);
                while (running.get()) {
                    String receivedMessage = in.readLine();
                    if (receivedMessage != null)
                        System.out.println(receivedMessage);
                }

                closeEverything();
            } catch (IOException e) {
                closeEverything();
            }
        }

        public void kill() {
            this.running.set(false);
        }
    }

    private class ReadingThreadUdp extends Thread {
        private final AtomicBoolean running = new AtomicBoolean(false);

        public void run() {
            try {
                running.set(true);
                while (running.get()) {
                    byte[] receiveBuffer = new byte[1024];
                    DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                    datagramSocket.receive(receivePacket);
                    String message = new String(receivePacket.getData());
                    System.out.println(message);
                }

                closeEverything();
            } catch (IOException e) {
                closeEverything();
            }
        }

        public void kill() {
            this.running.set(false);
        }
    }

    private class ReadingThreadMulticast extends Thread {
        private final AtomicBoolean running = new AtomicBoolean(false);

        public void run() {
            try {
                running.set(true);
                while (running.get()) {
                    byte[] receiveBuffer = new byte[1024];
                    DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                    multicastSocket.receive(receivePacket);
                    String message = new String(receivePacket.getData());
                    String name = message.substring(message.indexOf('[') + 1, message.indexOf(']'));
                    if (!name.equals(Client.this.name))
                        System.out.println(message);
                }

                closeEverything();
            } catch (IOException e) {
                closeEverything();
            }
        }

        public void kill() {
            this.running.set(false);
        }
    }

    private class WritingThread extends Thread {
        private byte[] sendBuffer;

        public void run() {
            scanner = new Scanner(System.in);
            System.out.print("Enter username to join the chat: ");
            name = scanner.nextLine();
            System.out.print("\n");
            System.out.println("Joined the chat! Enjoy!");
            System.out.print("\n");

            // TCP Message to notfiy other users about the new one
            out.println(name);

            // UDP Message to notfiy server about the new udp client
            sendDatagram("@REGISTERUDPCLIENT ", 'U');

            String message;
            Character messageType;
            while (true) {
                message = scanner.nextLine();

                // OPTION @EXIT - LEAVE THE CHAT
                if (message.equals("@EXIT")) {
                    break;
                }
                // OPTION
                // @U filename - SEND ASCII ART USING UDP
                // OR
                // @M filename - SEND ASCII ART USING MULTICAST ADDRESS
                if (message.startsWith("@U ") || message.startsWith("@M ")) {
                    String filename = message.split("\\s+")[1];
                    File f = new File(filename);
                    if (!f.exists()) {
                        System.out.println("Cannot send ASCII ART message : File " + filename
                                + " doesn't exist!");
                        continue;
                    }
                    if (f.isDirectory()) {
                        System.out.println("Cannot send ASCII ART message : File " + filename + " is not text file!");
                        continue;
                    }
                    if (!filename.endsWith(".txt")) {
                        System.out.println("Cannot send ASCII ART message : File " + filename
                                + " is not text file!");
                        continue;
                    }

                    messageType = message.charAt(1);
                    message = "[" + name + "]\n" + getTextFromFile(filename);

                    sendDatagram(message, messageType);

                    continue;
                }
                // NO OPTION - SEND NORMAL TCP MESSAGE
                out.println(message);
            }

            readingThreadTcp.kill();
            readingThreadUdp.kill();
            readingThreadMulticast.kill();
        }

        private String getTextFromFile(String filename) {
            StringBuilder stringBuilder = new StringBuilder();
            try (Stream<String> stream = Files.lines(Paths.get(filename), StandardCharsets.UTF_8)) {
                stream.forEach(line -> stringBuilder.append(line).append("\n"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            return stringBuilder.toString();
        }

        private void sendDatagram(String message, Character type) {
            try {
                sendBuffer = message.getBytes();
                DatagramPacket datagramPacket;
                switch (type) {
                    case 'U':
                        datagramPacket = new DatagramPacket(sendBuffer, sendBuffer.length, serverAddress, serverPort);
                        datagramSocket.send(datagramPacket);
                        break;
                    case 'M':
                        datagramPacket = new DatagramPacket(sendBuffer, sendBuffer.length, multicastAddress,
                                multicastPort);
                        datagramSocket.send(datagramPacket);
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void closeEverything() {
        System.out.println("Disconnecting...");
        try {
            socket.close();
            in.close();
            out.close();
            datagramSocket.close();
            multicastSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.exit(1);
        }
    }
}