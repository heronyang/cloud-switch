package main;

import main.WebServlet;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class Main {

	public static void main(String[] args) {

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

	}

}
