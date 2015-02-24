import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class q4_mr1_reducer {
	public static void main(String[] args) {
		try {
			System.setIn(new java.io.FileInputStream("q4_sample1_2"));
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String line;
			
			String curr_key = null;
			StringBuilder sb = null;
			
			while ((line = br.readLine()) != null) {
				int limiter = line.indexOf('\t');
				String key = line.substring(0, limiter);
				String value = line.substring(limiter + 1, line.length());
				
				if (curr_key != null && curr_key.equals(key)) {
					sb.append(value).append(',');
				} else {
					if (curr_key != null) {
						System.out.println(sb.toString());
					}
					curr_key = key;
					sb = new StringBuilder();
					String location = key.substring(0, key.indexOf('|'));
					String tag = key.substring(key.indexOf('|') + 1, limiter);
					sb.append(location).append('\t').append(tag).append(':').append(value).append(',');
				}
			}
			if (curr_key != null) {
				System.out.println(sb.toString());
			}
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}
