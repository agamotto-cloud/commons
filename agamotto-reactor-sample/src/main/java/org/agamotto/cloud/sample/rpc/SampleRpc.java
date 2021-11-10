package org.agamotto.cloud.sample.rpc;

import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.agamotto.cloud.sample.grpc.proto.SimpleSampleGrpc;
import org.agamotto.cloud.sample.grpc.proto.SimpleSampleReply;
import org.agamotto.cloud.sample.grpc.proto.SimpleSampleRequest;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SampleRpc extends SimpleSampleGrpc.SimpleSampleImplBase {

    @Override
    public void sayHello(SimpleSampleRequest request, StreamObserver<SimpleSampleReply> responseObserver) {
        log.info("say:{}", request.getName());
        SimpleSampleReply simpleSampleReply = SimpleSampleReply.newBuilder().setMessage("sss").build();
        responseObserver.onNext(simpleSampleReply);
        responseObserver.onCompleted();
    }
}
