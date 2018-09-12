package cms.sre.httpclient_connection_helper;

import org.apache.http.client.HttpClient;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.PrivateKeyDetails;
import org.apache.http.ssl.PrivateKeyStrategy;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.rmi.ssl.SslRMIClientSocketFactory;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Paths;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Map;

public class HttpClientFactory {

    private static boolean isNotEmptyOrNull(String str){
        return str != null && str.length() > 0;
    }

    public static SSLContext sslContext(HttpClientParameters httpClientParameters) throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException, KeyManagementException, UnrecoverableKeyException {
        File trustStore = null;
        if(isNotEmptyOrNull(httpClientParameters.getTrustStoreLocation()) && isNotEmptyOrNull(httpClientParameters.getTrustStorePassword())){
            trustStore = Paths.get(httpClientParameters.getTrustStoreLocation())
                    .toFile();
        }

        File keyStore = null;
        if(isNotEmptyOrNull(httpClientParameters.getKeyStoreKeyPassword()) && isNotEmptyOrNull(httpClientParameters.getKeyStoreLocation()) && isNotEmptyOrNull(httpClientParameters.getKeyStorePassword())){
            keyStore = Paths.get(httpClientParameters.getKeyStoreLocation())
                    .toFile();
        }

        SSLContext sslContext = SSLContext.getDefault();
        if(keyStore != null || trustStore != null){
            SSLContextBuilder custom = SSLContexts.custom()
                    .setProtocol("TLSv1.2");

            if(keyStore != null){
                custom = custom.loadKeyMaterial(keyStore,
                        httpClientParameters.getKeyStorePassword().toCharArray(),
                        httpClientParameters.getKeyStoreKeyPassword().toCharArray(),
                        new PrivateKeyStrategy() {
                            @Override
                            public String chooseAlias(Map<String, PrivateKeyDetails> aliases, Socket socket) {
                                return aliases.keySet()
                                        .iterator()
                                        .next();
                            }
                        });
            }

            if(trustStore != null) {
                custom = custom.loadTrustMaterial(trustStore, httpClientParameters.getTrustStorePassword().toCharArray());
            }

            sslContext = custom.build();
        }
        return sslContext;
    }

    private static HostnameVerifier hostnameVerifier(){
        return NoopHostnameVerifier.INSTANCE;
    }

    private static SSLConnectionSocketFactory sslConnectionSocketFactory(SSLContext sslContext, HostnameVerifier hostnameVerifier){
        return new SSLConnectionSocketFactory(sslContext,
                new String[]{"TLSv1.2"},
                new String[]{"TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256"},
                hostnameVerifier);
    }

    private static HttpClientConnectionManager httpClientConnectionManager(SSLConnectionSocketFactory sslConnectionSocketFactory){
        RegistryBuilder<ConnectionSocketFactory> builder = RegistryBuilder.<ConnectionSocketFactory>create();
        if(sslConnectionSocketFactory != null){
            builder = builder.register("http", sslConnectionSocketFactory)
                    .register("https", sslConnectionSocketFactory);
        }

        return new PoolingHttpClientConnectionManager(builder.build());
    }

    public static HttpClient httpClient(HttpClientParameters httpClientParameters) throws CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException, UnrecoverableKeyException {
        SSLContext sslContext = sslContext(httpClientParameters);

        HostnameVerifier hostnameVerifier = hostnameVerifier();
        SSLConnectionSocketFactory sslConnectionSocketFactory = sslConnectionSocketFactory(sslContext, hostnameVerifier);
        HttpClientConnectionManager httpClientConnectionManager = httpClientConnectionManager(sslConnectionSocketFactory);

        return HttpClients.custom()
                .setSSLContext(sslContext)
                .setSSLSocketFactory(sslConnectionSocketFactory)
                .setConnectionManager(httpClientConnectionManager)
                .build();
    }
}
