package at.ac.tuwien.sepm.ss16.qse18.service.impl.merge;

import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.SubjectTopicDao;
import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;
import at.ac.tuwien.sepm.ss16.qse18.domain.export.ExportTopic;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hans-Joerg Schroedl
 */
@Service public class TopicConflictDetection {

    private static final Logger logger = LogManager.getLogger();
    private Subject subject;
    private List<ExportTopic> importedTopics;

    private SubjectTopicDao subjectTopicDao;

    private ApplicationContext applicationContext;

    @Autowired public TopicConflictDetection(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void initialize(Subject subject, List<ExportTopic> importedTopics) {
        this.subject = subject;
        this.importedTopics = importedTopics;
    }

    @Autowired public void setSubjectTopicDao(SubjectTopicDao subjectTopicDao) {
        this.subjectTopicDao = subjectTopicDao;
    }

    public List<TopicConflict> getConflictingTopics() throws ServiceException {
        List<Topic> existingTopics = getExistingTopics();
        List<TopicConflict> duplicates = new ArrayList<>();
        for (Topic existingTopic : existingTopics)
            for (ExportTopic importedTopic : importedTopics) {
                if (checkIfDuplicate(duplicates, existingTopic, importedTopic)) {
                    TopicConflict conflict = applicationContext.getBean(TopicConflict.class);
                    conflict.setTopics(existingTopic, importedTopic);
                    duplicates.add(conflict);
                    break;
                }
            }
        return duplicates;
    }

    private boolean checkIfDuplicate(List<TopicConflict> duplicates, Topic existingTopic,
        ExportTopic importedTopic) {
        String existingTopicName = existingTopic.getTopic();
        String importedTopicName = importedTopic.getTopic().getTopic();
        if (existingTopicName.equals(importedTopicName)) {
            TopicConflict duplicate = new TopicConflict();
            duplicate.setTopics(existingTopic, importedTopic);
            duplicates.add(duplicate);
            return true;
        }
        return false;
    }

    private List<Topic> getExistingTopics() throws ServiceException {
        try {
            return subjectTopicDao.getTopicToSubject(subject);
        } catch (DaoException e) {
            logger.error(e);
            throw new ServiceException("Could not read topics for subject.");
        }
    }

}
