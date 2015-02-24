import java.io.*;

import org.json.simple.*;
import org.json.simple.parser.*;


public class q6_mr1_mapper {

	public static void main(String[] args) throws IOException, ParseException {
		//System.setIn(new java.io.FileInputStream(new java.io.File("q6_sample_1")));
		//System.setOut(new PrintStream(new File("q6_sample_2")));
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String line;
		
		JSONParser parser = new JSONParser();
		
		while ((line = br.readLine()) != null) {
			if (line.length() > 1 && line.contains("photo")) {	// ignore empty lines and non-photo tweets
				JSONObject obj = (JSONObject) parser.parse(line);
				if (obj.containsKey("entities") && obj.get("entities") != null) {
					JSONObject entities_obj = (JSONObject) obj.get("entities");
					if (entities_obj.containsKey("media") && entities_obj.get("media") != null) {
						String user_id = (String) ((JSONObject) obj.get("user")).get("id_str");
						String tweet_id = (String) obj.get("id_str");
						JSONArray media_array = (JSONArray) entities_obj.get("media");
						int counter = 0;
						for (Object item : media_array) {
							JSONObject item_obj = (JSONObject) item;
							if (item_obj.containsKey("type") && ((String) item_obj.get("type")).equals("photo")) {
								counter++;
							}
						}
						System.out.println(user_id + "|" + tweet_id + "\t" + counter);
					}
				}
			}
		}
	}

}
