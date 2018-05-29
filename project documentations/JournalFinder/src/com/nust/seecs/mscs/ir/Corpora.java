package com.nust.seecs.mscs.ir;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Corpora {

	private List<Journal> journals = new ArrayList<Journal>();
	private Map<String,Double> idfs = new HashMap<String, Double>();
	private double avgDocLength = 0;
	private double corpusLength = 0;	// total number of terms in corpus
	
	
	public Corpora(){
		
	}
	
	public void startIndexing(){
		File[] directories = new File(Constants.DB_PATH).listFiles(File::isDirectory);
		for(int f=0; f< directories.length; f++){
			Journal journl = new Journal(Constants.DB_PATH +"/"+ directories[f].getName(), directories[f].getName());
			journals.add(journl);
			corpusLength = corpusLength + journl.getAvgJournalSize();
		}
		avgDocLength = corpusLength / journals.size();
	}
	
	public String getResults(Document doc) {
		double score = 0;
		String suggestedJournal = "";
		
		List<String> terms = doc.getTerms();
		computeIDF(terms);
		
		for (Journal j : journals) {
			double CurrScore = j.score(terms, avgDocLength, idfs);
			System.out.println("Journal: " + j.getName() + " score : " + CurrScore);
			if (score < CurrScore) {
				suggestedJournal = j.getName();
				score = CurrScore;
			}
		}
		return suggestedJournal;
	}
	
	public double computeTermIDF(String term) {
		if (idfs.containsKey(term))
			return idfs.get(term);

		int corSize = getcorpusLength();
		int corFre = getCorpusFequency(term);
		double idf = Math.log((corSize - corFre + 0.5d) / (corFre + 0.5d));
		idfs.put(term, idf);
		return idf;
	}

	public void computeIDF(List<String> terms) {
		for (String t : terms) {
			double score = computeTermIDF(t);
			System.out.println("term idf : " + score);
		}
	}

	public int getcorpusLength() {
		int count = 0;
		for (Journal j : journals) {
			count = count + j.getDocCount();
		}
		return count;
	}

	public int getCorpusFequency(String term) {
		int count = 0;
		for (Journal j : journals) {
			count = count + j.getDocFrequency(term);
		}
		return count;
	}

}
