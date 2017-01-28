package in.ahmrkb.skyline.set;

import in.ahmrkb.prj.ProjectValues;
import in.ahmrkb.prj.Tuple;
import in.ahmrkb.util.Utility;

import java.util.ArrayList;

import org.ejml.data.DenseMatrix64F;
import org.ejml.simple.SimpleMatrix;

public class Facet {
	private int dimensions;
	private SimpleMatrix points;
	private int currPtr = 0;
	private SimpleMatrix elementaryVectors;
	private Tuple normalVector = null;

	public Facet(int dimension) {
		this.dimensions = dimension;
		points = new SimpleMatrix(dimension, dimension);
		elementaryVectors = SimpleMatrix.identity(dimension);
	}

	public Tuple getNormalVectorToFacet() {
		// Check if all points are added
		if (currPtr < dimensions) {
			throw new IllegalStateException(
					"Facet is not completely constructed.");
		}
		// Check if normal vector already created.
		// Assumption is once a facet is created no changed are made to it.
		if (normalVector != null) {
			return normalVector;
		}
		SimpleMatrix edgeVector, refVector = points.extractVector(true, 0), normalVector, temp, minor;
		SimpleMatrix crossProduct = new SimpleMatrix(dimensions - 1, dimensions);
		for (int i = 1; i < dimensions; i++) {
			edgeVector = points.extractVector(true, i).minus(refVector);
			for (int j = 0; j < dimensions; j++) {
				crossProduct.set(i - 1, j, edgeVector.get(0, j));
			}
		}
		normalVector = new SimpleMatrix(dimensions, 1);
		normalVector.zero();
		int sign = 1;
		for (int i = 0; i < dimensions; i++) {
			temp = elementaryVectors.extractMatrix(0, dimensions, i, i + 1);
			minor = Utility.deleteColumn(crossProduct, i, dimensions - 1,
					dimensions);
			temp = temp.scale(sign * minor.determinant());
			sign = sign * -1;
			normalVector = normalVector.plus(temp);
		}
		this.normalVector = new Tuple(normalVector.getMatrix());
		return this.normalVector;
	}

	public void addRepresentingPoint(DenseMatrix64F endPoint) {
		if (currPtr < dimensions) {
			points.setRow(currPtr, 0, endPoint.getData());
			currPtr++;
		}
	}

	public boolean containsPoint(Tuple normalVector, Tuple newPoint) {
		if (currPtr < dimensions) {
			throw new IllegalStateException(
					"Facet is not completely constructed.");
		}
		SimpleMatrix pointInFacet = points.extractMatrix(0, 1, 0, dimensions)
				.transpose();
		SimpleMatrix newPt = new SimpleMatrix(newPoint.getValues());
		SimpleMatrix nv = new SimpleMatrix(normalVector.getValues());
		double innerProduct = nv.dot(newPt.minus(pointInFacet));
		// if the above dot product is zero, then newPoint is in plane.
		if (Math.abs(innerProduct) < ProjectValues.ERROR)
			return true;
		return false;
	}

	public ArrayList<Facet> getExpandedFacet(Tuple newPoint) {
		ArrayList<Facet> expandedFacets = new ArrayList<>();
		Facet newFacet;
		DenseMatrix64F newPt = newPoint.getValues();
		for (int i = 0, index = 1; i < dimensions; i++) {
			newFacet = new Facet(dimensions);
			for (int j = 0; j < dimensions; j++) {
				if (j != index) {
					newFacet.addRepresentingPoint(points.extractMatrix(j,
							j + 1, 0, dimensions).getMatrix());
				} else {
					newFacet.addRepresentingPoint(newPt);
				}
			}
			expandedFacets.add(newFacet);
			index = (index + 1) % dimensions;
		}
		return expandedFacets;
	}

	@Override
	public String toString() {
		return points.toString();
	}

	@Override
	public boolean equals(Object object) {
		if (object == null)
			return false;
		if (!(object instanceof Facet))
			return false;
		Facet other = (Facet) object;
		Tuple first = this.getNormalVectorToFacet();
		Tuple second = other.getNormalVectorToFacet();
		if (first.size() != second.size()
				|| first.getDimensions() != second.getDimensions())
			return false;
		for (int i = 0, len = first.size(); i < len; i++) {
			if (Math.abs(first.get(i) - second.get(i)) > ProjectValues.ERROR)
				return false;
		}
		return true;
	}
}
