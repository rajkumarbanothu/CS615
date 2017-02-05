

import java.util.ArrayList;
import java.util.Date;

public abstract class SkylineAlgorithm {
	protected abstract ArrayList<Tuple> computeSkylineTuples();

	public ArrayList<Tuple> getSkylineTuples() {
		long startTime = System.currentTimeMillis();
		ArrayList<Tuple> result = computeSkylineTuples();
		System.out.println("*************\n** Results **\n*************\n\n"
				+ result + "\n\n");
		System.out.println("Skyline count: " + result.size());
		System.out.println("\nTime taken: "
				+ (System.currentTimeMillis() - startTime) + " ms");
		return result;
	}

	public static enum ALGOTYPE {
		PRIVATE, DEFAULT, NAIVE, BUCKET
	}
}
