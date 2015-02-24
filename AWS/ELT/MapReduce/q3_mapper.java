import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class q3_mapper {
	public static void main(String[] args) {
		try {
			//System.setIn(new java.io.FileInputStream("sample"));
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			JSONParser parser = new JSONParser();
			String line;
			while((line = br.readLine()) != null) {
				if (line.contains("retweeted_status")) {
					JSONObject tweet_obj = (JSONObject)parser.parse(line);
					if (tweet_obj.containsKey("retweeted_status") && ((JSONObject)tweet_obj.get("retweeted_status")) != null) {
						String user_a = (String)((JSONObject)tweet_obj.get("user")).get("id_str");
						JSONObject retweet_status_obj = (JSONObject)tweet_obj.get("retweeted_status");
						if (retweet_status_obj.containsKey("user") && retweet_status_obj.get("user") != null) {
							String user_x = (String)((JSONObject)((JSONObject)tweet_obj.get("retweeted_status")).get("user")).get("id_str");
							System.out.println(user_x + '\t' + user_a);
							System.out.println(user_a + '\t' + '\'' + user_x);
						}
					}
				}
			}
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (ParseException parsee) {
			parsee.printStackTrace();
		}
	}
}
