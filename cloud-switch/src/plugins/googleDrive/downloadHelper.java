package plugins.googleDrive;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import org.apache.commons.io.FileUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import static java.lang.Thread.sleep;

/**
 * Created by inteltao on 2016/12/19.
 * Information Networking Institute, Carnegie Mellon University
 **/
public class downloadHelper {
    private java.io.File DATA_STORAGE_DIR;
    private static final String DRIVEROOT="root";

    private Drive service;

    private void httpDownloadHelper(String savePath,File folder, queryHelper QHelper) throws InterruptedException {
        sleep(200);
        java.io.File dir = new java.io.File(savePath);
        if(!dir.exists()) {
            dir.mkdir();
        }
        ArrayList<File> files= new ArrayList<File>();
        ArrayList<File> folders = new ArrayList<File>();
        if (savePath==DATA_STORAGE_DIR.getAbsolutePath()) {
            files.addAll(QHelper.queryDriveFile(service, DRIVEROOT));
            folders.addAll(QHelper.queryDriveFolder(service, DRIVEROOT));
        }
        else {
            files.addAll(QHelper.queryDriveFile(service, folder.getId()));
            folders.addAll(QHelper.queryDriveFolder(service, folder.getId()));
        }
        for (File file:files) {
           downloadFile(file,savePath);
        }
        for (File findFolder:folders) {
            String newPath=savePath+"/"+findFolder.getName();
            System.out.println("newPath: "+newPath);
            httpDownloadHelper(newPath,findFolder,QHelper);
        }
    }

    public String downloadAll(Drive newService, String owner) throws InterruptedException {
        String path=DATA_STORAGE_DIR.getAbsolutePath();
        System.out.println(path);
        service=newService;
        queryHelper QHelper= new queryHelper();
        QHelper.setOWNER(owner);
        httpDownloadHelper(path,null,QHelper);
        return path;
    }

    private void downloadFile(File file, String savePath) {
        try {
            java.io.File targetFile= new java.io.File(savePath, file.getName());
            if ((!targetFile.exists())) {
                OutputStream out = new FileOutputStream(targetFile);
                String fileMimeType=file.getMimeType();
                System.out.println("MimeType for file "+file.getName()+" is "+fileMimeType);
                if (!mimeType.ifGoogleDoc(fileMimeType)) {
                    service.files().get(file.getId())
                            .executeMediaAndDownloadTo(out);
                }
                else {
                    service.files().export(file.getId(),mimeType.convertExportType(fileMimeType, file.getName()))
                            .executeMediaAndDownloadTo(out);
                }
            }
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public void setDataStoreDirectory(String path) {
        DATA_STORAGE_DIR= new java.io.File(
               path, ".downloadTemp"+System.currentTimeMillis());
    }

    public void downloadDestroy() {
        try {
            if (DATA_STORAGE_DIR.exists()  && DATA_STORAGE_DIR.isDirectory())
                FileUtils.deleteDirectory(DATA_STORAGE_DIR);
            System.out.println("Download State Destroyed!");
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
