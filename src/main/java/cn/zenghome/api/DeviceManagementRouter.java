package cn.zenghome.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration(proxyBeanMethods = false)
class DeviceManagementRouter {

    @Bean
    public RouterFunction<ServerResponse> route(UpgradeHandler upgradeHandler) {
        var upgradeViaOssRoute = RouterFunctions
                .route(POST("/upgrade-oss").and(accept(MediaType.APPLICATION_JSON)), upgradeHandler::upgradeViaOss);
        var upgradeViaCdnRoute = RouterFunctions
                .route(POST("/upgrade-cdn").and(accept(MediaType.APPLICATION_JSON)), upgradeHandler::upgradeViaCdn);
        var defaultUpgradeRoute = RouterFunctions
                .route(POST("/upgrade").and(accept(MediaType.APPLICATION_JSON)), upgradeHandler::upgradeViaCdn);
        var firmwareCountRoute = RouterFunctions
                .route(GET("/firmware-count").and(accept(MediaType.ALL)), upgradeHandler::firmwareCount);
        return RouterFunctions.route()
                .add(firmwareCountRoute)
                .add(upgradeViaOssRoute)
                .add(upgradeViaCdnRoute)
                .add(defaultUpgradeRoute)
                .filter(wrapResult())
                .build();
    }

    private HandlerFilterFunction<ServerResponse, ServerResponse> wrapResult() {
        return (request, next) -> next.handle(request).flatMap(response -> {
            // Java 25 模式匹配：如果是 EntityResponse (即包含具体对象) 且还没包装过
            if (response instanceof EntityResponse<?> entityResponse
                    && !(entityResponse.entity() instanceof Result)) {
                // 重新构造包含 Result<T> 的响应
                return ServerResponse.from(response)
                        .bodyValue(Result.ok(entityResponse.entity()));
            }
            return Mono.just(response);
        }).switchIfEmpty(ServerResponse.ok().bodyValue(new Result<>(true, null, null)));
    }
}
