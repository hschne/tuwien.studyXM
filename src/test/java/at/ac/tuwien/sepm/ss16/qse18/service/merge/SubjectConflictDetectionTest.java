package at.ac.tuwien.sepm.ss16.qse18.service.merge;

import at.ac.tuwien.sepm.ss16.qse18.dao.SubjectDao;
import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import at.ac.tuwien.sepm.ss16.qse18.service.impl.SubjectServiceImpl;
import at.ac.tuwien.sepm.ss16.qse18.service.impl.TopicServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static at.ac.tuwien.sepm.ss16.qse18.DummyEntityFactory.createDummySubject;
import static at.ac.tuwien.sepm.ss16.qse18.DummyEntityFactory.createDummySubjects;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * @author Hans-Joerg Schroedl
 */
@RunWith(MockitoJUnitRunner.class) public class SubjectConflictDetectionTest {

    @Mock SubjectDao subjectDaoMock;

    private ConflictDetection conflictDetection;

    @Before public void setUp() throws Exception {
        conflictDetection = new ConflictDetection();
        conflictDetection.setSubjectDao(subjectDaoMock);
    }

    @Test(expected = ServiceException.class) public void test_getConflictingSubject_throwsExceptionIfNoConflict() throws Exception {
        conflictDetection = new ConflictDetection();
        conflictDetection.getConflictingSubject();
    }

    @Test public void test_conflictExists_NoConflictExists() throws Exception {
        when(subjectDaoMock.getSubjects()).thenReturn(createDummySubjects());

        Subject importSubject = createDummySubject();
        importSubject.setName("Another name");
        boolean result =conflictDetection.conflictExists(importSubject);

        assertFalse(result);

    }

    @Test public void test_conflictExists_ConflictExists() throws Exception {
        when(subjectDaoMock.getSubjects()).thenReturn(createDummySubjects());

        //create dummySubject creates the same subject contained in create DummySubjects
        Subject importSubject = createDummySubject();
        boolean result =conflictDetection.conflictExists(importSubject);

        assertTrue(result);
    }


    @Test public void test_getConflictingSubject_conflictingSubjectReturned() throws Exception {
        Subject existingSubject = createDummySubject();
        List<Subject> subjectList = new ArrayList<>();
        subjectList.add(existingSubject);
        when(subjectDaoMock.getSubjects()).thenReturn(subjectList);

        Subject importSubject = createDummySubject();
        conflictDetection.conflictExists(importSubject);
        Subject conflictingSubject = conflictDetection.getConflictingSubject();

        assertEquals(conflictingSubject, existingSubject);
    }

}
