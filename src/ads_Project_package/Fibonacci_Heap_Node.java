package ads_Project_package;

public class Fibonacci_Heap_Node {

	Fibonacci_Heap_Node child;
	Fibonacci_Heap_Node parent;
	Fibonacci_Heap_Node right_sibling;
	Fibonacci_Heap_Node left_sibling;
	int data;
	int degree;
	boolean childCut;
	String hashtag ;
	
	/**
	 * This constructor initializes node fields and assigns data.
	 * @author Maulik_Lalani
	 * @param data
	 */
	public Fibonacci_Heap_Node(int data,String hash ) {
		// TODO Auto-generated constructor stub
		this.child = null;
		this.parent= null;
		this.right_sibling = null;
		this.left_sibling = null;
		this.data =data;
		this.degree = 0;
		this.childCut = false;
		this.hashtag=hash;
	}
}
