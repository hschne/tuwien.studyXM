package at.ac.tuwien.sepm.ss16.qse18.service.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.*;
import at.ac.tuwien.sepm.ss16.qse18.domain.*;
import at.ac.tuwien.sepm.ss16.qse18.domain.export.ExportQuestion;
import at.ac.tuwien.sepm.ss16.qse18.domain.export.ExportSubject;
import at.ac.tuwien.sepm.ss16.qse18.domain.export.ExportTopic;
import at.ac.tuwien.sepm.ss16.qse18.service.ImportService;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import at.ac.tuwien.sepm.ss16.qse18.service.impl.merge.SubjectConflict;
import at.ac.tuwien.sepm.ss16.qse18.service.impl.merge.SubjectConflictDetection;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Implementation of {@link ImportService}. This implementation reads from a zip file and
 * imports the contained subject into the database.
 *
 */
@Service public class ImportServiceImpl implements ImportService {

    private static final Logger logger = LogManager.getLogger();
    private static final String OUTPUT_PATH = "src/main/resources/import";
    private static final String IMAGE_PATH = "src/main/resources/images";
    private static final String RESOURCE_PATH = "src/main/resources/resources";
    private Map<String,String> resources = new HashMap<>();
    @Autowired ApplicationContext applicationContext;
    private String unzippedDir = "";
    @Autowired private SubjectConflictDetection subjectConflictDetection;
    @Autowired private SubjectConflict conflict;
    @Autowired private SubjectDao subjectDao;
    @Autowired private SubjectTopicDao subjectTopicDao;
    @Autowired private TopicDao topicDao;
    @Autowired private QuestionDao questionDao;
    @Autowired private ResourceDao resourceDao;
    @Autowired private AnswerDao answerDao;
    @Autowired private ImportUtil importUtil;

    @Autowired
    public void setSubjectConflictDetection(SubjectConflictDetection subjectConflictDetection) {
        this.subjectConflictDetection = subjectConflictDetection;
    }

    @Override public SubjectConflict importSubject(File zipFile) throws ServiceException {
        logger.debug("Importing subject from file " + zipFile);

        unzipFile(zipFile.getAbsolutePath(), zipFile.getName());
        ExportSubject subject = deserialize();

        if (subjectConflictDetection.conflictExists(subject)) {
            Subject conflictingSubject = subjectConflictDetection.getConflictingExistingSubject();
            if (existingSubjectEmpty(conflictingSubject)) {
                logger.debug("Existing subject {} is empty, importing all topics", conflictingSubject);
                List<ExportTopic> topics = subject.getTopics();
                for (ExportTopic topic : topics) {
                    importTopic(topic, conflictingSubject);
                }
                return null;
            }
            conflict.setSubjects(conflictingSubject, subject);
            checkIfImportedDuplicate(conflictingSubject);
            return conflict;
        }
        else if(unzippedDir != null){
            File imageFiles = new File(unzippedDir + File.separator + "image/");
            File resourceFiles = new File(unzippedDir + File.separator + "resource/");

            copyFile(imageFiles, IMAGE_PATH);
            copyFile(resourceFiles, RESOURCE_PATH);
        }

        setDatabaseAutocommit(false);
        try {
            importSubject(subject);
        } catch (ServiceException e) {
            rollbackChanges();
            throw e;
        }
        setDatabaseAutocommit(true);
        return null;
    }

    @Override public void importSubject(ExportSubject exportSubject) throws ServiceException {
        Subject subject = createSubject(exportSubject.getSubject());
        List<ExportTopic> exportTopics = exportSubject.getTopics();
        for (ExportTopic exportTopic : exportTopics) {
            importTopic(exportTopic, subject);
        }
    }

