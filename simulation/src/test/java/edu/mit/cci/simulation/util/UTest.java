package edu.mit.cci.simulation.util;

import com.sun.jersey.simple.container.SimpleServerFactory;
import edu.mit.cci.simulation.model.SimulationException;
import edu.mit.cci.simulation.model.Tuple;
import edu.mit.cci.simulation.model.Variable;
import edu.mit.cci.simulation.model.VariableDataOnDemand;
import org.joda.time.Years;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.Closeable;
import java.io.IOException;
import java.util.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Transactional
public class UTest {

    private U u = new U();

    @Autowired
    private VariableDataOnDemand vdod;



    @Test
    public void escape() {
        org.junit.Assert.assertTrue(true);
    }

    @Test
    public void unescape() {
       Assert.assertEquals("5",U.unescape("5;")[0]);
    }

    @Test
    public void executePost() throws IOException {

        List<String> expect = new ArrayList<String>();
        expect.add("a=[b]");
        expect.add("c=[d]");

        Closeable c = new MockHttpServer().run("http://localhost:8080/");
        Map<String,String> params = new HashMap<String,String>();
        params.put("a","b");
        params.put("c","d");
        List<String> result = Arrays.asList(U.executePost("http://localhost:8080/echo", params).split("&"));
        c.close();

        org.junit.Assert.assertTrue(result.containsAll(expect));
        Assert.assertTrue(expect.containsAll(result));
    }

    @Test
    public void parseVariableMap() throws SimulationException {
        Variable one = new Variable("test1","test1",3);
        Variable two = new Variable("test2","test2",3);
        one.persist();
        two.persist();
        StringBuilder builder = new StringBuilder();
        String[] vals1 = {"4", "5", "6"};
        builder.append(one.getId()).append("=").append(U.escape(vals1));
        builder.append("&");
        String[] vals2 = {"7", "8", "9"};
        builder.append(two.getId()).append("=").append(U.escape(vals2));

        Map<Variable,String[]> expected = new HashMap<Variable,String[]>();
        expected.put(one,vals1);
        expected.put(two,vals2);


        List<Tuple> result = U.parseVariableMap(builder.toString());
        Assert.assertNotNull(result);
        for (Tuple t:result) {
            Assert.assertArrayEquals(t.getValues(),expected.get(t.getVar()));
        }

    }

    @Test
    public void stringRepresentationFromTuple() throws SimulationException {
        Variable one = new Variable("test1","test1",3);
        Variable two = new Variable("test2","test2",3);
        one.persist();
        two.persist();
        String[] vals1 = {"4", "5", "6"};
        Tuple t1 = new Tuple(one);
        t1.setValues(vals1);
        String[] vals2 = {"7", "8", "9"};
        Tuple t2 = new Tuple(two);
        t2.setValues(vals2);


        Map<Variable,Tuple> expected = new HashMap<Variable,Tuple>();
        expected.put(one,t1);
        expected.put(two,t2);


        String result = U.createStringRepresentationFromTuple(expected);
        List<Tuple> tester = U.parseVariableMap(result);

        for (Tuple t:tester) {
            Assert.assertArrayEquals(t.getValues(),expected.get(t.getVar()).getValues());
        }

    }

    @Test
    public void mappifyUnmappify() throws Exception {
       Map<String,String> m = new HashMap<String,String>();
       m.put("alsfkjh","as;dfjk");
       m.put("@#)$(%*","W}EOPT{}][><");
       String s = U.stringifyMap(m);
       Map<String,String> m2 = U.mapifyString(s);


        for (Map.Entry<String,String> ent:m.entrySet()) {
             Assert.assertEquals(m2.get(ent.getKey()),ent.getValue());
        }
    }

    @Test
    public void copyRange() throws Exception {
        String[] expect = new String[]{"2","3","1","2","3"};

        Tuple t = new Tuple(new Variable("Test","Test",6));
        t.setValue_("1;2;3;1;2;3");

        Tuple t1 = new Tuple(new Variable("Test","test",5));
        t1.setValue_("4;5;6;");

        U.copyRange(t,t1,1,6);

        Assert.assertArrayEquals(expect,t1.getValues());

    }

    @Test
    public void append() throws Exception {
        String[] expect = new String[]{"1","2","3","1","2","3","4","5","6"};

        Tuple t = new Tuple(new Variable("Test","test",9));
        t.setValue_("1;2;3;1;2;3");

        Tuple t1 = new Tuple(new Variable("Test","test",3));
        t1.setValue_("4;5;6;");

        U.join(t, t1);

        Assert.assertArrayEquals(expect,t.getValues());

    }

    @Test
    public void addToMap() throws Exception {
        String map = "foo=bar&baz=foo&foobaz=barfoo";

        String test = U.updateStringMap("s","r",map);
        Assert.assertEquals(map+"&s=r",test);
        test = U.updateStringMap("s","f",test);
        Assert.assertEquals(map+"&s=f",test);
        test=U.updateStringMap("foo","brr",map);
        Assert.assertEquals("foo=brr&baz=foo&foobaz=barfoo",test);

        test=U.updateStringMap("baz","brr",map);
        Assert.assertEquals("foo=bar&baz=brr&foobaz=barfoo",test);
    }



}


