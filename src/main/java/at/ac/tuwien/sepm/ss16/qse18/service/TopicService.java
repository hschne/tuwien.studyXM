package at.ac.tuwien.sepm.ss16.qse18.service;

import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;

import java.util.List;

public interface TopicService {
    List<Topic> getTopicsFromSubject(int subjectid) throws ServiceException;
}
