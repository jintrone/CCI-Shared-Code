package edu.mit.cci.simulation.util;

import org.apache.commons.io.IOUtils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * User: jintrone
 * Date: 3/20/11
 * Time: 4:47 PM
 */
public class CSVReader {

    List<String> headers = null;
    Reader reader = null;

    List<String> lines;

    int currLine = 0;

    public CSVReader(String s) throws IOException {
        reader = new FileReader(s);
        lines = IOUtils.readLines(reader);
        reader.close();
        headers = parseLine(0);
    }




    public List<String> parseLine(int idx) {
        if (idx > lines.size()) return Collections.emptyList();
        else {
            String str = lines.get(idx);
           CSVParser parser;
        }
    }
}
