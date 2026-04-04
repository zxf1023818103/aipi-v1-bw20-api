package cn.zenghome.api;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Data
@ToString(onlyExplicitlyIncluded = true)
@Configuration
@ConfigurationProperties(prefix = "alibaba.cloud")
public class AlibabaCloudConfiguration {

    @ToString.Include
    private String accessKeyId;

    private String accessKeySecret;

    private String securityToken;

    @ToString.Include(name = "accessKeySecret")
    public String getMaskedAccessKeySecret() {
        return mask(accessKeySecret, 2, 2);
    }

    @ToString.Include(name = "securityToken")
    public String getMaskedSecurityToken() {
        return mask(securityToken, 3, 3);
    }

    private static String mask(String str, int head, int tail) {
        if (str == null || str.isEmpty()) {
            return "";
        }
        int len = str.length();
        if (len <= (head + tail)) {
            return "******";
        }
        return str.substring(0, head) + "******" + str.substring(len - tail);
    }

    @PostConstruct
    public void logConfig() {
        log.info(toString());
    }
}
