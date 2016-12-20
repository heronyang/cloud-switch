package plugins.googleDrive;

/**
 * Created by inteltao on 2016/12/20.
 * Information Networking Institute, Carnegie Mellon University
 **/
public class mimeType {
    private static final String GOOGLE_DOC = "application/vnd.google-apps.document";
    private static final String GOOGLE_UNKNOWN = "application/vnd.google-apps.unknown";
    private static final String GOOGLE_SHEET = "application/vnd.google-apps.spreadsheet";
    private static final String GOOGLE_DRAWING = "application/vnd.google-apps.drawing";
    private static final String PLAIN_TEXT = "text/plain";
    private static final String MS_EXCEL = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    private static final String MS_DOC="application/vnd.openxmlformats-officedocument.wordprocessingml.document";
    private static final String JPEG = "image/jpeg";
    private static final String PDF="application/pdf";

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
