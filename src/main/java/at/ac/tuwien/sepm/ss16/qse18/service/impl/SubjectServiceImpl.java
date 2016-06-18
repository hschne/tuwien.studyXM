package at.ac.tuwien.sepm.ss16.qse18.service.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.SubjectDao;
import at.ac.tuwien.sepm.ss16.qse18.dao.impl.SubjectDaoJdbc;
import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;
import at.ac.tuwien.sepm.ss16.qse18.domain.validation.DtoValidatorException;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import at.ac.tuwien.sepm.ss16.qse18.service.SubjectService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static at.ac.tuwien.sepm.ss16.qse18.domain.validation.DtoValidator.validateSubject;

/**
 * Class SubjectServiceImpl
 * concrete implementation of SubjectService
 *
 * @author Zhang Haixiang, Hans-Jörg Schrödl
 */
@Service public class SubjectServiceImpl implements SubjectService {
    private static final Logger logger = LogManager.getLogger();
    private SubjectDao sd;

    /**
     * Creates a new subject service implementation object
     *
     * @param sd The subject DAO
     */
    @Autowired public SubjectServiceImpl(SubjectDaoJdbc sd) {
        this.sd = sd;
    }

    @Override public Subject getSubject(int id) throws ServiceException {
        try {
            return sd.getSubject(id);
        } catch (DaoException e) {
            logger.error("Could not get Subject", e);
            throw new ServiceException(e.getMessage());
        }
    }

    @Override public List<Subject> getSubjects() throws ServiceException {
        try {
            return sd.getSubjects();
        } catch (DaoException e) {
            logger.error("Could not get a list of all subjects", e);
            throw new ServiceException(e.getMessage());
        }
    }

    @Override public Subject createSubject(Subject subject) throws ServiceException {
        try {
            validateSubject(subject);
            checkIfDuplicate(subject);
            return sd.createSubject(subject);
        } catch (DtoValidatorException | DaoException e) {
            logger.error("Could not create Subject", e);
            throw new ServiceException(e.getMessage());
        }
    }

    @Override public boolean deleteSubject(Subject subject) throws ServiceException {
        try {
            sd.deleteSubject(subject);
            return true;
        } catch (DaoException e) {
            logger.error("Could not delete Subject", e);
            throw new ServiceException(e.getMessage());
        }
    }

    @Override public Subject updateSubject(Subject subject) throws ServiceException {
        try {
            validateSubject(subject);
            return sd.updateSubject(subject);
        } catch (DtoValidatorException | DaoException e) {
            logger.error("Could not update Subject", e);
            throw new ServiceException(e.getMessage());
        }
    }

    private void checkIfDuplicate(Subject subject) throws ServiceException {
        boolean duplicateExists =
            getSubjects().stream().anyMatch(p -> p.getName().equalsIgnoreCase(subject.getName()));
        if (duplicateExists) {
            throw new ServiceException("Could not create subject '" + subject.getName() +
                "'. Another subject with the same name already exists.");
        }

    }


}
