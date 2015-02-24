import java.io.*;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class q5_mapper {
	public static void main(String[] args) throws IOException, ParseException {
		//System.setIn(new java.io.FileInputStream(new java.io.File("q5_sample_1")));
		//System.setOut(new PrintStream(new File("q5_sample_2")));
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String line;
		
		JSONParser parser = new JSONParser();
		
		while ((line = br.readLine()) != null) {
			if (line.length() > 1) {	// ignore empty lines
				JSONObject obj = (JSONObject) parser.parse(line);
				String tweet_id = (String) obj.get("id_str");
				String user_id = (String) ((JSONObject) obj.get("user")).get("id_str");
				
				System.out.println(user_id + "\ts1|" + tweet_id);	// score 1
				
				if (obj.containsKey("retweeted_status") && obj.get("retweeted_status") != null) {
					String retweeted_user = (String) ((JSONObject)((JSONObject) obj.get("retweeted_status")).get("user")).get("id_str");
					System.out.println(retweeted_user + "\ts2|" + tweet_id);	// score 2
					System.out.println(retweeted_user + "\ts3|" + user_id);		// score 3
				}
			}
		}
	}
}
