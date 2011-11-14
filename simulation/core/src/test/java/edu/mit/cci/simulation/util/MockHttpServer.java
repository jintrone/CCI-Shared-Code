package edu.mit.cci.simulation.util;

import com.sun.jersey.api.core.DefaultResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.simple.container.SimpleServerFactory;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MultivaluedMap;
import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * User: jintrontine
 * Date: 2/23/11
 * Time: 3:38 PM
 */
@Path("/")
public class MockHttpServer {

    private static String canned = "";

    public void setCannedResponse(String response) {
      this.canned = response;
    }


    @POST
    @Path("echo")
    @Produces("text/plain")
    public String echoMessage(MultivaluedMap<String, String> formparams) {
        StringBuilder builder = new StringBuilder();
        String sep = "";
        for (Map.Entry<String, List<String>> ent : formparams.entrySet()) {
            builder.append(sep);
            builder.append(ent.getKey());
            builder.append("=");
            builder.append(ent.getValue().toString());
            sep = "&";
        }
        return builder.toString();
    }



    @POST
    @Path("canned")
    @Produces("text/plain")
    public String cannedMessage(MultivaluedMap<String, String> formparams) {
        return canned==null?"No response set":canned;
    }

    public Closeable run(String url) throws IOException {
        ResourceConfig config = new DefaultResourceConfig(this.getClass());

       return SimpleServerFactory.create(url,config);
    }


}
