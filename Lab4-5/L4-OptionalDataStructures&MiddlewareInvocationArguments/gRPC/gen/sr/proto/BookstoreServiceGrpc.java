package sr.proto;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.54.0)",
    comments = "Source: bookstore.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class BookstoreServiceGrpc {

  private BookstoreServiceGrpc() {}

  public static final String SERVICE_NAME = "tutorial.BookstoreService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<sr.proto.BookstoreServiceProtos.AddBookOptionalRequest,
      sr.proto.BookstoreServiceProtos.AddBookResponse> getAddBookOptionalMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "AddBookOptional",
      requestType = sr.proto.BookstoreServiceProtos.AddBookOptionalRequest.class,
      responseType = sr.proto.BookstoreServiceProtos.AddBookResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<sr.proto.BookstoreServiceProtos.AddBookOptionalRequest,
      sr.proto.BookstoreServiceProtos.AddBookResponse> getAddBookOptionalMethod() {
    io.grpc.MethodDescriptor<sr.proto.BookstoreServiceProtos.AddBookOptionalRequest, sr.proto.BookstoreServiceProtos.AddBookResponse> getAddBookOptionalMethod;
    if ((getAddBookOptionalMethod = BookstoreServiceGrpc.getAddBookOptionalMethod) == null) {
      synchronized (BookstoreServiceGrpc.class) {
        if ((getAddBookOptionalMethod = BookstoreServiceGrpc.getAddBookOptionalMethod) == null) {
          BookstoreServiceGrpc.getAddBookOptionalMethod = getAddBookOptionalMethod =
              io.grpc.MethodDescriptor.<sr.proto.BookstoreServiceProtos.AddBookOptionalRequest, sr.proto.BookstoreServiceProtos.AddBookResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "AddBookOptional"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  sr.proto.BookstoreServiceProtos.AddBookOptionalRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  sr.proto.BookstoreServiceProtos.AddBookResponse.getDefaultInstance()))
              .setSchemaDescriptor(new BookstoreServiceMethodDescriptorSupplier("AddBookOptional"))
              .build();
        }
      }
    }
    return getAddBookOptionalMethod;
  }

  private static volatile io.grpc.MethodDescriptor<sr.proto.BookstoreServiceProtos.AddBookNoOptionalRequest,
      sr.proto.BookstoreServiceProtos.AddBookResponse> getAddBookNoOptionalMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "AddBookNoOptional",
      requestType = sr.proto.BookstoreServiceProtos.AddBookNoOptionalRequest.class,
      responseType = sr.proto.BookstoreServiceProtos.AddBookResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<sr.proto.BookstoreServiceProtos.AddBookNoOptionalRequest,
      sr.proto.BookstoreServiceProtos.AddBookResponse> getAddBookNoOptionalMethod() {
    io.grpc.MethodDescriptor<sr.proto.BookstoreServiceProtos.AddBookNoOptionalRequest, sr.proto.BookstoreServiceProtos.AddBookResponse> getAddBookNoOptionalMethod;
    if ((getAddBookNoOptionalMethod = BookstoreServiceGrpc.getAddBookNoOptionalMethod) == null) {
      synchronized (BookstoreServiceGrpc.class) {
        if ((getAddBookNoOptionalMethod = BookstoreServiceGrpc.getAddBookNoOptionalMethod) == null) {
          BookstoreServiceGrpc.getAddBookNoOptionalMethod = getAddBookNoOptionalMethod =
              io.grpc.MethodDescriptor.<sr.proto.BookstoreServiceProtos.AddBookNoOptionalRequest, sr.proto.BookstoreServiceProtos.AddBookResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "AddBookNoOptional"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  sr.proto.BookstoreServiceProtos.AddBookNoOptionalRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  sr.proto.BookstoreServiceProtos.AddBookResponse.getDefaultInstance()))
              .setSchemaDescriptor(new BookstoreServiceMethodDescriptorSupplier("AddBookNoOptional"))
              .build();
        }
      }
    }
    return getAddBookNoOptionalMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static BookstoreServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<BookstoreServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<BookstoreServiceStub>() {
        @java.lang.Override
        public BookstoreServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new BookstoreServiceStub(channel, callOptions);
        }
      };
    return BookstoreServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static BookstoreServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<BookstoreServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<BookstoreServiceBlockingStub>() {
        @java.lang.Override
        public BookstoreServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new BookstoreServiceBlockingStub(channel, callOptions);
        }
      };
    return BookstoreServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static BookstoreServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<BookstoreServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<BookstoreServiceFutureStub>() {
        @java.lang.Override
        public BookstoreServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new BookstoreServiceFutureStub(channel, callOptions);
        }
      };
    return BookstoreServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void addBookOptional(sr.proto.BookstoreServiceProtos.AddBookOptionalRequest request,
        io.grpc.stub.StreamObserver<sr.proto.BookstoreServiceProtos.AddBookResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getAddBookOptionalMethod(), responseObserver);
    }

    /**
     */
    default void addBookNoOptional(sr.proto.BookstoreServiceProtos.AddBookNoOptionalRequest request,
        io.grpc.stub.StreamObserver<sr.proto.BookstoreServiceProtos.AddBookResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getAddBookNoOptionalMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service BookstoreService.
   */
  public static abstract class BookstoreServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return BookstoreServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service BookstoreService.
   */
  public static final class BookstoreServiceStub
      extends io.grpc.stub.AbstractAsyncStub<BookstoreServiceStub> {
    private BookstoreServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected BookstoreServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new BookstoreServiceStub(channel, callOptions);
    }

    /**
     */
    public void addBookOptional(sr.proto.BookstoreServiceProtos.AddBookOptionalRequest request,
        io.grpc.stub.StreamObserver<sr.proto.BookstoreServiceProtos.AddBookResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getAddBookOptionalMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void addBookNoOptional(sr.proto.BookstoreServiceProtos.AddBookNoOptionalRequest request,
        io.grpc.stub.StreamObserver<sr.proto.BookstoreServiceProtos.AddBookResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getAddBookNoOptionalMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service BookstoreService.
   */
  public static final class BookstoreServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<BookstoreServiceBlockingStub> {
    private BookstoreServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected BookstoreServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new BookstoreServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public sr.proto.BookstoreServiceProtos.AddBookResponse addBookOptional(sr.proto.BookstoreServiceProtos.AddBookOptionalRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getAddBookOptionalMethod(), getCallOptions(), request);
    }

    /**
     */
    public sr.proto.BookstoreServiceProtos.AddBookResponse addBookNoOptional(sr.proto.BookstoreServiceProtos.AddBookNoOptionalRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getAddBookNoOptionalMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service BookstoreService.
   */
  public static final class BookstoreServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<BookstoreServiceFutureStub> {
    private BookstoreServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected BookstoreServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new BookstoreServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<sr.proto.BookstoreServiceProtos.AddBookResponse> addBookOptional(
        sr.proto.BookstoreServiceProtos.AddBookOptionalRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getAddBookOptionalMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<sr.proto.BookstoreServiceProtos.AddBookResponse> addBookNoOptional(
        sr.proto.BookstoreServiceProtos.AddBookNoOptionalRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getAddBookNoOptionalMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_ADD_BOOK_OPTIONAL = 0;
  private static final int METHODID_ADD_BOOK_NO_OPTIONAL = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_ADD_BOOK_OPTIONAL:
          serviceImpl.addBookOptional((sr.proto.BookstoreServiceProtos.AddBookOptionalRequest) request,
              (io.grpc.stub.StreamObserver<sr.proto.BookstoreServiceProtos.AddBookResponse>) responseObserver);
          break;
        case METHODID_ADD_BOOK_NO_OPTIONAL:
          serviceImpl.addBookNoOptional((sr.proto.BookstoreServiceProtos.AddBookNoOptionalRequest) request,
              (io.grpc.stub.StreamObserver<sr.proto.BookstoreServiceProtos.AddBookResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getAddBookOptionalMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              sr.proto.BookstoreServiceProtos.AddBookOptionalRequest,
              sr.proto.BookstoreServiceProtos.AddBookResponse>(
                service, METHODID_ADD_BOOK_OPTIONAL)))
        .addMethod(
          getAddBookNoOptionalMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              sr.proto.BookstoreServiceProtos.AddBookNoOptionalRequest,
              sr.proto.BookstoreServiceProtos.AddBookResponse>(
                service, METHODID_ADD_BOOK_NO_OPTIONAL)))
        .build();
  }

  private static abstract class BookstoreServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    BookstoreServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return sr.proto.BookstoreServiceProtos.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("BookstoreService");
    }
  }

  private static final class BookstoreServiceFileDescriptorSupplier
      extends BookstoreServiceBaseDescriptorSupplier {
    BookstoreServiceFileDescriptorSupplier() {}
  }

  private static final class BookstoreServiceMethodDescriptorSupplier
      extends BookstoreServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    BookstoreServiceMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (BookstoreServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new BookstoreServiceFileDescriptorSupplier())
              .addMethod(getAddBookOptionalMethod())
              .addMethod(getAddBookNoOptionalMethod())
              .build();
        }
      }
    }
    return result;
  }
}
