package at.ac.tuwien.sepm.ss16.qse18.dao.impl;

import at.ac.tuwien.sepm.ss16.qse18.domain.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

import at.ac.tuwien.sepm.ss16.qse18.domain.validation.DtoValidator;
import at.ac.tuwien.sepm.ss16.qse18.domain.validation.DtoValidatorException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * This Class tests the DtoValidator.class which is is responsible for verifying DTO's.
 * Created by Julian on 03.06.2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(DtoValidator.class)

public class DtoValidatorTest {

    @Mock private DtoValidator dtoValidator;

    @Before
    public void setUp() throws Exception {
        dtoValidator = new DtoValidator();
    }

    /**
     * This Test calls validate(Question) with null.
     * This should throw an DtoValidatorException.
     * @throws Exception
     */
    @Test(expected = DtoValidatorException.class)
    public void test_validate_withNullShouldThrowException() throws Exception {
        Question q = null;
        dtoValidator.validate(q);
    }

    /**
     * This Test calls validate(Question) with a question which has no question text.
     * This should throw an DtoValidatorException.
     * @throws Exception
     */
    @Test(expected = DtoValidatorException.class)
    public void test_validate_withNoQuestionTextShouldThrowException() throws Exception {
        dtoValidator.validate(new Question(null,QuestionType.MULTIPLECHOICE,1));
    }

    /**
     * This Test calls validate(Question) with a question which has a question text
     * longer than 2000 characters.
     * This should throw an DtoValidatorException.
     * @throws Exception
     */
    @Test(expected = DtoValidatorException.class)
    public void test_validate_withQuestionToLongShouldThrowException() throws Exception {

        String questionText = "";
        for(int i = 0;i<252;i++)
        {
            questionText+= "Question";
        }
        assertTrue("The question text should be > 2000 characters.",questionText.length()>2000);
        dtoValidator.validate(new Question(questionText,QuestionType.MULTIPLECHOICE,1));
    }

    /**
     * This Test calls validate(Question) with an empty String as question text.
     * This should throw an DtoValidatorException.
     * @throws Exception
     */
    @Test(expected = DtoValidatorException.class)
    public void test_validate_withEmptyQuestionStringShouldThrowException() throws Exception {
        dtoValidator.validate(new Question(" ",QuestionType.MULTIPLECHOICE,1));
    }

    /**
     * This Test calls validate(Question) with question time < 0.
     * This should throw an DtoValidatorException.
     * @throws Exception
     */
    @Test(expected = DtoValidatorException.class)
    public void test_validate_withInvalidQuestionTimeShouldThrowException() throws Exception {

        dtoValidator.validate(new Question("question",QuestionType.MULTIPLECHOICE,-1));
    }

    /**
     * This Test calls validate(Question) with question without question type.
     * This should throw an DtoValidatorException.
     * @throws Exception
     */
    @Test(expected = DtoValidatorException.class)
    public void test_validate_withInvalidQuestionTypeShouldThrowException() throws Exception {
        dtoValidator.validate(new Question("question",null,-1));
    }

    /**
     * This Test calls validate(Question) with a valid question.
     * This shouldn't throw an Exception.
     * @throws Exception
     */
    @Test
    public void test_validate_withValidQuestion() throws Exception {
        dtoValidator.validate(new Question("question",QuestionType.MULTIPLECHOICE,1));
    }

    /**
     * This Test calls validate(exercise) with null.
     * This should throw an DtoValidatorException.
     * @throws Exception
     */
    @Test(expected = DtoValidatorException.class)
    public void test_validate_withNullExamShouldThrowException() throws Exception {
        ExerciseExam e = null;
        dtoValidator.validate(e);
    }

    /**
     * This Test calls validate(exercise) with an invalid timestamp.
     * This should throw an DtoValidatorException.
     * @throws Exception
     */
    @Test(expected = DtoValidatorException.class)
    public void test_validateAuthor_withInvalidTimeExamShouldThrowException() throws Exception {

        ExerciseExam e = new ExerciseExam();
        e.setCreated(null);
        e.setAuthor("Julian");
        dtoValidator.validate(e);
    }

