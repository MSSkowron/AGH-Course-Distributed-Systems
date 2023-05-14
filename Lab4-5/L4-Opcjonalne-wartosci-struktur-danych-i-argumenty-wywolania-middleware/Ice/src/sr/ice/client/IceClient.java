package sr.ice.client;

import com.zeroc.Ice.*;
import Bookstore.*;

import java.lang.Exception;

public class IceClient
{
    public static void main(String[] args)
    {
        try {
            Communicator communicator = Util.initialize();

            ObjectPrx base = communicator.stringToProxy("bookstore:tcp -h 127.0.0.2 -p 10000 -z : udp -h 127.0.0.2 -p 10000 -z");

            BookstoreServicePrx service = BookstoreServicePrx.checkedCast(base);

            if(service == null)
            {
                throw new RuntimeException("Invalid proxy");
            }

            // Just arguments
            boolean res1 = service.addBookNoOptional("Harry Potter", "J.K Rowling", 1997);
            System.out.println(res1);

            boolean res2 = service.addBookOptional("Harry Potter", "J.K Rowling", java.util.OptionalInt.empty());
            System.out.println(res2);

            boolean res3 = service.addBookOptional("Harry Potter", "J.K Rowling", 1997);
            System.out.println(res3);

            // Structs
            boolean res4 = service.addBookStructNoOptional(new BookNoOptional ("Harry Potter", "J.K Rowling", 1997));
            System.out.println(res4);

            boolean res5 = service.addBookStructOptional(new BookOptional("Harry Potter", "J.K Rowling"));
            System.out.println(res5);

            boolean res6 = service.addBookStructOptional(new BookOptional("Harry Potter", "J.K Rowling", 1997));
            System.out.println(res6);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}