package at.ac.tuwien.sepm.ss16.qse18.service.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.ExamDao;
import at.ac.tuwien.sepm.ss16.qse18.dao.impl.ExamDaoJdbc;
import at.ac.tuwien.sepm.ss16.qse18.domain.Exam;
import at.ac.tuwien.sepm.ss16.qse18.service.ExamService;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import at.ac.tuwien.sepm.util.DTOValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author Zhang Haixiang
 */
public class ExamServiceImpl implements ExamService {
    private ExamDao examDao;
    private static final Logger logger = LogManager.getLogger();


    @Autowired public ExamServiceImpl(ExamDao examDao) {
        this.examDao = examDao;
    }


    @Override public Exam getExam(int examID) throws ServiceException {
        logger.debug("entering method getExam with parameters {}", examID);
        if(examID <= 0){
            logger.error("Service Exception getExam {}", examID);
            throw new ServiceException("Invalid Exam ID, please check your input");
        }

        try {
           return this.examDao.getExam(examID);
        }catch (DaoException e){
            logger.error("Service Exception getExam {}", examID);
            throw new ServiceException(e.getMessage());
        }
    }

    @Override public List<Exam> getExams() throws ServiceException {
        logger.debug("entering method getExams()");
        try{
            return this.examDao.getExams();

        }catch (DaoException e){
            logger.error("Service Exception getExams");
            throw new ServiceException(e.getMessage());
        }
    }

    @Override public Exam createExam(Exam exam) throws ServiceException {
        logger.debug("entering method createExam with parameters {}", exam);
        if(!DTOValidator.validate(exam)){
            logger.error("Service Exception createExam {}", exam);
            throw new ServiceException("Invalid values, please check your input");
        }

        try{
            return this.examDao.create(exam, exam.getExamQuestions());//TODO ändern

        }catch (DaoException e){
            logger.error("Service Exception createExam {}", exam);
            throw new ServiceException(e.getMessage());
        }
    }

    @Override public Exam deleteExam(Exam exam) throws ServiceException {
        logger.debug("entering method deleteExam with parameters {}", exam);
        if(!DTOValidator.validate(exam)){
            logger.error("Service Exception deleteExam {}", exam);
            throw new ServiceException("Invalid values, please check your input");
        }

        try{
            return this.examDao.delete(exam);

        }catch (DaoException e){
            logger.error("Service Exception deleteExam {}", exam);
            throw new ServiceException(e.getMessage());
        }
    }

}
