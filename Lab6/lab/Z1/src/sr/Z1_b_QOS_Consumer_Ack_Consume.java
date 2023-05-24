import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Z1_b_QOS_Consumer_Ack_Consume {

    public static void main(String[] argv) throws Exception {

        // info
        System.out.println("Z1 CONSUMER");

        // connection & channel
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // queue
        String QUEUE_NAME = "queue1";
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        // prefetch count
        int prefetchCount = 1; // Ustawienie liczby wiadomości pobieranych przez konsumenta na raz
        channel.basicQos(prefetchCount);

        // consumer (handle msg)
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, StandardCharsets.UTF_8);
                System.out.println("Received: " + message);

                // Process the message for a specified time (in seconds)
                int timeToSleep = Integer.parseInt(message);
                try {
                    Thread.sleep(timeToSleep * 1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // Acknowledge the message after processing
                channel.basicAck(envelope.getDeliveryTag(), false);
                System.out.println("Sent ACK");
            }
        };


        // start listening
        System.out.println("Waiting for messages...");
        channel.basicConsume(QUEUE_NAME, false, consumer);

        // close
//        channel.close();
//        connection.close();
    }
}
