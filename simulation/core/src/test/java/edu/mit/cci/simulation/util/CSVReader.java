package edu.mit.cci.simulation.util;

import com.Ostermiller.util.CSVParser;
import org.apache.commons.io.IOUtils;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * User: jintrone
 * Date: 3/20/11
 * Time: 4:47 PM
 */


public class CSVReader implements Iterable<Map<String,String>>{

    List<String> headers = new ArrayList<String>();
    Reader reader = null;

   String[][] lines;



    public CSVReader(String s) throws IOException {
        reader = new FileReader(s);
        lines = CSVParser.parse(reader);
        for (String str:lines[0]) {
           headers.add(str);

        }
    }




    public Map<String,String> readLine(int idx) {

        if (idx < lines.length) {
            Map<String,String> result = new HashMap<String,String>();
            int i = 0;
            for (String s:lines[idx]) {
                result.put(headers.get(i++),s);
            }
            return result;
        }
        else return Collections.emptyMap();
    }

    @Override
    public Iterator<Map<String, String>> iterator() {
        return new Iterator<Map<String,String>>() {

            int currLine = 1;

            @Override
            public boolean hasNext() {
               return currLine < lines.length;
            }

            @Override
            public Map<String, String> next() {
                return readLine(currLine++);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("This iterator does not support the removal of items");
            }
        };
    }
}
