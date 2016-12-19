
package test.java;
import plugins.googleDrive.GoogleDrive;
import org.junit.Test;
import plugins.googleDrive.queryHelper;

import static java.lang.System.exit;

/**
 * Created by inteltao on 2016/12/19.
 * Information Networking Institute, Carnegie Mellon University
 **/
public class driveTest {
    @Test
    public void driveTest() throws InterruptedException {
        GoogleDrive drive=new GoogleDrive();
        if(!drive.auth()) {
            System.out.println("auth failure!!");
            exit(-1);
        }
        System.out.println("auth success!");
        drive.downloadAll();
    }
}
