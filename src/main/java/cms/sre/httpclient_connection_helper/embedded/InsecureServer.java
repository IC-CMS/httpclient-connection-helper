package cms.sre.httpclient_connection_helper.embedded;

import fi.iki.elonen.NanoHTTPD;

public class InsecureServer extends NanoHTTPD implements EmbeddedServer{
    private String response;

    public InsecureServer(String response){
        super(EmbeddedServer.randomPort());
        this.response = response;
    }

    public String getResponse(){return this.response;}

    @Override
    public Response serve(IHTTPSession session){
        return this.response != null && this.response.length() > 0 ? this.newFixedLengthResponse(this.response) : this.newFixedLengthResponse("EMPTY RESPONSE");
    }

    @Override
    public boolean isSecure() {
        return false;
    }

    @Override
    public String getUrl() {
        return "http://localhost:"+this.getListeningPort();
    }

    @Override
    public void finalize(){
        if(this.isRunning()){
            super.closeAllConnections();
            super.stop();
        }
    }

    @Override
    public boolean isRunning(){
        boolean isAlive = super.isAlive();
        return isAlive;
    }
}
