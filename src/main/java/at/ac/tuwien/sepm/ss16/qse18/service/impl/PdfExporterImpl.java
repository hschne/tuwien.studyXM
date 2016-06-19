package at.ac.tuwien.sepm.ss16.qse18.service.impl;

import at.ac.tuwien.sepm.ss16.qse18.domain.Answer;
import at.ac.tuwien.sepm.ss16.qse18.domain.ExerciseExam;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.QuestionType;
import at.ac.tuwien.sepm.ss16.qse18.service.QuestionService;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static at.ac.tuwien.sepm.ss16.qse18.service.impl.PdfExporterImpl.HtmlConstant.*;

/**
 * This class handles the export of PDF-files.
 *
 * @author Julian
 */
@Service public class PdfExporterImpl {

    private static final Logger logger = LogManager.getLogger();

    private QuestionService questionService;
    private ExerciseExam exam;
    private List<Question> questions;
    private Map<Integer, List<Answer>> answers;

    private PdfExportWriter pdfExportWriter;

    @Autowired public PdfExporterImpl(QuestionService questionService) {
        logger.debug("Creating new PDfExporter");
        this.questionService = questionService;
    }


    @Autowired public void setPdfExportWriter(PdfExportWriter pdfExportWriter) {
        this.pdfExportWriter = pdfExportWriter;
    }

    /**
     * Exports the exam as PDF-file to the specified output path.
     *
     * @param outPath export location
     * @param exam    to be exported as PDF-File
     */
    public void exportPdf(String outPath, ExerciseExam exam) throws ServiceException {
        logger.debug("Exporting exam as PDF-File");

        pdfExportWriter.setOutPath(outPath);
        this.exam = exam;
        this.questions = this.exam.getExamQuestions();
        answers = new HashMap<>();

        try {
            extractQuestionsAndAnswersFromExam();
            generatePdf();
        } catch (ServiceException e) {
            logger.error(e);
            throw new ServiceException(
                "Unable to extract Information from exam. " + e.getMessage());
        }
    }

    /**
     * Extracts the questions and answers from the exam and stores it in an
     * HashMap<QID,List<Answers>> for fast access.
     */
    private void extractQuestionsAndAnswersFromExam() throws ServiceException {
        logger.debug("Extracting questions and answers from exam.");

        Collections.sort(questions, (q1, q2) -> {
            if (q1.getQuestionId() > q2.getQuestionId()) {
                return 1;
            } else {
                return -1;
            }
        });
        for (Question q : questions) {
            answers.put(q.getQuestionId(), questionService.getCorrespondingAnswers(q));
        }
    }

    /**
     * Exports the exam as PDF-File to the location given by the outPath.
     * Please don't reduce the readability of this method by adding line breaks!
     */
    private void generatePdf() throws ServiceException {

        //NOTE: Generates Header
        generateHeader();

        //NOTE: Generates Questions
        for (Question question : questions) {
            writeQuestion(question);
        }
        pdfExportWriter.write("<p style=\"page-break-after:always;\"></p>");
        createSolutions();

        pdfExportWriter.close();
    }

    private void createSolutions() throws ServiceException {
        pdfExportWriter.write("<p>Solution for this exam: </p>");
        pdfExportWriter.write(BR);

        for (Question q : questions) {
            pdfExportWriter.write(
                H1_START_TAG + "Correct answers for question [ID " + q.getQuestionId()
                    + "]:</h1></p>");
            for (Answer a : answers.get(q.getQuestionId())) {
                if (q.getType() != QuestionType.OPENQUESTION) {
                    pdfExportWriter.write("<p>" + a.getAnswer() + " is " + a.isCorrect() + ".</p>");
                } else {
                    pdfExportWriter.write("<p>" + a.getAnswer() + " is correct Solution.</p>");
                }
            }
            pdfExportWriter.write(BR);
        }
        pdfExportWriter.write(H1_START_TAG + "Total points: __/" + questions.size() + H1_END_TAG);
    }

    private void writeQuestion(Question q) throws ServiceException {
        String id = "[ID: ";
        if (q.getType() == QuestionType.MULTIPLECHOICE) {
            pdfExportWriter
                .write(H1_START_TAG + id + q.getQuestionId() + "] " + q.getQuestion() + H1_END_TAG);
            for (Answer a : answers.get(q.getQuestionId())) {
                pdfExportWriter.write("<p>" + "[ ] " + a.getAnswer() + "</p>");
            }
        } else if (q.getType() == QuestionType.SINGLECHOICE) {
            pdfExportWriter
                .write(H1_START_TAG + id + q.getQuestionId() + "] " + q.getQuestion() + H1_END_TAG);
            for (Answer a : answers.get(q.getQuestionId())) {
                pdfExportWriter.write("<p>" + "O " + a.getAnswer() + "</p>");
            }
        } else if (q.getType() == QuestionType.OPENQUESTION) {
            pdfExportWriter
                .write(H1_START_TAG + id + q.getQuestionId() + "] " + q.getQuestion() + H1_END_TAG);
            pdfExportWriter.write("<p>answer:_________________</p>");
        } else {
            pdfExportWriter.write(H1_START_TAG + id + q.getQuestionId() + "]</h1></p>");
            String base64Image = pdfExportWriter.imageToBase64(q.getQuestion(), 200, 200);
            pdfExportWriter.write(base64Image);

            for (Answer a : answers.get(q.getQuestionId())) {
                pdfExportWriter.write("<p>" + "[ ] " + a.getAnswer() + "</p>");
            }
        }
        pdfExportWriter.write(BRHR);

    }

    private void generateHeader() throws ServiceException {
        String image = pdfExportWriter.imageToBase64("src/main/resources/icons/logo.png", 55, 30);
        pdfExportWriter.write(image);
        pdfExportWriter.write(H1_START_TAG + "Date: " + exam.getCreated() + H1_END_TAG);
        pdfExportWriter.write(H1_START_TAG + "Author: " + exam.getAuthor() + H1_END_TAG);
        pdfExportWriter.write(H1_START_TAG + "Author: " + exam.getAuthor() + H1_END_TAG);
        pdfExportWriter.write(H1_START_TAG + "Exam ID: " + exam.getExamid() + H1_END_TAG);
        pdfExportWriter.write(
            H1_START_TAG + "Estimated time: " + exam.getExamTime() + " minutes" + H1_END_TAG);
        pdfExportWriter.write(H1_START_TAG + "Points reachable: " + questions.size() + H1_END_TAG);
        pdfExportWriter.write(BR);
        pdfExportWriter.write(BR);
    }


    class HtmlConstant {

        static final String BR = "<BR></BR>";
        static final String BRHR = "<BR></BR><hr></hr>";
        static final String H1_START_TAG = "<p><h1>";
        static final String H1_END_TAG = "</h1></p>";

        private HtmlConstant() {
        }
    }
}
