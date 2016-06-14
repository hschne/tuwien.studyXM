package at.ac.tuwien.sepm.ss16.qse18.service.merge;

import at.ac.tuwien.sepm.ss16.qse18.dao.QuestionTopicDao;
import at.ac.tuwien.sepm.ss16.qse18.dao.SubjectTopicDao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;

/**
 * @author Hans-Joerg Schroedl
 */
@RunWith(MockitoJUnitRunner.class) public class TopicConflictTest {

    @Mock private QuestionTopicDao questionTopicDao;

    private TopicConflict topicConflict;

    @Before public void setUp(){
        topicConflict = new TopicConflict();
    }

    @Test public void test(){

    }

}
