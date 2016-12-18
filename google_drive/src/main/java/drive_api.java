/**
 * Created by inteltao on 2016/12/16.
 * Information Networking Institute, Carnegie Mellon University
 * The sample code is adapted from Google Github samples
 **/
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;

import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.*;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import org.mortbay.jetty.AbstractGenerator;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class drive_api {
    /**
     * Application name.
     */
    private static final String APPLICATION_NAME =
            "Drive_API";

    /**
     * Directory to store user credentials for this application.
     */
    private static final java.io.File DATA_CREDENTIALS_DIR = new java.io.File(
            System.getProperty("user.home"), ".credentials/drive-java-quickstart");

    /**
     * Global instance of the {@link FileDataStoreFactory}.
     */
    private static FileDataStoreFactory DATA_STORE_FACTORY;

    /**
     * Global instance of the JSON factory.
     */
    private static final JsonFactory JSON_FACTORY =
            JacksonFactory.getDefaultInstance();

    /**
     * Global instance of the HTTP transport.
     */
    private static HttpTransport HTTP_TRANSPORT;

    /**
     * Global instance of the scopes required by this quickstart.
     * <p>
     * If modifying these scopes, delete your previously saved credentials
     * at ~/.credentials/drive-java-quickstart
     */
    private static final List<String> SCOPES =
            Arrays.asList(DriveScopes.DRIVE);

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_CREDENTIALS_DIR);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Creates an authorized Credential object.
     *
     * @return an authorized Credential object.
     * @throws IOException
     */
    public static Credential authorize() throws IOException {
        // Load client secrets.
        InputStream in =
                drive_api.class.getResourceAsStream("/client_secret.json");
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                        .setDataStoreFactory(DATA_STORE_FACTORY)
                        .setAccessType("offline")
                        .build();
        Credential credential = new AuthorizationCodeInstalledApp(
                flow, new LocalServerReceiver()).authorize("user");
        System.out.println(
                "Credentials saved to " + DATA_CREDENTIALS_DIR.getAbsolutePath());
        return credential;
    }
    /**
     * Build and return an authorized Drive client service.
     * @return an authorized Drive client service
     * @throws IOException
     */
    public static Drive getDriveService() throws IOException {
        Credential credential = authorize();
        return new Drive.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public static void downloadFile(String fileID, Drive service, OutputStream outputStream){
        try{
            service.files().export(fileID, "application/pdf")
            .executeMediaAndDownloadTo(outputStream);
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
    /**
     * Query all the files or folders in the Drive Service specified by thier parent
     * @param service the drive application
     * @param parentID the parent ID of the file's directory
     * @param ifFile true if you want files, false if you want folders
     * @return an queried file list
     * @throws IOException
     */
   public static List<File> queryFiles(Drive service,String parentID,Boolean ifFile) {
       List<File> files;
       List<File> results = new ArrayList<File>();
       String pageToken = null;
        try {
            System.out.println("Searched Parent: "+service.files().get(parentID).execute().getName());
            String q="'"+parentID+"' in parents and trashed = false";
            if (!ifFile) q=q+" and mimeType = 'application/vnd.google-apps.folder'";
            else q=q+" and mimeType != 'application/vnd.google-apps.folder'";
            do {
                FileList result;
                result = service.files().list()
                        .setPageSize(1000)
                        .setFields("nextPageToken, files(id, name)")
                        .setPageToken(pageToken)
                        .setQ(q)
                        .execute();
                files = result.getFiles();
                if (files == null || files.size() == 0) {
                    System.out.println("No files found.");
                } else {
                    System.out.println("Files: "+" "+files.size());
                    for (File file : files) {
                        results.add(file);
                        System.out.printf("%s (%s)\n", file.getName(), file.getId());
                    }
                }
                pageToken = result.getNextPageToken();
            } while (pageToken!=null);
            return results;
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }
}
