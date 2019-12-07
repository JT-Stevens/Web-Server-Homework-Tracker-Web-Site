/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package homeworktracker;

import BusinessEntities.HomeWork;
import BusinessEntities.HomeworkType;
import BusinessEntities.Student;
import DataAcess.HomeWorkFillDb;
import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.*;
/**
 *
 * @author Jeremy Stevens
 */
public class RequestProcessor implements Runnable {

    private final static Logger logger = Logger.getLogger(RequestProcessor.class.getCanonicalName());

    private File rootDirectory;
    private String indexFileName = "index.html";
    private Socket connection;

    public RequestProcessor(File rootDirectory, String indexFileName, Socket connection) {
        if (rootDirectory.isFile()) {
            throw new IllegalArgumentException("Expecting directory. got file");
        }
        try {
            rootDirectory = rootDirectory.getCanonicalFile();
        } catch (IOException ex) {

        }
        this.rootDirectory = rootDirectory;

        if (indexFileName != null) {
            this.indexFileName = indexFileName;
            this.connection = connection;
        }
    }

    @Override
    public void run() {
        //For security checks
        String root = rootDirectory.getPath();
        HomeWorkFillDb fillDb = new HomeWorkFillDb();

        try {
            OutputStream raw = new BufferedOutputStream(connection.getOutputStream());
            Writer out = new OutputStreamWriter(raw);
            Reader in = new InputStreamReader(new BufferedInputStream(connection.getInputStream()), "US-ASCII");

            StringBuilder requestLine = new StringBuilder();
            while (true) {
                int c = in.read();
                if (c == '\r' || c == '\n') {
                    break;
                }
                requestLine.append((char) c);
            }

            String get = requestLine.toString();

            logger.info(connection.getRemoteSocketAddress() + " " + get);

            String[] tokens = get.split("\\s+");
//            String[] tokens = get.split("/");
            String method = tokens[0];
            String version = "";
            if (method.equals("GET")) {
                String fileName = tokens[1];
                if (fileName.endsWith("/")) {
                    fileName += indexFileName;
                }
                String contentType = URLConnection.getFileNameMap().getContentTypeFor(fileName);
                if (tokens.length > 2) {
                    version = tokens[2];
                }

                File theFile = new File(rootDirectory, fileName.substring(1, fileName.length()));

                //Don't let clients outside the document root
                if (theFile.canRead() && theFile.getCanonicalPath().startsWith(root)) {

                    byte[] theData = Files.readAllBytes(theFile.toPath());
                    if (version.startsWith("HTTP/")) {
                        sendHeader(out, "HTTP/1.0 200 OK", contentType, theData.length);
                    }

                    raw.write(theData);
                    raw.flush();
                } else {//No file
                    HtmlViews view = new HtmlViews();
                    String body;
                    switch (fileName.substring(1, fileName.length())) {
                        case "Assignments/SrA":
                            body = view.ShowClassAssignments(1);
                            sendHeader(out, "HTTP/1.0 200 OK", contentType, body.length());

                            break;
                        case "Assignments/SrB":
                            body = view.ShowClassAssignments(2);
                            sendHeader(out, "HTTP/1.0 200 OK", contentType, body.length());

                            break;
                        case "Assignments/JrA":
                            body = view.ShowClassAssignments(3);
                            sendHeader(out, "HTTP/1.0 200 OK", contentType, body.length());

                            break;
                        case "Assignments/JrB":
                            body = view.ShowClassAssignments(4);
                            sendHeader(out, "HTTP/1.0 200 OK", contentType, body.length());

                            break;
                        case "CreateAssignment":
                            body = view.createAssignment();
                            sendHeader(out, "HTTP/1.0 200 OK", contentType, body.length());

                            break;

                        case "CreateStudent":
                            body = view.createStudent();
                            sendHeader(out, "HTTP/1.0 200 OK", contentType, body.length());

                            break;
                        case "Students/SrA":
                            body = view.showStudentsByClass(1);
                            sendHeader(out, "HTTP/1.0 200 OK", contentType, body.length());

                            break;
                        case "Students/SrB":
                            body = view.showStudentsByClass(2);
                            sendHeader(out, "HTTP/1.0 200 OK", contentType, body.length());

                            break;
                        case "Students/JrA":
                            body = view.showStudentsByClass(3);
                            sendHeader(out, "HTTP/1.0 200 OK", contentType, body.length());

                            break;
                        case "Students/JrB":
                            body = view.showStudentsByClass(4);
                            sendHeader(out, "HTTP/1.0 200 OK", contentType, body.length());

                            break;
                        default:
                            body = new StringBuilder("<HTML>\r\n")
                                    .append("<head><title>File not Found</title>\r\n")
                                    .append("</head>\r\n")
                                    .append("<body>")
                                    .append("<h1>HTTP Error 404 File Not Found</h1>\r\n")
                                    .append("</body></html>\r\n").toString();

                            if (version.startsWith("HTTP/")) {
                                sendHeader(out, "HTTP/1.0 404 File Not Found", "text/html; charset = utf-8", body.length());
                            }
                    }

                    out.write(body);
                    out.flush();

                }
            } else if (method.equals("POST")) {
                String fileName = tokens[1];
                HtmlViews view = new HtmlViews();
                String body;

                BufferedReader br = new BufferedReader(in);
                StringBuilder payload;
                String[] payloadInfo;
                String[] userData;

                payload = new StringBuilder();
                while (br.ready()) {
                    payload.append((char) br.read());
                }
                payloadInfo = payload.toString().split("\\s+");
                userData = payloadInfo[payloadInfo.length - 1].split("&");

                HashMap<String, String> queryStringData = new HashMap();
                for (String qs : userData) {
                    queryStringData.put(qs.substring(0, qs.indexOf("=")), qs.substring(qs.indexOf('=') + 1, qs.length()));
                }

                for (Map.Entry<String, String> entry : queryStringData.entrySet()) {
                    entry.setValue(entry.getValue().replace("+", " "));
                }

                switch (fileName.substring(1, fileName.length())) {
                    case "createAssignment":

//                       if(queryStringData.size() != 4){
//                           out.write("<h1>Missing data!</h1>");
//                           out.flush();
//                           return;
//                       }
                        if (queryStringData.get("desc") == null
                                || queryStringData.get("homeworkType") == null
                                || queryStringData.get("course") == null
                                || queryStringData.get("class") == null) {

                            out.write("<h1>Missing data!</h1>");
                            out.flush();
                            return;
                        }

                        HomeWork hw = new HomeWork();

                        hw.setHomeworkDescription(queryStringData.get("desc"));
                        hw.setHomeworkType(HomeworkType.values()[Integer.parseInt(queryStringData.get("homeworkType"))]);
                        if (queryStringData.get("due") != null) {
                            hw.setDueDate(new SimpleDateFormat("yyyy-mm-dd").parse(queryStringData.get("due")));
                        }
                        hw.setCourseID(Integer.parseInt(queryStringData.get("course")));
                        hw.setClassID(Integer.parseInt(queryStringData.get("class")));

                        fillDb.insertAssignment(hw);
                        body = view.createAssignment();
                        out.write(body);
                        out.flush();

                        break;

                    case "CreateStudent":

                        if (queryStringData.get("name") == null
                                || queryStringData.get("class") == null) {

                            out.write("<h1>Missing data!</h1>");
                            out.flush();
                            return;
                        }

                        Student student = new Student();
                        student.setName(queryStringData.get("name"));
                        student.setClassID(Integer.parseInt(queryStringData.get("class")));

                        if (!fillDb.insertStudent(student)) {
                            body = "<h1>Student Save failed<h1>";
                        } else {
                            body = view.createStudent();
                        }
                        
                        out.write(body);
                        out.flush();
                        break;

                    default:
                        body = new StringBuilder("<HTML>\r\n")
                                .append("<head><title>File not Found</title>\r\n")
                                .append("</head>\r\n")
                                .append("<body>")
                                .append("<h1>HTTP Error 404 File Not Found</h1>\r\n")
                                .append("</body></html>\r\n").toString();

                        if (version.startsWith("HTTP/")) {
                            sendHeader(out, "HTTP/1.0 404 File Not Found", "text/html; charset = utf-8", body.length());
                        }
                        out.write(body);
                        out.flush();

                        break;
                }

            } else { // method is not implemented
                String body = new StringBuilder("<HTML>\r\n")
                        .append("<head><title>Not Implemented</title>\r\n")
                        .append("</head>\r\n")
                        .append("<body>")
                        .append("<h1>HTTP Error 501: Not Implemented</h1>\r\n")
                        .append("</body></html>\r\n").toString();

                if (version.startsWith("HTTP/")) {
                    sendHeader(out, "HTTP/1.0 404 File Not Found", "text/html; charset = utf-8", body.length());
                }
                out.write(body);
                out.flush();
            }
        } catch (IOException ex) {
            logger.log(Level.WARNING, "Error talking to " + connection.getRemoteSocketAddress(), ex);
        } catch (ParseException ex) {
            Logger.getLogger(RequestProcessor.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                connection.close();
            } catch (IOException ex) {
            }
        }
    }

    private void sendHeader(Writer out, String responseCode, String contentType, int length) throws IOException {
        out.write(responseCode + "\r\n");
        Date now = new Date();
        out.write("Date: " + now + "\r\n");
        out.write("content-length: " + length + "\r\n");
        out.write("Content-type: " + contentType + "\r\n\r\n");
        out.flush();
    }

}
