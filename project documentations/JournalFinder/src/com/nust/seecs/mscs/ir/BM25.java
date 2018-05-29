package com.nust.seecs.mscs.ir;

public class BM25 {

	public static final double k_1 = 1.5d;
	public static final double b = 0.6d;

	
	public static double score(double tf, double docSize,
			double avgDocSize, double idf) {

		return (tf * (k_1 + 1d) / (k_1 * ((1 - b) + b * docSize / avgDocSize) + tf))
				* idf;
	}
}
