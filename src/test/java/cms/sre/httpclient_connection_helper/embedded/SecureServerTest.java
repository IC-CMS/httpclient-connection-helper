package cms.sre.httpclient_connection_helper.embedded;

import cms.sre.httpclient_connection_helper.HttpClientFactory;
import cms.sre.httpclient_connection_helper.HttpClientParameters;
import org.apache.commons.lang3.SystemUtils;
import org.junit.Assert;
import org.junit.Test;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

public class SecureServerTest {
    private HttpClientParameters httpClientParameters(){
        String keyStoreLocation = PathUtils.getAbsolutePathForClasspathResource("client_keystore.jks");
        String trustStoreLocation = PathUtils.getAbsolutePathForClasspathResource("cacerts.jks");

        return new HttpClientParameters()
                .setKeyStoreKeyPassword("changeit")
                .setKeyStorePassword("changeit")
                .setKeyStoreLocation(keyStoreLocation)
                .setTrustStoreLocation(trustStoreLocation)
                .setTrustStorePassword("changeit");
    }

    @Test
    public void startStopTest() throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException {
        String response = "This doesn't matter";
        SSLContext sslContext = HttpClientFactory.sslContext(httpClientParameters());
        SecureServer secureServer = new SecureServer(response, sslContext);
        secureServer.start();
        Assert.assertTrue(secureServer.isRunning());
        Assert.assertTrue(secureServer.isSecure());
        secureServer.stop();
    }

}
