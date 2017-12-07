import algorithm.ParallelPEXAlgorithm;
import algorithm.StandardPEXAlgorithm;

public class driver {
	public static void main(String[] args) {
		int k = 2;
		String searchString = "annual";
		String inputString = "annual.CPM.anniversary.annua.cannibal.nuality.annual.anteal.annnneal.a.nn.al";
		
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < 14000; i++)
			sb.append(inputString);
		inputString = sb.toString();
		System.out.println(inputString.length());
		

		long startTime = System.nanoTime();
		StandardPEXAlgorithm standardPEX = new StandardPEXAlgorithm();
		standardPEX.createTree(searchString, 0, searchString.length()-1, k, null, searchString.length()/(k+1));
		standardPEX.findIndices(inputString);
		standardPEX.searchCandidates(inputString);
		long endTime = System.nanoTime();
		long duration = (endTime - startTime);
		System.out.println("Standard: " + duration/1000000000.0 + "s");
		
		startTime = System.nanoTime();
		ParallelPEXAlgorithm parallelPEX = new ParallelPEXAlgorithm(6);
		parallelPEX.createTree(searchString, 0, searchString.length()-1, k, null, searchString.length()/(k+1));
		parallelPEX.findIndices(inputString);
		parallelPEX.searchCandidates(inputString);
		endTime = System.nanoTime();
		duration = (endTime - startTime);
		System.out.println("Parallel: " + duration/1000000000.0 + "s");
	}
}
