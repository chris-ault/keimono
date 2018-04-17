package keimono;


//Untested unclean Good resources


import org.apache.commons.net.ftp.*;

public class FtpDownload {
	private void ftpDownload() {
		/*
	    FTPClient ftp = null;
	    try {
	        ftp = new FTPClient();
	        ftp.connect(mServer);

	        try {
	            int reply = ftp.getReplyCode();
	            if (!FTPReply.isPositiveCompletion(reply)) {
	                throw new Exception("Connect failed: " + ftp.getReplyString());
	            }
	            if (!ftp.login(mUser, mPassword)) {
	                throw new Exception("Login failed: " + ftp.getReplyString());
	            }
	            try {
	                ftp.enterLocalPassiveMode();
	                if (!ftp.setFileType(FTP.BINARY_FILE_TYPE)) {
	                    Log.e(TAG, "Setting binary file type failed.");
	                }
	                transferFile(ftp);
	            } catch(Exception e) {
	                handleThrowable(e);
	            } finally {
	                if (!ftp.logout()) {
	                    Log.e(TAG, "Logout failed.");
	                }
	            }
	        } catch(Exception e) {
	            handleThrowable(e);
	        } finally {
	            ftp.disconnect();
	        }
	    } catch(Exception e) {
	        handleThrowable(e);
	    }
	}

	private void transferFile(FTPClient ftp) throws Exception {
	    long fileSize = getFileSize(ftp, mFilePath);
	    InputStream is = retrieveFileStream(ftp, mFilePath);
	    downloadFile(is, buffer, fileSize);
	    is.close();

	    if (!ftp.completePendingCommand()) {
	        throw new Exception("Pending command failed: " + ftp.getReplyString());
	    }
	}

	private InputStream retrieveFileStream(FTPClient ftp, String filePath)
	throws Exception {
	    InputStream is = ftp.retrieveFileStream(filePath);
	    int reply = ftp.getReplyCode();
	    if (is == null
	            || (!FTPReply.isPositivePreliminary(reply)
	                    && !FTPReply.isPositiveCompletion(reply))) {
	        throw new Exception(ftp.getReplyString());
	    }
	    return is;
	}

	private byte[] downloadFile(InputStream is, long fileSize)
	throws Exception {
	    byte[] buffer = new byte[fileSize];
	    if (is.read(buffer, 0, buffer.length)) == -1) {
	        return null;
	    }
	    return buffer; // <-- Here is your file's contents !!!
	}

	private long getFileSize(FTPClient ftp, String filePath) throws Exception {
	    long fileSize = 0;
	    FTPFile[] files = ftp.listFiles(filePath);
	    if (files.length == 1 && files[0].isFile()) {
	        fileSize = files[0].getSize();
	    }
	    Log.i(TAG, "File size = " + fileSize);
	    return fileSize;
	}
*/
}

}
