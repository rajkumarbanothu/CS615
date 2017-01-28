package in.ahmrkb.skyline.point;

import in.ahmrkb.prj.Database;
import in.ahmrkb.prj.ProjectValues;
import in.ahmrkb.prj.ProjectValues.DOMINATOR;
import in.ahmrkb.prj.SkylineAlgorithm;
import in.ahmrkb.prj.Tuple;

import java.util.ArrayList;

public class NaiveAlgorithm extends SkylineAlgorithm {
	@Override
	public ArrayList<Tuple> computeSkylineTuples() {
		boolean dominated = false;
		ArrayList<Tuple> resultSet = new ArrayList<>();
		Tuple first, second;
		for (int i = 0, len = Database.size(); i < len; i++) {
			dominated = false;
			first = Database.get(i);
			for (int j = 0; j < len; j++) {
				if (i == j)
					continue;
				second = Database.get(j);
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
}
