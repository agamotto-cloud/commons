package org.agamotto.cloud.grpc.util;

import io.grpc.stub.StreamObserver;
import reactor.core.publisher.Flux;

import java.util.function.BiConsumer;

public class CallUtils {

    public static <Req, Resp> Flux<Resp> callServer(RpcFun<Req, Resp> callServerFun, Req request) {

        return Flux.create(synchronousSink -> {
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
