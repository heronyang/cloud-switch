package plugins.dropbox;

// Include the Dropbox SDK.
import com.dropbox.core.*;
import java.io.*;
import java.util.Locale;
import java.awt.Desktop;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import main.StoragePlugin;

public class Dropbox implements StoragePlugin {

    final String APP_KEY = "9pspc1npg8fzdun";
    final String APP_SECRET = "v86a5mua157sizq";

    DbxAppInfo appInfo;
    DbxRequestConfig config;

    private String accessToken;

	public void load() {

		appInfo = new DbxAppInfo(APP_KEY, APP_SECRET);
		config = new DbxRequestConfig("JavaTutorial/1.0", Locale.getDefault().toString());

	}

	public void run() {
		// TODO Auto-generated method stub
		
	}

	public void unload() {
		// TODO Auto-generated method stub
		
	}

	public boolean auth() {

		DbxWebAuthNoRedirect webAuth = new DbxWebAuthNoRedirect(config, appInfo);

		String authorizeUrl = webAuth.start();
		System.out.print("Paste the authorization code: ");

        try {
            openWebpage(new URL(authorizeUrl));
        } catch(java.net.MalformedURLException e) {
            System.err.println("failed to open authorize url");
            return false;
        }

		try {

			String code = new BufferedReader(new InputStreamReader(System.in)).readLine().trim();
			DbxAuthFinish authFinish = webAuth.finish(code);
			accessToken = authFinish.accessToken;

            printAccountName();

		} catch(IOException e){
			e.printStackTrace();
            return false;
		} catch(DbxException dbxE) {
			dbxE.printStackTrace();
            return false;
		}

        return true;

	}

    private void printAccountName() {

        DbxClient client = new DbxClient(config, accessToken);
        try {
            System.out.println("Linked account: " + client.getAccountInfo().displayName);
		} catch(DbxException dbxE) {
			dbxE.printStackTrace();
		}

    }

	public String downloadAll() {

		DbxClient client = new DbxClient(config, accessToken);
        try {
            DbxEntry.WithChildren listing = client.getMetadataWithChildren("/");
            System.out.println("Files in the root path:");
            for (DbxEntry child : listing.children) {
                System.out.println("    " + child.name + ": " + child.toString());
            }
        } catch(DbxException dbxE) {
			dbxE.printStackTrace();
		}


        /*
		FileOutputStream outputStream = new FileOutputStream("magnum-opus.txt");
		try {
			DbxEntry.File downloadedFile = client.getFile("/magnum-opus.txt", null,
					outputStream);
			System.out.println("Metadata: " + downloadedFile.toString());
		} finally {
			outputStream.close();
		}
        */
		return null;
	}

	public void uploadAll(String path) {
		// TODO Auto-generated method stub

        try {
            DbxClient client = new DbxClient(config, accessToken);
            System.out.println("Linked account: " + client.getAccountInfo().displayName);
        } catch(DbxException dbxE) {
			dbxE.printStackTrace();
		}

        /*
		File inputFile = new File("working-draft.txt");
		FileInputStream inputStream = new FileInputStream(inputFile);
		try {
			DbxEntry.File uploadedFile = client.uploadFile("/magnum-opus.txt",
					DbxWriteMode.add(), inputFile.length(), inputStream);
			System.out.println("Uploaded: " + uploadedFile.toString());
		} finally {
			inputStream.close();
		}
        */
	}

	private void openWebpage(URI uri) {
		Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
		if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
			try {
				desktop.browse(uri);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void openWebpage(URL url) {
		try {
			openWebpage(url.toURI());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

}
