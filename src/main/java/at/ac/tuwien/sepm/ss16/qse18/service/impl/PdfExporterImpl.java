package at.ac.tuwien.sepm.ss16.qse18.service.impl;

import at.ac.tuwien.sepm.ss16.qse18.domain.ExerciseExam;
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
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.io.*;

/**
 * @author Julian on 07.06.2016. This class handles the export of PDF-files.
 */
@Service public class PdfExporterImpl {

    private static final Logger logger = LogManager.getLogger();
    private static final String tmp = "src/main/resources/temporary/tmp.html";
    private String outPath;
    private ExerciseExam exam;

    public PdfExporterImpl() {

    }

    /**
     * Creates a new PdfExporter
     *
     * @param outPath the output path  for the PDF-file.
     * @param exam    exam which will be converted to a PDF-file.
     * @throws ServiceException won't be thrown,
     */
    public PdfExporterImpl(String outPath, ExerciseExam exam) throws ServiceException {
        logger.debug("Creating new PdfExporter");
        this.outPath = outPath;
        this.exam = exam;
    }

    /**
     * Exports the exam as PDF-file to the specified output path.
     */
    public void exportPdf() throws ServiceException {
        logger.debug("Exporting PDF-File");
        try {
            generatePdf();
        } catch (DocumentException e) {
            logger.error("Unable to create PDF file.  " + e.getMessage());
            throw new ServiceException("Unable to create PDF file. " + e.getMessage());
        } catch (IOException e) {
            logger.error("Unable to write to PDF file.  " + e.getMessage());
            throw new ServiceException("Unable to write to PDF file. " + e.getMessage());
        }
    }

    /**
     * Exports the exam as PDF-File to the location given by the outPath.
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

        //TODO: Generate the exam - this code is just to showcase the exporter
        /*
        p.parse(new ByteArrayInputStream(
            ("<p>" + "author: " + exam.getAuthor() + " id: " + exam.getExamid() + " date: " + exam
                .getCreated() + "</p>").getBytes()));
        */
        p.parse(new ByteArrayInputStream(imageToBase64("src/main/resources/images/graph.png", 200, 200).getBytes()));
        p.parse(new ByteArrayInputStream(("<p>This is a question.</p>").getBytes()));
        p.parse(new ByteArrayInputStream(("<p>answer 1.</p>").getBytes()));
        p.parse(new ByteArrayInputStream(("<p>answer 2.</p>").getBytes()));
        p.parse(new ByteArrayInputStream(("<p>answer 3.</p>").getBytes()));
        p.parse(new ByteArrayInputStream(("<p>answer 4.</p>").getBytes()));

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
        @Override public Image retrieve(String src) {
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

        @Override public String getImageRootPath() {
            return null;
        }
    }
}
