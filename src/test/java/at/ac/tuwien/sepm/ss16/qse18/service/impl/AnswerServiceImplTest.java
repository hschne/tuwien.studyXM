package at.ac.tuwien.sepm.ss16.qse18.service.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.AnswerDao;
import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.domain.Answer;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.QuestionType;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.when;

/**
 * @author Bicer Cem
 */
@RunWith(MockitoJUnitRunner.class) public class AnswerServiceImplTest {

    private AnswerServiceImpl answerService;
    @Mock private AnswerDao mockAnswerDao;

    @Before public void setUp() throws Exception {
        answerService = new AnswerServiceImpl(mockAnswerDao);
    }

    @Test(expected = ServiceException.class) public void test_getAnswer_withInvalidId_Fail()
        throws Exception {
        when(mockAnswerDao.getAnswer(anyInt())).thenThrow(DaoException.class);

        answerService.getAnswer(-1);
    }

    @Test public void test_getAnswer_withValidId() throws Exception {
        Answer test = new Answer(1, QuestionType.MULTIPLECHOICE, "TestAnswer", true,
            new Question("TestQuestion", QuestionType.MULTIPLECHOICE, 1));

        when(mockAnswerDao.getAnswer(anyInt())).thenReturn(test);

        Answer testResult = mockAnswerDao.getAnswer(1);
        assertEquals("The IDs should equal", test.getAnswerId(), testResult.getAnswerId());
        assertEquals("The answer texts should equal", test.getAnswer(), testResult.getAnswer());
        assertEquals("The question should equal", test.getQuestion(), testResult.getQuestion());
        assertEquals("The questiontype should equal", test.getType(), testResult.getType());
    }

    @Test(expected = ServiceException.class) public void test_getAnswer_withDaoException_Fail()
        throws Exception {
        when(mockAnswerDao.getAnswer()).thenThrow(DaoException.class);

        answerService.getAnswer();
    }

    @Test public void test_getAnswer_goodCase() throws Exception {
        List<Answer> testAnswers = new ArrayList<>();
        testAnswers
            .add(new Answer(1, QuestionType.MULTIPLECHOICE, "Answer1", false, new Question()));
        testAnswers.add(new Answer(2, QuestionType.SINGLECHOICE, "Answer2", true, new Question()));
        testAnswers
            .add(new Answer(3, QuestionType.MULTIPLECHOICE, "Answer3", false, new Question()));

        when(mockAnswerDao.getAnswer()).thenReturn(testAnswers);

        List<Answer> testResult = answerService.getAnswer();
        assertEquals("There should be 3 elements in the list", testResult.size(), 3);
        assertEquals("The first element should have the ID 1", testAnswers.get(0).getAnswerId(), 1);
        assertEquals("The answer text of the second element should be \"Answer2\"",
            testResult.get(1).getAnswer(), "Answer2");
        assertEquals("The third answers type should be \"MULTIPLECHOICE\"",
            testAnswers.get(2).getType(), QuestionType.MULTIPLECHOICE);
    }

    @Test(expected = ServiceException.class) public void test_createAnswer_withNull_Fail()
        throws Exception {
        when(mockAnswerDao.createAnswer(any(Answer.class))).thenThrow(DaoException.class);

        Answer testResult = answerService.createAnswer(null);
    }

    @Test public void test_createAnswer_withValidAnswer() throws Exception {
        Answer testAnswer =
            new Answer(1, QuestionType.MULTIPLECHOICE, "TestAnswer", true, new Question());

        when(mockAnswerDao.createAnswer(any(Answer.class))).thenReturn(testAnswer);

        Answer testResult = answerService.createAnswer(testAnswer);
        assertEquals("The created answer should have the ID 1", testResult.getAnswerId(), 1);
        assertEquals("The created answer should have the answer text \"TestAnswer\"",
            testResult.getAnswer(), "TestAnswer");
    }

    @Test(expected = ServiceException.class) public void test_updateAnswer_withNull_Fail()
        throws Exception {
        when(mockAnswerDao.updateAnswer(any(Answer.class))).thenThrow(DaoException.class);

        answerService.updateAnswer(null);
    }

    @Test public void test_updateAnswer_withValidAnswer() throws Exception {
        Answer testAnswer =
            new Answer(1, QuestionType.MULTIPLECHOICE, "TestAnswer", false, new Question());
        Answer testResultExpected =
            new Answer(1, QuestionType.MULTIPLECHOICE, "TestAnswerNEW", false, new Question());

        when(mockAnswerDao.updateAnswer(any(Answer.class))).thenReturn(testResultExpected);

        Answer testResult = answerService.updateAnswer(testAnswer);
        assertEquals("The answerId of the resulting answer should equal the expected answerId",
            testResult.getAnswerId(), testResultExpected.getAnswerId());
        assertNotEquals("The answer text of the resulting answer should "
                + "differ from the method argument testAnswer", testResult.getAnswer(),
            testAnswer.getAnswer());
        assertEquals(
            "The answer text of the resulting answer should equal the expected answer text",
            testResult.getAnswer(), testResultExpected.getAnswer());
    }

    @Test(expected = ServiceException.class) public void test_deleteAnswer_withNull_Fail()
        throws Exception {
        when(mockAnswerDao.deleteAnswer(any(Answer.class))).thenThrow(DaoException.class);

        answerService.deleteAnswer(null);
    }

    @Test public void test_deleteAnswer_withValidAnswer() throws Exception {
        Answer testAnswer =
            new Answer(1, QuestionType.MULTIPLECHOICE, "TestAnswer", false, new Question());

        when(mockAnswerDao.deleteAnswer(any(Answer.class))).thenReturn(testAnswer);

        Answer testResult = answerService.deleteAnswer(testAnswer);
        assertEquals("The result should equal the method argument", testAnswer.getAnswerId(),
            testResult.getAnswerId());
    }

    @After public void tearDown() throws Exception {
        // nothing to tear down
    }

}
