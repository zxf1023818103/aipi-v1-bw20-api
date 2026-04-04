package cn.zenghome.api;

import org.jspecify.annotations.NullMarked;

import java.net.URI;

@NullMarked
public record UpgradeResponse(String name, String host, int port, String path, boolean tlsEnabled) {

    private static int getEffectivePort(URI uri) {
        int port = uri.getPort();
        if (port != -1) return port;

        // 根据协议返回默认端口
        return switch (uri.getScheme().toLowerCase()) {
            case "http" -> 80;
            case "https" -> 443;
            default -> throw new RuntimeException(String.format("Unsupported URI scheme: %s", uri.getScheme()));
        };
    }

    static UpgradeResponse from(String name, String url) {
        var uri = URI.create(url);
        int port = getEffectivePort(uri);
        String path = uri.getRawPath() + (uri.getRawQuery() == null ? "" : "?" + uri.getRawQuery());
        return new UpgradeResponse(name, uri.getHost(), port, path, uri.getScheme().equalsIgnoreCase("https"));
    }
}
