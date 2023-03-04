import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;

public class TCPClientHandler extends Thread {
    private Socket clientSocket;
    private Server chatServer;
    private PrintWriter out;
    private BufferedReader in;
    private String clientName;

    public TCPClientHandler(Socket socket, Server server) {
        this.clientSocket = socket;
        this.chatServer = server;
    }

    public void run() {
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            clientName = in.readLine();
            chatServer.logMessage(clientName + " has joined the chat.");

            chatServer.addTCPClientHandler(this);

            String message;
            while (true) {
                message = in.readLine();
                String formattedMessage = String.format("[%s | %s]: %s", clientName,
                        LocalDateTime.now().format(chatServer.formatter), message);
                chatServer.logMessage("TCP - " + clientName + ": " + message);
                chatServer.sendBroadcastTCP(formattedMessage, this);
            }
        } catch (IOException e) {
            System.out.println("TCPCLIENTHANDLER 39");
            closeEverything();
        }
    }

    public void closeEverything() {
        try {
            in.close();
            out.close();
            clientSocket.close();
            chatServer.removeClient(this);
        } catch (IOException e) {
            chatServer.removeClient(this);
        }
    }

    public void sendToClient(String message) {
        out.println(message);
    }

    public String getClientName() {
        return this.clientName;
    }
}