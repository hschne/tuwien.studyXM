package at.ac.tuwien.sepm.ss16.qse18.service.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.ExamDao;
import at.ac.tuwien.sepm.ss16.qse18.dao.impl.ExamDaoJdbc;
import at.ac.tuwien.sepm.ss16.qse18.domain.Exam;
import at.ac.tuwien.sepm.ss16.qse18.service.ExamService;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author Philipp Ganiu
 */
public class ExamServiceImpl implements ExamService {
    private ExamDao examDao;

    @Autowired public ExamServiceImpl(ExamDaoJdbc examDao) {
        this.examDao = examDao;
    }


    @Override public Exam getExam(int id) throws ServiceException {
        //TODO
        return null;
    }

    @Override public List<Exam> getExams() throws ServiceException {
        //TODO
        return null;
    }

    @Override public Exam createExam(Exam exam) throws ServiceException {
        //TODO
        return null;
    }

    @Override public boolean deleteExam(Exam exam) throws ServiceException {
        //TODO
        return false;
    }

    @Override public Exam updateExam(Exam exam) throws ServiceException {
        //TODO
        return null;
    }
}
