package at.ac.tuwien.sepm.ss16.qse18.domain.export;

import at.ac.tuwien.sepm.ss16.qse18.domain.Answer;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Felix on 14.06.2016.
 */
public class ExportQuestion implements Serializable {
    private Question question;
    private ExportResource resource;
    private List<Answer> answers;

    public ExportQuestion(Question question, ExportResource resource,  List<Answer> answers) {
        this.resource = resource;
        this.question = question;
        this.answers = answers;
    }

    public ExportResource getResource() {
        return resource;
    }

    public void setResource(ExportResource resource) {
        this.resource = resource;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

}
