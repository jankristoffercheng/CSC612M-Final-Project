package model;

import java.util.ArrayList;

public class Node {
	private Node parent;
	private int from;
	private int to;
	private int error;
	private String pattern;
	private ArrayList<Integer> indices;
	
	public Node() {
		parent = null;
		indices = new ArrayList<Integer>();
	}
	
	public Node getParent() {
		return parent;
	}
	public void setParent(Node parent) {
		this.parent = parent;
	}
	public int getFrom() {
		return from;
	}
	public void setFrom(int from) {
		this.from = from;
	}
	public int getTo() {
		return to;
	}
	public void setTo(int to) {
		this.to = to;
	}
	public int getError() {
		return error;
	}
	public void setError(int error) {
		this.error = error;
	}
	public String getPattern() {
		return pattern;
	}
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	public ArrayList<Integer> getIndices() {
		return indices;
	}
	public void setIndices(ArrayList<Integer> indices) {
		this.indices = indices;
	}
	public void addIndex(int index) {
		this.indices.add(index);
	}
}
