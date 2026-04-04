package cn.zenghome.api;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Data
@ToString
@Configuration
@ConfigurationProperties(prefix = "fc")
public class FcConfiguration {

    String accountId;

    String functionHandler;

    String functionMemorySize;

    String functionName;

    String region;

    String customListenPort;

    String instanceId;

    @PostConstruct
    public void logConfig() {
        log.info(toString());
    }
}
