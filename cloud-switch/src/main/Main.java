package main;

import main.WebServlet;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import plugins.dropbox.Dropbox;
import plugins.googleDrive.GoogleDrive;

import java.util.Properties;
import java.util.Scanner;


public class Main {
	//Prints the welcome message
	private static void welcome() {
		System.out.println("/***************************************************************************************/");
		System.out.println("/** THIS IS THE MAIN PROGRAM FOR CLOUD SWITCH                                         **/");
		System.out.println("/** This program can help you transfer files between different cloud drive, including **/");
		System.out.println("/** Google Drive, Dropbox, iCloud, etc (Currently support Google Drive and Dropbox    **/");
		System.out.println("/** This is a autonomous program, all you need to do is give us the authentication    **/");
		System.out.println("/***************************************************************************************/");
	}

	public static void main(String[] args) {
		welcome();
		Scanner scanner=new Scanner(System.in);
		System.out.println("/***************************************************************************************/");
		System.out.println("/******************************STEP 0: Setup Config*************************************/");
		System.out.println("/** We will have various configuration in this step                                   **/");
		System.out.println("/***************************************************************************************/");
		System.out.println("Please enter your email address so we can inform you when the transform is done:");
		String emailAddress = scanner.nextLine();
		System.out.println("Please specify the type of transform you want to perform, so far we have two choices:");
		System.out.println("enter 1 if you want Google Drive ---> Dropbox; enter 2 Dropbox ---> Google Drive:");
		int choice = scanner.nextInt();
		// init
		StoragePlugin googleDrive = new GoogleDrive();
		StoragePlugin dropbox = new Dropbox();
		System.out.println("/***************************************************************************************/");
		System.out.println("/******************************STEP 1: AUTHENTICATE USERS*******************************/");
		System.out.println("/***************************************************************************************/");
		System.out.println("In this step, you will be directed to the authentication page of Google Drive and Dropbox");
		System.out.println("Press ENTER to continue");
		googleDrive.load();
		dropbox.load();
		// auth
		if(!googleDrive.auth() || !dropbox.auth()) {
			System.err.println("Authedication error");
			return;
		}
		System.out.println("/***************************************************************************************/");
		System.out.println("/*******************************STEP 2: TRANSFER****************************************/");
		System.out.println("/***************************************************************************************/");
		// transfer
		if (choice == 1) {
			if (transfer(googleDrive, dropbox) == false) {
				System.err.println("Download failed");
			}
		}
		else if (choice == 2) {
			if (transfer(dropbox, googleDrive) == false) {
				System.err.println("Download failed");
			}
		}


		// deinit
		googleDrive.unload();
		dropbox.unload();
		//Send email to the user, this function not complete
		sendEmail(emailAddress);
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

	private static void sendEmail(String emailAddress) {

	}

}