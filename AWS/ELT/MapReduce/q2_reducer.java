import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class q2_reducer {
	public static void main(String[] args) {
		try {
			//System.setIn(new FileInputStream("sample2"));
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String input;
			while ((input = br.readLine()) != null) {
				System.out.println(input);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
