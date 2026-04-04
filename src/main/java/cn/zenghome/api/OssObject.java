package cn.zenghome.api;

import org.jspecify.annotations.NonNull;

import java.net.URI;

public record OssObject(@NonNull String bucket, @NonNull String key) {
    public static @NonNull OssObject from(@NonNull String objectName) {
        var uri = URI.create(objectName);
        var bucket = uri.getHost();
        var key = uri.getPath().substring(1);
        return new OssObject(bucket, key);
    }
}
