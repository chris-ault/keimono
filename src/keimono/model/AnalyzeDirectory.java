package keimono.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

import javafx.concurrent.Task;

import keimono.model.Article;

@SuppressWarnings("unused")
public class AnalyzeDirectory extends Task<ArrayList<Article>> {

	private static int problemFile = 0;
	private final String[] relevantWords;
	private final FTPClient ftpClient;
	private final MaxentTagger tagger;
	private int fileCount = 0;
	private int filesCompleted = 0;
	private final String selectedDirectory;
	private ArrayList<Article> taggedArticles;

	public AnalyzeDirectory(FTPClient client, String[] words, MaxentTagger tagger, String directory) {
		this.ftpClient = client;
		this.relevantWords = words;
		this.tagger = tagger;
		this.selectedDirectory = directory;
		taggedArticles = new ArrayList<Article>();
	}

	@Override
	protected ArrayList<Article> call() throws Exception {

		try {
			String rootDir = "/data/2018/";
			String curDir = rootDir + selectedDirectory + "/";
			System.out.println(curDir);
			FTPFile[] files = ftpClient.listFiles(curDir + "*.json");

			analyzeDir(curDir, files);

		} catch (UnknownHostException ho) {
			System.out.println("Server MIA");
		} catch (IOException e) {
			e.printStackTrace();
		}

		return taggedArticles;
	}

	private void analyzeDir(String dir, FTPFile files[]) {

		// int i = 0;

		System.out.println("File count: " + files.length);
		int fileCount = (int) files.length;

		for (int i = 0; i < files.length; ++i) {
			if (this.isCancelled()) {
				updateMessage("Cancelled");
				break;
			}
			// update progress bar
			this.updateProgress(i, files.length);
			float complete = (float) i / (float) files.length * (float) 100;
			updateMessage(String.format("File %d out of %d in directory %.0f%% complete\tWith %d problem files", i,
					files.length, complete, problemFile));
			// System.out.printf(i + " out of " + files.length +" "+
			// (double)i/(double)files.length*(float)100 +"% complete");
			System.out.println(dir + files[i].getName().toLowerCase());
			downloadAnalyzeDisplay(dir + files[i].getName());
		}

	}

	public String RBSI(BufferedReader buffIn) throws IOException { // ReadBigStringIn
		StringBuilder everything = new StringBuilder();
		String line;
		while ((line = buffIn.readLine()) != null) {
			everything.append(line);
		}
		return everything.toString();
	}

	public void downloadAnalyzeDisplay(String address) throws JsonSyntaxException {

		String theFile = "";
		try {
			System.out.println("The file to test is [" + address + "]");
			boolean online = true;
			if (online) {
				// APPROACH #2: using InputStream retrieveFileStream(String)
				String remoteFile2 = address;
				// File downloadFile2 = new File("crawler2.0.txt");
				// OutputStream outputStream2 = new BufferedOutputStream(new
				// FileOutputStream(downloadFile2));
				InputStream inputStream = ftpClient.retrieveFileStream(remoteFile2);
				byte[] bytesArray = new byte[1000000];
				theFile = ""; // Not setting an an encoding for UTF-8 encoding;
				int bytesRead = -1;
				while ((bytesRead = inputStream.read(bytesArray)) != -1) {
					// outputStream2.write(bytesArray, 0, bytesRead); // Don't need to save the file
					// anymore

					theFile += new String(bytesArray, "UTF-8"); // This is more than likely just "UTF-8" but guess
																// encoding is safe
				}
				// System.out.println("---Encoding of File appears to be" +
				// guessEncoding(bytesArray));
				boolean success = ftpClient.completePendingCommand();
				if (success) {
					// System.out.println("\n\n---File has been stored as string successfully.");
				} // else ftp layer problem
					// outputStream2.close();
				inputStream.close();
			}
			if (!online) {
				// String sample = "This is a small piece of sample text";
				// build stuff needed for Gson
				// String path = "article__1522392853.html.json";
				BufferedReader bufferedReader = new BufferedReader(new FileReader(address));
				theFile = RBSI(bufferedReader); // This parses a file reader buffered reader, we now have a string
												// instead
			}

			try { // Clean that text!
				theFile = cleanTextContent(theFile);
				// This parses the json string and removes non printable's and extra '_' that
				// cause trouble
				if (getLastnCharacters(theFile, 2).equals("\"}")) {
					//create article object from current article to tag
					Article theArticle = new Article(parse(theFile, "title"), parse(theFile, "url"), parse(theFile, "filename"), parse(theFile, "text"));
					String printable = parse(theFile, "text");
					String article = printable.replaceAll("_", "");
					System.out.print("Tagging article now...");
					String tagged = tagger.tagString(article);
					int count = StringUtils.countMatches(tagged, "_"); // How many parts of speech?
					System.out.println("Tagged, Relevancy info follows...");
					Boolean good = false;
					int positive = 0;
					double relevancy = 0.0;
					for (int i = 0; i < count; i++) { // Loop for each part
						String[] group = tagged.split(" "); // Separate words
						String[] pos = group[i].split("_"); // Break groups up
						//pos[1] is the part of speech
						if (pos[1].charAt(0) == 78) { // 'N' words
							good = true;
						} else if (pos[1].charAt(0) == 86) { // 'V' words
							good = true;
						} else {
							// System.out.println(pos[0]+" is garbage\n");
						}
						if (good) { // Good words match relevant words?
							for (int x = 0; x < relevantWords.length; ++x) {
								if (relevantWords[x].toLowerCase().compareTo(pos[0].toLowerCase()) == 0) {
									relevancy = (double) ++positive / (double) i * 100.0;
									System.out.printf("\t\t\t\t%d count / %d total\n", positive, i);
									theArticle.addKeywordHit(relevantWords[x]); //add keyword hit to the article object
								}
							}
							good = false;
						}
					}
					relevancy = (double) positive / (double) count * 100.0;
					System.out.printf("%d occurances in %d count,\t \n\n", positive, count);
					theArticle.setCount();			//initialize hitcount property of article
					taggedArticles.add(theArticle);	//add article to list
					filesCompleted++;
					// Increment progress when file is parsed
				} else {
					System.out.println("Incomplete download! Error count is " + ++problemFile + "\n\n");
					filesCompleted++;

				}
			} catch (JsonSyntaxException j) {
				problemFile++;

				System.out.println("Json Problems in file " + address + " & " + problemFile + " problem files\n\n");

			}

		} catch (IOException ex) {
			System.out.println("Error: " + ex.getMessage());
			ex.printStackTrace();
		}
	}

	private String cleanTextContent(String text) {
		// strips off all non-ASCII characters
		text = text.replaceAll("[^\\x00-\\x7F]", "");

		// replace all the ASCII control characters with space
		text = text.replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", " ");

		// removes non-printable characters from Unicode
		text = text.replaceAll("\\p{C}", "");

		// other
		text = text.replaceAll("\\n", " ");

		return text.trim();
	}

	public String getLastnCharacters(String inputString, int subStringLength) {
		int length = inputString.length();
		if (length <= subStringLength) {
			return inputString;
		}
		int startIndex = length - subStringLength;
		return inputString.substring(startIndex);
	}

	public String parse(String jsonLine, String elementToGet) throws IOException {
		JsonElement jelement = new JsonParser().parse(jsonLine);
		JsonObject jobject = jelement.getAsJsonObject();
		if (!jobject.get(elementToGet).isJsonNull()) {
			String result = jobject.get(elementToGet).getAsString();
			return result;
		} else {
			throw new IOException();
		}
	}

}
