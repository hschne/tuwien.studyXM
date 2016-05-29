package at.ac.tuwien.sepm.ss16.qse18.service.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.SubjectDao;
import at.ac.tuwien.sepm.ss16.qse18.dao.impl.SubjectDaoJdbc;
import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import at.ac.tuwien.sepm.ss16.qse18.service.SubjectService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * Class SubjectServiceImpl
 * concrete implementation of SubjectService
 *
 * @author Zhang Haixiang
 */
@Service public class SubjectServiceImpl implements SubjectService {
    private final Logger logger = LogManager.getLogger(this.getClass());
    private SubjectDao sd;

    @Autowired public SubjectServiceImpl(SubjectDaoJdbc sd) {
        this.sd = sd;
    }

    @Override public Subject getSubject(int id) throws ServiceException {
        try {
            return sd.getSubject(id);
        } catch(DaoException e) {
            logger.error("Could not get Subject", e);
            throw new ServiceException(e.getMessage());
        }
    }

    @Override public List<Subject> getSubjects() throws ServiceException {
        try {
            return sd.getSubjects();
        } catch(DaoException e) {
            logger.error("Could not get a list of all subjects", e);
            throw new ServiceException(e.getMessage());
        }
    }

    @Override public Subject createSubject(Subject subject) throws ServiceException {
        verifyCreate(subject);
        verifyUpdate(subject);
        try {
            return  sd.createSubject(subject);
        } catch(DaoException e) {
            logger.error("Could not create Subject", e);
            throw new ServiceException(e.getMessage());
        }

    }

    @Override public boolean deleteSubject(Subject subject) throws ServiceException {
        try {
            sd.deleteSubject(subject);
            return true;
        } catch(DaoException e) {
            logger.error("Could not delete Subject", e);
            throw new ServiceException(e.getMessage());
        }
    }

    @Override public Subject updateSubject(Subject subject) throws ServiceException {
        verifyUpdate(subject);
        try {
            return  sd.updateSubject(subject);
        } catch(DaoException e) {
            logger.error("Could not update Subject", e);
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * verifyCreate
     * verifies whether the subject is valid or not
     * @param subject the subject that should be created
     * @throws ServiceException
     *
     * */
    private void verifyCreate(Subject subject) throws ServiceException {
        if (getSubjects().stream().anyMatch(p -> Objects.equals(p.getName(), subject.getName()))) {
            throw new ServiceException("Subject name already taken");
        }
    }

    /**
     * verifyUpdate
     * verifies whether the subject is valid or not
     * @param subject the subject that should be updated
     * @throws ServiceException
     *
     * */
    private void verifyUpdate(Subject subject) throws ServiceException {
        if(subject.getName().isEmpty()) {
            throw new ServiceException("Subject name must not be empty");
        }
        if(subject.getEcts() < 0) {
            throw new ServiceException("ECTS cannot be negative");
        }
    }
}
