package at.ac.tuwien.sepm.ss16.qse18.gui.resource;

import at.ac.tuwien.sepm.ss16.qse18.domain.Resource;
import at.ac.tuwien.sepm.ss16.qse18.domain.ResourceType;
import at.ac.tuwien.sepm.ss16.qse18.gui.GuiController;
import at.ac.tuwien.sepm.ss16.qse18.gui.MainFrameController;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableResource;
import at.ac.tuwien.sepm.util.AlertBuilder;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.NoSuchFileException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * @author Hans-Joerg Schroedl
 */
@Component @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) public class ResourceEditController
    implements GuiController {

    private final Logger logger = LogManager.getLogger();
    @FXML TextField resourceName;
    @FXML TextField filePath;

    @Autowired ResourceOverviewController overviewController;
    @Autowired MainFrameController mainFrameController;
    @Autowired AlertBuilder alertBuilder;

    private File out;

    private File in;

    @FXML public void handleSelectFile() {
        FileChooser fileChooser = new FileChooser();
        String defaultPath = "src/main/resources/resources/";
        File defaultDirectory = new File(defaultPath);
        fileChooser.setInitialDirectory(defaultDirectory);

        fileChooser.setTitle("Add resource");
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            logger.debug("A File was selected");
            out = new File(defaultPath + generateFileName(selectedFile.getName()));
            in = selectedFile;
            filePath.setText(defaultPath + selectedFile.getName());
        }
    }

    @FXML public void handleOk() {
        try {
            copyFile(in, out);
            Resource resource = createResourceFromFields();
            overviewController.addResource(new ObservableResource(resource));
            mainFrameController.handleResources();
        } catch (Exception e) {
            logger.error(e);
            showAlert(e);
        }

    }

    @FXML public void handleCancel() {
        mainFrameController.handleResources();
    }

    private void copyFile(File sourceFile, File destFile) throws IOException {
        if(sourceFile == null || destFile == null){
            throw new NoSuchFileException("Can not create a new resource. Please select a file first");
        }
        try (FileInputStream source = new FileInputStream(sourceFile);
            FileOutputStream destination = new FileOutputStream(destFile)) {
            FileChannel sourceChannel = source.getChannel();
            FileChannel destinationChannel = destination.getChannel();
            sourceChannel.transferTo(0, sourceChannel.size(), destinationChannel);
        }
    }


    private Resource createResourceFromFields() {
        Resource resource = new Resource();
        resource.setType(getResourceType());
        resource.setName(resourceName.getText());
        resource.setReference(out.getAbsolutePath());
        return resource;
    }

    private ResourceType getResourceType() {
        String fileName = out.getName();
        String extension = getExtension(fileName);
        switch (extension) {
            case "pdf":
                return ResourceType.PDF;
            default:
                return ResourceType.NOTE;
        }
    }

    private String getExtension(String fileName) {
        int i = fileName.lastIndexOf('.');
        String extension = "";
        if (i > 0) {
            extension = fileName.substring(i + 1);
        }
        return extension;
    }

    private String generateFileName(String fileName) {
        logger.debug("Checking for duplicate image files");
        File dir = new File("src/main/resources/resources/");
        String[] files = dir.list();
        Set<String> fileSet = new HashSet<>();
        fileSet.addAll(Arrays.asList(files));

        if (fileSet.contains(fileName)) {
            logger.debug("Duplicate found, generating new file name");
            String extension = getExtension(fileName);
            return UUID.randomUUID().toString() + "." + extension;
        } else {
            logger.debug("No duplicate found");
            return fileName;
        }
    }

    private void showAlert(Exception e) {
        Alert alert = alertBuilder.alertType(Alert.AlertType.ERROR).title("Error")
            .headerText("An error occurred").contentText(e.getMessage()).setResizable(true).build();
        alert.showAndWait();
    }

}
