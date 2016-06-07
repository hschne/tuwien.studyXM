package at.ac.tuwien.sepm.ss16.qse18.service;

import at.ac.tuwien.sepm.ss16.qse18.domain.Exam;
import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;
import at.ac.tuwien.sepm.ss16.qse18.domain.validation.DtoValidatorException;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by Felix on 05.06.2016.
 */
public interface ExamService {
    Exam getExam(int examID) throws ServiceException;
    List<Exam> getExams() throws ServiceException;
    Exam createExam(Exam exam) throws ServiceException;
    Exam deleteExam(Exam exam) throws ServiceException;
    void validate(Exam exam) throws DtoValidatorException;
    List<Integer> getAllExerciseExamsOfExam(Exam exam) throws ServiceException;

}
