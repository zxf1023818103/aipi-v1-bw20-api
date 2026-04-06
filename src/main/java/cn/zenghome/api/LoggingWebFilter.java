package cn.zenghome.api;

import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tools.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;

@Slf4j
@NullMarked
@Component
public class LoggingWebFilter implements WebFilter {

    private final ObjectMapper objectMapper;

    public LoggingWebFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        // 1. 装饰 Request 记录请求体
        ServerHttpRequestDecorator loggingRequest = new ServerHttpRequestDecorator(exchange.getRequest()) {
            @Override
            public Flux<DataBuffer> getBody() {
                return super.getBody().doOnNext(buffer -> {
                    String body = buffer.toString(StandardCharsets.UTF_8);
                    try {
                        Object json = objectMapper.readValue(body, Object.class);
                        body = objectMapper.writeValueAsString(json);
                    } catch (Exception e) {
                        // 如果解析失败（不是JSON），就保持原样
                    }
                    log.info("> {} {}", path, body);
                });
            }
        };

        // 2. 装饰 Response 记录响应体
        ServerHttpResponseDecorator loggingResponse = new ServerHttpResponseDecorator(exchange.getResponse()) {
            @Override
            public Mono<Void> writeWith(org.reactivestreams.Publisher<? extends DataBuffer> body) {
                return super.writeWith(Flux.from(body).doOnNext(buffer -> {
                    String bodyStr = buffer.toString(StandardCharsets.UTF_8);
                    log.info("< {} {}", path, bodyStr);
                }));
            }
        };

        return chain.filter(exchange.mutate()
                .request(loggingRequest)
                .response(loggingResponse)
                .build());
    }
}
