import algorithm.StandardPEXAlgorithm;

public class driver {
	public static void main(String[] args) {
		int k = 0;
		String searchString = "annual";
		String inputString = "annual.cpm.anniversary.annua.cannibal.nuality.annual.anteal.annnneal.a.nn.al";
		
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < 100000; i++)
			sb.append(inputString);
		inputString = sb.toString();
		System.out.println(inputString.length());

		StandardPEXAlgorithm standardPEX = new StandardPEXAlgorithm();
		standardPEX.createTree(searchString, 0, searchString.length()-1, k, null, searchString.length()/(k+1));
		
		long startTime = System.nanoTime();
		standardPEX.findIndices(inputString);
		long endTime = System.nanoTime();
		long duration = (endTime - startTime);
		long sum = duration;
		System.out.println("Standard find: " + duration/1000000000.0 + "s");
		
		startTime = System.nanoTime();
		standardPEX.searchCandidates(inputString);
		endTime = System.nanoTime();
		duration = (endTime - startTime);
		sum += duration;
		System.out.println("Standard search: " + duration/1000000000.0 + "s");
		System.out.println("Standard total: " + sum/1000000000.0 + "s");
		System.out.println(standardPEX.getIndices().size());
	}
}
