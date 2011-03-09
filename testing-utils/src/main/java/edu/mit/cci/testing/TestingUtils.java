package edu.mit.cci.testing;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Utility class that provides useful methods helpful when doing tests.
 * 
 * @author Janusz Parfieniuk
 * 
 */
public class TestingUtils {
	private TestingUtils() {
	}

	/**
	 * Load specified properties file into system properties (they will be
	 * accessible with System.getProperty(). It is similar to setting -D
	 * arguments on command line.
	 * 
	 * @param filePath
	 *            path to properties file
	 * @throws TestingUtilsException
	 *             in case of an error with file access
	 */
	public static void loadPropertiesToSystem(String filePath) throws TestingUtilsException {
		try {
			Properties properties = new Properties();

			FileInputStream fis = new FileInputStream(filePath);

			properties.load(fis);
			fis.close();

			for (Object property : properties.keySet()) {
				System.setProperty(property.toString(), properties.getProperty(property.toString()));
			}
		} catch (IOException e) {
			throw new TestingUtilsException("Can't read properties file " + filePath, e);
		}
	}

}
