package com.thekratos.IpCleaner;

public class IpRange {
	private long startAddress;
	private long endAddress;
	private String carrier;
	private Integer lineNum;

	
	public IpRange(long sa, long ea, String ca, int n) {
		this.startAddress = sa;
		this.endAddress = ea;
		this.carrier = ca;
		this.lineNum = n;
	}
	
	public long getStartAddress(){
		return startAddress;
	}
	
	public long getEndAddress(){
		return endAddress;
	}
	
	public long getLineNum(){
		return lineNum;
	}
	
	public String getCarrier(){
		return carrier;
	}
	
	@Override
	public String toString(){
		return "[ start: " + IpReader.longToIp(startAddress) + ", end: " + IpReader.longToIp(endAddress) + ", carrier: " + carrier + ", line: " + lineNum + "]";
	}
		
}
