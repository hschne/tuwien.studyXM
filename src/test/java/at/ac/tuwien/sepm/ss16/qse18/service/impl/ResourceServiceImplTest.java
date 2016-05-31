package at.ac.tuwien.sepm.ss16.qse18.service.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.ResourceDao;
import at.ac.tuwien.sepm.ss16.qse18.domain.Resource;
import at.ac.tuwien.sepm.ss16.qse18.domain.validation.DtoValidator;
import at.ac.tuwien.sepm.ss16.qse18.service.ResourceService;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.List;

import static at.ac.tuwien.sepm.ss16.qse18.DummyEntityFactory.createDummyResource;
import static at.ac.tuwien.sepm.ss16.qse18.DummyEntityFactory.createDummyResources;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * @author Hans-Joerg Schroedl
 */
@RunWith(PowerMockRunner.class) public class ResourceServiceImplTest {

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

    @PrepareForTest(DtoValidator.class) @Test
    public void test_createResource_createdResourceReturned() throws Exception {
        PowerMockito.mockStatic(DtoValidator.class);
        Resource expectedResource = createDummyResource();
        when(mockDaoJdbc.createResource(any(Resource.class))).thenReturn(expectedResource);

        Resource resource = resourceService.createResource(new Resource());

        assertEquals(expectedResource, resource);
    }

    @PrepareForTest(DtoValidator.class) @Test(expected = ServiceException.class)
    public void test_createResource_exceptionThrown() throws Exception {
        PowerMockito.mockStatic(DtoValidator.class);
        when(mockDaoJdbc.createResource(any(Resource.class))).thenThrow(DaoException.class);

        resourceService.createResource(new Resource());

    }



}
