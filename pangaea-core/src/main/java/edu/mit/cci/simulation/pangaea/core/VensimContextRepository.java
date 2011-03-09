package edu.mit.cci.simulation.pangaea.core;

import java.util.HashSet;
import java.util.Set;

import com.vensim.Vensim;

/**
 * <p>Singleton that provides vensim context pool that allows geting and releasing contexts. 
 * It works in a way simmilar to how database pools work.
 * </p>
 * 
 * @author Janusz Parfieniuk
 *
 */
public class VensimContextRepository {
	
	private static VensimContextRepository instance = null;
	private Set<Integer> availableContexts = new HashSet<Integer>();
	private Object mutex = new Object();
	
	/**
	 * Max number of open contexts. It shouldn't exceed number of computer processors/cores.
	 */
	public static final int OPEN_CONTEXTS = 16; 
	
	/**
	 * Returns vensim context repositiory instance (in singleton manner).
	 * 
	 * @return context repository
	 * @throws VensimException in case of any error in initialization
	 */
	public static synchronized VensimContextRepository getInstance() throws VensimException {
		if (instance == null) {
			instance = new VensimContextRepository();
		}
		return instance;
	}
	
	/**
	 * Private constructor to prevent instatiation.
	 * 
	 * @throws VensimException in case of any error
	 */
	private VensimContextRepository() throws VensimException {
		// create contexts
		for (int i = 0; i < OPEN_CONTEXTS; i++) {
			int ctx = Vensim.ContextAdd(1);
			if (ctx == 0) {
				throw new VensimException("Can't create new vensim context.");
			}
			availableContexts.add(ctx);
		}
	}
	
	/**
	 * Returns context that can be used. If no free context is available this method
	 * blocks waiting for release of a context.
	 * 
	 * @return context id
	 * @throws VensimException in case of any error
	 */
	public int getContext() throws VensimException {
		synchronized(mutex) {
			if (availableContexts.size() <= 0) {
				try {
	                mutex.wait();
                } catch (InterruptedException e) {
	                throw new VensimException("Can't wait on a mutex in context repository", e);
                }
			}
			int ret = availableContexts.iterator().next();
			availableContexts.remove(ret);
			return ret;
		}
	}
	
	/**
	 * Releases a context by adding it to pool of available contexts. If there is any thread
	 * waiting for free context it will be woken up.
	 * 
	 * @param ctx released context
	 */
	public void releaseContext(int ctx) {
		synchronized (mutex) {
			availableContexts.add(ctx);
			if (availableContexts.size() == 1) {
				// wake up one thread
				mutex.notify();
			}
        }
	}
	
	/**
	 * Returns available contexts count 
	 * 
	 * @return available contexts count
	 */
	public int getAvailableContextsCount() {
		synchronized (mutex) {
			return availableContexts.size();
		}
	}
		

}
