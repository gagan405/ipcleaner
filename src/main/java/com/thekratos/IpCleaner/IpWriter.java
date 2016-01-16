package com.thekratos.IpCleaner;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.log4j.Logger;

public class IpWriter {
	private Logger logger = Logger.getLogger(IpWriter.class);
	private String resultFile = "";
	private FileWriter fw;
	private BufferedWriter bw;
	
	public IpWriter(String filePath) {
		File file = new File(filePath);
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			fw = new FileWriter(file, true);	//take the file in append mode
			bw = new BufferedWriter(fw);
			this.resultFile = filePath;
		} catch (IOException e) {
			logger.error("Failed to create file : " + filePath, e);
		}
	}
	
	public void write(String s) throws IOException{
		try {
			if(bw != null){
				bw.write(s + System.lineSeparator());
			}
			else{
				throw new Exception("BufferedWriter is Null!");
			}
		} catch (Exception e) {
			logger.error("Failed to write to file : " + resultFile,e);
		}
	}
	
	public void close() throws IOException{
		if(bw!=null){
			bw.flush();
			fw.flush();
			bw.close();
			fw.close();
		}
	}

	
	
}
