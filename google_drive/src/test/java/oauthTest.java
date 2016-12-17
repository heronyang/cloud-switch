import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import org.junit.Test;

/**
 * Created by inteltao on 2016/12/16.
 * Information Networking Institute, Carnegie Mellon University
 **/
public class oauthTest {
    @Test
    public void oauthTest()
    {
        System.out.println("Let the game begin");
        try {
            Drive service = drive_api.getDriveService();
            drive_api.downloadFile("1keOX2DcSkZgqg2wrTmSi57yQU_DiH5Cf3N0Wxlc2T1c",service,null);
            printFiles(service);
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
    private void printFiles(Drive service) {
        try {
            // Print the names and IDs for up to 10 files.
            FileList result = service.files().list()
                    .setPageSize(10)
                    .setFields("nextPageToken, files(id, name)")
                    .execute();
            List<File> files = result.getFiles();
            if (files == null || files.size() == 0) {
                System.out.println("No files found.");
            } else {
                System.out.println("Files:");
                for (File file : files) {
                    System.out.printf("%s (%s)\n", file.getName(), file.getId());
                }
            }
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
