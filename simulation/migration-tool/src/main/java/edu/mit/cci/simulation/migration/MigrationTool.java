package edu.mit.cci.simulation.migration;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import mit.simulation.climate.model.Simulation;
import mit.simulation.climate.model.persistence.ServerRepository;

import org.apache.cayenne.BaseContext;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.access.DataContext;
import org.hibernate.Session;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import edu.mit.cci.simulation.model.DefaultSimulation;
import edu.mit.cci.simulation.model.Tuple;

public class MigrationTool {
	static ServerRepository repo;
	public static void main(String[] args) {
		ObjectContext context = DataContext.createDataContext();
        BaseContext.bindThreadObjectContext(DataContext.createDataContext());
		repo = ServerRepository.instance();
		
		Collection<Simulation> sims = repo.getAllSimulations();
		System.out.println(sims.size());
		ClassPathXmlApplicationContext springCtx = new ClassPathXmlApplicationContext("META-INF/spring/applicationContext.xml");
		/*
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistenceUnit");
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		Object x = session.get(edu.mit.cci.simulation.model.Simulation.class, 1L);
		System.out.println(x);
		*/
		//edu.mit.cci.simulation.model.Simulation
		
		for (String name: springCtx.getBeanDefinitionNames()) {
			System.out.println(name);
		}
		EntityManagerFactory emf = springCtx.getBean(EntityManagerFactory.class);

		//EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistenceUnit");
		Object x = emf.createEntityManager().find(DefaultSimulation.class, 1L);
		System.out.println(DefaultSimulation.findAllDefaultSimulations().size());
		
		//System.out.println(DefaultSimulation.findAllDefaultSimulations());
		
	}

}
