package plugins.googleDrive;

import com.google.api.services.drive.Drive;
import main.StoragePlugin;

import java.io.IOException;

public class GoogleDrive implements StoragePlugin {
	private Drive SERVICE;
	private String OWNER;

	private downloadHelper DHelper = new downloadHelper();
	private uploadHelper UHelper=new uploadHelper();

	//This function will try to setup the config of the downloading process, including
	//download path, etc.
	public void load() {
		String s=configHelper.getCurrentPath();
		System.out.println("Default temp file would be the path of your current folder:" +s);
		DHelper.setDataStoreDirectory(s);
	}

	public void run() {
		// TODO Auto-generated method stub
		
	}

	public void unload() {
		// TODO Auto-generated method stub
		
	}

	public boolean auth() {
		try {
			System.out.println("Hello World!");
			SERVICE = authHelper.getDriveService();
			OWNER=authHelper.setOwner(SERVICE);

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
		// TODO Auto-generated method stub
		
	}

}
