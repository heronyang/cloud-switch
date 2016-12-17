import com.google.api.client.auth.oauth2.Credential;

import java.io.IOException;
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
            Credential credential = drive_api.authorize();
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
