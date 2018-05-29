package com.nust.seecs.mscs.ir;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Journal {

	private String name = "";
	private List<Document> documents = new ArrayList<Document>();
	private Integer journalSize = 0;
	private double avgJournalSize = 0;

	public Journal(String folderPath, String name) {
		this.name = name;
		// iterate all files and create document and fill in list
		populateDocuments(folderPath);
		avgJournalSize = journalSize / getDocCount();
	}

	private void populateDocuments(String folderPath) {
		File folder = new File(folderPath);
		File[] listOfFiles = folder.listFiles();

		for (File file : listOfFiles) {
			if (file.isFile()) {
				System.out.println(file.getName());
				Document doc = new Document(folderPath + "/" + file.getName(),file.getName());
				documents.add(doc);
				journalSize = journalSize + doc.getTotalTerms();
			}
		}
	}

	public int getDocCount() {
		return documents.size();
	}

	public int getDocFrequency(String term) {
		int count = 0;
		for (Document d : documents) {
			if (d.containsTerm(term))
				count++;
		}
		return count;
	}

	public double getAvgJournalSize() {
		return avgJournalSize;
	}

	public int getJournalSize() {
		return journalSize;
	}

	public String getName() {
		return name;
	}

	public double score(List<String> terms, double avgDocSize,
			Map<String, Double> idfs) {
		double score = 0;
		for (Document d : documents) {
			score = score + d.score(terms, avgDocSize, idfs);
		}
		return (score / getDocCount());
	}
}
