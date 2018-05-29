package com.nust.seecs.mscs.ir;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.SentenceUtils;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class Document {

	private int totalTerms = 0;
	private String name = "";
	private Map<String, Integer> termFrequency = new HashMap<String, Integer>();

	public Document(String filePath, String name) {
		this.name = name;
		extractFeatures(filePath);
		setTotalTerms(termFrequency.size());
	}

	public boolean containsTerm(String term) {
		return termFrequency.containsKey(term);
	}

	public Integer getTermFrequency(String term) {
		Integer freq = 0;
		if (termFrequency.containsKey(term))
			freq = termFrequency.get(term);
		return freq;
	}

	public List<String> getTerms() {
		List<String> termList = new ArrayList<String>(termFrequency.keySet());
		return termList;
	}

	public void extractFeatures(String filePath){
		MaxentTagger tagger =  new MaxentTagger(Constants.TAGGER_PATH);
		TokenizerFactory<CoreLabel> ptbTokenizerFactory = PTBTokenizer.factory(new CoreLabelTokenFactory(),"untokenizable=noneKeep");
        BufferedReader r;
		try {
			r = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "utf-8"));

			DocumentPreprocessor documentPreprocessor = new DocumentPreprocessor(r);
			documentPreprocessor.setTokenizerFactory(ptbTokenizerFactory);
			for (List<HasWord> sentence : documentPreprocessor) {
				List<TaggedWord> tSentence = tagger.tagSentence(sentence);
				System.out.println(SentenceUtils.listToString(tSentence, false));
				for (TaggedWord tw : tSentence) {
					if (tw.tag().startsWith("NN")) {
						System.out.println(tw.word());
						if(termFrequency.containsKey(tw.word())){
							Integer newVal = termFrequency.get(tw.word()) + 1;
							termFrequency.put(tw.word(), newVal);
						}
						else termFrequency.put(tw.word(), 1);
					}
				}
			}
		} catch (UnsupportedEncodingException | FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public double score(List<String> terms, double avgDocSize, Map<String,Double> idfs){
		double score = 0;
		for (String term : terms)
			score = score + BM25.score(getTermFrequency(term), getTotalTerms(), avgDocSize, idfs.get(term));
		return score;
	}

	public int getTotalTerms() {
		return totalTerms;
	}

	public void setTotalTerms(int totalTerms) {
		this.totalTerms = totalTerms;
	}
}
