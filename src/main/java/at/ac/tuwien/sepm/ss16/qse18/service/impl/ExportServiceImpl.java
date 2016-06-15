package at.ac.tuwien.sepm.ss16.qse18.service.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.*;
import at.ac.tuwien.sepm.ss16.qse18.domain.*;
import at.ac.tuwien.sepm.ss16.qse18.domain.export.ExportQuestion;
import at.ac.tuwien.sepm.ss16.qse18.domain.export.ExportResource;
import at.ac.tuwien.sepm.ss16.qse18.domain.export.ExportSubject;
import at.ac.tuwien.sepm.ss16.qse18.domain.export.ExportTopic;
import at.ac.tuwien.sepm.ss16.qse18.service.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author Felix Almer, Cem Bicer, Philipp Ganiu
 */
@Service public class ExportServiceImpl implements ExportService{
    private static final Logger logger = LogManager.getLogger();

    private Subject subject;
    private List<String> paths = new ArrayList<>();
    @Autowired private TopicService topicService;
    @Autowired private QuestionService questionService;
    @Autowired private ResourceQuestionDao resourceQuestionDao;

    @Override public boolean export(String outputpath) throws ServiceException {
        if(this.subject == null) {
            throw new ServiceException("No subject selected for export.");
        }

        paths.clear();
        boolean success = false;
        ZipOutputStream zipOutputStream = null;

        try {
            zipOutputStream = new ZipOutputStream(new FileOutputStream(outputpath));
            zipOutputStream.putNextEntry(new ZipEntry("image/"));
            zipOutputStream.putNextEntry(new ZipEntry("resource/"));
            zipOutputStream.putNextEntry(new ZipEntry("meta.txt"));
            zipOutputStream.write(generateMeta().getBytes());
            zipOutputStream.closeEntry();
            addToZipFile(null, serializeSubject(), zipOutputStream);
            System.out.println(paths.size());
            for(String s: paths){
                File file = new File("./" + s);
                if(file.exists()) {
                    if(s.contains("src/main/resources/images/")) {
                        addToZipFile("image/" + file.getName(), file.getAbsolutePath(),
                            zipOutputStream);
                    }
                    else {
                        addToZipFile("resource/" + file.getName(), file.getAbsolutePath(),
                            zipOutputStream);
                    }
                }
                else {
                    logger.error("File not found " + s);
                }
            }
            success = true;
        } catch(FileNotFoundException e) {
            logger.error("Path is not valid");
            throw new ServiceException("Outputpath is not valid", e);
        }
        catch (IOException e){
            logger.error("Couldn't create image or resource directory", e);
            throw new ServiceException("Couldn't create image or resource directory", e);
        }finally {
            if(zipOutputStream != null) {
                try {
                    zipOutputStream.close();
                } catch(IOException e) {
                    logger.error(e.getMessage(), e);
                    throw new ServiceException("Could not close stream while writing to file", e);
                }
            }
        }

        logger.debug("Exporting subject: {}", this.subject);
        return success;
    }

    @Override public void setSubject (Subject subject) {
        this.subject = subject;
    }



    private void addToZipFile(String zipPath, String fileName, ZipOutputStream zipOutputStream)
        throws ServiceException {
        logger.debug("Writing to zip file: " + fileName);
        try {
            FileInputStream fis = new FileInputStream(new File(fileName));
            //ZipEntry zipEntry = new ZipEntry(fileName);
            zipOutputStream.putNextEntry(new ZipEntry(zipPath == null ? fileName : zipPath));
            byte[] bytes = new byte[1024];
            int length;
            while ((length = fis.read(bytes)) >= 0) {
                zipOutputStream.write(bytes, 0, length);
            }
            zipOutputStream.closeEntry();
            fis.close();

            if(zipPath == null) {
                File file = new File(fileName);
                file.delete();
            }
        } catch (FileNotFoundException e) {
            logger.error("File " + fileName + " not found", e);
            throw new ServiceException("File " + fileName + " not found", e);
        } catch (IOException e) {
            logger.error("Couldn't write " + fileName + "to zip file", e);
            throw new ServiceException("Couldn't write " + fileName + "to zip file", e);
        }
    }

    private String generateMeta() {
        return "studyXM Subject Export\n\rGenerated: " +
            new SimpleDateFormat("dd.MM.YYYY HH:mm:ss").format(new Date());
    }

    private List<Topic> getTopics() throws ServiceException {
        return topicService.getTopicsFromSubject(this.subject);
    }

    private List<Question> getQuestionsFromTopic(Topic topic) throws ServiceException {
        return questionService.getQuestionsFromTopic(topic);
    }

    private List<Answer> getAnswersFromQuestion(Question question) throws ServiceException {
        return questionService.getCorrespondingAnswers(question);
    }

    private List<Note> getNotesFromResource(Resource resource) {
        return new ArrayList<>(); //TODO
    }

    private Resource getResourcesFromQuestion(Question question) throws ServiceException {
        try {
            return resourceQuestionDao.getResourceOfQuestion(question);
        } catch(DaoException e) {
            logger.error("Could not get resource of question {}", question, e);
            throw new ServiceException("Could not get resource of question.", e);
        }
    }

    private String serializeSubject() throws ServiceException {
        List<Topic> topics = getTopics();
        List<ExportTopic> exportTopics = new ArrayList<>();

        for(Topic t : topics) {
            List<Question> ql = getQuestionsFromTopic(t);
            for(Question q : ql){
                if(q.getType() == QuestionType.NOTECARD){
                    paths.add(q.getQuestion());
                }
            }
            List<ExportQuestion> eql = new ArrayList<>();
            for(Question q : ql) {
                List<Answer> al = getAnswersFromQuestion(q);
                Resource r = getResourcesFromQuestion(q);
                if(r != null){
                    if(!paths.contains(r.getReference())){
                        paths.add(r.getReference());
                    }
                }
                ExportResource er = new ExportResource(r, getNotesFromResource(r));
                eql.add(new ExportQuestion(q, er, al));
            }
            exportTopics.add(new ExportTopic(t, eql));
        }

        ExportSubject exportSubject = new ExportSubject(this.subject, exportTopics);

        try {
            FileOutputStream out = new FileOutputStream("ExportSubject.ser");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(out);
            objectOutputStream.writeObject(exportSubject);
            out.close();
        } catch(IOException e) {
            logger.error("Could not write subject and relations to file", e);
            throw new ServiceException("Could not export subject", e);
        }
        return "ExportSubject.ser";
    }



}
