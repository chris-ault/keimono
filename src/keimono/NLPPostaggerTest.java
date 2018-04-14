package keimono;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import com.google.gson.*;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class NLPPostaggerTest {

	public static void main(String[] args) throws IOException, ClassNotFoundException{
		String[] relevantWords = {"state","appealing","Spyware","problem","rescue","suffer","infection","infecting"};
		//Initialize the tagger
		MaxentTagger tagger = new MaxentTagger("rsc\\english-bidirectional-distsim.tagger");

		//String sample = "This is a small piece of sample text";
		//build stuff needed for Gson
		String path = "article__1522392853.html.json";
        BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
		String article = parse(RBSI(bufferedReader));

		String tagged = tagger.tagString(article);
		int count = StringUtils.countMatches(tagged, "_"); 				//How many parts of speech?
		System.out.println(article+"\n\n\n\n"+tagged);
		Boolean good = false;
		int positive = 0;
		 double relevancy = 0.0 ;
		for(int i=0;i<count;i++) {										//Loop for each part
		String[] group = tagged.split(" ");								//Separate words
		//System.out.println(group[i]);
		String[] pos = group[i].split("_");								//Break groups up
		//System.out.println(pos[0]+pos[1]);  							//pos[1] is the part of speech
			if ( 	pos[1].charAt(0)==78) {									//N words
				System.out.println(pos[0]+" is a noun");
				good = true;
			} else
				if(	pos[1].charAt(0)==86) {							//V words
					System.out.println(pos[0]+" is a Verb");
					good = true;
			}else{
				//System.out.println(pos[0]+" is garbage\n");
			}
			if(good) {												//Good words match relevant words?
				for(int x=0; x<relevantWords.length; ++x){
				    if(relevantWords[x].toLowerCase().compareTo(pos[0].toLowerCase()) == 0){
				   //System.out.println("Matched on "+pos[0]+" !!This is really good!!");
				    relevancy = (float)positive++/(float)i*100;
				    	System.out.printf("\t\t\t\t%.2f%% Relevancy so far, %d count / %d total\n",relevancy,positive,i);
				    }
				}
				good = false;
			}
		}
		System.out.printf("%d occurances in %d count,\t %.2f%% Relevancy\n",positive,count,relevancy);	}

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
