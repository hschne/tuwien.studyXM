package at.ac.tuwien.sepm.ss16.qse18.service.merge;

import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hans-Joerg Schroedl
 */
@Service public class SubjectConflict {

    private Subject existingSubject;
    private TopicConflictDetection topicConflictDetection;
    private List<TopicConflict> topicConflicts;

    @Autowired
    public void setTopicConflictDetection(TopicConflictDetection topicConflictDetection) {
        this.topicConflictDetection = topicConflictDetection;
    }

    public void initialize(Subject existingSubject, Subject importedSubject) {
        this.existingSubject = existingSubject;
    }

    public List<TopicConflict> getTopicConflicts(){
        return topicConflicts;
    }

    public void getConflictingTopics() throws ServiceException {
        //TODO: Here we need to acquire topics from imported subjects
        List<Topic> importedTopics = new ArrayList<>();
        topicConflictDetection.initialize(existingSubject, importedTopics);
        topicConflicts = topicConflictDetection.getConflictingTopics();
    }

    public List<Topic> getNonConflictingImported() {
        List<Topic> nonConflictingImported = new ArrayList<>();
        topicConflicts.forEach(p -> nonConflictingImported.remove(p.getImportedTopic()));
        return nonConflictingImported;
    }

    public void resolve() throws ServiceException{
        for(TopicConflict topicConflict : topicConflicts){
            topicConflict.resolve();
        }
    }

    public void setTopicConflicts(List<TopicConflict> topicConflicts) {
        this.topicConflicts = topicConflicts;
    }
}
