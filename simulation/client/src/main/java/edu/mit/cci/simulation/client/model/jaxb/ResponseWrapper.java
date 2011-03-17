package edu.mit.cci.simulation.client.model.jaxb;

import edu.mit.cci.simulation.client.Scenario;
import edu.mit.cci.simulation.client.Simulation;
import org.apache.log4j.Logger;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@XmlRootElement(name="payload")
public class ResponseWrapper {

	private static Logger logger = Logger.getLogger(ResponseWrapper.class);

		@XmlElement(name="timestamp")
		public long timestamp  = System.currentTimeMillis();
		@XmlElementWrapper(name="simulations")
		@XmlElement(name="simulation")
		public List<Simulation> sims = new ArrayList<Simulation>();
		@XmlElementWrapper(name="scenarios")
		@XmlElement(name="scenario")
		public List<Scenario> scenarios = new ArrayList<Scenario>();

		public ResponseWrapper() {
		}

		public ResponseWrapper(Collection data) {
			add(data);
		}

		public void add(Collection data) {
			for (Object datum:data) {
				add(datum);
			}
		}

		public void add(Object datum) {
			if (datum instanceof Simulation) {
				sims.add((Simulation)datum);
			} else if (datum instanceof Scenario) {
				scenarios.add((Scenario)datum);
			} else {
				logger.warn("Unknown object type "+datum.getClass());
			}
		}
	}