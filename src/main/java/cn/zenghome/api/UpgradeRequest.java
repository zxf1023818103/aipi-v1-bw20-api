package cn.zenghome.api;

import jakarta.annotation.Nullable;
import org.jspecify.annotations.NonNull;

public record UpgradeRequest(@Nullable String firmwareName, @NonNull String product, @NonNull String deviceName) {
}
