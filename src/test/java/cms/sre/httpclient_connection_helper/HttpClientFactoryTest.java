package cms.sre.httpclient_connection_helper;

import cms.sre.httpclient_connection_helper.embedded.PathUtils;
import cms.sre.httpclient_connection_helper.embedded.SecureServer;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.junit.Assert;
import org.junit.Test;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

public class HttpClientFactoryTest {

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
    public void httpClientFactoryTest() throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException {
        String response = "This does matter";
        SSLContext sslContext = HttpClientFactory.sslContext(httpClientParameters());
        SecureServer secureServer = new SecureServer(response, sslContext);
        secureServer.start();

        HttpClient httpClient = HttpClientFactory.httpClient(httpClientParameters());
        HttpGet get = new HttpGet(secureServer.getUrl());
        HttpResponse closeableHttpResponse = httpClient.execute(get);

        String actualResponse = EntityUtils.toString(closeableHttpResponse.getEntity());
        Assert.assertEquals(response, actualResponse);

        secureServer.stop();
    }
}
