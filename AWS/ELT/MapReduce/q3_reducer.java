import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class q3_reducer {
	public static void main(String[] args) {
		try {
			//System.setIn(new java.io.FileInputStream("retweet_sample"));
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String line;
			
			String user_x = null;
			String curr_user_x = null;
			// A{X} => X|A, this will store A
			Set<Long> plus_set = new TreeSet<Long>();
			// A{X} => A|'X this will store X
 			Set<String> minus_set = new HashSet<String>();
			StringBuilder output = new StringBuilder();

			while((line = br.readLine()) != null) {
				int tab = line.indexOf('\t');
				user_x = line.substring(0, tab);
				String user_a = line.substring(tab + 1, line.length());
					
				if (curr_user_x != null && !curr_user_x.equals(user_x)) {
					// output
					if (!plus_set.isEmpty()) {
						for (Long e : plus_set) {
							if (minus_set.contains(e.toString())) {
								output.append('(' + e.toString() + ')');
							} else {
								output.append(e.toString());
							}
							output.append("\\n");
						}
						output.delete(output.length() - 2, output.length());
						System.out.println(output.toString());
					}
				}
				
				if (curr_user_x == null || (curr_user_x != null && !curr_user_x.equals(user_x))) {
					// initialize
					plus_set.clear();
					minus_set.clear();
					output = new StringBuilder();
	
					output.append(user_x);
					output.append('\t');
					curr_user_x = user_x;
				}
				
				// process A
				if (user_a.charAt(0) == '\'') {
					user_a = user_a.substring(1, user_a.length());
					minus_set.add(user_a);
				} else {
					plus_set.add(Long.parseLong(user_a));
				}
			}
			// if miss last line
			// output
			if (curr_user_x != null && curr_user_x.equals(user_x) && !plus_set.isEmpty()) {
				for (Long e : plus_set) {
					if (minus_set.contains(e)) {
						output.append('(' + e + ')');
					} else {
						output.append(e);
					}
					output.append(',');
				}
				output.delete(output.length() - 1, output.length());
				System.out.println(output.toString());
			}
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}
