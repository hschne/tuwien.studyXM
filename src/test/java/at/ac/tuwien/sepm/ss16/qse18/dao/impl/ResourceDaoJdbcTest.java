package at.ac.tuwien.sepm.ss16.qse18.dao.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.DaoBaseTest;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Hans-Joerg Schroedl
 */
public class ResourceDaoJdbcTest extends DaoBaseTest {

    private ResourceDaoJdbc resourceDaoJdbc;

    @Before public void setUp() throws Exception {
        super.setUp();
        this.resourceDaoJdbc = new ResourceDaoJdbc();
    }
}
