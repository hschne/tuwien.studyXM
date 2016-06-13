package at.ac.tuwien.sepm.ss16.qse18.service.merge;

import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.SubjectTopicDao;
import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Hans-Joerg Schroedl
 */
@Service
public class SubjectConflict {

    private static final  Logger logger = LogManager.getLogger();

    private Subject existingSubject;
    private Subject importedSubject;

    private SubjectTopicDao subjectTopicDao;

    @Autowired public void setSubjectTopicDao(SubjectTopicDao subjectTopicDao) {
        this.subjectTopicDao = subjectTopicDao;
    }

    public void setSubjects(Subject existingSubject, Subject importedSubject){
        this.existingSubject = existingSubject;
        this.importedSubject = importedSubject;
    }

    public void resolveConflict() throws ServiceException {

    }



}
