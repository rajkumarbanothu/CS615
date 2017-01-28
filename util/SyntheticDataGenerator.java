package in.ahmrkb.util;

import in.ahmrkb.prj.ProjectValues;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.invoke.ConstantCallSite;
import java.text.DecimalFormat;

public class SyntheticDataGenerator {
	private static int dim = 2;
	private static int minReqd = 1;
	private static int datapoints = 10000;
	public static void main(String[] args) {
		File file = new File("/data/ip.txt");
		FileOutputStream fos = null;
		double value, probability;
		int addedTillNow;
//		long lValue;
		DecimalFormat formatter = new DecimalFormat("0.0000000000");
		byte[] tab = "\t".getBytes();
		byte[] newline = System.getProperty("line.separator").getBytes();
		try {
			fos = new FileOutputStream(file);
			for (Integer i = 0; i <= datapoints; i++) {
				fos.write(i.toString().getBytes());
				fos.write(tab);
				addedTillNow = 0;
				for (int j = 1; j <= dim; j++) {
					if (j != 1)
						fos.write(tab);
					if (dim - j + 1 <= minReqd - addedTillNow) {
						value = Math.random();
//						lValue = (long)(value * 10);
//						fos.write(formatter.format(lValue).getBytes());
						fos.write(formatter.format(value).getBytes());
						addedTillNow++;
					} else {
						probability = Math.random();
						if (probability > 0.5) {
							value = Math.random();
//							lValue = (long)(value * 10);
//							fos.write(formatter.format(lValue).getBytes());
							fos.write(formatter.format(value).getBytes());
							addedTillNow++;
						} else {
							fos.write(formatter.format(
									ProjectValues.MISSING_VAL_REPR).getBytes());
						}
					}
				}
				fos.write(newline);
			}
			fos.close();
		} catch (IOException ioEx) {
			ioEx.printStackTrace();
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (IOException ioEx) {
				ioEx.printStackTrace();
			}
		}
	}
}
