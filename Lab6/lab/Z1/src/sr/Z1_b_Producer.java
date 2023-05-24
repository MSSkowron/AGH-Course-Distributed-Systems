import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Z1_b_Producer {
    private static final String QUEUE_NAME = "queue1";

    public static void main(String[] argv) throws Exception {
        // info
        System.out.println("Z1 PRODUCER");

        // connection & channel
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // queue
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        // producer (publish msg)
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Enter the number of messages to send:");
        int numMessages = Integer.parseInt(br.readLine());

        System.out.println("Enter the duration for short tasks (in seconds):");
        int shortTaskDuration = Integer.parseInt(br.readLine());

        System.out.println("Enter the duration for long tasks (in seconds):");
        int longTaskDuration = Integer.parseInt(br.readLine());

        for (int i = 1; i <= numMessages; i++) {
            String message;
            if (i % 2 == 0) {
                message = String.valueOf(longTaskDuration);
            } else {
                message = String.valueOf(shortTaskDuration);
            }

            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println("Sent: " + message);
        }

        // close
        channel.close();
        connection.close();
    }
}
