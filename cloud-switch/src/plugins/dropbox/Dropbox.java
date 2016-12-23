package plugins.dropbox;

import java.io.*;
import java.util.Locale;
import java.awt.Desktop;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import com.dropbox.core.*;
import main.StoragePlugin;

public class Dropbox implements StoragePlugin {

    DbxAppInfo appInfo;
    DbxRequestConfig config;
    DbxClient client;

	public void load() {

		appInfo = new DbxAppInfo(Config.APP_KEY, Config.APP_SECRET);
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
			String accessToken = authFinish.accessToken;
            client = new DbxClient(config, accessToken);

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

        try {
            System.out.println("Linked account: " + client.getAccountInfo().displayName);
		} catch(DbxException dbxE) {
			dbxE.printStackTrace();
		}

    }

	public String downloadAll() {

        return downloadFolder("/");

	}

    private String downloadFolder(String path) {

        System.out.println("> Checking folder " + path);
        createFolderIfNotExist(Config.TEMP_PATH + path);

        try {
            DbxEntry.WithChildren listing = client.getMetadataWithChildren(path);
            for (DbxEntry child: listing.children) {
                System.out.println("> Found: " + child.name + " - " + child);
                if(child.isFile()) {
                    downloadFile(child.path);
                }
                if(child.isFolder()) {
                    downloadFolder(child.path);
                }
            }
        } catch(DbxException dbxE) {
			dbxE.printStackTrace();
            return null;
		}

        return "";

    }

    private void createFolderIfNotExist(String path) {
        File directory = new File(String.valueOf(path));
        if( !directory.exists() ){
            directory.mkdir();
        }
    }

    private String downloadFile(String path) {

        System.out.println("> Downloading file " + path);

        String filepath = Config.TEMP_PATH + path;
        FileOutputStream  outputStream;

        try {
            outputStream = new FileOutputStream(filepath);
			DbxEntry.File downloadedFile = client.getFile(path, null, outputStream);
			System.out.println("> Downloaded file " + downloadedFile.toString());
			outputStream.close();
		} catch(IOException e){
			e.printStackTrace();
            return null;
		} catch(Exception e) {
            System.err.println("Error in downloading " + path);
            return null;
        }

        return filepath;

    }

	public void uploadAll(String path) {

        try {
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
