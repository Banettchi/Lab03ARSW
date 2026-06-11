package edu.eci.arsw.wellness.recreation;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.63.0)",
    comments = "Source: recreation.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class RecreationServiceGrpc {

  private RecreationServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "RecreationService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<edu.eci.arsw.wellness.recreation.ResourceRequest,
      edu.eci.arsw.wellness.recreation.ResourceResponse> getReserveResourceMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ReserveResource",
      requestType = edu.eci.arsw.wellness.recreation.ResourceRequest.class,
      responseType = edu.eci.arsw.wellness.recreation.ResourceResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<edu.eci.arsw.wellness.recreation.ResourceRequest,
      edu.eci.arsw.wellness.recreation.ResourceResponse> getReserveResourceMethod() {
    io.grpc.MethodDescriptor<edu.eci.arsw.wellness.recreation.ResourceRequest, edu.eci.arsw.wellness.recreation.ResourceResponse> getReserveResourceMethod;
    if ((getReserveResourceMethod = RecreationServiceGrpc.getReserveResourceMethod) == null) {
      synchronized (RecreationServiceGrpc.class) {
        if ((getReserveResourceMethod = RecreationServiceGrpc.getReserveResourceMethod) == null) {
          RecreationServiceGrpc.getReserveResourceMethod = getReserveResourceMethod =
              io.grpc.MethodDescriptor.<edu.eci.arsw.wellness.recreation.ResourceRequest, edu.eci.arsw.wellness.recreation.ResourceResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ReserveResource"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  edu.eci.arsw.wellness.recreation.ResourceRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  edu.eci.arsw.wellness.recreation.ResourceResponse.getDefaultInstance()))
              .setSchemaDescriptor(new RecreationServiceMethodDescriptorSupplier("ReserveResource"))
              .build();
        }
      }
    }
    return getReserveResourceMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static RecreationServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<RecreationServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<RecreationServiceStub>() {
        @java.lang.Override
        public RecreationServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new RecreationServiceStub(channel, callOptions);
        }
      };
    return RecreationServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static RecreationServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<RecreationServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<RecreationServiceBlockingStub>() {
        @java.lang.Override
        public RecreationServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new RecreationServiceBlockingStub(channel, callOptions);
        }
      };
    return RecreationServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static RecreationServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<RecreationServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<RecreationServiceFutureStub>() {
        @java.lang.Override
        public RecreationServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new RecreationServiceFutureStub(channel, callOptions);
        }
      };
    return RecreationServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void reserveResource(edu.eci.arsw.wellness.recreation.ResourceRequest request,
        io.grpc.stub.StreamObserver<edu.eci.arsw.wellness.recreation.ResourceResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getReserveResourceMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service RecreationService.
   */
  public static abstract class RecreationServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return RecreationServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service RecreationService.
   */
  public static final class RecreationServiceStub
      extends io.grpc.stub.AbstractAsyncStub<RecreationServiceStub> {
    private RecreationServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RecreationServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new RecreationServiceStub(channel, callOptions);
    }

    /**
     */
    public void reserveResource(edu.eci.arsw.wellness.recreation.ResourceRequest request,
        io.grpc.stub.StreamObserver<edu.eci.arsw.wellness.recreation.ResourceResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getReserveResourceMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service RecreationService.
   */
  public static final class RecreationServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<RecreationServiceBlockingStub> {
    private RecreationServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RecreationServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new RecreationServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public edu.eci.arsw.wellness.recreation.ResourceResponse reserveResource(edu.eci.arsw.wellness.recreation.ResourceRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getReserveResourceMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service RecreationService.
   */
  public static final class RecreationServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<RecreationServiceFutureStub> {
    private RecreationServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RecreationServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new RecreationServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<edu.eci.arsw.wellness.recreation.ResourceResponse> reserveResource(
        edu.eci.arsw.wellness.recreation.ResourceRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getReserveResourceMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_RESERVE_RESOURCE = 0;

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
        case METHODID_RESERVE_RESOURCE:
          serviceImpl.reserveResource((edu.eci.arsw.wellness.recreation.ResourceRequest) request,
              (io.grpc.stub.StreamObserver<edu.eci.arsw.wellness.recreation.ResourceResponse>) responseObserver);
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
          getReserveResourceMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              edu.eci.arsw.wellness.recreation.ResourceRequest,
              edu.eci.arsw.wellness.recreation.ResourceResponse>(
                service, METHODID_RESERVE_RESOURCE)))
        .build();
  }

  private static abstract class RecreationServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    RecreationServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return edu.eci.arsw.wellness.recreation.RecreationProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("RecreationService");
    }
  }

  private static final class RecreationServiceFileDescriptorSupplier
      extends RecreationServiceBaseDescriptorSupplier {
    RecreationServiceFileDescriptorSupplier() {}
  }

  private static final class RecreationServiceMethodDescriptorSupplier
      extends RecreationServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    RecreationServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (RecreationServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new RecreationServiceFileDescriptorSupplier())
              .addMethod(getReserveResourceMethod())
              .build();
        }
      }
    }
    return result;
  }
}
