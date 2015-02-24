import java.io.*;

/**
 * CAUTION: This reduce should be run on only ONE reducer!!
 * args -D mapred.reduce.tasks=1
 * 
 * @author Sean
 *
 */
public class q6_mr2_reducer {
	
	public static void main(String[] args) throws IOException {
		//System.setIn(new java.io.FileInputStream("q6_sample2_2"));
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String line;

		String user_id = null;
		int counter = 0;
		
		while ((line = br.readLine()) != null) {
			int limiter = line.indexOf('\t');
			String curr_user = line.substring(0, limiter);
			int curr_value = Integer.parseInt(line.substring(limiter + 1, line.length()));
			
			if (user_id != null && user_id.equals(curr_user)) {
				counter += curr_value;
			} else {
				if (user_id != null) {
					System.out.println(user_id + "\t" + counter);
				}
				user_id = curr_user;
				counter = 0;
				counter += curr_value;
			}
		}
		System.out.println(user_id + "\t" + counter);
	}
	
}