    /**
     * This Test calls validate(exercise) with an exam which has no author.
     * This should throw an DtoValidatorException.
     * @throws Exception
     */
    @Test(expected = DtoValidatorException.class)
    public void test_validateAuthor_withNullAuthorExamShouldThrowException() throws Exception {

        ExerciseExam e = new ExerciseExam();
        e.setCreated(new Timestamp(1));
        e.setAuthor(null);
        dtoValidator.validate(e);
    }

    /**
     * This Test calls validate(exercise) with an exam which has an empty author string.
     * This should throw an DtoValidatorException.
     * @throws Exception
     */
    @Test(expected = DtoValidatorException.class)
    public void test_validateAuthor_withEmptyAuthorExamShouldThrowException() throws Exception {

        ExerciseExam e = new ExerciseExam();
        e.setCreated(new Timestamp(1));
        e.setAuthor("");
        assertTrue("The author should be empty.",e.getAuthor().isEmpty());
        dtoValidator.validate(e);
    }

    /**
     * This Test calls validate(exercise) with an exam which has an invalid author string.
     * This should throw an DtoValidatorException.
     * @throws Exception
     */
    @Test(expected = DtoValidatorException.class)
    public void test_validateAuthor_withInvalidAuthorExamShouldThrowException() throws Exception {

        ExerciseExam e = new ExerciseExam();
        e.setCreated(new Timestamp(1));
        e.setAuthor(" ");
        assertFalse("The author shouldn't be empty.",e.getAuthor().isEmpty());
        dtoValidator.validate(e);
    }

    /**
     * This Test calls validate(exercise) with an author name longer than 80 characters.
     * This should throw an DtoValidatorException.
     * @throws Exception
     */
    @Test(expected = DtoValidatorException.class)
    public void test_validateAuthor_withAuthorToLongExamShouldThrowException() throws Exception {

        ExerciseExam e = new ExerciseExam();
        e.setCreated(new Timestamp(1));
        String authorText = "";
        for(int i = 0;i<14;i++)
        {
            authorText+= "Author";
        }
        assertTrue("The author text should be > 80 characters.",authorText.length()>80);
        e.setAuthor(authorText);
        dtoValidator.validate(e);
    }

    /**
     * This Test calls validate(exercise)with an valid exam;
     * This shouldn't throw an Exception.
     * @throws Exception
     */
    @Test
    public void test_validateAuthor_withValidExam() throws Exception {

        ExerciseExam e = new ExerciseExam();
        e.setAuthor("Julian");
        e.setCreated(new Timestamp(1));
        e.setExamTime(10);
        dtoValidator.validate(e);
    }

    /**
     * This Test calls validate(Answers) with null.
     * This should throw an DtoValidatorException.
     * @throws Exception
     */
    @Test(expected = DtoValidatorException.class)
    public void test_validate_withNullAnswersShouldThrowException() throws Exception {

        List<Answer> answers = null;
        dtoValidator.validate(answers);
    }

    /**
     * This Test calls validate(Answers) with an empty answer list.
     * This should throw an DtoValidatorException.
     * @throws Exception
     */
    @Test(expected = DtoValidatorException.class)
    public void test_validate_withEmptyAnswersShouldThrowException() throws Exception {
        dtoValidator.validate(new LinkedList<>());
    }

    /**
     * This Test calls validate(Answers) with one answer which is wrong.
     * This should throw an DtoValidatorException.
     * @throws Exception
     */
    @Test(expected = DtoValidatorException.class)
    public void test_validate_withNoCorrectAnswerShouldThrowException() throws Exception {

        List<Answer> answers = new LinkedList<>();
        Answer wrongAnswer = new Answer();
        wrongAnswer.setCorrect(false);
        wrongAnswer.setAnswer("abc");
        answers.add(wrongAnswer);
        assertFalse("The should be one answer in the List.",answers.isEmpty());
        dtoValidator.validate(answers);
    }

    /**
     * This Test calls validate(Answers) with one answer which is correct.
     * This shouldn't throw an Exception.
     * @throws Exception
     */
    @Test
    public void test_validate_withCorrectAnswer() throws Exception {

        List<Answer> answers = new LinkedList<>();
        Answer wrongAnswer = new Answer();
        wrongAnswer.setAnswer("abc");
        wrongAnswer.setCorrect(true);
        answers.add(wrongAnswer);
        assertFalse("The should be one answer in the List.",answers.isEmpty());
        dtoValidator.validate(answers);
    }

