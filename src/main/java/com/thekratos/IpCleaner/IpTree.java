/**
 * 
 */
package com.thekratos.IpCleaner;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * @author gbm
 *
 */
public class IpTree {
	private Logger logger = Logger.getLogger(IpTree.class);
	
	private IpRangeTreeNode root;
	
	public IpTree(IpRangeTreeNode rt) {
		this.root = rt;
	}
	
	public IpTree() {
		this.root = null;
	}
	
	public IpRangeTreeNode getRange(long startAddr){
		return searchRange(startAddr, root);
	}
	
	private IpRangeTreeNode searchRange(long startAddr, IpRangeTreeNode root){
		if(root.getStartAddr() == startAddr) return root;
		if(root.getStartAddr() < startAddr){
			if(root.getRightChild() != null)
				return searchRange(startAddr, root.getRightChild());
			else return null;
		}
		else{
			if(root.getLeftChild() != null)
				return searchRange(startAddr, root.getLeftChild());
			else return null;
		} 
	}
	
	public void addRange(IpRange r){
		IpRangeTreeNode node = new IpRangeTreeNode(r.getStartAddress(), r.getEndAddress(), (int)r.getLineNum(), r.getCarrier());
		if(this.root == null){
			this.root = node;
		}
		else addRange(node, root);
	}
	
	private void addRange(IpRangeTreeNode r, IpRangeTreeNode root){
		if(r.getStartAddr() == root.getStartAddr()){
			if(r.getCarrier().equalsIgnoreCase(root.getCarrier())){
				logger.info("Overlapping ranges with same carrier. Line num " + r.getLineNums() + " and " + root.getLineNums());
				root.addEndAddr(r.getEndAddress());
				root.addLine(r.getLineNums());
				return;
			} else {
				logger.warn("Overlapping ranges with different carriers. Not adding to the tree " + r.getLineNums() + " and " + root.getLineNums());
				return;
			}
		}
		if(r.getStartAddr() < root.getStartAddr()){
			if(r.getMaxEndAddress() >= root.getStartAddr()){
				if(!r.getCarrier().equalsIgnoreCase(root.getCarrier())){
					logger.warn("Overlapping ranges with different carriers. Not adding to the tree " + r.getLineNums() + " and " + root.getLineNums());
				} else {
					logger.info("Overlapping ranges with same carrier. Line num " + r.getLineNums() + " and " + root.getLineNums());
					deleteNode(root);
					r.addLine(root.getLineNums());
					r.addEndAddr(root.getEndAddress());
					if(this.root != null)
						addRange(r, this.root);
					else 
						this.root = r;
				}
			}else {
				if(r.getMaxEndAddress() < root.getStartAddr() -1 ){
					//	Non overlapping range, lesser than root... continue in left subtree
					if(root.getLeftChild() == null){
						root.setLeftChild(r);
						r.setRoot(root);
					} else {
						addRange(r, root.getLeftChild());
					}
				} else{
					//Non overlapping range, but adjacent. Can be merged
					if(r.getCarrier().equalsIgnoreCase(root.getCarrier())){
						logger.info("Overlapping ranges with same carrier. Line num " + r.getLineNums() + " and " + root.getLineNums());
						deleteNode(root);
						r.addLine(root.getLineNums());
						r.addEndAddr(root.getEndAddress());
						if(this.root != null)
							addRange(r, this.root);
						else
							this.root = r;
					}
				}
			}
		}
		
		else if(r.getStartAddr() > root.getStartAddr()){
			if(r.getStartAddr() <= root.getMaxEndAddress()){
				if(!r.getCarrier().equalsIgnoreCase(root.getCarrier())){
					logger.warn("Overlapping ranges with different carriers. Not adding to the tree. Line num " + r.getLineNums() + " and " + root.getLineNums());
				} else {
					logger.info("Overlapping ranges with same carrier. Line num " + r.getLineNums() + " and " + root.getLineNums());
					root.addLine(r.getLineNums());
					root.addEndAddr(r.getEndAddress());
				}
			} else {
				//Non overlapping range with higher than the root -- continue in the right subtree
				if(root.getRightChild() == null){
					root.setRightChild(r);
					r.setRoot(root);
				} else {
					addRange(r, root.getRightChild());
				}
			}
			
		}
	}
	
