package cn.zenghome.api;

import org.jspecify.annotations.NonNull;
import org.springframework.data.annotation.Id;

public record Firmware(@Id String objectName, @NonNull String product, @NonNull String version, @NonNull String name) {
}
