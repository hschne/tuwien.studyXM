package at.ac.tuwien.sepm.ss16.qse18.service.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.SubjectTopicDao;
import at.ac.tuwien.sepm.ss16.qse18.dao.TopicDao;
import at.ac.tuwien.sepm.ss16.qse18.dao.impl.TopicDaoJdbc;
import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import at.ac.tuwien.sepm.ss16.qse18.service.SubjectTopicService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Philipp Ganiu
 */
@Service
public class SubjectTopicServiceImpl implements SubjectTopicService {
    private final Logger logger = LogManager.getLogger(this.getClass());
    private SubjectTopicDao subjectTopicDao;

    @Autowired
    public SubjectTopicServiceImpl(SubjectTopicDao subjectTopicDao) {
        this.subjectTopicDao = subjectTopicDao;
    }
    @Override
    public List<Topic> getTopicToSubject(Subject subject) throws ServiceException {
        try{
            return subjectTopicDao.getTopicToSubject(subject);
        }
        catch (DaoException e){
            logger.error(e);
            throw new ServiceException(e.getMessage());
        }
    }
}
