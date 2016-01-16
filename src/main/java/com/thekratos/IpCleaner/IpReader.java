/**
 * 
 */
package com.thekratos.IpCleaner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


/**
 * @author gbm
 *
 */
public class IpReader {
	private String csv_file;
	private ArrayList<String> csvContents = null;
	private final String CSV_FILE = "ipranges.csv";
	
	private final int CARRIER_INDEX = 1;
	private final int LINE_NUM = 0;
	private final int START_INDEX = 2;
	private final int END_INDEX = 3;
	
	public IpReader() throws Exception {
		csvContents = getCSVContents();
		
	}
	private ArrayList<String> getCSVContents() throws Exception{
		if(csvContents == null){
			ClassLoader cl = IpReader.class.getClassLoader();
				InputStream is = cl.getResourceAsStream(CSV_FILE);
			  BufferedReader r = new BufferedReader(new InputStreamReader(is));
			  try {
					String l = r.readLine();
					csvContents = new ArrayList<String>();
					while((l = r.readLine()) != null) {
						csvContents.add(l);
			    }
					is.close();
				} catch (IOException e) {
					throw new Exception(e.getMessage());
				}  
			}
		    return csvContents;
	}
	
	public List<IpRange> getRanges(){
		List<IpRange> range = new ArrayList<>();
		for(String s : csvContents){
			String[] tokens = s.split(",");
			range.add(new IpRange(IpReader.parseIp(tokens[START_INDEX]), IpReader.parseIp(tokens[END_INDEX]),
					tokens[CARRIER_INDEX], Integer.parseInt(tokens[LINE_NUM])));
		}
		return range;
	}
	
	public static long parseIp(String address) {
		long result = 0;
		if ((address.charAt(address.length() - 1) == '\n')) {
			address = address.substring(0, address.length() - 1);
		}

		if ((address.charAt(address.length() - 1) == '\r')) {
			address = address.substring(0, address.length() - 1);
		}
		// iterate over each octet
		for (String part : address.split(Pattern.quote("."))) {
			// shift the previously parsed bits over by 1 byte
			result = result << 8;
			// set the low order bits to the current octet
			result |= Integer.parseInt(part) & 0xff;
		}
		return result;
	}
	
	public static String longToIp(long ip) {
	    StringBuilder sb = new StringBuilder(15);

	    for (int i = 0; i < 4; i++) {
	        sb.insert(0, Long.toString(ip & 0xff));

	        if (i < 3) {
	            sb.insert(0, '.');
	        }

	        ip >>= 8;
	    }

	    return sb.toString();
	  }
	
}
