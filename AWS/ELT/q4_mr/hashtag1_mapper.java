import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class hashtag1_mapper {
	public static void main(String[] args) {
		try {
			//System.setIn(new java.io.FileInputStream("sample"));
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			JSONParser parser = new JSONParser();
			String line;
			while((line = br.readLine()) != null) {
				if (line.length() > 1) {
					JSONObject tweet_obj = (JSONObject)parser.parse(line);
					String location = "";
					
					if (tweet_obj.containsKey("place") && tweet_obj.get("place") != null && ((JSONObject)tweet_obj.get("place")).containsKey("name") && ((JSONObject)tweet_obj.get("place")).get("name") != null) {
						location = (String)((JSONObject)tweet_obj.get("place")).get("name");
					}
					
					if (location == null || location.length() < 1) {
						if (((JSONObject)tweet_obj.get("user")).get("time_zone") != null) {
							location = (String) ((JSONObject)tweet_obj.get("user")).get("time_zone");
							Matcher matcher = Pattern.compile("\\btime\\b").matcher(location.toLowerCase());
							if (matcher.find()) {
								continue;
							}
						} else {
							continue;
						}
					}
					
					if (location == null || location.length() < 1)
						continue;
					
					String created_time = (String) tweet_obj.get("created_at");
					try {
						Date date = new SimpleDateFormat("EEE MMM d HH:mm:ss +0000 yyyy", Locale.ENGLISH).parse(created_time);
						DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
						created_time = df.format(date);
					} catch (java.text.ParseException e) {
						e.printStackTrace();
					}
					
					String tweet_id = (String) ((JSONObject) tweet_obj).get("id_str");
					JSONArray tag_list = (JSONArray)((JSONObject)tweet_obj.get("entities")).get("hashtags");
					if (tag_list == null) {
						continue;
					} else {
						HashMap<String, Integer> hashtag_map = new HashMap<String, Integer>();
						for (int i = 0; i < tag_list.size(); i++) {
							String hash_text = (String) ((JSONObject) tag_list.get(i)).get("text");
							if (!hashtag_map.containsKey(hash_text)) {
								hashtag_map.put(hash_text, i);
							}
						}
						location = StringEscapeUtils.escapeJson(location);
						for (Map.Entry<String, Integer> e : hashtag_map.entrySet()) {
							System.out.println(location + '+' + created_time + "|" + StringEscapeUtils.escapeJson(e.getKey()) + "\t" + tweet_id + "|" + e.getValue());
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