    /**
     * This Test calls validate(Resource) with null.
     * This should throw an DtoValidatorException.
     * @throws Exception
     */
    @Test (expected = DtoValidatorException.class)
    public void test_validate_withNullRescourceShouldThrowException() throws Exception {

        Resource r = null;
        dtoValidator.validate(r);
    }

    /**
     * This Test calls validate(Resource) with an invalid resource type.
     * This should throw an DtoValidatorException.
     * @throws Exception
     */
    @Test (expected = DtoValidatorException.class)
    public void test_validate_withInvalidRescourceTypeShouldThrowException() throws Exception {

        File file = new File("src/main/resources/resources/dummy");
        assertTrue(file.exists());
        assertFalse(file.isDirectory());
        Resource r = new Resource(99, null,"ResourceTest",file.getPath());
        dtoValidator.validate(r);
    }

    /**
     * This Test calls validate(Resource) with an empty resource name.
     * This should throw an DtoValidatorException.
     * @throws Exception
     */
    @Test (expected = DtoValidatorException.class)
    public void test_validate_withEmptyRescourceNameShouldThrowException() throws Exception {

        File file = new File("src/main/resources/resources/dummy");
        assertTrue(file.exists());
        assertFalse(file.isDirectory());
        Resource r = new Resource(99, ResourceType.PDF,"",file.getPath());
        dtoValidator.validate(r);
    }

    /**
     * This Test calls validate(Resource) with an empty resource name.
     * This should throw an DtoValidatorException.
     * @throws Exception
     */
    @Test (expected = DtoValidatorException.class)
    public void test_validate_withEmptyReferenceNameShouldThrowException() throws Exception {
        Resource r = new Resource(99, ResourceType.PDF,"ResourceTest","");
        dtoValidator.validate(r);
    }

    /**
     * This Test calls validate(Resource) with an non existing reference.
     * This should throw an DtoValidatorException.
     * @throws Exception
     */
    @Test (expected = DtoValidatorException.class)
    public void test_validate_withNonExistingReferenceShouldThrowException() throws Exception {

        File file = new File("src/main/resources/resources/notDummy");
        assertFalse(file.exists());
        assertFalse(file.isDirectory());
        Resource r = new Resource(99, ResourceType.PDF,"ResourceTest",file.getPath());
        dtoValidator.validate(r);
    }

    /**
     * This Test calls validate(Resource) with an directory as reference.
     * This should throw an DtoValidatorException.
     * @throws Exception
     */
    @Test (expected = DtoValidatorException.class)
    public void test_validate_withDirecotryAsReferenceShouldThrowException() throws Exception {

        File file = new File("src/main/resources/resources");
        assertTrue(file.exists());
        assertTrue(file.isDirectory());
        Resource r = new Resource(99, ResourceType.PDF,"ResourceTest",file.getPath());
        dtoValidator.validate(r);
    }

    /**
     * This Test calls validate(Resource) with an valid resource.
     * This shouldn't throw an Exception.
     * @throws Exception
     */
    @Test
    public void test_validate_withValidResource() throws Exception {

        File file = new File("src/main/resources/resources/dummy");
        assertTrue(file.exists());
        assertFalse(file.isDirectory());
        Resource r = new Resource(99,ResourceType.PDF,"ResourceTest",file.getPath());
        dtoValidator.validate(r);
    }

    /**
     * This Test calls validate(Resource) with white space as resource name.
     * This should throw an DtoValidatorException.
     * @throws Exception
     */
    @Test (expected = DtoValidatorException.class)
    public void test_validate_withWhiteSpaceAsResourceNameShouldThrowException() throws Exception {

        File file = new File("src/main/resources/resources/dummy");
        assertTrue(file.exists());
        assertFalse(file.isDirectory());
        Resource r = new Resource(99,ResourceType.PDF," ",file.getPath());
        dtoValidator.validate(r);
    }

    @After
    public void tearDown() throws Exception {
        // not needed
    }

}
