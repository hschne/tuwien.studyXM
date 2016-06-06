package at.ac.tuwien.sepm.ss16.qse18.service.impl;

/**
 * Implementation of {@link at.ac.tuwien.sepm.ss16.qse18.service.ExamService}
 * <p>
 * Created by Felix on 05.06.2016.
 */
import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.ExamDao;
import at.ac.tuwien.sepm.ss16.qse18.domain.Exam;
import at.ac.tuwien.sepm.ss16.qse18.domain.validation.DtoValidatorException;
import at.ac.tuwien.sepm.ss16.qse18.service.ExamService;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

@Service public class ExamServiceImpl implements ExamService {
    private static final Logger logger = LogManager.getLogger(ExamServiceImpl.class);
    private ExamDao examDao;

    @Autowired public ExamServiceImpl(ExamDao examDao) {
        this.examDao = examDao;
    }

    @Override public Exam getExam(int examID) throws ServiceException {
        logger.debug("entering method getExam with parameter {}", examID);

        if (examID <= 0) {
            logger.error("Service Exception getExam {}", examID);
            throw new ServiceException("Invalid Exam ID, please check your input.");
        }

        try {
            return this.examDao.getExam(examID);
        } catch (DaoException e) {
            logger.error("Could not get Exam from database", e);
            throw new ServiceException("Could not get Exam from database.", e);
        }
    }

    @Override public List<Exam> getExams() throws ServiceException {
        logger.debug("entering method getExams");

        try {
            return this.examDao.getExams();
        } catch (DaoException e) {
            logger.error("Could not fetch list of exams from database", e);
            throw new ServiceException("Could not fetch list of exams from database.", e);
        }
    }

    @Override public Exam createExam(Exam exam) throws ServiceException {
        logger.debug("entering createExam with parameters {}", exam);
        try {
            return this.examDao.create(exam);
        } catch (DaoException e) {
            logger.error("Could not create new exam", e);
            throw new ServiceException("Could not create new exam", e);
        }
    }

    @Override public Exam deleteExam(Exam exam) throws ServiceException {
        //TODO
        return null;
    }

    @Override public void validate(Exam exam) throws DtoValidatorException {

        logger.debug("Validating exam");
        String name = exam.getName();
        if (name.trim().isEmpty()) {
            throw new DtoValidatorException("Name of an exam must not be empty.");
        }
        int subjectId = exam.getSubject();
        if (subjectId <= 0) {
            throw new DtoValidatorException("Invalid subject id. Pleas select another subject");
        }
        Timestamp dueDate = exam.getDueDate();
        if (dueDate == null) {
            throw new DtoValidatorException("Due Date must not be empty.");
        }
        LocalDate date = dueDate.toLocalDateTime().toLocalDate();
        if (date.isBefore(LocalDate.now())) {
            throw new DtoValidatorException("Due date can not be in the past.");
        }

    }
}
