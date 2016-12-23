package main;

import plugins.dropbox.Dropbox;
import plugins.googleDrive.GoogleDrive;

public class Main {
	
	private static boolean transfer(StoragePlugin from, StoragePlugin to) {
		
		try {
			String savedPath = from.downloadAll();
            if(savedPath == null) {
                return false;
            }
			to.uploadAll(savedPath);
		}
		catch (Throwable t) {
			t.printStackTrace();
		}

        return true;
		
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
		if(transfer(dropbox, googleDrive) == false) {
            System.err.println("Download failed");
        }
		
		// deinit
		googleDrive.unload();
		dropbox.unload();

	}

}
