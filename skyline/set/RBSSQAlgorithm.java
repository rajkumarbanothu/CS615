package in.ahmrkb.skyline.set;

import in.ahmrkb.prj.Database;
import in.ahmrkb.prj.ProjectValues;
import in.ahmrkb.prj.SkylineAlgorithm;
import in.ahmrkb.prj.Tuple;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

import org.ejml.alg.dense.mult.VectorVectorMult;

public class RBSSQAlgorithm extends SkylineAlgorithm {
	
	@Override
	public ArrayList<Tuple> computeSkylineTuples() {
		ArrayList<Tuple> resultSet = generateSkylineSets();
		return resultSet;
	}

	public ArrayList<Tuple> generateSkylineSets() {
		generateInitNormalVectors();
		Facet initialFacet = generateInitialFacet();
		ArrayDeque<Facet> facetsToProcess = new ArrayDeque<>(10);
		facetsToProcess.offerFirst(initialFacet);
		Facet currentFacet;
		while (!facetsToProcess.isEmpty()) {
			currentFacet = facetsToProcess.pollFirst();
			if (wasFacetEncounteredBefore(currentFacet)) {
				// This facet was already encountered before. Adding this 
				// again will possibly cause infinite recursion.
				continue;
			}
			facetsSeenTillNow.add(currentFacet);
			Tuple normalVector = currentFacet.getNormalVectorToFacet();
			Tuple tangentPoint = findTangentPoint(normalVector,
					ProjectValues.SET_SIZE);
			// If the new found point is not on facet, expand the facets
			if (!currentFacet.containsPoint(normalVector, tangentPoint)) {
				convexSkylineSet.add(tangentPoint);
				facetsToProcess.addAll(currentFacet.getExpandedFacet(tangentPoint));
			}
		}
//		System.out.println(convexSkylineSet);
		return convexSkylineSet;
	}

	private static void generateInitNormalVectors() {
		Tuple normalVector = null;
		for (int i = 0; i < ProjectValues.DIM_SIZE; i++) {
			normalVector = new Tuple(ProjectValues.DIM_SIZE);
			for (int j = 0; j < ProjectValues.DIM_SIZE; j++) {
				if (i == j)
					normalVector.add(-1.0);
				else
					normalVector.add(0.0);
			}
			normalVectors.add(normalVector);
		}
	}

	private static Facet generateInitialFacet() {
		Tuple tangentPoint;
		Facet initialFacet = new Facet(ProjectValues.DIM_SIZE);
		for (int i = 0; i < ProjectValues.DIM_SIZE; i++) {
			tangentPoint = findTangentPoint(normalVectors.get(i),
					ProjectValues.SET_SIZE);
			initialFacet.addRepresentingPoint(tangentPoint.getValues());
			convexSkylineSet.add(tangentPoint);
		}
		return initialFacet;
	}

	static class InnerProductComparator implements Comparator<Tuple> {
		public int compare(Tuple one, Tuple two) {
			if (two.getInnerProduct() < one.getInnerProduct()) {
				return 1;
			} else if (two.getInnerProduct() > one.getInnerProduct()) {
				return -1;
			} else {
				return 0;
			}
		}
	}

	private static Tuple findTangentPoint(Tuple normalVector, int s) {
		Tuple current, newTuple;
		InnerProductComparator ipc = new InnerProductComparator();
		PriorityQueue<Tuple> topSTuples = new PriorityQueue<Tuple>(s, ipc);

		for (int i = 0, tupleCount = Database.size(); i < tupleCount; i++) {
			current = Database.get(i);
			newTuple = new Tuple(current.getDimensions());
			newTuple.getValues().set(current.getValues());
			newTuple.setInnerProduct(VectorVectorMult.innerProd(
					current.getValues(), normalVector.getValues()));
			topSTuples.add(newTuple);
			while (topSTuples.size() > s) {
				topSTuples.remove();
			}
		}
		return getSetTuple(topSTuples, s);
	}

	private static Tuple getSetTuple(PriorityQueue<Tuple> tuples, int size) {
		Tuple tuple = new Tuple(ProjectValues.DIM_SIZE), current;
		int i = 1;
		while (!tuples.isEmpty()) {
			current = tuples.poll();
			for (int j = 0, len = current.size(); j < len; j++) {
				if (i == 1)
					tuple.add(current.get(j));
				else
					tuple.set(j, tuple.get(j) + current.get(j));
			}
			i++;
		}
		return tuple;
	}

	static ArrayList<Tuple> normalVectors = new ArrayList<>(
			ProjectValues.DIM_SIZE);
	static ArrayList<Tuple> convexSkylineSet = new ArrayList<>();
	static ArrayList<Facet> facetsSeenTillNow = new ArrayList<>();

	private boolean wasFacetEncounteredBefore(Facet current) {
		for (Facet facet : facetsSeenTillNow) {
			if (facet.equals(current))
				return true;
		}
		return false;
	}
}
