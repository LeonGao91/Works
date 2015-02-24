package cosineDistance;

import java.io.File;
/**
 * @author Team7
 * This class will calculate the cosine distance between test file to Conan Doyle
 */
public class MainRoutine {
	public static void main(String[] args){
		File input = new File("conan");
		CosineDistance conan = new CosineDistance(input);
		input = new File("shaks");
		CosineDistance shaks = new CosineDistance(input);
		input = new File(args[0]);
		CosineDistance test = new CosineDistance(input);
		Double conanDis = conan.distance(test.getMap());
		Double shaksDis = shaks.distance(test.getMap());
		System.out.println("Distance with Conan = " + conanDis);
		System.out.println("Distance with Shakspeare = " + shaksDis);
		String result = conanDis < shaksDis ? "Conan Doyle" : "William Shakspeare";
		System.out.println("This text is written by " + result);
	}
}
