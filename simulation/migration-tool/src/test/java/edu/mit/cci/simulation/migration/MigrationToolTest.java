package edu.mit.cci.simulation.migration;

import java.io.IOException;
import java.util.Locale;

import org.apache.cayenne.BaseContext;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.access.DataContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/META-INF/spring/applicationContext.xml"})
public class MigrationToolTest {
	
	@Test
    @Transactional
    @Rollback(false)
	public void performMigration() throws MigrationException, IOException {
		Locale.setDefault(Locale.US);
		
		ObjectContext context = DataContext.createDataContext();
        BaseContext.bindThreadObjectContext(DataContext.createDataContext());
        
		MigrationTool migrationTool = new MigrationTool();
		migrationTool.doMigrate();
	}
	
}
