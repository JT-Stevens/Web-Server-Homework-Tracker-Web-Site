/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package homeworktracker;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;
import java.util.logging.*;

/**
 *
 * @author Jeremy
 */
public class JHTTP {

    private static final Logger HTTP_LOGGER = Logger.getLogger(JHTTP.class.getCanonicalName());
    private static final int NUM_THREADS = 50;
//    private static final String INDEX_FILE = "index.html";

    private final File rootDirectory;
    private final int port;
    private final String file;

    public JHTTP(File rootDirectory, int port, String file) throws IOException {
        if (!rootDirectory.isDirectory()) {
            throw new IOException(rootDirectory + " does not exist as a directory");
        }
        this.rootDirectory = rootDirectory;
        this.port = port;
        this.file = file;
    }

    public JHTTP(File rootDirectory, int port) throws IOException {
        if (!rootDirectory.isDirectory()) {
            throw new IOException(rootDirectory + " does not exist as a directory");
        }
        this.rootDirectory = rootDirectory;
        this.port = port;
        this.file = "index.html";
    }

    public void start() throws IOException {
        ExecutorService pool = Executors.newFixedThreadPool(NUM_THREADS);

        try (ServerSocket server = new ServerSocket(port)) {
            //Need both of these. Let's make sure they are right.
            HTTP_LOGGER.info("Accepting connections on port " + server.getLocalPort());
            HTTP_LOGGER.info("Documnet Root: " + rootDirectory);

            while (true) {
                try {
                    Socket request = server.accept();
                    Runnable r = new RequestProcessor(rootDirectory, file, request);
                    pool.submit(r);
                } catch (IOException ex) {
                    HTTP_LOGGER.log(Level.WARNING, "Error accepting connection", ex);
                }
            }
        }
    }
}
