package in.ahmrkb.skyline.point;

import in.ahmrkb.prj.Tuple;

import java.util.ArrayList;

public class Bucket {
	private int bitmap = 0;
	private ArrayList<Tuple> bucketContents = new ArrayList<>();

	public Bucket(int bitmap) {
		this.bitmap = bitmap;
	}

	public void add(Tuple tuple) {
		bucketContents.add(tuple);
	}

	public int getBitmap() {
		return bitmap;
	}

	public ArrayList<Tuple> getBucketContents() {
		return bucketContents;
	}
}
