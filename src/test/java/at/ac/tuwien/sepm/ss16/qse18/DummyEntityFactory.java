package at.ac.tuwien.sepm.ss16.qse18;

import at.ac.tuwien.sepm.ss16.qse18.domain.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Class Dummy EntityFactory
 * contains methods that create test objects of the domain entities
 *
 * @author Hans-Joerg Schroedl
 */
public class DummyEntityFactory {

    public static Subject createDummySubject() {
        Subject s = new Subject();
        s.setSubjectId(1);
        s.setName("TESTING");
        s.setEcts(1.0f);
        s.setSemester("WS10");
        return s;
    }

    public static Topic createDummyTopic() {
        Topic topic = new Topic();
        topic.setTopic("Topic");
        topic.setTopicId(1);
        topic.setNumberOfQuestions("2");
        return topic;
    }

    public static List<Topic> createDummyTopics() {
        List<Topic> topics = new ArrayList<>();
        topics.add(new Topic());
        return topics;
    }


    public static List<Exam> createDummyExams() {
        List<Exam> exams = new ArrayList<>();
        exams.add(new Exam());
        return exams;
    }

    public static Resource createDummyResource() {
        Resource resource = new Resource(1, ResourceType.NOTE, "reference");
        return resource;
    }

    public static List<Resource> createDummyResources() {
        List<Resource> resources = new ArrayList<>();
        resources.add(createDummyResource());
        return resources;
    }
}
