package edu.mit.cci.simulation.pangaea.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sun.grizzly.http.SelectorThread;
import com.sun.jersey.api.container.grizzly.GrizzlyWebContainerFactory;

import edu.mit.cci.testing.TestingUtils;
import edu.mit.cci.testing.TestingUtilsException;


public class RootResourceTest {
	private static SelectorThread threadSelector;
	private static HttpClient httpClient = new DefaultHttpClient();
	private static final String baseUri = "http://localhost:9998/";
	
	@BeforeClass
	public static void startServer() throws IllegalArgumentException, IOException, TestingUtilsException {

		TestingUtils.loadPropertiesToSystem(RootResourceTest.class.getClassLoader().getResource("test.properties").getFile());
		
        final Map<String, String> initParams = new HashMap<String, String>();

        initParams.put("com.sun.jersey.config.property.packages", "edu.mit.cci.simulation.pangaea.servlet");
        threadSelector = GrizzlyWebContainerFactory.create(baseUri, initParams);
        System.in.read();
	}
	
	@AfterClass
	public static void stopServer() {
        threadSelector.stopEndpoint();
	}
	
	@Test
	public void accuracyTest() throws ClientProtocolException, IOException {
		HttpPost post = new HttpPost(baseUri);
		HttpResponse resp = httpClient.execute(post);
		post.setEntity(new StringEntity("i_smpp_s_wskaznik=0&i_smpp_s_slownik=0&i_smpp_s_fraza=&" + 
				"i_smpp_s_typ_dok_nr_sek=0&i_smpp_s_rok_od=2010&i_smpp_s_mc_od=8&i_smpp_s_dzien_od=5&" + 
				"i_smpp_s_rok_do=0&i_smpp_s_mc_do=0&i_smpp_s_dzien_do=0&i_smpp_s_pub_nr_sek=0&" +
				"i_smpp_s_au_nr_sek=0&i_smpp_s_sl_nr_sek=0&i_smpp_s_kl_nr_sek=0&i_smpp_s_sygnatura=&" +
				"i_smpp_s_zatwierdz=%C2%A0Szukaj%C2%A0"));
		//post.setPara
		
		Assert.assertEquals(200, resp.getStatusLine().getStatusCode());
		System.out.println(IOUtils.toString(resp.getEntity().getContent()));
		//resp.getEntity().
		
		//httpClient.
	}

}
