package in.ahmrkb.prj;

import java.util.Arrays;

import org.ejml.data.DenseMatrix64F;

public class Tuple {
	private DenseMatrix64F values;
	private int dimensions;
	private int id;
	private int currPtr = 0;
	private double innerProduct = 0.0;

	public Tuple(int dimensions) {
		this.dimensions = dimensions;
		// Making a column vector
		this.values = new DenseMatrix64F(dimensions, 1);
	}

	public Tuple(DenseMatrix64F values) {
		// values is a column matrix
		this.values = values;
		this.dimensions = values.getNumRows();
	}

	public int getDimensions() {
		return dimensions;
	}

	public DenseMatrix64F getValues() {
		return values;
	}

	public void setValues(DenseMatrix64F values) {
		this.values = values;
	}

	public void setDimensions(int dimensions) {
		this.dimensions = dimensions;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getInnerProduct() {
		return innerProduct;
	}

	public void setInnerProduct(double innerProduct) {
		this.innerProduct = innerProduct;
	}

	public void set(int index, Double value) {
		values.unsafe_set(index, 0, value);
	}

	public Double get(int index) {
		return values.unsafe_get(index, 0);
	}

	public int size() {
		return values.getNumRows();
	}

	public void add(Double value) {
		if (currPtr < dimensions) {
			values.set(currPtr, 0, value);
			currPtr++;
		}
	}

	@Override
	public String toString() {
		return "\nValues: " + Arrays.toString(values.getData())
				+ ProjectValues.NEWLINE;
	}
}
