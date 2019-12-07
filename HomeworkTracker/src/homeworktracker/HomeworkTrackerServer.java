/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package homeworktracker;

import BusinessEntities.HomeWork;
import DataAcess.HomeWorkFillDb;
import java.io.*;
import java.net.*;
import java.util.Date;
import java.util.concurrent.*;
import java.util.logging.*;

/**
 * @author Jeremy Stevens
 * @project HomeworkTracker
 * @date 2019/11/13
 */
public class HomeworkTrackerServer {

    //Server port
//    public final static int PORT = 13;
    private final static Logger AUDIT_LOGGER = Logger.getLogger("requests");
    private final static Logger ERROR_LOGGER = Logger.getLogger("errors");

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        File docroot;
        try {
            docroot = new File("D:\\src\\JavaWebServer\\Web-Server-Homework-Tracker-Web-Site\\WebPages");
//            docroot = new File(args[0]);
        } catch (ArrayIndexOutOfBoundsException ex) {
            printLine("Usage: java JHTTP docroot port");
            return;
        }                              

        //Set the port to listen on
        int port = 80;
        
        try{
            JHTTP webserver = new JHTTP(docroot, port);
            webserver.start();
        } catch(IOException ex){
            ERROR_LOGGER.log(Level.SEVERE, "Server could not start", ex);
        }

    }

    static void printLine(String msg) {
        System.err.println(msg);

    }
}

   