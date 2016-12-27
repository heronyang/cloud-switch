package main;

import main.WebServlet;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import plugins.dropbox.Dropbox;
import plugins.googleDrive.GoogleDrive;

public class Main {

	public static void main(String[] args) {

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

/*
        Server server = new Server(8080);
 
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);
 
        context.addServlet(new ServletHolder(new WebServlet()),"/*");
        context.addServlet(new ServletHolder(new WebServlet("dropbox2googledrive")),"/dropbox2googledrive/*");
        context.addServlet(new ServletHolder(new WebServlet("googledrive2dropbox")),"/googledrive2dropbox/*");
 
		try {
			server.start();
			server.join();
		} catch(Exception e) {
			System.err.println("Error in starting the web server");
        }
*/

	}

	private static boolean transfer(StoragePlugin from, StoragePlugin to) {
		
		try {
			String savedPath = from.downloadAll();
            System.out.println(from + " saved all files to " + savedPath);
            if(savedPath == null) {
                return false;
            }
			to.uploadAll(savedPath);
		} catch (Throwable t) {
			t.printStackTrace();
		}

        return true;
		
	}

}
