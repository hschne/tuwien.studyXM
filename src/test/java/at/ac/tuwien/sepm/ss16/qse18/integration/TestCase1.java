package at.ac.tuwien.sepm.ss16.qse18.integration;

import at.ac.tuwien.sepm.ss16.qse18.dao.ConnectionH2;
import at.ac.tuwien.sepm.ss16.qse18.dao.DataBaseConnection;
import at.ac.tuwien.sepm.ss16.qse18.dao.impl.SubjectDaoJdbc;
import at.ac.tuwien.sepm.ss16.qse18.dao.impl.SubjectTopicDaoJdbc;
import at.ac.tuwien.sepm.ss16.qse18.dao.impl.TopicDaoJdbc;
import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * This class provides the test case no. 1 which is defined in the test plan
 * <p>
 * Description:
 * Testing the relation between SubjectDao and TopicDao
 *
 * @author Bicer Cem
 */
public class TestCase1 {
    private DataBaseConnection database;
    private SubjectDaoJdbc subjectDao;
    private TopicDaoJdbc topicDao;
    private SubjectTopicDaoJdbc subjectTopicDao;

    private Subject createdSubject;
    private Topic createdTopic;

    @Before public void setUp() throws Exception {
        // setting up database to match precondition
        database = new ConnectionH2();
        database.getConnection().setAutoCommit(false);

        subjectDao = new SubjectDaoJdbc(database);
        topicDao = new TopicDaoJdbc(database);
        subjectTopicDao = new SubjectTopicDaoJdbc(database);

        topicDao.setSubjectTopicDaoJdbc(subjectTopicDao);

        Subject subject = new Subject();
        subject.setName("TestSubject");
        subject.setEcts(6.5f);
        subject.setSemester("SS16");
        subject.setAuthor("TestAuthor");

        createdSubject = subjectDao.createSubject(subject);
        //

        // creating topic and saving it into database
        Topic topic = new Topic();
        topic.setTopic("TestTopic");

        createdTopic = topicDao.createTopic(topic, createdSubject);
        //
    }

    @Test public void test_gettingSubject_FromTopic() throws Exception {
        List<Subject> result = subjectTopicDao.getSubjectsFromTopic(createdTopic);
        boolean found = false;

        for (Subject s : result) {
            if (s.getSubjectId() == createdSubject.getSubjectId()) {
                assertTrue("Subjects with the same ID should have the same name",
                    s.getName().equals(createdSubject.getName()));
                assertTrue("Subjects with the same ID should have the same semester string",
                    s.getSemester().equals(createdSubject.getSemester()));
                assertTrue("Subjects with the same ID should have the same author",
                    s.getAuthor().equals(createdSubject.getAuthor()));
                assertEquals("Subjects with the same ID should have the same timespent values",
                    s.getTimeSpent(), createdSubject.getTimeSpent());
                assertEquals("Subjects with the same ID should have the same ects", s.getEcts(),
                    createdSubject.getEcts(), 0.00001);
                found = true;
            }
        }

        assertTrue("Created subject has to be in the List", found);
    }

    @Test public void test_gettingTopic_FromSubject() throws Exception {
        List<Topic> result = subjectTopicDao.getTopicToSubject(createdSubject);
        boolean found = false;

        for (Topic t : result) {
            if (t.getTopicId() == createdTopic.getTopicId()) {
                assertTrue("Topics with the same ID should have the same topic string",
                    t.getTopic().equals(createdTopic.getTopic()));
            }
        }
    }

    @After public void tearDown() throws Exception {
        // rolling back all changes
        database.getConnection().rollback();
        // closing database connection
        database.closeConnection();
    }
}
