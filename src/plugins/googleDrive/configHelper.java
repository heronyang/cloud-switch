package plugins.googleDrive;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by inteltao on 2016/12/22.
 * Information Networking Institute, Carnegie Mellon University
 **/
public class configHelper {
    public static String getCurrentPath() {
        Path currentRelativePath= Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        return s;
    }
}
