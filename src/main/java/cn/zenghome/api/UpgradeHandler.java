package cn.zenghome.api;

import com.aliyun.sdk.service.oss2.OSSClient;
import com.aliyun.sdk.service.oss2.OSSClientBuilder;
import com.aliyun.sdk.service.oss2.PresignOptions;
import com.aliyun.sdk.service.oss2.credentials.StaticCredentialsProvider;
import com.aliyun.sdk.service.oss2.models.GetObjectRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
@Component
@RegisterReflectionForBinding({Result.class, UpgradeResponse.class, UpgradeRequest.class})
class UpgradeHandler {

    private final FirmwareRepository firmwareRepository;

    private final OSSClientBuilder ossClientBuilder;

    private final CdnConfiguration cdnConfiguration;

    public UpgradeHandler(FirmwareRepository firmwareRepository, OssConfiguration ossConfiguration,
                          AlibabaCloudConfiguration alibabaCloudConfiguration, FcConfiguration fcConfiguration,
                          CdnConfiguration cdnConfiguration) {
        this.firmwareRepository = firmwareRepository;
        this.cdnConfiguration = cdnConfiguration;
        ossClientBuilder = OSSClient.newBuilder()
                .region(ossConfiguration.getRegion())
                .accountId(fcConfiguration.getAccountId())
                .useApacheHttpClient4(true)
                .credentialsProvider(new StaticCredentialsProvider(alibabaCloudConfiguration.getAccessKeyId(),
                        alibabaCloudConfiguration.getAccessKeySecret(), alibabaCloudConfiguration.getSecurityToken()));
    }

    public Mono<ServerResponse> upgradeViaOss(ServerRequest request) {
        try (OSSClient client = ossClientBuilder.build()) {
            return request.bodyToMono(UpgradeRequest.class)
                    .flatMap(upgradeRequest -> firmwareRepository
                            .findFirstByProductOrderByVersionDesc(upgradeRequest.product())
                            .filter(firmware -> !firmware.name().equals(upgradeRequest.firmwareName())))
                    .flatMap(firmware -> {
                        var ossObject = OssObject.from(firmware.objectName());
                        var presignResult = client.presign(GetObjectRequest.newBuilder().bucket(ossObject.bucket()).key(ossObject.key().substring(1)).build(),
                                PresignOptions.newBuilder().expiration(Duration.ofMinutes(15)).build());
                        var response = UpgradeResponse.from(firmware.name(), presignResult.url());
                        return ServerResponse.ok().bodyValue(response);
                    });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Mono<ServerResponse> upgradeViaCdn(ServerRequest request) {
        return request.bodyToMono(UpgradeRequest.class)
                .flatMap(upgradeRequest -> firmwareRepository
                        .findFirstByProductOrderByVersionDesc(upgradeRequest.product())
                        .filter(firmware -> !firmware.name().equals(upgradeRequest.firmwareName())))
                .flatMap(firmware -> {
                    var ossObject = OssObject.from(firmware.objectName());
                    return ServerResponse.ok().bodyValue(new UpgradeResponse(firmware.name(), cdnConfiguration.getHost(),
                            cdnConfiguration.getPort(), ossObject.key(), cdnConfiguration.isTlsEnabled()));
                });
    }

    public Mono<ServerResponse> firmwareCount(ServerRequest request) {
        return request.bodyToMono(Void.class)
                .then(firmwareRepository.count())
                .flatMap(count -> ServerResponse.ok().bodyValue(count));
    }
}
