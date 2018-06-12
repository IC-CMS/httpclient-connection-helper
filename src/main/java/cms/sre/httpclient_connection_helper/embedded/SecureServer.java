package cms.sre.httpclient_connection_helper.embedded;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocketFactory;

public class SecureServer extends InsecureServer {

    public SecureServer(String response, SSLContext sslContext){
        super(response);
        super.makeSecure(sslContext.getServerSocketFactory(), new String[]{sslContext.getProtocol()});
    }

    @Override
    public boolean isSecure(){
        return true;
    }


}
