import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class q4_mr2_mapper {
	public static void main(String[] args) {
		try {
			System.setIn(new java.io.FileInputStream("q4_sample1_3"));
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String line;
			while((line = br.readLine()) != null) {
				System.out.println(line);
			}
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}
