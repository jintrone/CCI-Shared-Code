package edu.mit.cci.simulation.client.comm;


import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;


import java.io.IOException;
import java.io.StringReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: jintrone
 * @date: May 19, 2010
 */
public class BasicConnector implements DeserializingConnector {

    /**
     * Utility class for accessing web service
     *
     * @author: jintrone
     * @date: Feb 28, 2010
     */


    private HttpClient client = null;

    private InetAddress serverAddress = null;

    private int port = 80;

    private Deserializer deserializer;

    public BasicConnector(String hostname, int port) throws UnknownHostException {

        serverAddress = InetAddress.getByName(hostname);
        this.port = port;
        this.deserializer = deserializer;
        client = new DefaultHttpClient();
        ((DefaultHttpClient)client).setRedirectStrategy(new DefaultRedirectStrategy() {

            @Override
            public boolean isRedirected(HttpRequest request, HttpResponse response, HttpContext context) throws ProtocolException {
                boolean result =  super.isRedirected(request, response, context);    //To change body of overridden methods use File | Settings | File Templates.
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {
                    return true;
                } else return result;
            }


        });
    }

    public BasicConnector(Deserializer deserializer, String hostname) throws UnknownHostException {
        this(hostname, 80);
    }

    @Override
    public void setDeserializer(Deserializer d) {
        this.deserializer = d;
    }



    /**
     * Runs a model on the server and returns the raw XML Document of the resultant scenario
     *
     * @param postparams Input parameters to be posted to the service
     * @return The raw xml of the of the scenario
     */
    @Override
    public Object post(RestAccessPoint location, Map<String, String> postparams, String... pathparam) throws IOException {

        HttpPost post = new HttpPost(location.create(serverAddress, port, pathparam));
        //post.setFollowRedirects(true);
        post.addHeader("accept", "text/xml");

        if (postparams != null && postparams.size() > 0) {
            List<NameValuePair> paramList = new ArrayList<NameValuePair>();
            int i = 0;
            for (Map.Entry<String, String> ent : postparams.entrySet()) {
                paramList.add(new BasicNameValuePair(ent.getKey(), ent.getValue()));
            }
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList, "UTF-8");
            post.setEntity(entity);
        }
        ResponseHandler<String> handler = new BasicResponseHandler();
        return deserializer.deserialize(new StringReader(client.execute(post, handler)));
    }

    @Override
    public <U> U get(Class<U> clazz, Map<String, String> queryparams, String... pathparam) throws IOException {
        ModelAccessPoint map = ModelAccessPoint.forClass(clazz);
        if (map == null) return null;
        else {
            return (U)get(map,queryparams,pathparam);
        }
    }

    @Override
    public Object get(RestAccessPoint location, Map<String, String> queryparams, String... pathparam) throws IOException {
        return rawGet(location.create(serverAddress, port, pathparam), queryparams);
    }

    private Object rawGet(String location, Map<String, String> queryparams) throws IOException {
        HttpGet get = new HttpGet(location);
         get.addHeader("accept", "text/xml");
        if (queryparams != null && queryparams.size() > 0) {
            HttpParams params = new BasicHttpParams();
            for (Map.Entry<String, String> ent : queryparams.entrySet()) {
                params.setParameter(ent.getKey(), ent.getValue());
            }
            get.setParams(params);
        }
        ResponseHandler<String> handler = new BasicResponseHandler();
        return deserializer.deserialize(new StringReader(client.execute(get, handler)));

    }
}
