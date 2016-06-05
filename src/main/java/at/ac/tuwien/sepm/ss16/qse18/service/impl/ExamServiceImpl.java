package at.ac.tuwien.sepm.ss16.qse18.service.impl;

/**
 * Created by Felix on 05.06.2016.
 */
import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.ExamDao;
import at.ac.tuwien.sepm.ss16.qse18.domain.Exam;
import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;
import at.ac.tuwien.sepm.ss16.qse18.service.ExamService;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;

@Service public class ExamServiceImpl implements ExamService {
    private static final Logger logger = LogManager.getLogger(ExamServiceImpl.class);
    private ExamDao examDao;

    @Override public Exam getExam(int examID) throws ServiceException {
        logger.debug("entering method getExam with parameter {}", examID);

        if(examID <= 0) {
            logger.error("Service Exception getExam {}", examID);
            throw new ServiceException("Invalid Exam ID, please check your input");
        }

        try {
            return this.examDao.getExam(examID);
        } catch(DaoException e) {
            logger.error("Could not get Exam from database", e);
            throw new ServiceException("Could not get Exam from database", e);
        }
    }

    @Override public List<Exam> getExams() throws ServiceException {
        logger.debug("entering method getExams");

        try {
            return this.examDao.getExams();
        } catch(DaoException e) {
            logger.error("Could not fetch list of exams from database", e);
            throw new ServiceException("Could not fetch list of exams from database", e);
        }
    }

    @Override public Exam createExam(Exam exam, Subject subject) throws ServiceException {
        logger.debug("entering createExam with parameters {}", exam, subject);

        if(subject == null || exam == null) {
            logger.error("Subject or exam was null, invalid input");
            throw new ServiceException("Subject and Exam must not be null");
        }

        if(subject.getSubjectId() <= 0) {
            logger.error("Subject must be in database to be related to the exam");
            throw new ServiceException("Subject ist not in the database, can not relate exam");
        }

        try {
            return this.examDao.create(exam, subject);
        } catch(DaoException e) {
            logger.error("Could not create new exam", e);
            throw new ServiceException("Could not create new exam", e);
        }
    }

    @Override public Exam deleteExam(Exam exam) throws ServiceException {
        //TODO
        return null;
    }
}
