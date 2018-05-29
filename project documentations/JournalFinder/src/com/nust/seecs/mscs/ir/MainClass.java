package com.nust.seecs.mscs.ir;

import java.util.Scanner;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class MainClass {

	public static void main2(String[] args) {

		String qFileName = "query3.txt";
		Corpora corpus = new Corpora();
		corpus.startIndexing();
		Scanner input = new Scanner(System.in);
		do {
			System.out.println("Enter Query file : ");
			qFileName = input.nextLine();
			if (qFileName.equalsIgnoreCase("exit"))
				break;
			System.out.println("Getting Suggestions for file : " + qFileName);
			Document query = new Document(Constants.QUERY_PATH + "/" + qFileName, "query");
			String jName = corpus.getResults(query);
			System.out.println("THE SUGGESTED JOURNAL FOR DOCUMENT : " + jName);
		} while (!qFileName.equalsIgnoreCase("exit"));
		System.out.println("Thanks For Using Elsevier Journal Finder");
		input.close();
	}
	public static void main(String[] args){
		MaxentTagger tagger =  new MaxentTagger(Constants.TAGGER_PATH);
//		String sample = "rolled-up";
		String sample = "a yellow building with white columns in the background;"
				+ " two palm trees in front of the house;"
				+ " cars are parking in front of the house;"
				+ " a woman and a child are walking over the square;";
		String tagged = tagger.tagString(sample);
		 
		System.out.println(tagged);
	}
}
