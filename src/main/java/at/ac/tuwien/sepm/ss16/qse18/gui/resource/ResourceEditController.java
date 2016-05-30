package at.ac.tuwien.sepm.ss16.qse18.gui.resource;

import at.ac.tuwien.sepm.ss16.qse18.domain.Resource;
import at.ac.tuwien.sepm.ss16.qse18.domain.ResourceType;
import at.ac.tuwien.sepm.ss16.qse18.gui.GuiController;
import at.ac.tuwien.sepm.ss16.qse18.gui.MainFrameController;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableResource;
import at.ac.tuwien.sepm.util.AlertBuilder;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
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

    private static final Logger logger = LogManager.getLogger();
    @FXML private TextField resourceName;
    @FXML private TextField filePath;
    @FXML private CheckBox checkBoxContinue;

    @Autowired ResourceOverviewController overviewController;
    @Autowired MainFrameController mainFrameController;
    @Autowired AlertBuilder alertBuilder;

    private File out;

    private File in;

    @FXML public void handleSelectFile() {
        logger.debug("Selecting file");
        FileChooser fileChooser = new FileChooser();
        String defaultPath = "src/main/resources/resources/";
        File defaultDirectory = new File(defaultPath);
        fileChooser.setInitialDirectory(defaultDirectory);

        fileChooser.setTitle("Add resource");
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            logger.debug("A file was selected: {}", selectedFile);
            out = new File(defaultPath + generateFileName(selectedFile.getName()));
            in = selectedFile;
            filePath.setText(defaultPath + selectedFile.getName());
        }
    }

    @FXML public void handleCreateResource() {
        logger.debug("Creating resource in database");
        try {
            copyFile(in, out);
            Resource resource = createResourceFromFields();
            overviewController.addResource(new ObservableResource(resource));
            resetOrChangeView();
            showSuccess("Resource has been created.");
        } catch (Exception e) {
            logger.error(e);
            showAlert(e);
        }
    }

    private void resetOrChangeView() {
        if(checkBoxContinue.isSelected()){
            resetFields();
        }
        else{
            mainFrameController.handleResources();
        }
    }

    private void resetFields(){
        in = null;
        out = null;
        resourceName.clear();
        filePath.clear();
    }

    @FXML public void handleCancel() {
        logger.debug("Canceling action");
        mainFrameController.handleResources();
    }

    private void copyFile(File sourceFile, File destFile) throws IOException {
        if(sourceFile == null || destFile == null){
            throw new NoSuchFileException("Could not create a new resource. Please select a file first.");
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
                return ResourceType.OTHER;
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
        Alert alert = alertBuilder.alertType(Alert.AlertType.INFORMATION).title("Error")
            .headerText("An error occurred").contentText(e.getMessage()).setResizable(true).build();
        alert.showAndWait();
    }

    private void showSuccess(String msg) {
        Alert alert = alertBuilder.alertType(Alert.AlertType.INFORMATION).title("Success")
            .headerText("The operation was successful!").contentText(msg).build();
        alert.showAndWait();
    }

}
