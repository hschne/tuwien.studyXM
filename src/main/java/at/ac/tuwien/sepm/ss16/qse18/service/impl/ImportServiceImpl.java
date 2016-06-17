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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service public class ImportServiceImpl implements ImportService {

    private static final Logger logger = LogManager.getLogger();
    private static final String OUTPUT_PATH = "src/main/resources/import";
    @Autowired ApplicationContext applicationContext;
    private String unzippedDir = "";
    @Autowired private SubjectConflictDetection subjectConflictDetection;
    @Autowired private SubjectConflict conflict;
    @Autowired private SubjectDao subjectDao;
    @Autowired private TopicDao topicDao;
    @Autowired private QuestionDao questionDao;
    @Autowired private ResourceDao resourceDao;
    @Autowired private AnswerDao answerDao;
    @Autowired private ImportUtil importUtil;

    // is needed for unit testing
    public void setSubjectDao(SubjectDao subjectDao) {
        this.subjectDao = subjectDao;
    }

    // is needed for unit testing
    public void setTopicDao(TopicDao topicDao) {
        this.topicDao = topicDao;
    }

    // is needed for unit testing
    public void setQuestionDao(QuestionDao questionDao) {
        this.questionDao = questionDao;
    }

    // is needed for unit testing
    public void setResourceDao(ResourceDao resourceDao) {
        this.resourceDao = resourceDao;
    }

    // is needed for unit testing
    public void setAnswerDao(AnswerDao answerDao) {
        this.answerDao = answerDao;
    }

    // is needed for unit testing
    public void setImportUtil(ImportUtil importUtil) {
        this.importUtil = importUtil;
    }

    // is needed for unit testing
    public void setSubjectConflict(SubjectConflict conflict) {
        this.conflict = conflict;
    }

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
            conflict.initialize(conflictingSubject, subject);
            if (conflict.isDuplicate()) {
                throw new ServiceException(
                    "The imported subject is a duplicate of existing subject '" + conflictingSubject
                        .getName() + "'.");
            }
            return conflict;
        }

        setDatabaseAutocommit(false);
        try {
            fillDatabase(subject);
        } catch (ServiceException e) {
            rollbackChanges();
            throw e;
        }
        setDatabaseAutocommit(true);
        return null;
    }

    @Override public void importTopic(Topic topic, Subject existingSubject) {

    }

    @Override public void importQuestion(ExportQuestion exportQuestion, Topic existingTopic) {

    }


    private void unzipFile(String inputPath, String fileName) throws ServiceException {
        logger.debug("Unzipping exported file");

        ZipInputStream zipIn = null;
        FileInputStream fileInputStream = null;
        ZipEntry zipEntry = null;

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
                String filePath = destDir + File.separator + zipEntry.getName();
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
            if (zipIn != null) {
                try {
                    zipIn.close();
                } catch (IOException e) {
                    logger.error("Could not close zipInputStream", e);
                    throw new ServiceException("Could not close zipInputStream", e);
                }
            }
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

    private ExportSubject deserialize() throws ServiceException {
        logger.debug("Deserializing ExportSubject.ser");

        ExportSubject result = null;

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

    private void fillDatabase(ExportSubject exportSubject) throws ServiceException {
        logger.debug("Filling database with values");

        if (exportSubject == null) {
            logger.error("Could not read subject from import file. Subject is null");
            throw new ServiceException("Could not read subject from import file. Subject is null");
        }

        Subject subject = createSubject(exportSubject.getSubject());
        List<Topic> topics = createTopics(subject, exportSubject.getTopics());

        int i = 0;
        for (ExportTopic exportTopic : exportSubject.getTopics()) {
            List<Question> questions = createQuestions(topics.get(i), exportTopic.getQuestions());

            int j = 0;
            for (ExportQuestion exportQuestion : exportTopic.getQuestions()) {
                resetQuestionAnswerRelation(exportQuestion.getAnswers(), questions.get(j));
                createAnswers(exportQuestion.getAnswers());
                j++;
            }

            createResource(exportTopic.getQuestions());
            i++;
        }
    }

    private void resetQuestionAnswerRelation(List<Answer> answers, Question question) {
        for (Answer answer : answers) {
            answer.setQuestion(question);
        }
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
            throw new ServiceException("Could not roll back changes", e);
        }
    }

    private Subject createSubject(Subject subject) throws ServiceException {
        logger.debug("Creating subject " + subject);

        try {
            return subjectDao.createSubject(subject);
        } catch (DaoException e) {
            logger.error("Could not import subject", e);
            throw new ServiceException("Could not import subject", e);
        }
    }

    private List<Topic> createTopics(Subject subject, List<ExportTopic> topics)
        throws ServiceException {
        logger.debug("Creating topics to " + subject);
        List<Topic> created = new ArrayList<>();

        try {
            for (ExportTopic eTopic : topics) {
                created.add(topicDao.createTopic(eTopic.getTopic(), subject));
            }
            return created;
        } catch (DaoException e) {
            logger.error("Could not import topics", e);
            throw new ServiceException("Could not import topics", e);
        }
    }

    private List<Question> createQuestions(Topic topic, List<ExportQuestion> questions)
        throws ServiceException {
        logger.debug("Create questions to topic " + topic);
        List<Question> created = new ArrayList<>();

        try {
            for (ExportQuestion eQuestion : questions) {
                eQuestion.getQuestion().setQuestionId(-1);
                created.add(questionDao.createQuestion(eQuestion.getQuestion(), topic));
            }
            return created;
        } catch (DaoException e) {
            logger.error("Could not import questions", e);
            throw new ServiceException("Could not import questions", e);
        }
    }

    private List<Answer> createAnswers(List<Answer> answers) throws ServiceException {
        logger.debug("Creating answers");
        List<Answer> created = new ArrayList<>();

        try {
            for (Answer answer : answers) {
                created.add(answerDao.createAnswer(answer));
            }
            return created;
        } catch (DaoException e) {
            logger.error("Could not import answers", e);
            throw new ServiceException("Could not import answers", e);
        }
    }

    private List<Resource> createResource(List<ExportQuestion> questions) throws ServiceException {
        logger.debug("Creating questions");
        List<Resource> created = new ArrayList<>();

        try {
            for (ExportQuestion eQuestion : questions) {
                Resource tmp = eQuestion.getResource().getResource();
                if (tmp != null) {
                    created.add(resourceDao.createResource(tmp));
                }
            }
            return created;
        } catch (DaoException e) {
            logger.error("Could not import resource", e);
            throw new ServiceException("Could not import resource", e);
        }
    }
}