	private void deleteNode(IpRangeTreeNode r){
		if(r == null) return;
		
		if((r.getLeftChild() == null) && r.getRightChild() == null){
			//leaf node
			if(r.getRoot() == null){
				this.root = null;
			} else {
				if((r.getRoot().getLeftChild() != null) && (r.getRoot().getLeftChild().equals(r))){
					r.getRoot().setLeftChild(null);
				} else {
					r.getRoot().setRightChild(null);
				}
			}
		}
				
		else if((r.getLeftChild() != null) && r.getRightChild() == null){
			if(r.getRoot() == null){
				this.root = r.getLeftChild();
				this.root.setRoot(null);
			} else {
				if((r.getRoot().getLeftChild() != null) && (r.getRoot().getLeftChild().equals(r))){
					r.getRoot().setLeftChild(r.getLeftChild());
				} else if((r.getRoot().getRightChild() != null) && (r.getRoot().getRightChild().equals(r))) {
					r.getRoot().setRightChild(r.getLeftChild());
				}
				r.getLeftChild().setRoot(r.getRoot());
			}
		}
	 else if((r.getLeftChild() == null) && r.getRightChild() != null){
		 if(r.getRoot() == null){
				this.root = r.getRightChild();
				this.root.setRoot(null);
			} else {
				if((r.getRoot().getLeftChild() != null) && (r.getRoot().getLeftChild().equals(r))){
					r.getRoot().setLeftChild(r.getRightChild());
				} else if((r.getRoot().getRightChild() != null) && (r.getRoot().getRightChild().equals(r))) {
					r.getRoot().setRightChild(r.getRightChild());
				}
				r.getRightChild().setRoot(r.getRoot());
			}
	 } else {
		 if(r.getRoot() == null){
				this.root = r.getRightChild();
		 }
		 r.getLeftChild().setRoot(r.getRightChild());
		 r.getRightChild().setRoot(r.getRoot());
		 IpRangeTreeNode leftChildOfRightSubTree =  r.getRightChild().getLeftChild();
		 r.getRightChild().setLeftChild(r.getLeftChild());
		 if((r.getRoot().getLeftChild() != null) && (r.getRoot().getLeftChild().equals(r))){
			 r.getRoot().setLeftChild(r.getRightChild());
		} else if((r.getRoot().getRightChild() != null) && (r.getRoot().getRightChild().equals(r))) {
			r.getRoot().setRightChild(r.getRightChild());
		}
		if(leftChildOfRightSubTree != null){
		 IpRangeTreeNode newLeftSubTree = r.getRightChild().getLeftChild();
		 while(true){
			 if(newLeftSubTree.getRightChild() == null) break;
			 	newLeftSubTree = newLeftSubTree.getRightChild();
		 }
		 newLeftSubTree.setRightChild(leftChildOfRightSubTree);
		 leftChildOfRightSubTree.setRoot(newLeftSubTree);
		}
	 }
		
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("*");
		
		updateString(sb, this.root, 1, 0);
		return sb.toString();
	}
	
	private void updateString(StringBuilder sb, IpRangeTreeNode root, int tabs, int type){
		if(root == null) return;
		sb.append(System.lineSeparator());
		for(int x = 0; x < tabs; x++){
			sb.append("  ").append("|");
		}
		sb.append(System.lineSeparator());
		for(int x = 0; x < tabs; x++){
			sb.append("   ");
		}
		sb.append("-->");
		if(type == 0) sb.append("{ROOT}");
		else if(type == 1) sb.append("{LEFT}");
		else sb.append("{RIGHT}");
		sb.append(root.toString());
		int idx = -1;
		if(root.getRightChild() != null){
			idx = tabs + 1;
		}
		updateString(sb, root.getLeftChild(), tabs +1, 1);
		updateString(sb, root.getRightChild(), tabs + 1, 2);
	}
	
	public boolean isCorrect(){
		return verifyTree(this.root);
	}
	
	private boolean verifyTree(IpRangeTreeNode node){
		boolean result = true;
		if(node == null){
			return true;
		}
		if((node.getLeftChild() == null) && (node.getRightChild() == null)){
			return true;
		}
		if(node.getLeftChild() != null){
			if((node.getLeftChild().getStartAddr() < node.getStartAddr()) &&
					(node.getLeftChild().getMaxEndAddress() < node.getStartAddr())){
				
				result = result & verifyTree(node.getLeftChild());
			} else{
				logger.error("Something wrong at " + node.getStartAddr() + " line " + node.getLineNums() + " left child " + node.getLeftChild().getStartAddr() + " line num "
						+ node.getLeftChild().getLineNums());
				return false;
			};
		}
		if(node.getRightChild() != null){
			if(node.getRightChild().getStartAddr() > node.getMaxEndAddress()){
				result = result & verifyTree(node.getRightChild());
			} else {
				logger.error("Something wrong at " + node.getStartAddr() + " line " + node.getLineNums() + " left child " + node.getRightChild().getStartAddr() + " line num "
						+ node.getRightChild().getLineNums());
				return false;
			}
		}
		
		return result;
	}

	public List<String> getCleanedStrings(){
		List<String> result = new ArrayList<>();
		getCleanStringsInOrder(result, this.root);
		return result;
	}
	
	private void getCleanStringsInOrder(List<String> res, IpRangeTreeNode node){
		if(node == null) return;
		getCleanStringsInOrder(res, node.getLeftChild());
		res.add(node.getCarrier()+","+IpReader.longToIp(node.getStartAddr()) +"," +  IpReader.longToIp(node.getMaxEndAddress()));
		getCleanStringsInOrder(res, node.getRightChild());
	}
	
}
