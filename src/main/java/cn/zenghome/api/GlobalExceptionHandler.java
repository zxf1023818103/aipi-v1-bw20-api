package cn.zenghome.api;

import org.jspecify.annotations.NullMarked;
import org.springframework.boot.webflux.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import tools.jackson.databind.ObjectMapper;

@Component
@Order()
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    @NullMarked
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        var response = exchange.getResponse();
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        var result = Result.fail(ex.getMessage() != null ? ex.getMessage() : "Unknown Error");

        return response.writeWith(Mono.fromCallable(() -> {
            var bufferFactory = response.bufferFactory();
            return bufferFactory.wrap(objectMapper.writeValueAsBytes(result));
        }));
    }
}
