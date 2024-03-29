package org.agamotto.cloud.sample.rpc;

import org.agamotto.cloud.grpc.util.CallUtils;
import org.agamotto.cloud.sample.grpc.proto.SimpleSampleGrpc;
import org.agamotto.cloud.sample.grpc.proto.SimpleSampleReply;
import org.agamotto.cloud.sample.grpc.proto.SimpleSampleRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class SampleRpcClient {

    @Autowired
    private SimpleSampleGrpc.SimpleSampleStub helloService;

    @Autowired
    private SimpleSampleGrpc.SimpleSampleBlockingStub helloBlockService;


    public Mono<String> callService(String name) {
        SimpleSampleRequest request = SimpleSampleRequest.newBuilder().setName(name).build();
        return CallUtils.callServer(helloService::sayHello, request).map(SimpleSampleReply::getMessage);
    }

    public String callBlockService(String name) {
        SimpleSampleRequest request = SimpleSampleRequest.newBuilder().setName(name).build();
        return helloBlockService.sayHello(request).getMessage();
    }

}
