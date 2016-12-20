package plugins.googleDrive;

import com.google.api.services.drive.Drive;
import main.StoragePlugin;

import java.io.IOException;

public class GoogleDrive implements StoragePlugin {
	public Drive SERVICE;
	public String OWNER;
	public void load() {
		// TODO Auto-generated method stub

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
		downloadHelper DHelper=new downloadHelper();
		DHelper.downloadAll(SERVICE,OWNER);
		return null;
	}

	public void uploadAll(String path) {
		// TODO Auto-generated method stub
		
	}

}
