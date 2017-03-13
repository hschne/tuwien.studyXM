package at.ac.tuwien.sepm.ss16.qse18.service.impl;

import at.ac.tuwien.sepm.ss16.qse18.domain.Answer;
import at.ac.tuwien.sepm.ss16.qse18.domain.ExerciseExam;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.QuestionType;
import at.ac.tuwien.sepm.ss16.qse18.service.QuestionService;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static at.ac.tuwien.sepm.ss16.qse18.DummyEntityFactory.*;
import static at.ac.tuwien.sepm.ss16.qse18.service.impl.PdfExporterImpl.HtmlConstant.H1_END_TAG;
import static at.ac.tuwien.sepm.ss16.qse18.service.impl.PdfExporterImpl.HtmlConstant.H1_START_TAG;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Hans-Joerg Schroedl
 */
@RunWith(MockitoJUnitRunner.class) public class PdfExporterImplTest {

    @Mock QuestionService questionServiceMock;
    @Mock PdfExportWriter pdfExportWriterMock;
    private PdfExporterImpl pdfExporter;

    @Before public void setUp() {
        pdfExporter = new PdfExporterImpl(questionServiceMock);
        pdfExporter.setPdfExportWriter(pdfExportWriterMock);
    }

    @Test public void test_exportPdf_questionTextWritten() throws Exception {
        ExerciseExam exam = createDummyExerciseExam();
        List<Question> questionList = new ArrayList<>();
        Question question = createDummyQuestion();
        question.setType(QuestionType.SINGLECHOICE);
        questionList.add(question);
        exam.setExamQuestions(questionList);

        pdfExporter.exportPdf("", exam);
        String expectedStringToWrite =
            H1_START_TAG + "[ID: " + question.getQuestionId() + "] " + question.getQuestion()
                + H1_END_TAG;

        verify(pdfExportWriterMock).write(expectedStringToWrite);
    }

    @Test public void test_exportPdf_multipleChoiceAnswersWritten() throws Exception {
        ExerciseExam exam = createExerciseExamWithQuestionType(QuestionType.MULTIPLECHOICE);

        pdfExporter.exportPdf("", exam);
        String expectedStringToWrite = "<p>" + "[ ] " + "Answer" + "</p>";

        verify(pdfExportWriterMock).write(expectedStringToWrite);
    }

    @Test public void test_exportPdf_singleChoiceAnswersWritten() throws Exception {
        ExerciseExam exam = createExerciseExamWithQuestionType(QuestionType.SINGLECHOICE);

        pdfExporter.exportPdf("", exam);
        String expectedStringToWrite = "<p>" + "O " + "Answer" + "</p>";

        verify(pdfExportWriterMock).write(expectedStringToWrite);
    }

    @Test public void test_exportPdf_openAnswersWritten() throws Exception {
        ExerciseExam exam = createExerciseExamWithQuestionType(QuestionType.OPENQUESTION);

        pdfExporter.exportPdf("", exam);
        String expectedStringToWrite = "<p>answer:_________________</p>";

        verify(pdfExportWriterMock).write(expectedStringToWrite);
    }

    @Test public void test_exportPdf_imageAnswersWritten() throws Exception {
        ExerciseExam exam = createExerciseExamWithQuestionType(QuestionType.NOTECARD);

        pdfExporter.exportPdf("", exam);
        String expectedStringToWrite = "<p>" + "[ ] " +"Answer" + "</p>";

        verify(pdfExportWriterMock).imageToBase64(anyString(), eq(200), eq(200));
        verify(pdfExportWriterMock).write(expectedStringToWrite);
    }

    private ExerciseExam createExerciseExamWithQuestionType(QuestionType questionType) throws ServiceException {
        ExerciseExam exam = createDummyExerciseExam();
        List<Question> questionList = new ArrayList<>();
        Question question = createDummyQuestion();
        question.setType(questionType);
        questionList.add(question);
        Answer dummyAnswer = createDummyAnswer();
        List<Answer> answers = new ArrayList<>();
        answers.add(dummyAnswer);
        when(questionServiceMock.getCorrespondingAnswers(question)).thenReturn(answers);
        exam.setExamQuestions(questionList);
        return exam;
    }



}
