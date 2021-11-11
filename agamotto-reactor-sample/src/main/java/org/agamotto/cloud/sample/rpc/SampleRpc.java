package org.agamotto.cloud.sample.rpc;

import com.google.protobuf.GeneratedMessageV3;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.agamotto.cloud.sample.grpc.proto.SimpleSampleGrpc;
import org.agamotto.cloud.sample.grpc.proto.SimpleSampleReply;
import org.agamotto.cloud.sample.grpc.proto.SimpleSampleRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.function.BiConsumer;

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

    private final SimpleSampleGrpc.SimpleSampleStub helloService = SimpleSampleGrpc.newStub(ManagedChannelBuilder.forTarget(null).build());


    public Flux<SimpleSampleReply> callService(SimpleSampleRequest request) {
        return callServer(helloService::sayHello, request);
    }

    public <Req, Resp> Flux<Resp> callServer(RpcFun<Req, Resp> callServerFun, Req request) {
        return Flux.generate(synchronousSink -> {
            callServerFun.accept(request, new StreamObserver<Resp>() {
                @Override
                public void onNext(Resp value) {
                    synchronousSink.next(value);
                }

                @Override
                public void onError(Throwable t) {
                    synchronousSink.error(t);
                }

                @Override
                public void onCompleted() {
                    synchronousSink.complete();
                }
            });
        });
    }


    @FunctionalInterface
    public interface RpcFun<Req, Resp> extends BiConsumer<Req, StreamObserver<Resp>> {

    }
}
