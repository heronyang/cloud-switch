package plugins.googleDrive;

import com.google.api.services.drive.Drive;
import main.StoragePlugin;

import java.io.IOException;

public class GoogleDrive implements StoragePlugin {
	public Drive service;
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
			service = authHelper.getDriveService();
			OWNER=authHelper.setOwner(service);
			return true;
		}
		catch (IOException e) {
			System.err.println(e.getMessage());
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return false;
	}

	public String downloadAll() {
		// TODO Auto-generated method stub
		return null;
	}

	public void uploadAll(String path) {
		// TODO Auto-generated method stub
		
	}

}
