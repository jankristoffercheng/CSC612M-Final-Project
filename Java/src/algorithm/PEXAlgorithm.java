package algorithm;

import java.util.ArrayList;
import java.util.List;

import model.Node;

public class PEXAlgorithm {
	protected ArrayList<Node> leaves;
	protected List<Integer> indices;
	
	public PEXAlgorithm() {
		leaves = new ArrayList<Node>();
		indices = new ArrayList<Integer>();
	}
	
	public ArrayList<Node> getLeaves() {
		return leaves;
	}

	public void setLeaves(ArrayList<Node> leaves) {
		this.leaves = leaves;
	}

	public List<Integer> getIndices() {
		return indices;
	}

	public void setIndices(List<Integer> indices) {
		this.indices = indices;
	}
	
	public void createTree(String subpattern, int i, int j, int k, Node parent, int plen) {
		Node node = new Node();
		node.setFrom(i);
		node.setTo(j);
		node.setParent(parent);
		node.setError(k);
		node.setPattern(subpattern);
		int left = (int) Math.ceil((k+1)/2);
		
		if(k == 0) {
			leaves.add(node);
		} else {
			int lk = (int) Math.floor((left*k)/(k+1));
			createTree(subpattern.substring(0, left*plen), i, i+left*plen-1, lk, node, plen);
			int rk = (int) Math.floor(((k + 1 - left) * k) / (k + 1));
			createTree(subpattern.substring(left*plen), i + left*plen, j, rk, node, plen);
		}
	}
}
