package cn.zenghome.api;

import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
@RegisterReflectionForBinding({Result.class, ParsedUri.class})
class UpgradeHandler {
    public Mono<ServerResponse> upgrade(ServerRequest request) {
        return ServerResponse.ok().bodyValue(ParsedUri.from(URI.create("https://aithinker-static.oss-cn-shenzhen.aliyuncs.com/ota/axk_vb6824_v1.9_ota.bin")));
    }
}
