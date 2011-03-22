package edu.mit.cci.testing.mock;

import com.sun.jersey.api.core.DefaultResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.simple.container.SimpleServerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MultivaluedMap;
import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * User: jintrone
 * Date: 2/23/11
 * Time: 3:38 PM
 */
@Path("/")
public class MockHttpServer {

    private static String canned = "";
    private PluggableHandler getHandler= null;
    private PluggableHandler postHandler = null;


    public void setPluginGetHandler(PluggableHandler handler) {
        this.getHandler = handler;
    }

     public void setPluginPostHandler(PluggableHandler handler) {
        this.postHandler = handler;
    }

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




    @POST
    @Path("plugin/{path1}/{path2}")
    @Produces("text/plain")
    public String custompost(@PathParam("path1") String path1, @PathParam("path2") String path2, MultivaluedMap<String, String> formparams) {
       if (postHandler != null) {
          return postHandler.getResponse(path1,path2,formparams);
       }
        return null;
    }



    @GET
    @Path("plugin/{path1}/{path2}")
    @Produces("text/plain")
    public String customget(@PathParam("path1") String path1, @PathParam("path2") String path2, MultivaluedMap<String, String> formparams) {
        return getHandler!=null?getHandler.getResponse(path1,path2,formparams):null;
    }

    public Closeable run(String url) throws IOException {
        ResourceConfig config = new DefaultResourceConfig(this.getClass());

       return SimpleServerFactory.create(url,config);
    }

    public static interface PluggableHandler {
        public String getResponse(String path1, String path2, MultivaluedMap<String,String> params);
    }


}
