package com.thekratos.IpCleaner;

import java.io.IOException;
import java.util.List;

public class ProcessIpRanges {
	
	private List<IpRange> input;
	private IpTree tree;
	public ProcessIpRanges(List<IpRange> ranges) {
		input = ranges;
		tree = new IpTree();
	}
	
	public void process() {
		for(IpRange ip : input){
			tree.addRange(ip);
		}
	}
	
	public void printTree(){
		System.out.println(tree.toString());
	}
	
	public void verifyTree(){
		System.out.println(tree.isCorrect());
	}
	
	public void outputToCleanedCsv() throws IOException{
		IpWriter writer = new IpWriter("/home/gbm/cleanedcsv.csv");
		List<String> contents = tree.getCleanedStrings();
		writer.write("Index, Carrier, Start Address, End Address");
		int idx = 1;
		for(String s : contents){
			writer.write(idx + "," + s);
			idx++;
		}
		writer.close();
	}
	
}
