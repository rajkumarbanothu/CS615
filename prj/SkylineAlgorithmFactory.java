package in.ahmrkb.prj;

import in.ahmrkb.prj.SkylineAlgorithm.ALGOTYPE;
import in.ahmrkb.skyline.point.BucketAlgorithm;
import in.ahmrkb.skyline.point.NaiveAlgorithm;
import in.ahmrkb.skyline.set.RBSSQAlgorithm;

public final class SkylineAlgorithmFactory {
	private SkylineAlgorithmFactory() {
		throw new RuntimeException(
				"SkylineAlgorithmFactory is not instantiable");
	}

	public static SkylineAlgorithm getSkylineAlgorithm(
			SkylineAlgorithm.ALGOTYPE algoType) {
		if (algoType == ALGOTYPE.PRIVATE) {
			return new RBSSQAlgorithm();
		} else if (algoType == ALGOTYPE.DEFAULT) {
			return new BucketAlgorithm();
		} else if (algoType == ALGOTYPE.NAIVE) {
			return new NaiveAlgorithm();
		} else if (algoType == ALGOTYPE.BUCKET) {
			return new BucketAlgorithm();
		}
		return new BucketAlgorithm();
	}

}
