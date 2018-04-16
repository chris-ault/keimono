package keimono;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.google.gson.*;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

/**
 * A program demonstrates how to upload files from local computer to a remote
 * FTP server using Apache Commons Net API.
 * @author www.codejava.net
 */
public class FTPDownloadFileDemo {

    public static void main(String[] args) {
        String server = "www.crawler.giize.com";
        int port = 21;
        String user = "spiderftp";
        String pass = "hello123";

        FTPClient ftpClient = new FTPClient();
        try {

            ftpClient.connect(server, port);
            ftpClient.login(user, pass);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            
         // lists files and directories in the current working directory
            FTPFile[] files = ftpClient.listFiles("/data/2018/techrepublic.com/article__1520363275.html.json");
             
            // iterates over the files and prints details for each
            DateFormat dateFormater = new SimpleDateFormat("MM-dd-YYYY HH:mm:ss");
             
            for (FTPFile file : files) {
                String details = file.getName();
                if (file.isDirectory()) {
                    details = "[" + details + "]";
                }
                details += "\t\t" + file.getSize()+" bytes";
                details += "\t\t" + dateFormater.format(file.getTimestamp().getTime());
                System.out.println(details);
            } 
            

            
            // APPROACH #1: using retrieveFile(String, OutputStream)
/*
            String remoteFile1 = "config/sitelist.hjson";
            File downloadFile1 = new File("sitelist.hjson");
            OutputStream outputStream1 = new BufferedOutputStream(new FileOutputStream(downloadFile1));
            boolean success = ftpClient.retrieveFile(remoteFile1, outputStream1);
            outputStream1.close();

            if (success) {
                System.out.println("File #1 has been downloaded successfully.");
            }

            
            // APPROACH #1: using retrieveFile(String, OutputStream)

            remoteFile1 = "data/2018/techrepublic.com/article__1522392853.html.json";
            downloadFile1 = new File("article__1522392853.html.json");
           outputStream1 = new BufferedOutputStream(new FileOutputStream(downloadFile1));
            success = ftpClient.retrieveFile(remoteFile1, outputStream1);
            outputStream1.close();

            if (success) {
                System.out.println("File #2 has been downloaded successfully.");
            }else {
            	System.out.println(success);
            }
*/

            // APPROACH #2: using InputStream retrieveFileStream(String)
            String remoteFile2 = "/data/2018/techrepublic.com/article__1520363275.html.json";
            File downloadFile2 = new File("failfile.txt");
            OutputStream outputStream2 = new BufferedOutputStream(new FileOutputStream(downloadFile2));
            InputStream inputStream = ftpClient.retrieveFileStream(remoteFile2);
            DataInputStream dis = new DataInputStream(inputStream);
            
            byte[] bytesArray = new byte[1000];
            String theFile = new String(bytesArray, "UTF-8"); // for UTF-8 encoding;
            int bytesRead = -1;
            while ((bytesRead = inputStream.read(bytesArray)) != -1) {
            	//System.out.println(dis.readUnsignedByte());
            	            	theFile += new String(bytesArray, "UTF-8");
                outputStream2.write(bytesArray, 0, bytesRead);
            }

            boolean  success = ftpClient.completePendingCommand();
            if (success) {
                System.out.println("File #3 has been stored as string successfully.");
                System.out.println(theFile.replaceAll("\\p{C}", ""));
                System.out.println(theFile.length());
            }
            outputStream2.close();
            inputStream.close();

        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        }
    }

