import java.io.*;
import java.util.*;

public class q5_reducer {
	
	private static class Result {
		private Set<String> s1;
		private Set<String> s2;
		private Set<String> s3;
		
		public Result() {
			s1 = new HashSet<String>();
			s2 = new HashSet<String>();
			s3 = new HashSet<String>();
		}
		
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			int total = s1.size() + s2.size() * 3 + s3.size() * 10;
			sb.append(s1.size()).append('\t').append(s2.size() * 3).append('\t').append(s3.size() * 10).append('\t').append(total);
			return sb.toString();
		}
	}
	
	public static void main(String[] args) throws IOException {
		//System.setIn(new java.io.FileInputStream(new java.io.File("q5_sample_2")));
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String line;
		
		String user_id = null;
		Result result = null;
		
		while ((line = br.readLine()) != null) {
			int limiter = line.indexOf('\t');
			int limiter2 = line.indexOf('|', limiter + 1);
			
			String curr_user = line.substring(0, limiter);
			String curr_flag = line.substring(limiter + 1, limiter2);
			String curr_value = line.substring(limiter2 + 1, line.length());
			
			if (user_id != null && user_id.equals(curr_user)) {
				processing(curr_value, curr_flag, result);
			} else {
				if (user_id != null) {
					System.out.println(user_id + '\t' + result.toString());
				}
				user_id = curr_user;
				result = new Result();
				processing(curr_value, curr_flag, result);
			}
		}
		System.out.println(user_id + '\t' + result.toString());
	}
	
	private static void processing(String v, String f, Result r) {
		if (f.equals("s1")) {
			r.s1.add(v);
		} else if (f.equals("s2")) {
			r.s2.add(v);
		} else {
			r.s3.add(v);
		}
	}
}
