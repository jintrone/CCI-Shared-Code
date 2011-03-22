package edu.mit.cci.simulation.client;


import edu.mit.cci.simulation.client.model.jaxb.ClientTupleListAdapter;
import org.junit.Assert;
import org.junit.Test;


import java.util.List;

/**
 * @author: jintrone
 * @date: May 18, 2010
 */
public class ClientTupleListAdapterTest {


    @Test
    public void testParser() {
        String data = "[[1,2,3][4,5,6]]";
        List<Tuple> parsed = ClientTupleListAdapter.parse(data);
        Assert.assertEquals("Parsed result is not the correct size",parsed.size(),2);
        Assert.assertEquals("Incorrect tuple length",parsed.get(0).getValues().length,3);
        Assert.assertEquals("Incorrect tuple length", parsed.get(1).getValues().length, 3);

    }
}
