package cms.sre.httpclient_connection_helper.embedded;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

public interface EmbeddedServer {
    /**
     * Start the server.
     *
     * @throws IOException
     *             if the socket is in use.
     */
    void start() throws IOException;

    /**
     * Stop the server.
     */
    void stop();

    boolean isRunning();

    /**
     * Determines if server is secured (i.e. servers content over https or http)
     * @return true is serves content securely.
     */
    boolean isSecure();

    String getUrl();

    public static int randomPort(){
        return ThreadLocalRandom
                .current()
                .nextInt(8080, 9000);
    }

}
