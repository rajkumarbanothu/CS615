package in.ahmrkb.prj;

public class DBConfig {
	private int dimensions;
	private Double missingValueRepr;
	private Double errorDelta;

	public DBConfig(int dimensions) {
		this(dimensions, ProjectValues.MISSING_VAL_REPR);
	}

	public DBConfig(int dimensions, Double missingValueRepr) {
		this(dimensions, missingValueRepr, ProjectValues.ERROR);
	}

	public DBConfig(int dimensions, Double missingValueRepr, Double errorDelta) {
		this.dimensions = dimensions;
		this.missingValueRepr = missingValueRepr;
		this.errorDelta = errorDelta;
	}

	public int getDimensions() {
		return dimensions;
	}

	public void setDimensions(int dimensions) {
		this.dimensions = dimensions;
	}

	public Double getMissingValueRepr() {
		return missingValueRepr;
	}

	public void setMissingValueRepr(Double missingValueRepr) {
		this.missingValueRepr = missingValueRepr;
	}

	public Double getErrorDelta() {
		return errorDelta;
	}

	public void setErrorDelta(Double errorDelta) {
		this.errorDelta = errorDelta;
	}

}
