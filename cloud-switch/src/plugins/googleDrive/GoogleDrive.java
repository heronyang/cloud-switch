package plugins.googleDrive;

import com.google.api.services.drive.Drive;
import main.StoragePlugin;

import java.io.IOException;

public class GoogleDrive implements StoragePlugin {
	private Drive SERVICE;
	private String OWNER;

	private downloadHelper DHelper = new downloadHelper();
	private uploadHelper UHelper=new uploadHelper();
	private authHelper AHelper=new authHelper();

	//This function will try to setup the config of the downloading process, including
	//download path, etc.
	public void load() {
		String s=configHelper.getCurrentPath();
		System.out.println("Current application is stateless");
		System.out.println("Default temp file would be the path of your current folder:" +s);
		DHelper.setDataStoreDirectory(s);
	}

	public void run() {
	}

	public void unload() {
		//AHelper.authDestroy();
		//UHelper.uploadDestroy();
		//DHelper.downloadDestroy();
	}

	public boolean auth() {
		try {
			SERVICE = AHelper.getDriveService();
			OWNER=AHelper.setOwner(SERVICE);
			return true;
		}
		catch (IOException e) {
			System.err.println(e.getMessage());
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return false;
	}

	public String downloadAll() throws InterruptedException {
		return DHelper.downloadAll(SERVICE,OWNER);
	}

	public void uploadAll(String path) {
		UHelper.uploadALL(path,SERVICE);
	}

}
