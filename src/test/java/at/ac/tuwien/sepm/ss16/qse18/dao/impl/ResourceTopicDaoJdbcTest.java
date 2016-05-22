package at.ac.tuwien.sepm.ss16.qse18.dao.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.DaoBaseTest;
import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;
import org.junit.Before;
import org.junit.Test;

import static at.ac.tuwien.sepm.ss16.qse18.DummyEntityFactory.createDummyTopic;
import static org.mockito.Mockito.verify;

/**
 * @author Hans-Joerg Schroedl
 */
public class ResourceTopicDaoJdbcTest extends DaoBaseTest   {

    private ResourceTopicDaoJdbc resourceTopicDaoJdbc;

    @Before public void setUp() throws Exception {
        super.setUp();
        resourceTopicDaoJdbc = new ResourceTopicDaoJdbc(mockConnectionH2);
    }

    @Test(expected = DaoException.class) public void test_removeResourceTopic_InvalidTopicFail()
        throws Exception {
        resourceTopicDaoJdbc.removeResourceTopic(null);
    }

    @Test public void test_removeResourceTopic_preparedStatmentExecuted() throws Exception {
        Topic topic = createDummyTopic();

        resourceTopicDaoJdbc.removeResourceTopic(topic);

        verify(mockPreparedStatement).executeUpdate();
    }
}
