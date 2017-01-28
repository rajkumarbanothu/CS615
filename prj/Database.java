package in.ahmrkb.prj;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

public final class Database {
	private Database() {
		throw new RuntimeException("Database is not instantiable");
	}

	private static ArrayList<Tuple> tuples = new ArrayList<>();
	private static boolean dbConfigured = false;

	public static void readValuesIntoDB(DataReader dataReader) {
		tuples = dataReader.readDataAndReturnTuples();
	}

	public static void configureDatabase(DBConfig configObj) {
		ProjectValues.DIM_SIZE = configObj.getDimensions();
		ProjectValues.ERROR = configObj.getErrorDelta();
		ProjectValues.MISSING_VAL_REPR = configObj.getMissingValueRepr();
		dbConfigured = true;
	}

	public static void readValuesIntoDB(String fileName) {
		if (!dbConfigured)
			throw new IllegalStateException("Please configure database first.");
		File file = new File(fileName);
		FileInputStream fReader = null;
		BufferedReader bReader = null;
		Tuple tuple = null;
		String line;
		int howMany = 0, id;
		try {
			fReader = new FileInputStream(file);
			bReader = new BufferedReader(new InputStreamReader(fReader));
			while ((line = bReader.readLine()) != null) {
				Scanner scanner = new Scanner(line);
				tuple = new Tuple(ProjectValues.DIM_SIZE);
				howMany = 0;
				while (scanner.hasNextDouble()) {
					if (howMany == 0) {
						id = new Double(scanner.nextDouble()).intValue();
						tuple.setId(id);
					} else {
						tuple.add(scanner.nextDouble());
					}
					howMany++;
				}
				if (howMany > 0) {
					tuples.add(tuple);
				}
				scanner.close();
			}
			fReader.close();
			bReader.close();
		} catch (IOException ioEx) {
			ioEx.printStackTrace();
		}
	}

	public static ArrayList<Tuple> getTuples() {
		return tuples;
	}

	public static int size() {
		return tuples.size();
	}

	public static Tuple get(int index) {
		return tuples.get(index);
	}
}
