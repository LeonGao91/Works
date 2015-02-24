package au.edu.cmu.at.toolkit;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;

public class HttpTools {
	
	private final static String API_KEY = "67316cc2468f62b15ce35797e643195588c45b36";
	private final static String URL_BASE = "http://elgg.cmu.edu.au/elgg/services/api/rest/json/";
	private final static String URL_GETTOKEN = "http://elgg.cmu.edu.au/elgg/services/api/rest/?method=auth.gettoken";
	
	public static String getToken(String username, String password) {
		
		String tokenString = "";	
		String status = "";
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		
		nameValuePairs.add(new BasicNameValuePair("username", username));
		nameValuePairs.add(new BasicNameValuePair("password", password));
		
		InputStream responseInputStream = httpPost(URL_GETTOKEN, nameValuePairs);
		
		try {
			
			Document document = builderFactory.newDocumentBuilder().parse(responseInputStream);
			status = document.getElementsByTagName("status").item(0).getTextContent().toString();
			
			if(status.equals("0")){
				tokenString = document.getElementsByTagName("result").item(0).getTextContent().toString();
			}else{
				tokenString = "invalid";
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return tokenString;

	}
	// limit is the number of blogs got at one time, offset stands for the index of blog
	public static String getFeeds(String token, int limit, int offset) {
		
		String feedsJsonString = "";
		String service = "blog.get_latest_posts";
		String getFeedsURL = URL_BASE + "?method=" + service + "&limit=" + limit +"&offset=" + offset +"&api_key=" + API_KEY + "&auth_token=" + token;
		
		feedsJsonString = httpGet(getFeedsURL);
		
		return feedsJsonString;
	}
	
	public static String getContacts(String token, String programs) {
		
		String contactsJsonString = "";
		String service = "member.get_members_by_program";
		String getContactsURL = URL_BASE + "?method=" + service +"&program=" + programs + "&api_key=" + API_KEY + "&auth_token=" + token;
		
		contactsJsonString = httpGet(getContactsURL);
		
		return contactsJsonString;
		
	}
	
	public static String getInfo(String token, String category){
		String infoJsonString="";
		String service="blog.get_posts_by_tag";
		String getInfoURL=URL_BASE+"?method="+service+"&limit=10&offset=0"+"&tag="+category+"&api_key="+API_KEY+"&auth_token="+token;
		System.out.println("Info URL:"+getInfoURL);
		infoJsonString=httpGet(getInfoURL);
		
		return infoJsonString;
	}
	public static String getBlog(String token, int guid){
		String blogJsonString="";
		String service="blog.get_blog";
		String getBlogURL=URL_BASE+"?method="+service+"&guid="+guid+"&api_key="+API_KEY+"&auth_token="+token;
		//System.out.println("Blog URL:"+getBlogURL);
		blogJsonString=httpGet(getBlogURL);
		
		return blogJsonString;
	}
	private static String httpGet(String url) {
		
		String responseString = "";
		HttpGet httpRequest = new HttpGet(url);
	
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse httpResponse = httpclient.execute(httpRequest);
			
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				responseString = EntityUtils.toString(httpResponse.getEntity());
			} else {
				responseString = "";
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return responseString;
	}
	
	private static InputStream httpPost(String url, List<NameValuePair> nameValuePairs) {
		
		InputStream inputStream = null;
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		
		try {
		
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			
			HttpResponse response = httpClient.execute(httpPost);
			inputStream = response.getEntity().getContent();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return inputStream;
	}
	
	public static String getMemberProfile(String username, String token){
		String profileJsonString = "";
		String service = "member.get_profile";
		String getContactsURL = URL_BASE + "?method=" + service +"&username=" + username + "&api_key=" + API_KEY + "&auth_token=" + token;
		
		profileJsonString = httpGet(getContactsURL);
		return profileJsonString;
	}
}