    @Override public void importTopic(ExportTopic exportTopic, Subject existingSubject)
        throws ServiceException {
        logger.debug("Creating topics to " + existingSubject);
        try {
            Topic topic = topicDao.createTopic(exportTopic.getTopic(), existingSubject);
            for (ExportQuestion exportQuestion : exportTopic.getQuestions()) {
                importQuestion(exportQuestion, topic);
            }
        } catch (DaoException e) {
            logger.error("Could not import topics.", e);
            throw new ServiceException(e.getMessage());
        }
    }

    @Override public void importQuestion(ExportQuestion exportQuestion, Topic existingTopic)
        throws ServiceException {
        try {
            exportQuestion.getQuestion().setQuestionId(-1);
            String oldQuestion = exportQuestion.getQuestion().getQuestion();
            if(getNewReference(oldQuestion)  != null){
                exportQuestion.getQuestion().setQuestion(getNewReference(oldQuestion));
            }
            Question question =
                questionDao.createQuestion(exportQuestion.getQuestion(), existingTopic);
            createAnswersFor(question, exportQuestion.getAnswers());
            createResource(exportQuestion);
        } catch (DaoException e) {
            logger.error(e);
            throw new ServiceException(e.getMessage());
        }
    }

    private void checkIfImportedDuplicate(Subject conflictingSubject) throws ServiceException {
        if (conflict.isDuplicate()) {
            throw new ServiceException(
                "The imported subject is a duplicate of existing subject '" + conflictingSubject
                    .getName() + "'.");
        }
    }

    private boolean existingSubjectEmpty(Subject existingSubject) throws ServiceException {
        try {
            List<Topic> existingTopics = subjectTopicDao.getTopicToSubject(existingSubject);
            return existingTopics.isEmpty();
        } catch (DaoException e) {
            logger.error(e);
            throw new ServiceException(e.getMessage(), e);
        }
    }

    private void unzipFile(String inputPath, String fileName) throws ServiceException {
        logger.debug("Unzipping exported file");

        ZipInputStream zipIn = null;
        FileInputStream fileInputStream = null;
        ZipEntry zipEntry;

        try {
            File destDir = new File(
                OUTPUT_PATH + File.separator + fileName.substring(0, fileName.length() - 4));
            unzippedDir = destDir.getAbsolutePath();

            if (!destDir.exists()) {
                destDir.mkdir();
            }

            fileInputStream = new FileInputStream(inputPath);
            zipIn = new ZipInputStream(fileInputStream);
            zipEntry = zipIn.getNextEntry();

            while (zipEntry != null) {
                String filePath = "";
                filePath = destDir + File.separator + zipEntry.getName();
                extractFileOrMakeDirectory(zipEntry, zipIn, filePath);
                zipIn.closeEntry();
                zipEntry = zipIn.getNextEntry();
            }
        } catch (FileNotFoundException e) {
            logger.error("Zip file \"" + fileName + "\" does not exist", e);
            throw new ServiceException("Zip file \"" + fileName + "\" does not exist", e);
        } catch (IOException e) {
            logger.error("Could not get zip entry", e);
            throw new ServiceException("Could not get zip entry", e);
        } finally {
            cleanUpUnzipping(fileInputStream, zipIn);
        }
    }

    private void cleanUpUnzipping(FileInputStream f, ZipInputStream z) throws ServiceException {
        try {
            if (f == null || z == null) {
                return;
            }

            f.close();
            z.close();
        } catch (IOException e) {
            logger.error("Could not close stream", e);
            throw new ServiceException("Could not close stream", e);
        }
    }

    private void extractFileOrMakeDirectory(ZipEntry zipEntry, ZipInputStream zipIn,
        String filePath) throws IOException {
        if (!zipEntry.isDirectory()) {
            extractFile(zipIn, filePath);
        } else {
            File dir = new File(filePath);
            dir.mkdir();
        }
    }

