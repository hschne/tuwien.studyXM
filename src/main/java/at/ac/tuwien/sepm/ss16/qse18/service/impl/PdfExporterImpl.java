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
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Julian on 07.06.2016. This class handles the export of PDF-files.
 */
@Service
public class PdfExporterImpl {

    private static final Logger logger = LogManager.getLogger();
    private static final String tmp = "src/main/resources/temporary/tmp.html";
    private static final String br = "<br></br>";
    private static final String brhr = "<br></br><hr></hr>";

    private QuestionServiceImpl questionService;
    private String outPath;
    private ExerciseExam exam;
    private List<Question> questions;
    private Map<Integer, List<Answer>> answers;

    @Autowired
    public PdfExporterImpl(QuestionServiceImpl questionService) {
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
            logger.error("Unable to extract Information from exam. " + e.getMessage());
            throw new ServiceException("Unable to extract Information from exam. " + e.getMessage());
        } catch (DocumentException e) {
            logger.error("Unable to create PDF file.  " + e.getMessage());
            throw new ServiceException("Unable to create PDF file. " + e.getMessage());
        } catch (IOException e) {
            logger.error("Unable to write to PDF file.  " + e.getMessage());
            throw new ServiceException("Unable to write to PDF file. " + e.getMessage());
        }
    }

    /**
     * Extracts the questions and answers from the exam and stores it in an
     * HashMap<QID,List<Answers>> for fast access.
     */
    private void extractQuestionsAndAnswersFromExam() throws ServiceException {
        logger.debug("Extracting questions and answers from exam.");

        Collections.sort(questions, new Comparator<Question>() {
            @Override
            public int compare(Question q1, Question q2) {
                if (q1.getQuestionId() > q2.getQuestionId()) {
                    return 1;
                } else {
                    return -1;
                }
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
        CSSResolver cssResolver = new StyleAttrCSSResolver();
        InputStream csspathtest = Thread.currentThread().getContextClassLoader().getResourceAsStream("src/main/resources/export.css");
        CssFile cssfiletest = XMLWorkerHelper.getCSS(csspathtest);
        cssResolver.addCss(cssfiletest);
        Pipeline<?> pipeline = new CssResolverPipeline(cssResolver, new HtmlPipeline(htmlContext, new PdfWriterPipeline(document, pdfWriter)));

        // XML Worker
        XMLWorker worker = new XMLWorker(pipeline, true);
        XMLParser p = new XMLParser(worker);

        //NOTE: Generates Header
        p.parse(new ByteArrayInputStream(imageToBase64("src/main/resources/icons/logo.png", 55, 30).getBytes()));
        p.parse(new ByteArrayInputStream(("<p><h1>" + "Date: " + exam.getCreated() + "</h1></p>").getBytes()));
        p.parse(new ByteArrayInputStream(("<p><h1>" + "Author: " + exam.getAuthor() + "</h1></p>").getBytes()));
        p.parse(new ByteArrayInputStream(("<p><h1>" + "Exam ID: " + exam.getExamid() + "</h1></p>").getBytes()));
        p.parse(new ByteArrayInputStream(("<p><h1>" + "Estimated time: " + exam.getExamTime() + " minutes</h1></p>").getBytes()));
        p.parse(new ByteArrayInputStream(("<p><h1>" + "Points reachable: " + questions.size() + "</h1></p>").getBytes()));
        p.parse(new ByteArrayInputStream((br).getBytes()));
        p.parse(new ByteArrayInputStream((br).getBytes()));

        //NOTE: Generates Questions
        for (Question q : questions) {
            if (q.getType() == QuestionType.MULTIPLECHOICE) {
                p.parse(new ByteArrayInputStream(("<p><h1>" + "[ID: " + q.getQuestionId() + "] " + q.getQuestion() + "</h1></p>").getBytes()));
                for (Answer a : answers.get(q.getQuestionId())) {
                    p.parse(new ByteArrayInputStream(("<p>" + "[ ] " + a.getAnswer() + "</p>").getBytes()));
                }
                p.parse(new ByteArrayInputStream((brhr).getBytes()));
            } else if (q.getType() == QuestionType.SINGLECHOICE) {
                p.parse(new ByteArrayInputStream(("<p><h1>" + "[ID: " + q.getQuestionId() + "] " + q.getQuestion() + "</h1></p>").getBytes()));
                for (Answer a : answers.get(q.getQuestionId())) {
                    p.parse(new ByteArrayInputStream(("<p>" + "O " + a.getAnswer() + "</p>").getBytes()));
                }
                p.parse(new ByteArrayInputStream((brhr).getBytes()));
            } else if (q.getType() == QuestionType.OPENQUESTION) {
                p.parse(new ByteArrayInputStream(("<p><h1>" + "[ID: " + q.getQuestionId() + "] " + q.getQuestion() + "</h1></p>").getBytes()));
                p.parse(new ByteArrayInputStream(("<p>answer:_________________</p>").getBytes()));
                p.parse(new ByteArrayInputStream((brhr).getBytes()));
            } else {
                p.parse(new ByteArrayInputStream(("<p><h1>" + "[ID: " + q.getQuestionId() + "]</h1></p>").getBytes()));
                p.parse(new ByteArrayInputStream(imageToBase64(q.getQuestion(), 200, 200).getBytes()));
                for (Answer a : answers.get(q.getQuestionId())) {
                    p.parse(new ByteArrayInputStream(("<p>" + "[ ] " + a.getAnswer() + "</p>").getBytes()));
                }
                p.parse(new ByteArrayInputStream((brhr).getBytes()));
            }
        }
        p.parse(new ByteArrayInputStream(("<p style=\"page-break-after:always;\"></p>").getBytes()));

        //NOTE: Generates Solution
        p.parse(new ByteArrayInputStream(("<p>Solution for this exam: </p>").getBytes()));
        p.parse(new ByteArrayInputStream((br).getBytes()));
        for (Question q : questions) {
            p.parse(new ByteArrayInputStream(("<p><h1>" + "Correct answers for question [ID " + q.getQuestionId() + "]:</h1></p>").getBytes()));
            for (Answer a : answers.get(q.getQuestionId())) {
                if (q.getType() != QuestionType.OPENQUESTION) {
                    p.parse(new ByteArrayInputStream(("<p>" + a.getAnswer() + " is " + a.isCorrect() + ".</p>").getBytes()));
                } else {
                    p.parse(new ByteArrayInputStream(("<p>" + a.getAnswer() + " is correct Solution.</p>").getBytes()));
                }
            }
            p.parse(new ByteArrayInputStream((br).getBytes()));
        }
        p.parse(new ByteArrayInputStream(("<p><h1>" + "Total points: __/" + questions.size() + "</h1></p>").getBytes()));

        XMLWorkerHelper.getInstance().parseXHtml(pdfWriter, document, new FileInputStream(tmp));
        document.close();
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
    static class Base64ImageProvider extends AbstractImageProvider {
        @Override
        public Image retrieve(String src) {
            int pos = src.indexOf("base64,");
            try {
                if (src.startsWith("data") && pos > 0) {
                    byte[] img = Base64.decode(src.substring(pos + 7));
                    return Image.getInstance(img);
                } else {
                    return Image.getInstance(src);
                }
            } catch (BadElementException ex) {
                return null;
            } catch (IOException ex) {
                return null;
            }
        }

        @Override
        public String getImageRootPath() {
            return null;
        }
    }
}
