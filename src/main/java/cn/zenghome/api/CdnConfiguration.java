package cn.zenghome.api;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Data
@NullMarked
@ToString
@Configuration
@ConfigurationProperties(prefix = "cdn")
class CdnConfiguration {

    private String host;

    private int port;

    private boolean tlsEnabled;

    @PostConstruct
    public void logConfig() {
        log.info(toString());
    }
}
