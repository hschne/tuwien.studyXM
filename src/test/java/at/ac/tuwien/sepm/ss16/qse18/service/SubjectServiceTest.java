package at.ac.tuwien.sepm.ss16.qse18.service;

import at.ac.tuwien.sepm.ss16.qse18.dao.SubjectDao;
import at.ac.tuwien.sepm.ss16.qse18.dao.impl.SubjectDaoJdbc;
import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import at.ac.tuwien.sepm.ss16.qse18.service.SubjectService;
import at.ac.tuwien.sepm.ss16.qse18.service.impl.SubjectServiceImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import static org.mockito.Mockito.*;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


/**
 * Tests for the subject layer implementation. Mocks were used to verify that the right methods in the subject Dao
 * were called
 *
 * @author  Zhang Haixiang
 */
@RunWith(MockitoJUnitRunner.class) public class SubjectServiceTest {

    @Mock private SubjectServiceImpl mockServiceImpl;
    @Mock private SubjectDaoJdbc mockDaoJdbc;

    private Subject testSubject1;

    @Before
    public void setUp() {
        mockServiceImpl = new SubjectServiceImpl(mockDaoJdbc);

        testSubject1 = new Subject();
        testSubject1.setAuthor("Testauthor");
        testSubject1.setEcts(3);
        testSubject1.setName("SEPM");
        testSubject1.setSemester("SS16");
        testSubject1.setSubjectId(1000);
        testSubject1.setTimeSpent(800);
    }

    //Testing getSubject(int)
    @Test
    public void testIf_getSubject_callsRightMethodInDao() throws Exception{
        mockServiceImpl.getSubject(1);
        verify(mockDaoJdbc).getSubject(1);
    }

    //Testing getSubjects()
    @Test
    public void testIf_getSubjects_callsRightMethodInDao() throws Exception{
        mockServiceImpl.getSubjects();
        verify(mockDaoJdbc).getSubjects();
    }

    //Testing createSubject()
    @Test
    public void testIf_createSubject_callsRightMethodInDao() throws Exception{
        mockServiceImpl.createSubject(testSubject1);
        verify(mockDaoJdbc).createSubject(testSubject1);
    }

    //Testing updateSubject()
    @Test
    public void testIf_updateSubject_callsRightMethodInDao() throws Exception{
        mockServiceImpl.updateSubject(testSubject1);
        verify(mockDaoJdbc).updateSubject(testSubject1);
    }

    //Testing deleteSubject()
    @Test
    public void testIf_deleteSubject_callsRightMethodInDao() throws Exception{
        mockServiceImpl.deleteSubject(testSubject1);
        verify(mockDaoJdbc).deleteSubject(testSubject1);
    }

    @After public void tearDown(){
        //nothing to tear down
    }
}
