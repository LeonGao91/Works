import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Phase1Mapper {
	public static HashMap<String, Integer> SentimentScore;
	public static HashSet<String> CensorWords;

	public static void main(String[] args) {

		if (SentimentScore == null) {
			SentimentScore = initializeScore();
		}
		if (CensorWords == null) {
			CensorWords = initializeCensor();
		}

		JSONParser parser = new JSONParser();

		try {
//			 System.setIn(new FileInputStream("sample.txt"));
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

			String input;
			while ((input = br.readLine()) != null) {
				StringBuilder output = new StringBuilder();

				try {
					JSONObject obj = (JSONObject) parser.parse(input);

					String tweet_id = (String) obj.get("id_str");
					String text = (String) obj.get("text");
					String created_time = (String) obj.get("created_at");
					String user_id = (String) ((JSONObject) obj.get("user")).get("id_str");

					try {
						Date date = new SimpleDateFormat(
								"EEE MMM d HH:mm:ss +0000 yyyy", Locale.ENGLISH)
								.parse(created_time);
						DateFormat df = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss");
						created_time = df.format(date);
					} catch (java.text.ParseException e) {
						e.printStackTrace();
					}

					int score = calScore(text);
					String replaced_text = replaceText(text);
					output.append(user_id + '+' + created_time)
							.append('\t')
							.append(tweet_id).append(':')
							.append(score).append(':')
							.append(StringEscapeUtils.escapeJson(replaced_text));
					System.out.println(output.toString());
				} catch (ParseException e) {
					continue;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static int calScore(String text) {
		int score = 0;
		Matcher matcher = Pattern.compile("[^A-Za-z0-9]").matcher(text);
		int start = 0;
		int end;
		while (matcher.find()) {
			end = matcher.start();
			String word = text.substring(start, end);
			String lcase = word.toLowerCase();
			if (SentimentScore.containsKey(lcase)) {
				score += SentimentScore.get(lcase);
			}
			start = matcher.end();
		}
		String word = text.substring(start, text.length());
		word = word.toLowerCase();
		if (SentimentScore.containsKey(word)) {
			score += SentimentScore.get(word);
		}
		return score;
	}

	 public static String replaceText(String originalText) {
//			originalText  = " http://t.co/841ioHyLrN C\tB!tch sh!t!SHIT "+originalText;
		 Pattern spPattern1 = Pattern.compile("\\bb!tch\\b|\\bsh!t\\b",Pattern.CASE_INSENSITIVE);
		 
		 Matcher m = spPattern1.matcher(originalText);
		 StringBuilder replace1 = new StringBuilder();
		 int start = 0;
		 while (m.find()) {
			 int end = m.start();
			 String pre = originalText.substring(start, end);
			 start = m.end();
			 String word = m.group();
			 char[] newWord = new char[word.length()];
				newWord[0] = word.charAt(0);
				for (int i = 1; i < word.length() - 1; i++) {
					newWord[i] = '*';
				}
				newWord[word.length() - 1] = word.charAt(word.length() - 1);
				replace1.append(pre).append(new String(newWord));
		 }
		 replace1.append(originalText.substring(start, originalText.length()));
			originalText = replace1.toString();
			StringBuilder replacing = new StringBuilder();
			Matcher matcher = Pattern.compile("[^A-Za-z0-9]").matcher(originalText);
			start = 0;
			int end;
			while (matcher.find()) {

				end = matcher.start();
				String word = originalText.substring(start, end);
//				String lowerWord = word.toLowerCase();
				if (CensorWords.contains(word.toLowerCase())) {
					char[] newWord = new char[word.length()];
					newWord[0] = word.charAt(0);
					for (int i = 1; i < word.length() - 1; i++) {
						newWord[i] = '*';
					}
					newWord[word.length() - 1] = word.charAt(word.length() - 1);
					replacing.append(new String(newWord)).append(matcher.group());
				} else {
					replacing.append(word).append(matcher.group());
				}
				start = matcher.end();
				// System.out.println(matcher.start() + ":" + matcher.group());
			}
			// check the last word
			String word = originalText.substring(start, originalText.length());
			if (CensorWords.contains(word.toLowerCase())) {
				char[] newWord = new char[word.length()];
				newWord[0] = word.charAt(0);
				for (int i = 1; i < word.length() - 1; i++) {
					newWord[i] = '*';
				}
				newWord[word.length() - 1] = word.charAt(word.length() - 1);

				replacing.append(new String(newWord));
			} else {
				replacing.append(word);
			}
			

			return replacing.toString();
		}

	private static HashMap<String, Integer> initializeScore() {
		SentimentScore = new HashMap<String, Integer>();
		URL url;
		try {
			url = new URL(
					"https://s3.amazonaws.com/F14CloudTwitterData/AFINN.txt");
			URLConnection conn = url.openConnection();
			InputStream is = conn.getInputStream();

			if (is != null) {

				BufferedReader br = new BufferedReader(new InputStreamReader(
						is, "UTF-8"));

				String line;
				while ((line = br.readLine()) != null) {
					int tab = line.indexOf("\t");

					SentimentScore.put(line.substring(0, tab),
							Integer.valueOf(line.substring(tab + 1)));
				}
				br.close();

			}
		} catch (MalformedURLException ex) {
			SentimentScore = null;
		} catch (IOException ex) {
			SentimentScore = null;
		}
		return SentimentScore;
	}

	public static HashSet<String> initializeCensor() {
		CensorWords = new HashSet<String>();
		URL url;
		try {
			url = new URL(
					"https://s3.amazonaws.com/F14CloudTwitterData/banned.txt");
			URLConnection conn = url.openConnection();
			InputStream is = conn.getInputStream();

			if (is != null) {

				BufferedReader br = new BufferedReader(new InputStreamReader(
						is, "UTF-8"));

				String line;
				while ((line = br.readLine()) != null) {
					String newWord = convertRot13(line);
					CensorWords.add(newWord);
				}
				br.close();
			}
		} catch (MalformedURLException ex) {
			CensorWords = null;
		} catch (IOException ex) {
			CensorWords = null;
		}
		return CensorWords;
	}

	public static String convertRot13(String text) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if (c >= 'a' && c <= 'm')
				c += 13;
			else if (c >= 'A' && c <= 'M')
				c += 13;
			else if (c >= 'n' && c <= 'z')
				c -= 13;
			else if (c >= 'N' && c <= 'Z')
				c -= 13;
			sb.append(c);
		}
		return sb.toString();
	}
}
