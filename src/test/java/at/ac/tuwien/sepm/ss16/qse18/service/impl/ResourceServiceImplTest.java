package at.ac.tuwien.sepm.ss16.qse18.service.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.ResourceDao;
import at.ac.tuwien.sepm.ss16.qse18.domain.Resource;
import at.ac.tuwien.sepm.ss16.qse18.service.ResourceService;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static at.ac.tuwien.sepm.ss16.qse18.DummyEntityFactory.createDummyResource;
import static at.ac.tuwien.sepm.ss16.qse18.DummyEntityFactory.createDummyResources;
import static org.junit.Assert.assertEquals;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * @author Hans-Joerg Schroedl
 */
@RunWith(MockitoJUnitRunner.class) public class ResourceServiceImplTest {

    @Mock private ResourceDao mockDaoJdbc;

    private ResourceService resourceService;

    @Before public void setUp() {
        resourceService = new ResourceServiceImpl(mockDaoJdbc);
    }

    @Test public void test_getResource_resourceReturned() throws Exception {
        Resource resourceToReturn = createDummyResource();
        when(mockDaoJdbc.getResource(1)).thenReturn(resourceToReturn);
        Resource resource = resourceService.getResource(1);
        assertEquals(resource, resourceToReturn);
    }

    @Test public void test_getResources_resourceReturned() throws Exception {
        List<Resource> resourcesToReturn = createDummyResources();
        when(mockDaoJdbc.getResources()).thenReturn(resourcesToReturn);
        List<Resource> result = resourceService.getResources();
        assertEquals(result, resourcesToReturn);
    }

    @Test(expected = ServiceException.class) public void test_getResource_daoExceptionCaught()
        throws Exception {
        when(mockDaoJdbc.getResource(1)).thenThrow(new DaoException("Error"));
        resourceService.getResource(1);

    }

    @Test(expected = ServiceException.class) public void test_getResources_daoExceptionCaught()
        throws Exception {
        when(mockDaoJdbc.getResources()).thenThrow(new DaoException("Error"));
        resourceService.getResources();

    }


}
