package edu.mit.cci.simulation.jaxb;

/**
 * User: jintrone
 * Date: 3/19/11
 * Time: 10:34 AM
 */
public class JaxbUtils {

    public static String RESOLVER_FACTORY_PROPERTY = "edu.mit.cci.jaxb.resolverFactory";


    public static JaxbReferenceResolver resolver;

    public static void reset() {
        String s = System.getProperty(RESOLVER_FACTORY_PROPERTY);
        JaxbReferenceResolver.Factory factory = null;
        if (s != null) {
            try {
                Class<JaxbReferenceResolver.Factory> factoryClass = (Class<JaxbReferenceResolver.Factory>) JaxbUtils.class.getClassLoader().loadClass(s);
                factory = factoryClass.newInstance();

            } catch (ClassNotFoundException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (IllegalAccessException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (InstantiationException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        resolver =  factory.instance();
    }

    public static JaxbReferenceResolver getResolver() {
       if (resolver==null) {
           reset();
       }
        return resolver;
    }
}
