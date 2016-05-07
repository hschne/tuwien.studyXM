package at.ac.tuwien.sepm.ss16.qse18.service.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.SubjectDao;
import at.ac.tuwien.sepm.ss16.qse18.dao.impl.SubjectDaoJdbc;
import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import at.ac.tuwien.sepm.ss16.qse18.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of the subject service layer. This class contains the business logic and delegates certain
 * methods(CRUD) to the subject Dao
 *
 * @author Zhang Haixiang
 */
@Service public class SubjectServiceImpl implements SubjectService {
    private SubjectDao sd;


    @Autowired public SubjectServiceImpl(SubjectDaoJdbc sd) {
        this.sd = sd;
    }

    @Override public Subject getSubject(int id) throws ServiceException {
        try {
            return sd.getSubject(id);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override public List<Subject> getSubjects() throws ServiceException {
        try {
            return sd.getSubjects();
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override public Subject createSubject(Subject subject) throws ServiceException {

        try {
            sd.createSubject(subject);
            return subject;
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }

    }

    @Override public boolean deleteSubject(Subject subject) throws ServiceException {
        try {
            sd.deleteSubject(subject);
            return true;
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override public Subject updateSubject(Subject subject) throws ServiceException {
        try {
            sd.updateSubject(subject);
            return subject;
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }

    }
}
