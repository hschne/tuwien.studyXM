package at.ac.tuwien.sepm.ss16.qse18.service.impl;

import at.ac.tuwien.sepm.ss16.qse18.domain.Answer;
import at.ac.tuwien.sepm.ss16.qse18.domain.ExerciseExam;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.QuestionType;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.codec.Base64;
import com.itextpdf.tool.xml.Pipeline;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.css.CssFile;
import com.itextpdf.tool.xml.css.StyleAttrCSSResolver;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.AbstractImageProvider;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static at.ac.tuwien.sepm.ss16.qse18.service.impl.PdfExporterImpl.HtmlConstant.H1_END_TAG;
import static at.ac.tuwien.sepm.ss16.qse18.service.impl.PdfExporterImpl.HtmlConstant.H1_START_TAG;

/**
 * @author Julian on 07.06.2016. This class handles the export of PDF-files.
 */
@Service public class PdfExporterImpl {

    private static final Logger logger = LogManager.getLogger();
    private static final String TMP = "src/main/resources/temporary/tmp.html";
    private static final String BR = "<BR></BR>";
    private static final String BRHR = "<BR></BR><hr></hr>";


    private QuestionServiceImpl questionService;
    private String outPath;
    private ExerciseExam exam;
    private List<Question> questions;
    private Map<Integer, List<Answer>> answers;

    @Autowired public PdfExporterImpl(QuestionServiceImpl questionService) {
        logger.debug("Creating new PDfExporter");
        this.questionService = questionService;
    }

    /**
     * Exports the exam as PDF-file to the specified output path.
     *
     * @param outPath export location
     * @param exam    to be exported as PDF-File
     */
    public void exportPdf(String outPath, ExerciseExam exam) throws ServiceException {
        logger.debug("Exporting exam as PDF-File");

        this.outPath = outPath;
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
        } catch (DocumentException e) {
            logger.error(e);
            throw new ServiceException("Unable to create PDF file. " + e.getMessage());
        } catch (IOException e) {
            logger.error(e);
            throw new ServiceException("Unable to write to PDF file. " + e.getMessage());
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
    private void generatePdf() throws DocumentException, IOException {

        Document document = new Document();
        PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(outPath));
        document.open();

        // HTML
        HtmlPipelineContext htmlContext = new HtmlPipelineContext(null);
        htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());
        htmlContext.setImageProvider(new Base64ImageProvider());

        //CSS
        CSSResolver cssResolver = configureCssResolver();
        Pipeline<?> pipeline = new CssResolverPipeline(cssResolver,
            new HtmlPipeline(htmlContext, new PdfWriterPipeline(document, pdfWriter)));

        // XML Worker
        XMLWorker worker = new XMLWorker(pipeline, true);
        XMLParser xmlParser = new XMLParser(worker);

        //NOTE: Generates Header
        generateHeader(xmlParser);

        //NOTE: Generates Questions
        for (Question question : questions) {
            writeQuestion(xmlParser, question);
        }
        xmlParser.parse(
            new ByteArrayInputStream(("<p style=\"page-break-after:always;\"></p>").getBytes()));

        //NOTE: Generates Solution
        createSolutions(xmlParser);

