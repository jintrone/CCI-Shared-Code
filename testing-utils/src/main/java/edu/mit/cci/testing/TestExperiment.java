package edu.mit.cci.testing;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Represents simple test scenario that has inputs and outputs. Test scenario
 * should be defined in xml file with format specified by XML Schema delivered
 * with this library. 
 * 
 * @author Janusz Parfieniuk
 *
 */
public class TestExperiment {
	private Map<String, String> inputs = new HashMap<String, String>();
	private Map<String, String> outputs = new HashMap<String, String>();
	private String id;

	/**
	 * Parses specified file for test experiment definitions and returns an array of 
	 * test experiments.
	 * 
	 * @param filePath path to file with experiments definitions
	 * @return array of experiment definitions
	 * @throws TestingUtilsException in case of any error
	 */
	public static TestExperiment[] parseTestExperiments(String filePath) throws TestingUtilsException {

		try {
			File file = new File(filePath);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db;
			db = dbf.newDocumentBuilder();
			Document doc = db.parse(file);
			doc.getDocumentElement().normalize();

			NodeList experimentNodes = doc.getElementsByTagName("experiment");

			TestExperiment[] experiments = new TestExperiment[experimentNodes.getLength()];

			for (int i=0; i < experimentNodes.getLength(); i++) {

				Element experiment = (Element) experimentNodes.item(i);

				TestExperiment testExperiment = new TestExperiment();
				experiments[i] = testExperiment;

				testExperiment.id = experiment.getAttribute("id");

				NodeList inputElements = experiment.getElementsByTagName("input");

				for (int j = 0; j < inputElements.getLength(); j++) {
					Element inputElement = (Element) inputElements.item(j);
					testExperiment.inputs.put(inputElement.getAttribute("name"), inputElement.getTextContent().trim());
				}


				NodeList outputElements = experiment.getElementsByTagName("output");

				for (int j = 0; j < outputElements.getLength(); j++) {
					Element outputElement = (Element) outputElements.item(j);
					testExperiment.outputs.put(outputElement.getAttribute("name"), outputElement.getTextContent().trim());
				}
			}
			return experiments;

		} catch (Exception e) {
			throw new TestingUtilsException(e);
        } 
	}

	/**
	 * Returns expected outputs definitions (name->value pairs).
	 *  
	 * @return map of outputs
	 */
	public Map<String, String> getOutputs() {
		return outputs;
	}
	
	/**
	 * Returns expected outputs definitions (name->value pairs).
	 *  
	 * @return map of inputs
	 */
	public Map<String, String> getInputs() {
		return inputs;
	}

	/**
	 * Returns id of test experiment.
	 *  
	 * @return id of test experiment
	 */
	public String getId() {
		return id;
	}

}
