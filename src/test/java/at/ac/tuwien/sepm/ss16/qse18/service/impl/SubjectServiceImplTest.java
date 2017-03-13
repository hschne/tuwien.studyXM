package at.ac.tuwien.sepm.ss16.qse18.service.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.impl.SubjectDaoJdbc;
import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;
import at.ac.tuwien.sepm.ss16.qse18.domain.validation.DtoValidator;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import at.ac.tuwien.sepm.ss16.qse18.service.impl.SubjectServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.core.classloader.annotations.PrepareForTest;

import java.util.ArrayList;
import java.util.List;

import static at.ac.tuwien.sepm.ss16.qse18.DummyEntityFactory.createDummySubject;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Class ExerciseExamServiceImplTest
 * Tests for the sercvice layer in ExerciseExamServiceImpl. In order to be isolated while testing, this
 * test class uses mocks primarily to bypass the database connection procedure.
 *
 * @author Zhang Haixiang
 */
@RunWith(MockitoJUnitRunner.class) @PrepareForTest(DtoValidator.class) public class SubjectServiceImplTest {

    @Mock private SubjectDaoJdbc mockDaoJdbc;

    private SubjectServiceImpl subjectService;

    @Before public void setUp() {
        subjectService = new SubjectServiceImpl(mockDaoJdbc);
    }

    @Test public void testIf_getSubject_callsRightMethodInDao() throws Exception {
        subjectService.getSubject(1);
        verify(mockDaoJdbc).getSubject(1);
    }

    @Test (expected = ServiceException.class) public void testIf_getSubject_daoExceptionThrown() throws Exception {
        when(mockDaoJdbc.getSubject(anyInt())).thenThrow(new DaoException(""));
        subjectService.getSubject(1);
    }

    @Test public void testIf_getSubjects_callsRightMethodInDao() throws Exception {
        subjectService.getSubjects();
        verify(mockDaoJdbc).getSubjects();
    }

    @Test (expected = ServiceException.class) public void testIf_getSubjects_daoExceptionThrown() throws Exception {
        when(mockDaoJdbc.getSubjects()).thenThrow(new DaoException(""));
        subjectService.getSubjects();
    }

    @Test public void testIf_createSubject_callsRightMethodInDao() throws Exception {
        Subject subject = createDummySubject();
        subjectService.createSubject(subject);
        verify(mockDaoJdbc).createSubject(subject);
    }

    @Test (expected = ServiceException.class) public void testIf_createSubject_daoExceptionThrown() throws Exception {
        when(mockDaoJdbc.createSubject(any(Subject.class))).thenThrow(new DaoException(""));
        subjectService.createSubject(createDummySubject());
    }

    @Test(expected = ServiceException.class) public void testIf_createSubject_duplicateSubject() throws Exception {
        Subject subject = createDummySubject();
        List<Subject> existingSubjects = new ArrayList<>();
        existingSubjects.add(subject);
        when(mockDaoJdbc.getSubjects()).thenReturn(existingSubjects);
        subjectService.createSubject(subject);
    }

    @Test(expected = ServiceException.class)
    public void test_createSubject_invalidNameThrowsException() throws Exception {
        Subject subject = createDummySubject();
        subject.setName("");
        subjectService.updateSubject(subject);
    }

    @Test(expected = ServiceException.class)
    public void test_createSubject_invalidEctsThrowsException() throws Exception {
        Subject subject = createDummySubject();
        subject.setEcts(-1);
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

    @Test (expected = ServiceException.class) public void testIf_deleteSubject_daoExceptionThrown() throws Exception {
        when(mockDaoJdbc.deleteSubject(any(Subject.class))).thenThrow(new DaoException(""));
        subjectService.deleteSubject(createDummySubject());
    }


}
