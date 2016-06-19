package at.ac.tuwien.sepm.ss16.qse18.service.impl;

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
import org.springframework.stereotype.Component;

import javax.xml.bind.DatatypeConverter;
import java.io.*;


/**
 * @author Hans-Joerg Schroedl
 */
@Component public class PdfExportWriter {

    private Logger logger = LogManager.getLogger();
    private XMLParser xmlParser;

    private Document document;

    private PdfWriter pdfWriter;

    private String outPath;

    void setOutPath(String outPath) throws ServiceException {
        this.outPath = outPath;
        try {
            initialize();
        } catch (FileNotFoundException | DocumentException e) {
            logger.error(e);
            throw new ServiceException(e.getMessage());
        }
    }


    void write(String string) throws ServiceException {
        try {
            xmlParser.parse(new ByteArrayInputStream(string.getBytes()));
        } catch (IOException e) {
            logger.error(e);
            throw new ServiceException("Could not write text");
        }
    }

    void close() throws ServiceException {
        try {
            String tempDocument = "src/main/resources/temporary/tmp.html";
            XMLWorkerHelper.getInstance()
                .parseXHtml(pdfWriter, document, new FileInputStream(tempDocument));
        } catch (IOException e) {
            logger.error(e);
            throw new ServiceException("Could not close pdf document.");
        }
        document.close();
    }

    /**
     * Converts an image to base64 string in HTML-Format.
     *
     * @param imgPath image location as path
     * @param width   with of the image
     * @param height  height of the image
     * @return The image converted to a base 64 string
     */
    String imageToBase64(String imgPath, int width, int height) throws ServiceException {
        logger.debug("Generating base64 code.");
        try {
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
        } catch (IOException e) {
            logger.error(e);
            throw new ServiceException("Could not convert image to base 64", e);
        }

    }

    private void initialize() throws FileNotFoundException, DocumentException {
        document = new Document();
        pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(outPath));
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
        xmlParser = new XMLParser(worker);
    }

    private CSSResolver configureCssResolver() {
        CSSResolver cssResolver = new StyleAttrCSSResolver();
        InputStream csspathtest = Thread.currentThread().getContextClassLoader()
            .getResourceAsStream("src/main/resources/export.css");
        CssFile cssfiletest = XMLWorkerHelper.getCSS(csspathtest);
        cssResolver.addCss(cssfiletest);
        return cssResolver;
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


}
