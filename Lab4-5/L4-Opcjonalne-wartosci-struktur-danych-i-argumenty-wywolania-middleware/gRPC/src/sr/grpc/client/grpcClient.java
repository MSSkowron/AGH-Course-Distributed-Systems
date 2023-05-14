package sr.grpc.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import sr.proto.BookstoreServiceGrpc;
import sr.proto.BookstoreServiceGrpc.BookstoreServiceBlockingStub;
import sr.proto.BookstoreServiceProtos;

public class grpcClient {
	public static void main(String[] args) {
		ManagedChannel channel = ManagedChannelBuilder.forAddress("127.0.0.2", 9090)
				.usePlaintext()
				.build();

		BookstoreServiceBlockingStub stub = BookstoreServiceGrpc.newBlockingStub(channel);

		BookstoreServiceProtos.AddBookNoOptionalRequest requestNoOptional = BookstoreServiceProtos.AddBookNoOptionalRequest.newBuilder()
				.setTitle("Harry Potter")
				.setAuthor("J.K. Rowling")
				.setYear(1997)
				.build();
		BookstoreServiceProtos.AddBookResponse responseOptional = stub.addBookNoOptional(requestNoOptional);
		System.out.println("Response received: " + responseOptional.getResponse());

		BookstoreServiceProtos.AddBookOptionalRequest requestOptionalNotSet = BookstoreServiceProtos.AddBookOptionalRequest.newBuilder()
				.setTitle("Harry Potter")
				.setAuthor("J.K. Rowling")
				.build();

		BookstoreServiceProtos.AddBookResponse responseOptionalNotSet = stub.addBookOptional(requestOptionalNotSet);
		System.out.println("Response received: " + responseOptionalNotSet.getResponse());

		BookstoreServiceProtos.AddBookOptionalRequest requestOptionalSet = BookstoreServiceProtos.AddBookOptionalRequest.newBuilder()
				.setTitle("Harry Potter")
				.setAuthor("J.K. Rowling")
				.setYear(1997)
				.build();

		BookstoreServiceProtos.AddBookResponse responseOptionalSet = stub.addBookOptional(requestOptionalSet);
		System.out.println("Response received: " + responseOptionalSet.getResponse());

		channel.shutdown();
	}
}