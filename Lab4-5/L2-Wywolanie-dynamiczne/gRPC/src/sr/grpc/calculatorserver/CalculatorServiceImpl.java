package sr.grpc.calculatorserver;

import io.grpc.Status;
import sr.grpc.gen.*;
import sr.grpc.gen.CalculatorGrpc.CalculatorImplBase;
import io.grpc.stub.StreamObserver;

import java.util.List;

public class CalculatorServiceImpl extends CalculatorImplBase {
	@Override
	public void add(AddRequest request, StreamObserver<AddResponse> responseObserver) {
		System.out.println("Called add");
		System.out.println(request);

		long a = request.getA();
		long b = request.getB();
		long result = a + b;

		AddResponse response = AddResponse.newBuilder()
				.setResult(result)
				.build();

		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}

	@Override
	public void subtract(SubtractRequest request, StreamObserver<SubtractResponse> responseObserver) {
		System.out.println("Called subtract");
		System.out.println(request);

		long a = request.getA();
		long b = request.getB();
		long result = a - b;

		SubtractResponse response = SubtractResponse.newBuilder()
				.setResult(result)
				.build();

		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}

	@Override
	public void op(OpRequest request, StreamObserver<OpResponse> responseObserver) {
		System.out.println("Called op");
		System.out.println(request);

		A a = request.getA1();
		long b = request.getB1();

		// something

		OpResponse response = OpResponse.newBuilder().build();

		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}

	@Override
	public void avg(AvgRequest request, StreamObserver<AvgResponse> responseObserver) {
		System.out.println("Called avg");
		System.out.println(request);

		List<Long> numbers = request.getN().getNumbersList();
		if (numbers.isEmpty()) {
			responseObserver.onError(Status.INVALID_ARGUMENT
					.withDescription("Input numbers cannot be empty.")
					.asRuntimeException());
			return;
		}

		double sum = 0;
		for (long num : numbers) {
			sum += num;
		}

		double avg = sum / numbers.size();

		AvgResponse response = AvgResponse.newBuilder()
				.setResult((float) avg)
				.build();

		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}
}