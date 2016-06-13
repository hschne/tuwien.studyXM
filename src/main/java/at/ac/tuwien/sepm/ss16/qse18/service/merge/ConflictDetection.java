package at.ac.tuwien.sepm.ss16.qse18.service.merge;

import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.SubjectDao;
import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author Hans-Joerg Schroedl
 */
@Service public class ConflictDetection {

    private static final Logger logger = LogManager.getLogger();

    private SubjectDao subjectDao;
    private Subject conflictingSubject = null;

    public Subject getConflictingSubject() throws ServiceException {
        if (conflictingSubject == null) {
            throw new ServiceException(
                "No conflicting subject available. Check if a conflict exists first.");
        }
        return conflictingSubject;
    }

    @Autowired public void setSubjectDao(SubjectDao subjectDao) {
        this.subjectDao = subjectDao;
    }

    public boolean conflictExists(Subject importedSubject) throws ServiceException {
        try {
            List<Subject> existingSubjects = subjectDao.getSubjects();
            for (Subject subject : existingSubjects) {
                if (conflictExists(subject, importedSubject)) {
                    return true;
                }
            }
            return false;
        } catch (DaoException e) {
            logger.error(e);
            throw new ServiceException(e.getMessage());
        }
    }

    private boolean conflictExists(Subject existingSubject, Subject importedSubject) {
        String existingName = existingSubject.getName();
        String importedName = importedSubject.getName();
        boolean namesEqual = Objects.equals(existingName, importedName);
        String existingSemester = existingSubject.getSemester();
        String importedSemester = importedSubject.getSemester();
        boolean semestersEqual = Objects.equals(existingSemester, importedSemester);
        if (namesEqual && semestersEqual) {
            conflictingSubject = existingSubject;
            return true;
        }
        return false;
    }

}
