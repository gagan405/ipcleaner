/**
 * 
 */
package com.thekratos.IpCleaner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author gbm
 *
 */
public class IpRangeTreeNode {
	private List<Integer> lineNums;
	private long startAddress;
	private List<Long> endAddress;
	private String carrier;
	
	private IpRangeTreeNode leftChild;
	private IpRangeTreeNode rightChild;
	private IpRangeTreeNode root;
	
	public IpRangeTreeNode(long sa, long ea, int n, String ca) {
		this.lineNums = new ArrayList<>();
		lineNums.add(n);
		this.endAddress = new ArrayList<>();
		endAddress.add(ea);
		this.carrier = ca;
		this.startAddress = sa;
		this.leftChild = null;
		this.rightChild = null;
		this.root = null;
	}
	
	public long getStartAddr(){
		return startAddress;
	}
	
	public List<Long> getEndAddress(){
		return endAddress;
	}
	
	public List<Integer> getLineNums(){
		return lineNums;
	}
	
	public String getCarrier(){
		return carrier;
	}
	
	public void addLine(int n){
		this.lineNums.add(n);
	}
	
	public void addLine(List<Integer> n){
		this.lineNums.addAll(n);
	}
	
	public void addEndAddr(long ea){
		this.endAddress.add(ea);
	}
	
	public void addEndAddr(List<Long> ea){
		this.endAddress.addAll(ea);
	}
	
	public void setLeftChild(IpRangeTreeNode node){
		this.leftChild = node;
	}
	
	public void setRightChild(IpRangeTreeNode node){
		this.rightChild = node;
	}
	
	public IpRangeTreeNode getLeftChild(){
		return this.leftChild;
	}
	
	public IpRangeTreeNode getRightChild(){
		return this.rightChild;
	}
	
	public void setRoot(IpRangeTreeNode node){
		this.root = node;
	}
	
	public IpRangeTreeNode getRoot(){
		return this.root;
	}
	
	public long getMaxEndAddress(){
		Collections.sort(endAddress);
		return endAddress.get(endAddress.size()-1);
	}
	
	@Override
	public boolean equals(Object o){
		if(o instanceof IpRangeTreeNode){
			if(((IpRangeTreeNode)o).getStartAddr() == this.startAddress) return true;
		}
		return false;
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		StringBuilder end = new StringBuilder();
		for(long add : endAddress){
			end.append(IpReader.longToIp(add)).append(", ");
		}
		end.deleteCharAt(end.lastIndexOf(","));
		sb.append("[start:").append(IpReader.longToIp(startAddress)).append(", end:").append(end).append(", carrier:")
		.append(carrier).append(", lineNum:").append(lineNums).append("]");
		return sb.toString();
	}
}
