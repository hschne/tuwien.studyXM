package at.ac.tuwien.sepm.ss16.qse18.service.impl.merge;

import at.ac.tuwien.sepm.ss16.qse18.domain.Answer;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;
import at.ac.tuwien.sepm.ss16.qse18.domain.export.ExportQuestion;
import at.ac.tuwien.sepm.ss16.qse18.domain.export.ExportResource;
import at.ac.tuwien.sepm.ss16.qse18.domain.export.ExportTopic;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static at.ac.tuwien.sepm.ss16.qse18.DummyEntityFactory.createDummyQuestions;
import static at.ac.tuwien.sepm.ss16.qse18.DummyEntityFactory.createDummyTopics;

/**
 * @author Hans-Joerg Schroedl
 */
public class DummyExportEntityFactory {

    static List<ExportTopic> createDummyExportTopics() {
        List<Topic> topics = createDummyTopics();
        return topics.stream().map(p -> new ExportTopic(p, createDummyExportQuestions()))
            .collect(Collectors.toList());
    }

    private static List<ExportQuestion> createDummyExportQuestions() {
        List<Question> questions = createDummyQuestions();
        return questions.stream().map(p -> new ExportQuestion(p, null, new ArrayList<Answer>()))
            .collect(Collectors.toList());
    }
}
