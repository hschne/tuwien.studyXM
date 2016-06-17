package at.ac.tuwien.sepm.ss16.qse18.service.impl.merge;

import at.ac.tuwien.sepm.ss16.qse18.dao.SubjectDao;
import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;
import at.ac.tuwien.sepm.ss16.qse18.domain.export.ExportSubject;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
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

    private SubjectConflictDetection subjectConflictDetection;

    @Before public void setUp() throws Exception {
        subjectConflictDetection = new SubjectConflictDetection();
        subjectConflictDetection.setSubjectDao(subjectDaoMock);
    }

    @Test(expected = ServiceException.class) public void test_getConflictingSubject_throwsExceptionIfNoConflict() throws Exception {
        subjectConflictDetection = new SubjectConflictDetection();
        subjectConflictDetection.getConflictingExistingSubject();
    }

    @Test public void test_conflictExists_NoConflictExists() throws Exception {
        when(subjectDaoMock.getSubjects()).thenReturn(createDummySubjects());

        ExportSubject exportSubject = new ExportSubject(createDummySubject(),null);
        exportSubject.getSubject().setName("Another name");
        boolean result = subjectConflictDetection.conflictExists(exportSubject);

        assertFalse(result);

    }

    @Test public void test_conflictExists_ConflictExists() throws Exception {
        when(subjectDaoMock.getSubjects()).thenReturn(createDummySubjects());

        //create dummySubject creates the same subject contained in create DummySubjects
        ExportSubject importSubject = new ExportSubject(createDummySubject(),null);
        boolean result = subjectConflictDetection.conflictExists(importSubject);

        assertTrue(result);
    }


    @Test public void test_getConflictingSubject_conflictingSubjectReturned() throws Exception {
        Subject existingSubject = createDummySubject();
        List<Subject> subjectList = new ArrayList<>();
        subjectList.add(existingSubject);
        when(subjectDaoMock.getSubjects()).thenReturn(subjectList);

        ExportSubject importSubject = new ExportSubject(createDummySubject(),null);
        subjectConflictDetection.conflictExists(importSubject);
        Subject conflictingSubject = subjectConflictDetection.getConflictingExistingSubject();

        assertEquals(conflictingSubject, existingSubject);
    }

}
