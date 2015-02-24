import java.io.*;


public class q6_mr1_reducer {
	
	public static void main(String[] args) throws IOException {
		//System.setIn(new java.io.FileInputStream(new java.io.File(args[0])));
		//System.setOut(new PrintStream(new File(args[1])));
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String line;
		
		String prev_key = "";
		String user_id = null;
		int counter = 0;
		
		while ((line = br.readLine()) != null) {
			int limiter = line.indexOf('\t');
			String curr_key = line.substring(0, limiter);
			if (!curr_key.equals(prev_key)) {
				prev_key = curr_key;
				String curr_user = line.substring(0, line.indexOf('|'));
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
		}
		System.out.println(user_id + "\t" + counter);
	}
	
}
