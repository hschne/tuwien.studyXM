package at.ac.tuwien.sepm.ss16.qse18.service.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.AnswerDao;
import at.ac.tuwien.sepm.ss16.qse18.dao.ExamDao;
import at.ac.tuwien.sepm.ss16.qse18.service.ExamService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;

/**
 * @author Hans-Joerg Schroedl
 */
@RunWith(MockitoJUnitRunner.class) public class ExamServiceImplTest {

    private ExamService examService;
    @Mock private ExamDao mockExamDao;

    @Before public void setUp() throws Exception {
        examService = new ExamServiceImpl(mockExamDao);
    }

    


}
