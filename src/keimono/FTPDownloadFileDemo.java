package keimono;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import com.google.gson.*; 

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
 
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
 
            // APPROACH #1: using retrieveFile(String, OutputStream)
            String remoteFile1 = "data/2018/thehackernews.com/2010_10_100-brazilian-website-hacked-by_1522339857.html.json";
            File downloadFile1 = new File("C:\\Users\\captn\\Documents\\School\\Capstone\\sitelist.hjson");
            OutputStream outputStream1 = new BufferedOutputStream(new FileOutputStream(downloadFile1));
            boolean success = ftpClient.retrieveFile(remoteFile1, outputStream1);
            outputStream1.close();
 
            if (success) {
                System.out.println("File #1 has been downloaded successfully.");
            }
            
            //2018/thehackernews.com/2010_10_100-brazilian-website-hacked-by_1522339857.html.json
            // APPROACH #1: using retrieveFile(String, OutputStream)
            remoteFile1 = "data/2018/thehackernews.com/2010_10_100-brazilian-website-hacked-by_1522339857.html.json";
            downloadFile1 = new File("C:\\Users\\captn\\Documents\\School\\Capstone\\2010_10_100-brazilian-website-hacked-by_1522339857.html.json");
           outputStream1 = new BufferedOutputStream(new FileOutputStream(downloadFile1));
            success = ftpClient.retrieveFile(remoteFile1, outputStream1);
            outputStream1.close();
 
            if (success) {
                System.out.println("File #2 has been downloaded successfully.");
            }else {
            	System.out.println(success);
            }
            
            // APPROACH #2: using InputStream retrieveFileStream(String)
            String remoteFile2 = "/crawlerStartTimes.txt";
            File downloadFile2 = new File("C:\\Users\\captn\\Documents\\School\\Capstone\\crawler2.0.txt");
            OutputStream outputStream2 = new BufferedOutputStream(new FileOutputStream(downloadFile2));
            InputStream inputStream = ftpClient.retrieveFileStream(remoteFile2);
            byte[] bytesArray = new byte[4096];
            int bytesRead = -1;
            while ((bytesRead = inputStream.read(bytesArray)) != -1) {
                outputStream2.write(bytesArray, 0, bytesRead);
            }
 
            success = ftpClient.completePendingCommand();
            if (success) {
                System.out.println("File #3 has been downloaded successfully.");
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
        
