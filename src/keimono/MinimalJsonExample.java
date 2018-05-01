package keimono;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.ParseException;
import com.eclipsesource.json.WriterConfig;

public class MinimalJsonExample {
	public static void main(String[] args) throws IOException, ParseException {

		// String file that contains the entire JSON File
		String contents = new String(Files.readAllBytes(Paths.get("sitelist.hjson")));

		JsonArray items = Json.parse(contents).asObject().get("base_urls").asArray();

		//  Working Methods (deleteItems & addItem)
		//
		//System.out.println(items.toString(WriterConfig.PRETTY_PRINT));
		//deleteItem(items,9);
		//addItem(items,"http://yahoo.com","RecursiveCrawler");
		//System.out.println(items.toString(WriterConfig.PRETTY_PRINT));

		
		// This was creates a proper object array pair
		// JsonObject test = Json.object().add("base_urls",items);

		JsonArray toEdit = new JsonArray();
		String[] sites = new String[20];
		String[] crawlers = new String[20];
		// for (JsonValue item : items) {
		for (int x = 0; x < items.size(); x++) {
			sites[x] = items.get(x).asObject().getString("url", "Unknown Item");
			crawlers[x] = items.get(x).asObject().getString("crawler", "Unknown Item");
			toEdit = toEdit.asArray().add(Json.object().add("crawler", crawlers[x]).add("url", sites[x]));
		}
		
		//Remove the first URL/index 0
		//toEdit.remove(0);
		
		//Put them all together for submitting to server
		JsonObject test = Json.object().add("base_urls", toEdit);

		//Display before storing
		//System.out.println(test.toString(WriterConfig.PRETTY_PRINT));

		//Store to file
		BufferedWriter writer = new BufferedWriter(new FileWriter("sitelist.hjson", false));
		test.writeTo(writer);
		writer.close();

		JsonArray jsonArray = Json.array().add("Bob").add(42);
		jsonArray = Json.array().add("Jane").add(41);
		jsonArray.set(1, 24);
		// System.out.println(jsonArray.get(0).toString(WriterConfig.PRETTY_PRINT));

	}
	
	
	public static JsonArray deleteItem(JsonArray ar,int pos){
		
		JsonArray newAr = new JsonArray();
		newAr = ar.remove(pos);
		
		return newAr;
	}
	
	public static JsonArray addItem(JsonArray ar,String url,String crawler){
		
		//JsonArray newAr = new JsonArray();
		ar.asArray().add(Json.object().add("crawler", crawler).add("url", url));
		return ar;
	}
	

}
