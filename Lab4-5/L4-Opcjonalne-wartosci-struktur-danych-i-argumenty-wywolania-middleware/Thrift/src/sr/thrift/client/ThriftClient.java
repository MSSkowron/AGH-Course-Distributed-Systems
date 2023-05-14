package sr.thrift.client;

import genjava.BookNoOptional;
import genjava.BookOptional;
import genjava.BookstoreService;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

public class ThriftClient {

	public static void main(String[] args) {

		try {
			TTransport transport = new TSocket("127.0.0.2", 9090);
			transport.open();

			TProtocol protocol = new TBinaryProtocol(transport);
			BookstoreService.Client client = new BookstoreService.Client(protocol);

			BookNoOptional bookNoOptional = new BookNoOptional();
			bookNoOptional.setTitle("Harry Potter");
			bookNoOptional.setAuthor("J.K. Rowling");
			bookNoOptional.setYear(1997);

			boolean res1 = client.addBookStructNoOptional(bookNoOptional);
			System.out.println(res1);

			BookOptional bookOptional = new BookOptional();
			bookOptional.setTitle("Harry Potter");
			bookOptional.setAuthor("J.K. Rowling");

			boolean res2 = client.addBookStructOptional(bookOptional);
			System.out.println(res2);

			BookOptional bookOptionalSet = new BookOptional();
			bookOptionalSet.setTitle("Harry Potter");
			bookOptionalSet.setAuthor("J.K. Rowling");
			bookOptionalSet.setYear(1997);

			boolean res3 = client.addBookStructOptional(bookOptionalSet);
			System.out.println(res3);

			transport.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}