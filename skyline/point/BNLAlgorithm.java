package in.ahmrkb.skyline.point;

import in.ahmrkb.prj.Database;
import in.ahmrkb.prj.ProjectValues;
import in.ahmrkb.prj.ProjectValues.DOMINATOR;
import in.ahmrkb.prj.SkylineAlgorithm;
import in.ahmrkb.prj.Tuple;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class BNLAlgorithm extends SkylineAlgorithm {

	@Override
	public ArrayList<Tuple> computeSkylineTuples() {
		ArrayList<Integer> queryDims = new ArrayList<>();
		for (int i = 0; i < ProjectValues.DIM_SIZE; i++) {
			queryDims.add(i);
		}
		return findSkylineSet(Database.getTuples(), queryDims);
	}

	private DOMINATOR whoDominates(Tuple first, Tuple second,
			ArrayList<Integer> checkDims) {
		boolean firstIsBetterOrEqual = true, secondIsBetterOrEqual = true;

		for (int i = 0, len = checkDims.size(); i < len; i++) {
			if (second.get(checkDims.get(i)) > first.get(checkDims.get(i))) {
				secondIsBetterOrEqual = false;
				break;
			}
		}
		// Checking whether first is better or equal to second
		for (int i = 0, len = checkDims.size(); i < len; i++) {
			if (first.get(checkDims.get(i)) > second.get(checkDims.get(i))) {
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

	private boolean checkAgainstWindow(Tuple currentTuple,
			ArrayList<Integer> queryDims) {
		Iterator<Tuple> iterator = window.iterator();
		Tuple tuple;
		while (iterator.hasNext()) {
			tuple = iterator.next();
			DOMINATOR dominator = whoDominates(tuple, currentTuple, queryDims);
			if (dominator == DOMINATOR.FIRST) {
				dominatedList.add(currentTuple.getId());
				dominatedList.add(tuple.getId());
				return true;
			} else if (dominator == DOMINATOR.SECOND) {
				dominatedList.add(tuple.getId());
				dominatedList.add(currentTuple.getId());
				iterator.remove();
			}
		}
		return false;
	}

	ArrayList<Tuple> findSkylineSet(ArrayList<Tuple> tuples,
			ArrayList<Integer> queryDims) {
		int recordsProcessed = 0, i = 0, totalRecords = tuples.size();
		boolean wasDominated = false;
		Tuple tuple;

		while (recordsProcessed < totalRecords) {
			tuple = tuples.get(i);
			recordsProcessed++;
			wasDominated = checkAgainstWindow(tuple, queryDims);
			if (!wasDominated) {
				window.add(tuple);
			}
			i++;
		}
		return new ArrayList<>(window);
	}

	private LinkedList<Tuple> window = new LinkedList<>();
	private ArrayList<Integer> dominatedList = new ArrayList<>();
}
