package org.agamotto.cloud.sample.rpc;

import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.agamotto.cloud.sample.grpc.proto.SimpleSampleGrpc;
import org.agamotto.cloud.sample.grpc.proto.SimpleSampleReply;
import org.agamotto.cloud.sample.grpc.proto.SimpleSampleRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Service
public class SampleRpc extends SimpleSampleGrpc.SimpleSampleImplBase {

    private Scheduler publishScheduler = Schedulers.newParallel("grpc-request-handle");
    private Scheduler subscribeScheduler = Schedulers.newParallel("grpc-result-handle");

    /**
     * 如同http的controller
     *
     * @param request
     * @param responseObserver
     */
    @Override
    public void sayHello(SimpleSampleRequest request, StreamObserver<SimpleSampleReply> responseObserver) {

     //   log.info("say:{}", request.getName());

        Mono<String> mono = Mono.just(request.getName());

        mono.publishOn(publishScheduler).subscribeOn(subscribeScheduler).
                subscribe(v -> {
                    SimpleSampleReply simpleSampleReply = SimpleSampleReply.newBuilder().setMessage(v).build();
                    responseObserver.onNext(simpleSampleReply);
                }, responseObserver::onError, responseObserver::onCompleted);
    }


}
