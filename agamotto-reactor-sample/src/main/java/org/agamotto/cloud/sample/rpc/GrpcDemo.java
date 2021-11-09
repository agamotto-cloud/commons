package org.agamotto.cloud.sample.rpc;

import io.grpc.stub.StreamObserver;
import org.agamotto.cloud.sample.rpc.lib.HelloReply;
import org.agamotto.cloud.sample.rpc.lib.HelloRequest;
import org.agamotto.cloud.sample.rpc.lib.SimpleGrpc;
import org.springframework.stereotype.Service;

@Service
public class GrpcDemo extends SimpleGrpc.SimpleImplBase {

    @Override
    public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
        HelloReply reply = HelloReply.newBuilder().setMessage("Hello ==> " + request.getName()).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
}
