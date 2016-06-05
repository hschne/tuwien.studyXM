package at.ac.tuwien.sepm.ss16.qse18.service.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.impl.SubjectDaoJdbc;
import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import at.ac.tuwien.sepm.ss16.qse18.service.impl.SubjectServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

/**
 * Class ExerciseExamServiceImplTest
 * Tests for the sercvice layer in ExamServiceImpl. In order to be isolated while testing, this
 * test class uses mocks primarily to bypass the database connection procedure.
 *
 * @author Zhang Haixiang
 */
@RunWith(MockitoJUnitRunner.class) public class SubjectServiceImplTest {

    @Mock private SubjectDaoJdbc mockDaoJdbc;

    private SubjectServiceImpl subjectService;

    @Before public void setUp() {
        subjectService = new SubjectServiceImpl(mockDaoJdbc);

    }

    //Testing getSubject(int)
    //----------------------------------------------------------------------------------------------
    @Test public void testIf_getSubject_callsRightMethodInDao() throws Exception {
        subjectService.getSubject(1);
        verify(mockDaoJdbc).getSubject(1);
    }

    @Test public void testIf_getSubjects_callsRightMethodInDao() throws Exception {
        subjectService.getSubjects();
        verify(mockDaoJdbc).getSubjects();
    }

    @Test public void testIf_createSubject_callsRightMethodInDao() throws Exception {
        Subject subject = createDummySubject();
        subjectService.createSubject(subject);
        verify(mockDaoJdbc).createSubject(subject);
    }

    @Test(expected = ServiceException.class)
    public void test_createSubject_invalidNameThrowsException() throws Exception {
        Subject subject = createDummySubject("", 3.0f);
        subjectService.updateSubject(subject);
    }

    @Test(expected = ServiceException.class)
    public void test_createSubject_invalidEctsThrowsException() throws Exception {
        Subject subject = createDummySubject("SEPM", -3.0f);
        subjectService.updateSubject(subject);
    }

    @Test public void testIf_updateSubject_callsRightMethodInDao() throws Exception {
        Subject subject = createDummySubject();
        subjectService.updateSubject(subject);
        verify(mockDaoJdbc).updateSubject(subject);
    }

    @Test public void testIf_deleteSubject_callsRightMethodInDao() throws Exception {
        Subject subject = createDummySubject();
        subjectService.deleteSubject(subject);
        verify(mockDaoJdbc).deleteSubject(subject);
    }
    //----------------------------------------------------------------------------------------------

    private Subject createDummySubject() {
        return createDummySubject("SEPM", 6.0f);
    }

    private Subject createDummySubject(String name, float ects) {
        Subject subject = new Subject();
        subject.setName(name);
        subject.setEcts(ects);
        subject.setAuthor("Author");
        subject.setSemester("SS16");
        subject.setSubjectId(1000);
        subject.setTimeSpent(800);
        return subject;
    }
}
