package cms.sre.httpclient_connection_helper;

public class HttpClientParameters {
    private String trustStorePassword;
    private String trustStoreLocation;
    private String keyStorePassword;
    private String keyStoreKeyPassword;
    private String keyStoreLocation;

    public String getTrustStorePassword() {
        return trustStorePassword;
    }

    public HttpClientParameters setTrustStorePassword(String trustStorePassword) {
        this.trustStorePassword = trustStorePassword;
        return this;
    }

    public String getTrustStoreLocation() {
        return trustStoreLocation;
    }

    public HttpClientParameters setTrustStoreLocation(String trustStoreLocation) {
        this.trustStoreLocation = trustStoreLocation;
        return this;
    }

    public String getKeyStorePassword() {
        return keyStorePassword;
    }

    public HttpClientParameters setKeyStorePassword(String keyStorePassword) {
        this.keyStorePassword = keyStorePassword;
        return this;
    }

    public String getKeyStoreKeyPassword() {
        return keyStoreKeyPassword;
    }

    public HttpClientParameters setKeyStoreKeyPassword(String keyStoreKeyPassword) {
        this.keyStoreKeyPassword = keyStoreKeyPassword;
        return this;
    }

    public String getKeyStoreLocation() {
        return keyStoreLocation;
    }

    public HttpClientParameters setKeyStoreLocation(String keyStoreLocation) {
        this.keyStoreLocation = keyStoreLocation;
        return this;
    }
}
