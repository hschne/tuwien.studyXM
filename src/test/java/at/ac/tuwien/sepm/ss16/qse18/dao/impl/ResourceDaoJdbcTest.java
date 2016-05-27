package at.ac.tuwien.sepm.ss16.qse18.dao.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.DaoBaseTest;
import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.domain.Resource;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

import static org.powermock.api.mockito.PowerMockito.when;

/**
 * @author Hans-Joerg Schroedl
 */
public class ResourceDaoJdbcTest extends DaoBaseTest {

    private ResourceDaoJdbc resourceDaoJdbc;

    @Before public void setUp() throws Exception {
        super.setUp();
        this.resourceDaoJdbc = new ResourceDaoJdbc(mockConnectionH2);
    }

    @Test public void test_getResource_resourceReturned() throws Exception {
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getInt("resourceId")).thenReturn(10);
        Resource resource = resourceDaoJdbc.getResource(1);

        assert (resource.getResourceId() == 10);
    }

    @Test(expected = DaoException.class) public void test_getResource_noResourceFound()
        throws Exception {
        when(mockResultSet.next()).thenReturn(false);
        resourceDaoJdbc.getResource(1);
    }

    @Test(expected = DaoException.class) public void test_getResource_sqlExceptionOccurs()
        throws Exception {
        when(mockResultSet.next()).thenThrow(new SQLException());
        resourceDaoJdbc.getResource(1);
    }

    @Test public void test_getResources_resourcesReturned() throws Exception {
        when(mockResultSet.next()).thenReturn(true, true, true, false);
        List<Resource> resource = resourceDaoJdbc.getResources();

        assert (resource.size() == 3);
    }

    @Test(expected = DaoException.class) public void test_getResources_sqlExceptionOccurs()
        throws Exception {
        when(mockResultSet.next()).thenThrow(new SQLException());
        resourceDaoJdbc.getResources();

    }





}
