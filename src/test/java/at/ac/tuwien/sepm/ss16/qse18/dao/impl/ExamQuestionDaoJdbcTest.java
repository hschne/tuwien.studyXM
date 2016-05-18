package at.ac.tuwien.sepm.ss16.qse18.dao.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.ConnectionH2;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;

/**
 * @author Zhang Haixiang
 *
 */
@RunWith(PowerMockRunner.class)@PrepareForTest(ConnectionH2.class) @PowerMockIgnore("javax.management.*")
public class ExamQuestionDaoJdbcTest {
    @Before public void setUp() throws Exception {

    }

    @After public void tearDown() throws Exception {

    }

}
