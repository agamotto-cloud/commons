package org.agamotto.cloud.sample.grpc.proto;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 * <pre>
 * The greeting service definition.
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.42.0)",
    comments = "Source: SimpleSample.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class SimpleSampleGrpc {

  private SimpleSampleGrpc() {}

  public static final String SERVICE_NAME = "SimpleSample";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<org.agamotto.cloud.sample.grpc.proto.SimpleSampleRequest,
      org.agamotto.cloud.sample.grpc.proto.SimpleSampleReply> getSayHelloMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SayHello",
      requestType = org.agamotto.cloud.sample.grpc.proto.SimpleSampleRequest.class,
      responseType = org.agamotto.cloud.sample.grpc.proto.SimpleSampleReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.agamotto.cloud.sample.grpc.proto.SimpleSampleRequest,
      org.agamotto.cloud.sample.grpc.proto.SimpleSampleReply> getSayHelloMethod() {
    io.grpc.MethodDescriptor<org.agamotto.cloud.sample.grpc.proto.SimpleSampleRequest, org.agamotto.cloud.sample.grpc.proto.SimpleSampleReply> getSayHelloMethod;
    if ((getSayHelloMethod = SimpleSampleGrpc.getSayHelloMethod) == null) {
      synchronized (SimpleSampleGrpc.class) {
        if ((getSayHelloMethod = SimpleSampleGrpc.getSayHelloMethod) == null) {
          SimpleSampleGrpc.getSayHelloMethod = getSayHelloMethod =
              io.grpc.MethodDescriptor.<org.agamotto.cloud.sample.grpc.proto.SimpleSampleRequest, org.agamotto.cloud.sample.grpc.proto.SimpleSampleReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SayHello"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.agamotto.cloud.sample.grpc.proto.SimpleSampleRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.agamotto.cloud.sample.grpc.proto.SimpleSampleReply.getDefaultInstance()))
              .setSchemaDescriptor(new SimpleSampleMethodDescriptorSupplier("SayHello"))
              .build();
        }
      }
    }
    return getSayHelloMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static SimpleSampleStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<SimpleSampleStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<SimpleSampleStub>() {
        @java.lang.Override
        public SimpleSampleStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new SimpleSampleStub(channel, callOptions);
        }
      };
    return SimpleSampleStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static SimpleSampleBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<SimpleSampleBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<SimpleSampleBlockingStub>() {
        @java.lang.Override
        public SimpleSampleBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new SimpleSampleBlockingStub(channel, callOptions);
        }
      };
    return SimpleSampleBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static SimpleSampleFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<SimpleSampleFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<SimpleSampleFutureStub>() {
        @java.lang.Override
        public SimpleSampleFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new SimpleSampleFutureStub(channel, callOptions);
        }
      };
    return SimpleSampleFutureStub.newStub(factory, channel);
  }

  /**
   * <pre>
   * The greeting service definition.
   * </pre>
   */
  public static abstract class SimpleSampleImplBase implements io.grpc.BindableService {

    /**
     * <pre>
     * Sends a greeting
     * </pre>
     */
    public void sayHello(org.agamotto.cloud.sample.grpc.proto.SimpleSampleRequest request,
        io.grpc.stub.StreamObserver<org.agamotto.cloud.sample.grpc.proto.SimpleSampleReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSayHelloMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getSayHelloMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                org.agamotto.cloud.sample.grpc.proto.SimpleSampleRequest,
                org.agamotto.cloud.sample.grpc.proto.SimpleSampleReply>(
                  this, METHODID_SAY_HELLO)))
          .build();
    }
  }

  /**
   * <pre>
   * The greeting service definition.
   * </pre>
   */
  public static final class SimpleSampleStub extends io.grpc.stub.AbstractAsyncStub<SimpleSampleStub> {
    private SimpleSampleStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SimpleSampleStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new SimpleSampleStub(channel, callOptions);
    }

    /**
     * <pre>
     * Sends a greeting
     * </pre>
     */
    public void sayHello(org.agamotto.cloud.sample.grpc.proto.SimpleSampleRequest request,
        io.grpc.stub.StreamObserver<org.agamotto.cloud.sample.grpc.proto.SimpleSampleReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSayHelloMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * <pre>
   * The greeting service definition.
   * </pre>
   */
  public static final class SimpleSampleBlockingStub extends io.grpc.stub.AbstractBlockingStub<SimpleSampleBlockingStub> {
    private SimpleSampleBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SimpleSampleBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new SimpleSampleBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * Sends a greeting
     * </pre>
     */
    public org.agamotto.cloud.sample.grpc.proto.SimpleSampleReply sayHello(org.agamotto.cloud.sample.grpc.proto.SimpleSampleRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSayHelloMethod(), getCallOptions(), request);
    }
  }

  /**
   * <pre>
   * The greeting service definition.
   * </pre>
   */
  public static final class SimpleSampleFutureStub extends io.grpc.stub.AbstractFutureStub<SimpleSampleFutureStub> {
    private SimpleSampleFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SimpleSampleFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new SimpleSampleFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * Sends a greeting
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<org.agamotto.cloud.sample.grpc.proto.SimpleSampleReply> sayHello(
        org.agamotto.cloud.sample.grpc.proto.SimpleSampleRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSayHelloMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_SAY_HELLO = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final SimpleSampleImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(SimpleSampleImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_SAY_HELLO:
          serviceImpl.sayHello((org.agamotto.cloud.sample.grpc.proto.SimpleSampleRequest) request,
              (io.grpc.stub.StreamObserver<org.agamotto.cloud.sample.grpc.proto.SimpleSampleReply>) responseObserver);
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

  private static abstract class SimpleSampleBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    SimpleSampleBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return org.agamotto.cloud.sample.grpc.proto.SampleGrpc.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("SimpleSample");
    }
  }

  private static final class SimpleSampleFileDescriptorSupplier
      extends SimpleSampleBaseDescriptorSupplier {
    SimpleSampleFileDescriptorSupplier() {}
  }

  private static final class SimpleSampleMethodDescriptorSupplier
      extends SimpleSampleBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    SimpleSampleMethodDescriptorSupplier(String methodName) {
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
      synchronized (SimpleSampleGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new SimpleSampleFileDescriptorSupplier())
              .addMethod(getSayHelloMethod())
              .build();
        }
      }
    }
    return result;
  }
}
