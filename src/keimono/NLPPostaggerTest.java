package keimono;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import com.google.gson.*;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class NLPPostaggerTest {

	public static void main(String[] args) throws IOException, ClassNotFoundException{

		//Initialize the tagger
		MaxentTagger tagger = new MaxentTagger("C:\\Users\\Louis\\Documents\\keimono\\rsc\\english-bidirectional-distsim.tagger");

		//String sample = "This is a small piece of sample text";
		//build stuff needed for Gson
		String path = "2010_10_100-brazilian-website-hacked-by_1522339857.html.json";
        BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
		String article = parse(RBSI(bufferedReader));

		String tagged = tagger.tagString(article);

		System.out.println(article);
		System.out.println(tagged);

	}

	public static String RBSI(BufferedReader buffIn) throws IOException { //ReadBigStringIn
        StringBuilder everything = new StringBuilder();
        String line;
        while( (line = buffIn.readLine()) != null) {
           everything.append(line);
        }
        return everything.toString();
    }


    public static  String parse(String jsonLine) {
        JsonElement jelement = new JsonParser().parse(jsonLine);
        JsonObject  jobject = jelement.getAsJsonObject();
        String result = jobject.get("text").getAsString();
        return result;
    }


}
