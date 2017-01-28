package in.ahmrkb.util;

import org.ejml.simple.SimpleMatrix;

public final class Utility {
	public static SimpleMatrix deleteRowAndCol(SimpleMatrix matrix, int rowNo,
			int columnNo, int totalRows, int totalCols) {
		SimpleMatrix mat = deleteRow(matrix, rowNo, totalRows, totalCols);
		mat = deleteColumn(mat, columnNo, totalRows - 1, totalCols);
		return mat;
	}

	public static SimpleMatrix deleteRow(SimpleMatrix matrix, int rowNo,
			int totalRows, int totalCols) {
		SimpleMatrix mat = new SimpleMatrix(totalRows - 1, totalCols);
		for (int i = 0, rowIdx = 0; i < totalRows; i++) {
			if (i == rowNo)
				continue;
			mat.insertIntoThis(rowIdx, 0,
					matrix.extractMatrix(i, i + 1, 0, totalCols));
			rowIdx++;
		}
		return mat;
	}

	public static SimpleMatrix deleteColumn(SimpleMatrix matrix, int columnNo,
			int totalRows, int totalCols) {
		SimpleMatrix mat = new SimpleMatrix(totalRows, totalCols - 1);
		for (int i = 0, columnIdx = 0; i < totalCols; i++) {
			if (i == columnNo)
				continue;
			mat.insertIntoThis(0, columnIdx,
					matrix.extractMatrix(0, totalRows, i, i + 1));
			columnIdx++;
		}
		return mat;
	}
}
