package in.ahmrkb.skyline.point;

import in.ahmrkb.prj.Database;
import in.ahmrkb.prj.ProjectValues;
import in.ahmrkb.prj.ProjectValues.DOMINATOR;
import in.ahmrkb.prj.SkylineAlgorithm;
import in.ahmrkb.prj.Tuple;

import java.util.ArrayList;
import java.util.HashMap;

public class BucketAlgorithm extends SkylineAlgorithm {
	@Override
	public ArrayList<Tuple> computeSkylineTuples() {
		bucketizeTuples();
		ArrayList<Tuple> candidateSkylineList = new ArrayList<>();
		for (Bucket bucket : allBuckets) {
			if (!bucket.getBucketContents().isEmpty()
					&& bucket.getBitmap() != 0) {
				candidateSkylineList.addAll(getLocalSkylines(bucket));
			}
		}
		return performPairwiseComparision(candidateSkylineList);
	}

	private ArrayList<Tuple> getLocalSkylines(Bucket bucket) {
		BNLAlgorithm bnlAlgo = new BNLAlgorithm();
		return bnlAlgo.findSkylineSet(
				bucket.getBucketContents(),
				getQueryDimensions(bucket.getBitmap(), bucket
						.getBucketContents().get(0).getDimensions()));
	}

	private void bucketizeTuples() {
		Tuple current;
		Bucket bucket;
		Integer bitmap;
		for (int i = 0, len = Database.size(); i < len; i++) {
			current = Database.get(i);
			bitmap = computeBitMapForTuple(current);
			bucket = bitmapBucketMapping.get(bitmap);
			if (bucket == null) {
				bucket = new Bucket(bitmap);
				bitmapBucketMapping.put(bitmap, bucket);
				allBuckets.add(bucket);
			}
			bucket.add(current);
		}
	}

	private boolean doesFirstDominateSecond(Tuple first, Tuple second,
			ArrayList<Integer> checkDims) {
		boolean firstIsBetterOrEqual = true, secondIsBetterOrEqual = true;

		return false;
	}

	private int computeBitMapForTuple(Tuple tuple) {
		int bitmap = 0;
		for (int i = 0, len = tuple.getDimensions(); i < len; i++) {
			if (tuple.get(i) == ProjectValues.MISSING_VAL_REPR) {
				bitmap = (bitmap << 1) ^ 0x0;
			} else {
				bitmap = (bitmap << 1) ^ 0x1;
			}
		}
		return bitmap;
	}

	private ArrayList<Integer> getQueryDimensions(int bitmap, int dimensions) {
		ArrayList<Integer> queryDims = new ArrayList<>();
		for (int i = 1; i <= dimensions; i++) {
			if ((bitmap & 0x1) != 0) {
				queryDims.add(dimensions - i);
			}
			bitmap >>= 1;
		}
		return queryDims;

	}

	public ArrayList<Tuple> performPairwiseComparision(
			ArrayList<Tuple> candidateSkylines) {
		boolean dominated = false;
		ArrayList<Tuple> resultSet = new ArrayList<>();
		Tuple first, second;
		for (int i = 0, len = candidateSkylines.size(); i < len; i++) {
			dominated = false;
			first = candidateSkylines.get(i);
			for (int j = 0; j < len; j++) {
				if (i == j)
					continue;
				second = candidateSkylines.get(j);
				if (whoDominates(first, second) == DOMINATOR.SECOND) {
					dominated = true;
					break;
				}
			}
			if (!dominated) {
				resultSet.add(first);
			}
		}
		return resultSet;
	}

	private DOMINATOR whoDominates(Tuple first, Tuple second) {
		boolean firstIsBetterOrEqual = true, secondIsBetterOrEqual = true;

		// Checking whether second is better or equal to first
		for (int i = 0, len = second.getDimensions(); i < len; i++) {
			if (second.get(i) == ProjectValues.MISSING_VAL_REPR
					|| first.get(i) == ProjectValues.MISSING_VAL_REPR)
				continue;
			if (second.get(i) > first.get(i)) {
				secondIsBetterOrEqual = false;
				break;
			}
		}
		// Checking whether first is better or equal to second
		for (int i = 0, len = first.getDimensions(); i < len; i++) {
			if (second.get(i) == ProjectValues.MISSING_VAL_REPR
					|| first.get(i) == ProjectValues.MISSING_VAL_REPR)
				continue;
			if (first.get(i) > second.get(i)) {
				firstIsBetterOrEqual = false;
				break;
			}
		}

		if (firstIsBetterOrEqual && !secondIsBetterOrEqual) {
			return DOMINATOR.FIRST;
		} else if (secondIsBetterOrEqual && !firstIsBetterOrEqual) {
			return DOMINATOR.SECOND;
		}
		return DOMINATOR.NONE;
	}

	private HashMap<Integer, Bucket> bitmapBucketMapping = new HashMap<>();
	private ArrayList<Bucket> allBuckets = new ArrayList<>();
}
