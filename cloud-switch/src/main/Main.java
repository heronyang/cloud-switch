package main;

import main.WebServlet;

import plugins.dropbox.Dropbox;
import plugins.googleDrive.GoogleDrive;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

public class Main {
	
	private static boolean transfer(StoragePlugin from, StoragePlugin to) {
		
		try {
			String savedPath = from.downloadAll();
            System.out.println(from + " saved all files to " + savedPath);
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

    public static void runServer() {

		Server server = new Server(8080);
		ServletContextHandler handler = new ServletContextHandler(server, "/");
		handler.addServlet(WebServlet.class, "/");

        try {
            server.start();
        } catch(Exception e) {
            System.err.println("Error in starting the web server");
        }

    }

    public static void runApplication() {

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
		if(transfer(googleDrive, dropbox) == false) {
            System.err.println("Download failed");
        }
		
		// deinit
		googleDrive.unload();
		dropbox.unload();

    }
	
	public static void main(String[] args) {

        runServer();

	}

}
