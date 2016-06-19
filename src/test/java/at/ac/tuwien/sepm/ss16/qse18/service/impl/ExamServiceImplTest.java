package at.ac.tuwien.sepm.ss16.qse18.service.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.ExamDao;
import at.ac.tuwien.sepm.ss16.qse18.dao.ExerciseExamDao;
import at.ac.tuwien.sepm.ss16.qse18.domain.Exam;
import at.ac.tuwien.sepm.ss16.qse18.domain.ExerciseExam;
import at.ac.tuwien.sepm.ss16.qse18.domain.validation.DtoValidatorException;
import at.ac.tuwien.sepm.ss16.qse18.service.ExamService;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static at.ac.tuwien.sepm.ss16.qse18.DummyEntityFactory.createDummyExam;
import static at.ac.tuwien.sepm.ss16.qse18.DummyEntityFactory.createDummyExams;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Hans-Joerg Schroedl
 */
@RunWith(MockitoJUnitRunner.class) public class ExamServiceImplTest {

    private ExamService examService;
    @Mock private ExamDao mockExamDao;
    @Mock private ExerciseExamDao mockExerciseExamDao;

    @Before public void setUp() throws Exception {
        examService = new ExamServiceImpl(mockExamDao, mockExerciseExamDao);
    }

    @Test public void getExam_withValidId_examReturned() throws Exception {
        Exam expectedResult = createDummyExam();
        when(mockExamDao.getExam(anyInt())).thenReturn(expectedResult);

        Exam result = examService.getExam(1);

        assertEquals(expectedResult, result);
    }

    @Test(expected = ServiceException.class) public void getExam_invalidId_serviceExceptionThrown()
        throws Exception {
        examService.getExam(-1);
    }

    @Test (expected = ServiceException.class) public void getExam_DaoException() throws Exception {
        when(mockExamDao.getExam(anyInt())).thenThrow(DaoException.class);
        examService.getExam(1);
    }

    @Test public void getExams_examsReturned() throws Exception {
        List<Exam> expectedResult = createDummyExams();
        when(mockExamDao.getExams()).thenReturn(expectedResult);

        List<Exam> result = examService.getExams();

        assertEquals(expectedResult, result);
    }

    @Test(expected = ServiceException.class) public void getExams_notSuccessfull_serviceExceptionThrown() throws Exception {
        when(mockExamDao.getExams()).thenThrow(new DaoException("Error"));

        examService.getExams();
    }

    @Test public void createExam_newExam_successFull() throws Exception {
        Exam exam = createDummyExam();
        examService.createExam(exam);

        verify(mockExamDao).create(exam);
    }

    @Test (expected = ServiceException.class) public void createExam_DaoException() throws Exception {
        when(mockExamDao.create(anyObject())).thenThrow(DaoException.class);
        examService.createExam(new Exam());
    }

    @Test (expected = DtoValidatorException.class) public void validate_invalidName_exceptionThrown() throws Exception {
        Exam exam = createDummyExam();
        exam.setName("");

        examService.validate(exam);
    }

    @Test (expected = DtoValidatorException.class) public void validate_invalidSubject_exceptionThrown() throws Exception {
        Exam exam = createDummyExam();
        exam.setName("Testing");
        exam.setSubject(-1);

        examService.validate(exam);
    }

    @Test (expected = DtoValidatorException.class) public void validate_invalidDate_exceptionThrown() throws Exception {
        Exam exam = createDummyExam();
        exam.setName("Testing");
        exam.setSubject(1);
        LocalDate yesterday = LocalDate.now().minusDays(1);
        exam.setDueDate(Timestamp.valueOf(yesterday.atStartOfDay()));

        examService.validate(exam);
    }

    @Test (expected = DtoValidatorException.class) public void validate_emptyName() throws Exception {
        Exam e = new Exam();
        e.setName(null);
        examService.validate(e);
    }

    @Test (expected = ServiceException.class) public void getAllExerciseExamsOfExam_getExamsFail()
        throws Exception {
        when(mockExerciseExamDao.getExams()).thenThrow(DaoException.class);
        examService.getAllExerciseExamsOfExam(new Exam());
    }

    @Test public void getAllExerciseExamsOfExam_success() throws Exception {
        when(mockExerciseExamDao.getExams()).thenReturn(new ArrayList<>());
        examService.getAllExerciseExamsOfExam(new Exam());
    }
}