    private void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[1024];
        int read;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }

    private void copyFile(File files, String path) throws ServiceException {
        int replace = 0;
        for (File f : files.listFiles()) {
            File dest = new File(path + File.separator + f.getName());
            while (dest.exists()) {
                replace++;
                String s = FilenameUtils.removeExtension(dest.toString());
                String extension = FilenameUtils.getExtension(dest.toString());
                String newName = s + "_" + replace + "." + extension;
                dest.renameTo(new File(newName));
                resources.put(dest.toString(),newName);
            }

            copyFile(f, dest);
        }
    }

    private void copyFile(File source, File dest) throws ServiceException {
        try {
            Files.copy(source.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            logger.error(e);
            throw new ServiceException("Could not copy file.", e);
        }
    }

    private ExportSubject deserialize() throws ServiceException {
        logger.debug("Deserializing ExportSubject.ser");

        ExportSubject result;
        try {
            FileInputStream fileIn = new FileInputStream(unzippedDir + "/ExportSubject.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            result = (ExportSubject) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException e) {
            logger.error(e);
            throw new ServiceException(e);
        } catch (ClassNotFoundException e) {
            logger.error("Could not find class", e);
            throw new ServiceException(e);
        }

        return result;
    }

    private void setDatabaseAutocommit(boolean value) throws ServiceException {
        logger.debug("Setting database autocommit to " + value);

        try {
            importUtil.setAutocommit(value);
        } catch (DaoException e) {
            logger.error("Could not prepare database", e);
            throw new ServiceException("Could not prepare database", e);
        }
    }

    private void rollbackChanges() throws ServiceException {
        logger.debug("Rolling back changes");

        try {
            importUtil.rollback();
        } catch (DaoException e) {
            logger.error("Could not roll back changes", e);
            throw new ServiceException("Could not roll back changes.", e);
        }
    }

    private Subject createSubject(Subject subject) throws ServiceException {
        logger.debug("Creating subject " + subject);

        try {
            return subjectDao.createSubject(subject);
        } catch (DaoException e) {
            logger.error("Could not import subject", e);
            throw new ServiceException(e.getMessage(), e);
        }
    }

    private void createAnswersFor(Question question, List<Answer> answers) throws ServiceException {
        logger.debug("Creating answers");
        try {
            for (Answer answer : answers) {
                answer.setQuestion(question);
                if(getNewReference(answer.getAnswer()) != null){
                    answer.setAnswer(getNewReference(answer.getAnswer()));
                }
                answerDao.createAnswer(answer);
            }
        } catch (DaoException e) {
            logger.error(e);
            throw new ServiceException(e.getMessage(), e);
        }
    }

    private void createResource(ExportQuestion question) throws ServiceException {
        logger.debug("Creating questions");
        try {
            Resource resource = question.getResource().getResource();
            String oldReference = resource.getReference();
            if(getNewReference(oldReference)  != null){
                resource.setReference(getNewReference(oldReference));
            }
            resourceDao.createResource(resource);
        } catch (DaoException e) {
            logger.error("Could not import resource", e);
            throw new ServiceException(e.getMessage(), e);
        }
    }

    // is needed for unit testing
    void setSubjectDao(SubjectDao subjectDao) {
        this.subjectDao = subjectDao;
    }

    // is needed for unit testing
    void setTopicDao(TopicDao topicDao) {
        this.topicDao = topicDao;
    }

    // is needed for unit testing
    void setQuestionDao(QuestionDao questionDao) {
        this.questionDao = questionDao;
    }

    // is needed for unit testing
    void setResourceDao(ResourceDao resourceDao) {
        this.resourceDao = resourceDao;
    }

    // is needed for unit testing
    void setAnswerDao(AnswerDao answerDao) {
        this.answerDao = answerDao;
    }

    // is needed for unit testing
    void setImportUtil(ImportUtil importUtil) {
        this.importUtil = importUtil;
    }

    // is needed for unit testing
    void setSubjectConflict(SubjectConflict conflict) {
        this.conflict = conflict;
    }


    private String getNewReference(String reference){

        String s = reference.replaceAll("/","\\\\");
        if(resources.containsKey(reference)){
            return resources.get(reference);
        }
        if(resources.containsKey(s)){
            return resources.get(s);
        }

        return null;
    }
}
