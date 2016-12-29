package plugins.googleDrive;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by inteltao on 2016/12/19.
 * Information Networking Institute, Carnegie Mellon University
 **/
public class queryHelper {
    private static String OWNER;


    public void setOWNER(String owner){
        OWNER=owner;
    }
    /**
     * Query all the files or folders in the Drive Service specified by their parent
     * @param service the drive application
     * @param parentID the parent ID of the file's directory
     * @param ifFile true if you want files, false if you want folders
     * @return an queried file list
     * @throws IOException
     */
    private static List<File> queryDriveHelper(Drive service, String parentID, Boolean ifFile) {
        List<File> files;
        List<File> results = new ArrayList<File>();
        String pageToken = null;
        try {
            System.out.println("Searched Parent: "+service.files().get(parentID).execute().getName());
            String q=setQueryCondition(parentID, ifFile);
            do {
                FileList result;
                result = service.files().list()
                        .setPageSize(1000)
                        .setFields("nextPageToken, files(id, name, parents, mimeType)")
                        .setPageToken(pageToken)
                        .setQ(q)
                        .execute();
                files = result.getFiles();
                if (files == null || files.size() == 0) {
                    System.out.println("No files found.");
                } else {
                    System.out.println("Files: "+" "+files.size());
                    for (File file : files) {
                        results.add(file);
                        System.out.printf("%s (%s)\n", file.getName(), file.getId());
                    }
                }
                pageToken = result.getNextPageToken();
            } while (pageToken!=null);
            return results;
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }
    private static String setQueryCondition(String parentID, Boolean ifFile)
    {
        String q;
        if (!parentID.isEmpty())
            q="'"+parentID+"' in parents and trashed = false";
        else q="trashed = false";//We won't download trashed file
        if (!ifFile) q=q+" and mimeType = 'application/vnd.google-apps.folder'";
        else q=q+" and mimeType != 'application/vnd.google-apps.folder'";
        q=q+" and '"+OWNER+"' in owners";
        return q;
    }

    public static List<File> queryDriveFile(Drive service,String parentID)
    {
        return queryDriveHelper(service,parentID,true);
    }
    public static List<File> queryDriveFolder(Drive service,String parentID)
    {
        return queryDriveHelper(service,parentID,false);
    }
}
