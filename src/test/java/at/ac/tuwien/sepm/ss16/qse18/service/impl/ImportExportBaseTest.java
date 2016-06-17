package at.ac.tuwien.sepm.ss16.qse18.service.impl;

import at.ac.tuwien.sepm.ss16.qse18.DummyEntityFactory;
import at.ac.tuwien.sepm.ss16.qse18.domain.*;

import java.util.ArrayList;
import java.util.List;

public abstract class ImportExportBaseTest {
    protected Subject testSubject;
    protected List<Topic> testTopics;
    protected List<Question> testQuestions1;
    protected List<Question> testQuestions2;
    protected Resource testResource;
    protected List<Answer> testAnswers1;
    protected List<Answer> testAnswers2;

    protected void createDummySubjectWithDummyRelations() {
        testSubject = DummyEntityFactory.createDummySubject();

        testTopics = new ArrayList<>();
        testTopics.add(new Topic(1, "TestTopic1"));
        testTopics.add(new Topic(2, "TestTopic2"));

        testQuestions1 = new ArrayList<>();
        testQuestions1.add(new Question(1, "TestQuestion1", QuestionType.MULTIPLECHOICE, 1));

        testQuestions2 = new ArrayList<>();
        testQuestions2.add(new Question(2, "TestQuestion2", QuestionType.SINGLECHOICE, 2));

        testResource =
            new Resource(1, ResourceType.PDF, "TestResource", "src/main/resources/resources/dummy");

        testAnswers1 = new ArrayList<>();
        testAnswers1.add(new Answer(1, testQuestions1.get(0).getType(), "TestAnswer1", true,
            testQuestions1.get(0)));
        testAnswers1.add(new Answer(2, testQuestions1.get(0).getType(), "TestAnswer2", false,
            testQuestions1.get(0)));

        testAnswers2 = new ArrayList<>();
        testAnswers2.add(new Answer(3, testQuestions2.get(0).getType(), "TestAnswer3", true,
            testQuestions2.get(0)));
        testAnswers2.add(new Answer(4, testQuestions2.get(0).getType(), "TestAnswer4", false,
            testQuestions2.get(0)));
    }
}
