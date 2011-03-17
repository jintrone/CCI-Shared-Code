package edu.mit.cci.simulation.client.model.jaxb;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.net.URL;

public class ClientUrlAdapter {
	@XmlElement(name="url")
	String url;

	public ClientUrlAdapter() {

	}

	public ClientUrlAdapter(URL url) {
		this.url = url.toString();
	}

	public static class Adapter extends XmlAdapter<ClientUrlAdapter,URL> {

		@Override
		public ClientUrlAdapter marshal(URL arg0) throws Exception {
			return new ClientUrlAdapter(arg0);
		}

		@Override
		public URL unmarshal(ClientUrlAdapter arg0) throws Exception {
			// TODO Auto-generated method stub
			return null;
		}

	}
}