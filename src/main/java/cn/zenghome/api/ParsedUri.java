package cn.zenghome.api;

import java.net.URI;

public record ParsedUri(String host, int port, String path, boolean tlsEnabled) {

    public static int getEffectivePort(URI uri) {
        int port = uri.getPort();
        if (port != -1) return port;

        // 根据协议返回默认端口
        return switch (uri.getScheme().toLowerCase()) {
            case "http" -> 80;
            case "https" -> 443;
            default -> throw new RuntimeException(String.format("Unsupported URI scheme: %s", uri.getScheme()));
        };
    }

    public static ParsedUri from(URI uri) {
        return new ParsedUri(uri.getHost(), getEffectivePort(uri),
                uri.getRawPath() + (uri.getRawQuery() != null ? "?" + uri.getRawQuery() : ""),
                uri.getScheme().equalsIgnoreCase("https"));
    }
}