        XMLWorkerHelper.getInstance().parseXHtml(pdfWriter, document, new FileInputStream(TMP));
        document.close();
    }

    private void createSolutions(XMLParser xmlParser) throws IOException {
        xmlParser.parse(new ByteArrayInputStream(("<p>Solution for this exam: </p>").getBytes()));
        xmlParser.parse(new ByteArrayInputStream((BR).getBytes()));

        for (Question q : questions) {
            xmlParser.parse(new ByteArrayInputStream(
                (H1_START_TAG + "Correct answers for question [ID " + q.getQuestionId()
                    + "]:</h1></p>").getBytes()));
            for (Answer a : answers.get(q.getQuestionId())) {
                if (q.getType() != QuestionType.OPENQUESTION) {
                    xmlParser.parse(new ByteArrayInputStream(
                        ("<p>" + a.getAnswer() + " is " + a.isCorrect() + ".</p>").getBytes()));
                } else {
                    xmlParser.parse(new ByteArrayInputStream(
                        ("<p>" + a.getAnswer() + " is correct Solution.</p>").getBytes()));
                }
            }
            xmlParser.parse(new ByteArrayInputStream((BR).getBytes()));
        }
        xmlParser.parse(new ByteArrayInputStream(
            (H1_START_TAG + "Total points: __/" + questions.size() + H1_END_TAG).getBytes()));
    }

    private void writeQuestion(XMLParser p, Question q) throws IOException {
        String id = "[ID: ";
        if (q.getType() == QuestionType.MULTIPLECHOICE) {
            p.parse(new ByteArrayInputStream(
                (H1_START_TAG + id + q.getQuestionId() + "] " + q.getQuestion() + H1_END_TAG)
                    .getBytes()));
            for (Answer a : answers.get(q.getQuestionId())) {
                p.parse(
                    new ByteArrayInputStream(("<p>" + "[ ] " + a.getAnswer() + "</p>").getBytes()));
            }
            p.parse(new ByteArrayInputStream((BRHR).getBytes()));
        } else if (q.getType() == QuestionType.SINGLECHOICE) {
            p.parse(new ByteArrayInputStream(
                (H1_START_TAG + id + q.getQuestionId() + "] " + q.getQuestion() + H1_END_TAG)
                    .getBytes()));
            for (Answer a : answers.get(q.getQuestionId())) {
                p.parse(
                    new ByteArrayInputStream(("<p>" + "O " + a.getAnswer() + "</p>").getBytes()));
            }
            p.parse(new ByteArrayInputStream((BRHR).getBytes()));
        } else if (q.getType() == QuestionType.OPENQUESTION) {
            p.parse(new ByteArrayInputStream(
                (H1_START_TAG + id + q.getQuestionId() + "] " + q.getQuestion() + H1_END_TAG)
                    .getBytes()));
            p.parse(new ByteArrayInputStream(("<p>answer:_________________</p>").getBytes()));
            p.parse(new ByteArrayInputStream((BRHR).getBytes()));
        } else {
            p.parse(new ByteArrayInputStream(
                (H1_START_TAG + id + q.getQuestionId() + "]</h1></p>").getBytes()));
            p.parse(new ByteArrayInputStream(imageToBase64(q.getQuestion(), 200, 200).getBytes()));
            for (Answer a : answers.get(q.getQuestionId())) {
                p.parse(
                    new ByteArrayInputStream(("<p>" + "[ ] " + a.getAnswer() + "</p>").getBytes()));
            }
            p.parse(new ByteArrayInputStream((BRHR).getBytes()));
        }
    }

    private CSSResolver configureCssResolver() {
        CSSResolver cssResolver = new StyleAttrCSSResolver();
        InputStream csspathtest = Thread.currentThread().getContextClassLoader()
            .getResourceAsStream("src/main/resources/export.css");
        CssFile cssfiletest = XMLWorkerHelper.getCSS(csspathtest);
        cssResolver.addCss(cssfiletest);
        return cssResolver;
    }

    private void generateHeader(XMLParser p) throws IOException {
        p.parse(new ByteArrayInputStream(
            imageToBase64("src/main/resources/icons/logo.png", 55, 30).getBytes()));
        p.parse(new ByteArrayInputStream(
            (H1_START_TAG + "Date: " + exam.getCreated() + H1_END_TAG).getBytes()));
        p.parse(new ByteArrayInputStream(
            (H1_START_TAG + "Author: " + exam.getAuthor() + H1_END_TAG).getBytes()));
        p.parse(new ByteArrayInputStream(
            (H1_START_TAG + "Exam ID: " + exam.getExamid() + H1_END_TAG).getBytes()));
        p.parse(new ByteArrayInputStream(
            (H1_START_TAG + "Estimated time: " + exam.getExamTime() + " minutes" + H1_END_TAG)
                .getBytes()));
        p.parse(new ByteArrayInputStream(
            (H1_START_TAG + "Points reachable: " + questions.size() + H1_END_TAG).getBytes()));
        p.parse(new ByteArrayInputStream((BR).getBytes()));
        p.parse(new ByteArrayInputStream((BR).getBytes()));
    }

    /**
     * Converts an image to base64 string in HTML-Format.
     *
     * @param imgPath image location
     * @param width   with of the image
     * @param height  height of the image
     */
    private String imageToBase64(String imgPath, int width, int height) throws IOException {
        logger.debug("Generating base64 code.");
        FileInputStream inputStream = new FileInputStream(imgPath);
        InputStream bis = new BufferedInputStream(inputStream);

        byte[] imageBytes = new byte[0];
        for (byte[] ba = new byte[bis.available()]; bis.read(ba) != -1; ) {
            byte[] baTmp = new byte[imageBytes.length + ba.length];
            System.arraycopy(imageBytes, 0, baTmp, 0, imageBytes.length);
            System.arraycopy(ba, 0, baTmp, imageBytes.length, ba.length);
            imageBytes = baTmp;
        }
        inputStream.close();
        return "<img src=\"data:image/png;base64," +
            DatatypeConverter.printBase64Binary(imageBytes) +
            "\" width=\"" + width + "\" height=\"" + height + "\"/>";
    }

    /**
     * This class is needed to decode base64 images with iText
     */
    private class Base64ImageProvider extends AbstractImageProvider {
        @Override public Image retrieve(String src) {
            int pos = src.indexOf("base64,");
            try {
                if (src.startsWith("data") && pos > 0) {
                    byte[] img = Base64.decode(src.substring(pos + 7));
                    return Image.getInstance(img);
                } else {
                    return Image.getInstance(src);
                }
            } catch (BadElementException | IOException ex) {
                logger.error(ex);
                return null;
            }
        }

        @Override public String getImageRootPath() {
            return null;
        }
    }


    class HtmlConstant {
        private HtmlConstant() {

        }
        static final String H1_START_TAG = "<p><h1>";
        static final String H1_END_TAG = "</h1></p>";
    }
}
