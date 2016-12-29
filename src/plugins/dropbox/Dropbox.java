package plugins.dropbox;

import java.io.*;
import java.util.Locale;
import java.awt.Desktop;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import java.sql.Timestamp;

import com.dropbox.core.*;
import main.StoragePlugin;

public class Dropbox implements StoragePlugin {

    DbxAppInfo appInfo;
    DbxRequestConfig config;
    DbxClient client;

    String downloadFolderPath = "";

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

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        this.downloadFolderPath = Config.TEMP_PATH + timestamp.getTime();
        System.out.println("Donwload folder path: " + this.downloadFolderPath);

        return downloadFolder("/");

	}

    private String downloadFolder(String path) {

        System.out.println("> Checking folder " + path);
        createFolderIfNotExist(this.downloadFolderPath + path);

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

        return this.downloadFolderPath + path;

    }

    private void createFolderIfNotExist(String path) {

        File directory = new File(String.valueOf(path));

        if( !directory.exists() ){
            directory.mkdir();
        }

    }

    private String downloadFile(String path) {

        System.out.println("> Downloading file " + path);

        String filepath = this.downloadFolderPath + path;
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
        uploadFolder(path, path);
	}

    private void uploadFolder(String path, String base) {

        System.out.println("> Checking folder " + path);

		File folder = new File(path);
		for(final File file: folder.listFiles()) {
			if (file.isDirectory()) {
				uploadFolder(file.getAbsolutePath(), base);
			} else {
                uploadFile(file.getAbsolutePath(), base);
			}
		}

    }

    private boolean uploadFile(String path, String base) {

		File inputFile = new File(path);
        String target = "/" + new File(base).toURI().relativize(new File(path).toURI()).getPath();
        System.out.println("> Found file " + path + ", will upload as " + target);

		try {
		    FileInputStream inputStream = new FileInputStream(inputFile);
			DbxEntry.File uploadedFile = client.uploadFile(target,
					DbxWriteMode.add(), inputFile.length(), inputStream);
			System.out.println("> Uploaded: " + uploadedFile.toString());
			inputStream.close();
            return true;
		} catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error while uploading " + path);
            return false;
        }

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
    
    @Override
    public String toString() {

        try {
            if(client != null) {
                String name = client.getAccountInfo().displayName;
                return "Dropbox (account name: " + name + ")";
            }
        } catch(DbxException dbxE) {
			dbxE.printStackTrace();
            return "Dropbox";
		}

        return "Dropbox";

    }

}
