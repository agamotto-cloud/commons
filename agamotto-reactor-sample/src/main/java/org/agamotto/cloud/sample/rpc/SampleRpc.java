package org.agamotto.cloud.sample.rpc;

import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.agamotto.cloud.sample.grpc.proto.SimpleSampleGrpc;
import org.agamotto.cloud.sample.grpc.proto.SimpleSampleReply;
import org.agamotto.cloud.sample.grpc.proto.SimpleSampleRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import javax.annotation.Resource;

@Slf4j
@Service
public class SampleRpc extends SimpleSampleGrpc.SimpleSampleImplBase {

    @Resource
    private Scheduler publishScheduler;
    @Resource
    private Scheduler subscribeScheduler;

    /**
     * 如同http的controller
     *
     * @param request
     * @param responseObserver
     */
    @Override
    public void sayHello(SimpleSampleRequest request, StreamObserver<SimpleSampleReply> responseObserver) {

        //todo
        Mono<String> mono = Mono.just(request.getName());

        mono.publishOn(publishScheduler).subscribeOn(subscribeScheduler).
                subscribe(v -> {
                    SimpleSampleReply simpleSampleReply = SimpleSampleReply.newBuilder().setMessage(v).build();
                    responseObserver.onNext(simpleSampleReply);
                }, responseObserver::onError, responseObserver::onCompleted);
    }


}
