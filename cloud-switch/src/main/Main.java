package main;

import plugins.dropbox.Dropbox;
import plugins.googleDrive.GoogleDrive;

public class Main {
	
	private static void transfer(StoragePlugin from, StoragePlugin to) {
		
		try {
			String savedPath = from.downloadAll();
			to.uploadAll(savedPath);
		}
		catch (Throwable t) {
			t.printStackTrace();
		}

		
	}
	
	public static void main(String[] args) {
		
		// NOTE: since we only have two plugins for testing purpose
		// at this point, don't bother to optimize the code here too much
		// it will be a good idea to have a pluginManager class in the future
		
		// init
		StoragePlugin googleDrive = new GoogleDrive();
		StoragePlugin dropbox = new Dropbox();
		
		googleDrive.load();
		dropbox.load();
		
		// auth
		if(!googleDrive.auth() || !dropbox.auth()) {
			System.err.println("Authedication error");
			return;
		}
		
		// transfer
		transfer(googleDrive, dropbox);
		
		// deinit
		googleDrive.unload();
		dropbox.unload();

	}

}
