package at.ac.tuwien.sepm.ss16.qse18.service.merge;

import at.ac.tuwien.sepm.ss16.qse18.dao.SubjectTopicDao;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hans-Joerg Schroedl
 */
@Service public class SubjectConflict {

    private TopicConflictDetection topicConflictDetection;
    private TopicConflict topicConflict;
    private Subject existingSubject;

    @Autowired public void setTopicConflict(TopicConflict topicConflict) {
        this.topicConflict = topicConflict;
    }

    @Autowired
    public void setTopicConflictDetection(TopicConflictDetection topicConflictDetection) {
        this.topicConflictDetection = topicConflictDetection;
    }

    public void initialize(Subject existingSubject, Subject importedSubject) {
        this.existingSubject = existingSubject;
    }

    public List<Duplicate<Question>> getConflictingQuestions() throws ServiceException {
        //TODO: Here we need to acquire topics from imported subjects
        List<Topic> importedTopics = new ArrayList<>();
        topicConflictDetection.initialize(existingSubject, importedTopics);
        List<Duplicate<Topic>> duplicateTopics = topicConflictDetection.getConflictingTopics();
        List<Duplicate<Question>> duplicateQuestions = new ArrayList<>();
        for (Duplicate<Topic> duplicateTopic : duplicateTopics) {
            topicConflict.initialize(duplicateTopic);
            duplicateQuestions.addAll(topicConflict.getConflictingQuestions());
        }
        return duplicateQuestions;

    }



}
