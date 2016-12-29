package plugins.googleDrive;

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;

import com.google.api.services.drive.model.File;
import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.util.Collections;
/**
 * Created by inteltao on 2016/12/19.
 * Information Networking Institute, Carnegie Mellon University
 **/
public class uploadHelper {
    private Drive service;
    private java.io.File uploadPath;
    private void uploadHelper(java.io.File currentDirectory, String drivePath) {
        java.io.File[] files = currentDirectory.listFiles();
       if (files==null) return;
        for (java.io.File file : files) {
            if (!file.isDirectory()) {
                uploadDriveFile(file,drivePath);
            } else {
                String newPath=uploadDriveFolder(file.getName(),drivePath);
                uploadHelper(file,newPath);
            }
        }
    }
    public void uploadALL(String savePath, Drive service) {
        java.io.File rootDirectory = new java.io.File(savePath);
        uploadPath = rootDirectory;
        this.service=service;
        try {
            String drivePath = service.files().get("root").execute().getId();
            uploadHelper(rootDirectory,drivePath);
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
    private void uploadDriveFile(java.io.File file,String filePath) {
        File fileMetadata = new File();
        fileMetadata.setName(file.getName());
        fileMetadata.setParents(Collections.singletonList(filePath));
        FileContent mediaContent = new FileContent(mimeType.OCT_STREAM, file);
        try {
            File newFile = service.files().create(fileMetadata, mediaContent)
                    .setFields("id, parents")
                    .execute();
            System.out.println("File ID: " + newFile.getId());
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private String uploadDriveFolder(String name,String folderPath) {
        File fileMetadata = new File();
        fileMetadata.setName(name);
        fileMetadata.setMimeType(mimeType.GOOGLE_FOLDER);
        fileMetadata.setParents(Collections.singletonList(folderPath));
        try {
            File file = service.files().create(fileMetadata)
                    .setFields("id, parents")
                    .execute();
            System.out.println("Folder ID: " + file.getId());
            return file.getId();
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public void uploadDestroy() {
        try {
            if (uploadPath.exists() && uploadPath.isDirectory())
                FileUtils.deleteDirectory(uploadPath);
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
