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
            ArrayList<File> folders = new ArrayList<File>();
            ArrayList<File> files = new ArrayList<File>();
            folders.addAll(drive_api.queryFiles(service,"root",false));
            files.addAll(drive_api.queryFiles(service,"root",true));
            while (!folders.isEmpty()) {
                String parentID=folders.get(0).getId();
                folders.remove(0);
                files.addAll(drive_api.queryFiles(service,parentID,true));
                folders.addAll(drive_api.queryFiles(service,parentID,false));
                Thread.sleep(500);
            }

        }
        catch (IOException e) {
            System.err.println(e.getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
