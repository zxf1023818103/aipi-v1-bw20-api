package cn.zenghome.api;

import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

@Component
@RegisterReflectionForBinding({Result.class, UpgradeResponse.class, UpgradeRequest.class})
class UpgradeHandler {

    private final FirmwareRepository firmwareRepository;

    private final String ossHost = "aithinker-static.oss-cn-shenzhen.aliyuncs.com";

    private final int ossPort = 443;

    private final boolean ossTlsEnabled = true;

    public UpgradeHandler(FirmwareRepository firmwareRepository) {
        this.firmwareRepository = firmwareRepository;
    }

    public Mono<ServerResponse> upgrade(ServerRequest request) {
        return request.bodyToMono(UpgradeRequest.class)
                .flatMap(upgradeRequest -> upgradeRequest.firmwareName() != null ? firmwareRepository
                        .findFirstByProductAndNameNotLikeOrderByVersionDesc(upgradeRequest.product(),
                                upgradeRequest.firmwareName()) :
                        firmwareRepository.findFirstByProductOrderByVersionDesc(upgradeRequest.product()))
                .flatMap(firmware -> ServerResponse.ok()
                        .bodyValue(new UpgradeResponse(firmware.name(), ossHost, ossPort,
                                UriComponentsBuilder.fromPath("/ota/")
                                        .path(firmware.objectName())
                                        .build()
                                        .toUriString(), ossTlsEnabled)));
    }
}
