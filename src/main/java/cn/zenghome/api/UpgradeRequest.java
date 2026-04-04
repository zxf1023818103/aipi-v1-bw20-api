package cn.zenghome.api;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public record UpgradeRequest(@Nullable String firmwareName, String product, String deviceName) {
}
