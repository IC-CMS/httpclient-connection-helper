package cms.sre.httpclient_connection_helper.embedded;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class InsecureServerTest {

    @Test
    public void constructorTest(){
        String response = "Hello World";
        InsecureServer insecureServer = new InsecureServer(response);
        Assert.assertEquals(response, insecureServer.getResponse());
        insecureServer.finalize();
    }

    @Test
    public void startStopTest() throws IOException {
        InsecureServer insecureServer = new InsecureServer("This doesn't Matter");
        insecureServer.start();
        insecureServer.stop();
    }

    @Test
    public void ioTest() throws IOException{
        String response = "Expected Result";
        InsecureServer insecureServer = new InsecureServer(response);
        insecureServer.start();

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet get = new HttpGet(insecureServer.getUrl());
        CloseableHttpResponse closeableHttpResponse = httpClient.execute(get);

        String actualResponse = EntityUtils.toString(closeableHttpResponse.getEntity());
        Assert.assertEquals(response, actualResponse);

        insecureServer.stop();
    }

    @Test
    public void isRunningTest() throws IOException {
        InsecureServer insecureServer = new InsecureServer("this doesn't matter");
        Assert.assertFalse(insecureServer.isRunning());
        insecureServer.start();
        Assert.assertTrue(insecureServer.isRunning());
        insecureServer.stop();
        Assert.assertFalse(insecureServer.isRunning());
    }

    @Test
    public void isSecureTest() throws IOException {
        InsecureServer insecureServer = new InsecureServer("Does not matter");
        Assert.assertFalse(insecureServer.isSecure());
        insecureServer.start();
        Assert.assertFalse(insecureServer.isSecure());
        insecureServer.stop();
        Assert.assertFalse(insecureServer.isSecure());
    }
}
