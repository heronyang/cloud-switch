package plugins.googleDrive;

/**
 * Created by inteltao on 2016/12/20.
 * Information Networking Institute, Carnegie Mellon University
 **/
public class mimeType {
    public static final String GOOGLE_DOC = "application/vnd.google-apps.document";
    public static final String GOOGLE_UNKNOWN = "application/vnd.google-apps.unknown";
    public static final String GOOGLE_SHEET = "application/vnd.google-apps.spreadsheet";
    public static final String GOOGLE_DRAWING = "application/vnd.google-apps.drawing";
    public static final String PLAIN_TEXT = "text/plain";
    public static final String MS_EXCEL = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    public static final String MS_DOC="application/vnd.openxmlformats-officedocument.wordprocessingml.document";
    public static final String JPEG = "image/jpeg";
    public static final String PDF="application/pdf";
    public static final String GOOGLE_FOLDER="application/vnd.google-apps.folder";
    public static final String OCT_STREAM="application/octet-stream";

    public static boolean ifGoogleDoc(String mimeType) {
        System.out.println("mime: "+mimeType);
        System.out.println("GOOGLE: "+GOOGLE_DOC);
        if ((mimeType.equals(GOOGLE_DOC)) || (mimeType.equals(GOOGLE_SHEET)) || (mimeType.equals(GOOGLE_DRAWING)))
            return true;
        return false;
    }

    public static String convertExportType(String mimeType, String fileName)
    {
        if (mimeType.equals(GOOGLE_DOC)) {
            if (ifMSDoc(fileName)) return MS_DOC;
            else return PLAIN_TEXT;
        }
        if (mimeType.equals(GOOGLE_SHEET)) return MS_EXCEL;
        if (mimeType.equals(GOOGLE_DRAWING)) return JPEG;
        return PDF;
    }



    private static boolean ifMSDoc(String fileName) {
        if (fileName.contains(".doc")) return true;
        else return false;
    }
}
