package main;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.HttpStatus;

import plugins.dropbox.Dropbox;
import plugins.googleDrive.GoogleDrive;

public class WebServlet extends HttpServlet {

	private String operation="";

	public WebServlet(){}
	public WebServlet(String operation) {
		this.operation = operation;
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

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);

        if(this.operation.equals("dropbox2googledrive")) {

            // TODO
            response.getWriter().println("request sent");
        } else if(this.operation.equals("googledrive2dropbox")) {

            // TODO
            response.getWriter().println("request sent");
        } else {
            response.getWriter().println("<a href=\"dropbox2googledrive/\">Dropbox -> Google Drive</a><br />");
            response.getWriter().println("<a href=\"googledrive2dropbox/\">Google Drive -> Dropbox</a><br />");
            response.getWriter().println("session=" + request.getSession(true).getId());
        }

	}

}
