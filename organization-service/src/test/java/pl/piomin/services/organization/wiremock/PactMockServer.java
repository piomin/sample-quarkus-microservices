package pl.piomin.services.organization.wiremock;

public class PactMockServer {

    private final String url;
    private final int port;

    public PactMockServer(String url, int port) {
        this.url = url;
        this.port = port;
    }

    public String getUrl() {
        return url;
    }

    public int getPort() {
        return port;
    }
}
