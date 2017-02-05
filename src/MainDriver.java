

import in.ahmrkb.prj.SkylineAlgorithm.ALGOTYPE;

public class MainDriver {

	public static void main(String[] args) {
		Database.configureDatabase(new DBConfig(2)); // No of dimensions

		Database.readValuesIntoDB("/data/ip.txt"); // Input file name

		SkylineAlgorithm algorithm = SkylineAlgorithmFactory
				.getSkylineAlgorithm(ALGOTYPE.PRIVATE);

		algorithm.getSkylineTuples();
	}
}
