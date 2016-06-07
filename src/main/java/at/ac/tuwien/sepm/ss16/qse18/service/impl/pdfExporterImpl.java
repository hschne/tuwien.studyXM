package at.ac.tuwien.sepm.ss16.qse18.service.impl;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;

import at.ac.tuwien.sepm.ss16.qse18.domain.Exam;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;

/**
 * @author Julian on 07.06.2016. This class handles the export of PDF-files.
 */
@Service
public class PdfExporterImpl {

    private static final String tmp = "/temporary/tmp.html";

    private String outPath;
    private Exam exam;

    private static FileWriter fileWriter;
    private static BufferedWriter bufferedWriter;
    private static Document document;
    private static PdfWriter pdfWriter;

    /**
     * Creates a new PdfExporter
     *
     * @param outPath the output path  for the PDF-file.
     * @param exam    exam which will be converted to a PDF-file.
     * @throws ServiceException won't be thrown,
     */
    public PdfExporterImpl(String outPath, Exam exam) throws ServiceException {
        this.outPath = outPath;
        this.exam = exam;
        try {
            fileWriter = new FileWriter(tmp);
        } catch (IOException e) {
            throw new ServiceException(e.getMessage());
        }
        bufferedWriter = new BufferedWriter(fileWriter);
    }

    /**
     * Exports the exam as PDF-file to the specified output path.
     */
    public void exportPdf() throws ServiceException {
        try {
            generateHtmlFile();
            generatePdf();
        } catch (DocumentException e) {
            throw new ServiceException("Unable to create PDF file. " + e.getMessage());
        } catch (IOException e) {
            throw new ServiceException("Unable to write to PDF file. " + e.getMessage());
        }
    }


    private void generateHtmlFile() throws IOException {

        //TODO: take the exam and extract answers and questions
        //TODO: write to HTML-File by calling writeToHtmlFile(...)
        //NOTE: the questions of Image Questions have to be written by calling writeImageToHtml(...)
        //NOTE: currently the pdf-reader won't display images, however the html file is correct.
        //NOTE: you can check it by using a browser.
        //NOTE: CSS should be used
        bufferedWriter.close();
    }

    /**
     * Writes a given string to the temporary HTML-file.
     *
     * @param s string to be written in the HTML-file.
     */
    private static void writeToHtmlFile(String s) throws IOException {
        bufferedWriter.write("<p>" + s + "</p>");
        bufferedWriter.newLine();
    }

    /**
     * Transforms the temporary HTML-File into a PDF-File and saves it to the output location;
     */
    private void generatePdf() throws DocumentException, IOException {

        document = new Document();
        pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(outPath));

        document.open();
        XMLWorkerHelper.getInstance().parseXHtml(pdfWriter, document, new FileInputStream(tmp));
        document.close();
    }

    /**
     * Writes an image converted to a base64 string in the temporary HTML-file.
     *
     * @param width  with of the image
     * @param height height of the image
     */
    private void writeImageToHtmlFile(String imgPath, int width, int height) throws IOException {

        FileInputStream inputStream = new FileInputStream(imgPath);
        InputStream bis = new BufferedInputStream(inputStream);

        byte[] imageBytes = new byte[0];
        for (byte[] ba = new byte[bis.available()];
             bis.read(ba) != -1; ) {
            byte[] baTmp = new byte[imageBytes.length + ba.length];
            System.arraycopy(imageBytes, 0, baTmp, 0, imageBytes.length);
            System.arraycopy(ba, 0, baTmp, imageBytes.length, ba.length);
            imageBytes = baTmp;
        }
        inputStream.close();
        bufferedWriter.write("<img src=\"data:image/png;base64," +
                DatatypeConverter.printBase64Binary(imageBytes) +
                "\" width=\"" + width + "\" height=\"" + height + "\"/>");
        bufferedWriter.newLine();
    }

    public void setOutPath(String outPath) {
        this.outPath = outPath;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }
}
