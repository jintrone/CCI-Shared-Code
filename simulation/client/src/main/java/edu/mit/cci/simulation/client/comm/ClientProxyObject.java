package edu.mit.cci.simulation.client.comm;

import edu.mit.cci.simulation.client.HasId;
import org.apache.log4j.Logger;


import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author: jintrone
 * @date: May 18, 2010
 */
public class ClientProxyObject<T> implements InvocationHandler {


    private T loadedObject = null;
    private Long refid;
    private Class<T> clz;
    private Resolver<T> resolver;

    private static Logger log = Logger.getLogger(ClientProxyObject.class);

    public ClientProxyObject(Class<T> clz, String id, Resolver<T> resolver) {

      this.clz = clz;
      this.refid = Long.parseLong(id);
      this.resolver = resolver;
    }



    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if ("getId".equals(method.getName())) {
            return refid;

                 
        } else if ("hashCode".equals(method.getName())) {
//            if (loadedObject!=null) {
//                return loadedObject.hashCode();
//            } else {
                return proxiedHashCode();
           // }
        } else if ("equals".equals(method.getName())) {
//            if (loadedObject!=null) {
//                return loadedObject.equals(args[0]);
//            } else {
                return proxiedEquals(args[0]);
            //}


        } else {
            T object = getObject();
            return method.invoke(object, args);
        }

    }

    public int proxiedHashCode() {
        return (clz.hashCode() * refid.hashCode())%13;
    }

    public boolean proxiedEquals(Object o) {
        try {
            return (clz.isInstance(o) && clz.getMethod("getId").invoke(o).equals(refid));
        } catch (Exception e) {
          log.warn("Proxied object should, but does not, implement getId: "+o);
        }
        return false;
    }

    private synchronized T getObject()
    {
      if(null == loadedObject)
      {
           RepositoryManager repo = ClientRepository.instance().getManager();
        if (repo ==null) {
            throw new RuntimeException("Client repository must be initialized prior to unmarshalling");
        }
          try {
            loadedObject = resolver.resolve();
          } catch (IOException e) {
              log.error("Could not resolve proxy object for "+refid+":"+clz);

          }
      }
      return loadedObject;
    }

    public static interface Resolver<T> {

        public T resolve() throws IOException;
    }
}
