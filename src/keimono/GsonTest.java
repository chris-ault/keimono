package keimono;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import com.google.gson.*;


public class GsonTest {

    public static void main(String[] args) throws IOException {
        String path = "2010_10_100-brazilian-website-hacked-by_1522339857.html.json";
        BufferedReader bufferedReader = new BufferedReader(new FileReader(path));

        System.out.println(parse(RBSI(bufferedReader)));

     
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
        String result = jobject.get("title").getAsString();
        return result;
    }
    
}