package cn.zenghome.api;

import java.net.URI;

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

    public static UpgradeResponse from(String firmwareName, URI uri) {
        return new UpgradeResponse(firmwareName, uri.getHost(), getEffectivePort(uri),
                uri.getRawPath() + (uri.getRawQuery() != null ? "?" + uri.getRawQuery() : ""),
                uri.getScheme().equalsIgnoreCase("https"));
    }
}
