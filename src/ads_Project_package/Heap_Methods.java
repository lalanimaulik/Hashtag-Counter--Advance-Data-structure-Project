package ads_Project_package;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class Heap_Methods {

	//Max head pointer
	Fibonacci_Heap_Node heapPointer=null;
	
	/**
	 * This function inserts new root node to the Fibonacci heap 
	 * @param newNode 
	 * @author Maulik_Lalani
	 * @return Fibonacci_Heap_Node 
	 */
	public Fibonacci_Heap_Node insertNewNode(Fibonacci_Heap_Node newNode){
		
		//If Fibonacci heap is empty 
		if(heapPointer == null){
			
			heapPointer = newNode;
			heapPointer.right_sibling=heapPointer;
			heapPointer.left_sibling= heapPointer;
		}
		// Add new node to the right sibling of the max Heap pointer and update the max heap pointer
		else{
			Fibonacci_Heap_Node holdRightPointer = heapPointer.right_sibling;
			heapPointer.right_sibling = newNode;
		
			if(holdRightPointer != null){
				newNode.right_sibling = holdRightPointer;
				holdRightPointer.left_sibling = newNode;
			}
			else{
				heapPointer.left_sibling = newNode;
				newNode.right_sibling = heapPointer;
			}
			newNode.left_sibling = heapPointer;
			
			// Update the max heap pointer.
			if(newNode.data > heapPointer.data){
				heapPointer= newNode;	
			}
		}
		return newNode;
	}
	
	/**
	 * Removes the max heap pointer from the fibonacci heap and does pairwise combining of all the root nodes.
	 * @author Maulik_Lalani
	 * @return Fibonacci_Heap_Node maxValue
	 * @param 
	 */
	public Fibonacci_Heap_Node removeMax(){

		Fibonacci_Heap_Node maxValue = heapPointer ;
		
		// Arraylist to store all the root nodes and it will be use for pairwise combine of nodes
 		ArrayList<Fibonacci_Heap_Node> rootNodes = new ArrayList<Fibonacci_Heap_Node>();
		
		Fibonacci_Heap_Node currentChild= heapPointer.child;
	
		//add all the child nodes of max heap node to the list.
		if(currentChild!=null){
			rootNodes.add(currentChild);
			currentChild=currentChild.right_sibling;
		}
		while(currentChild!=heapPointer.child && currentChild!=null){
			
			rootNodes.add(currentChild);
			currentChild= currentChild.right_sibling;
		}
		
		//add all the siblings of the heap pointer to the list 
		currentChild = heapPointer.right_sibling;
		
		while(currentChild!=heapPointer && currentChild!=null){
			
			rootNodes.add(currentChild);
			currentChild= currentChild.right_sibling;
		}
		
		// This method will perform pairwise combine of all the root nodes.
		meldTrees(rootNodes);
		return maxValue;
	}
	
	/**
	 * This method iterates through the list of root nodes and calls pairwiseCombine() method to merge nodes based on the degree 
	 * @author Maulik_Lalani
	 * @param rootNodes (Contains all the rootnodes for the pairwise combine)
	 * @return void
	 */
	public void meldTrees(ArrayList<Fibonacci_Heap_Node> rootNodes ){
		
		// This map is used as a table which stores the node and corresponding degrees  
		HashMap<Integer,Fibonacci_Heap_Node> nodeDegrees = new HashMap<Integer,Fibonacci_Heap_Node>();
	
		
		//check if not empty 
		Fibonacci_Heap_Node currentChild = null ;
		if(rootNodes.size()>0){
			currentChild= rootNodes.get(0);
		}
		int length =0;
		
		//iterate through the root nodes and create the max heap with unique degrees of all the nodes.
		while(length<rootNodes.size()){
			
			// If node degree is already present in the table then call pairwiseCombine()
			if(nodeDegrees.containsKey(currentChild.degree)){
				
				if(currentChild.data>nodeDegrees.get(currentChild.degree).data){
					int previousKey = currentChild.degree;
					currentChild=pairwiseCombine(currentChild,nodeDegrees.get(currentChild.degree));
					nodeDegrees.remove(previousKey);
					
				}else{
					int previousKey = currentChild.degree;	
					currentChild=pairwiseCombine(nodeDegrees.get(currentChild.degree),currentChild);
					nodeDegrees.remove(previousKey);
				}
			}
			//If root degree is not present in the table then put the root node and degree in the table. 
			else{
				currentChild.left_sibling=currentChild;
				currentChild.right_sibling=currentChild;
				nodeDegrees.put(currentChild.degree, currentChild);
				length++;
				//go to next node 
				if(length<rootNodes.size()){
					currentChild=rootNodes.get(length);
				}
			}
		}
		// Once we have root nodes with unique degrees, combine them to form the circular linked list using mergeAllNodes() method
		heapPointer=null;
		for (Fibonacci_Heap_Node currentRootNode : nodeDegrees.values()) {
			mergeAllNodes(currentRootNode);
		}
	}
	
	//first node is large
	/**
	 * This method combines two root nodes based on the frequency and make small node the child of the large node.
	 * @author Maulik_Lalani
	 * @param largeNode
	 * @param smallNode
	 * @return Fibonacci_Heap_Node
	 */
	public Fibonacci_Heap_Node pairwiseCombine(Fibonacci_Heap_Node largeNode, Fibonacci_Heap_Node smallNode){
		
		Fibonacci_Heap_Node previousChild;
		previousChild= largeNode.child;
		
		// traverse through the child node and go the right most child from root.
		for(int i=0;i<largeNode.degree-1;i++){
			previousChild = previousChild.right_sibling;
		}

		//If large node does not have child.
		if(previousChild==null){
			largeNode.child= smallNode;
			smallNode.parent =largeNode;
			smallNode.left_sibling= smallNode;
			smallNode.right_sibling = smallNode;
			largeNode.left_sibling = largeNode;
			largeNode.right_sibling = largeNode;
			largeNode.parent=null;
		}
		// If large node has child 
		else{
			smallNode.right_sibling=largeNode.child;
			largeNode.child.left_sibling= smallNode;
			previousChild.right_sibling= smallNode;
			smallNode.left_sibling=previousChild;
			largeNode.left_sibling = largeNode;
			largeNode.right_sibling = largeNode;
			smallNode.parent= largeNode;
			largeNode.parent=null;
		}
		// Increase the degree of the large node 
		largeNode.degree= largeNode.degree+ 1;
		return largeNode;
	}
	
	
	/** This method assigns appropriate right and left child of the newly created root node
	 * @author Maulik_Lalani
	 * @param newNode
	 * @return newNode
	 */
	public Fibonacci_Heap_Node mergeAllNodes(Fibonacci_Heap_Node newNode){
		
		//If fibonacci heap is empty 
		if(heapPointer == null){
			heapPointer = newNode;		
		}
		else{
			Fibonacci_Heap_Node holdRightPointer = heapPointer.right_sibling;
			heapPointer.right_sibling = newNode;
			
			//If heap has more than one root node
			if(holdRightPointer != null){
				newNode.right_sibling = holdRightPointer;
				holdRightPointer.left_sibling = newNode;
			}
			
			//If heap has only one root node
			else{
				heapPointer.left_sibling = newNode;
				newNode.right_sibling = heapPointer;
			}
			newNode.left_sibling = heapPointer;
			
			//update the heap pointer 
			if(newNode.data > heapPointer.data){
				heapPointer= newNode;
			}
		}
		return newNode;
	}
	
	
	/**
	 * This method updates the frequency of a hashtag, removes it from the tree if it is greater than parent and inserts it at the top.
	 * @author Maulik_Lalani
	 * @param node
	 * @param frequency
	 */
	public void increaseKey(Fibonacci_Heap_Node node, int frequency){
			
		node.data= node.data+frequency;
		node.childCut=true;
		Fibonacci_Heap_Node currentNode =node;
		
		// Traverse the tree upwards from that node and check the child cut field and cut from the tree if child cut is true.
		while(currentNode != null && node.parent!=null){
 
			Fibonacci_Heap_Node tempNode = currentNode;
			currentNode= currentNode.parent;
			
			//If Child cut field is true and it is larger than the parent, cut the node and re-insert it.
			if(tempNode.childCut==true && tempNode.parent!=null && node.parent.data<node.data){
				
				if(tempNode==tempNode.parent.child && tempNode.parent.degree>1){
					tempNode.parent.child = tempNode.right_sibling;
				}
				else if(tempNode.parent.degree==1){
					tempNode.parent.child= null;
				}
				
				tempNode.left_sibling.right_sibling = tempNode.right_sibling;
				tempNode.right_sibling.left_sibling = tempNode.left_sibling;
				tempNode.parent.degree = tempNode.parent.degree-1;
				tempNode.parent = null;	
				tempNode.childCut=false;
				insertNewNode(tempNode);
			}
			// If parent is null then just update the child cut field.
			else if(tempNode.parent==null){
					tempNode.childCut=false;
					if(tempNode.data>heapPointer.data){
						heapPointer=tempNode;
					}
			}
			// Update the child cut field if it is true.
			else{
				tempNode.childCut=true;
				break;
			}
		}
		if(node.parent==null && node.data>heapPointer.data){
			node.childCut=false;
			heapPointer = node;
			
		}
	}
}
