package algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import model.Node;

public class ParallelPEXAlgorithm extends PEXAlgorithm{
	
	private int nThreads;
	
	public ParallelPEXAlgorithm(int nThreads) {
		indices = Collections.synchronizedList(new ArrayList<Integer>());
		this.nThreads = nThreads;
	}
	
	public void findIndices(String inputString) {
		ExecutorService executor = Executors.newFixedThreadPool(nThreads);
		for(Node leaf : leaves) {
			Runnable worker = new FindIndicesThread(inputString, leaf);  
            executor.execute(worker);
		}
		executor.shutdown();  
        while (!executor.isTerminated()) {   } 
	}
	
	public void searchCandidates(String inputString) {
		ExecutorService executor = Executors.newFixedThreadPool(nThreads);
		for(Node leaf : leaves) {
			for(int index : leaf.getIndices()) {
				Runnable worker = new FindStringThread(inputString, leaf, index, indices);  
	            executor.execute(worker);
			}		
		}
		executor.shutdown();  
        while (!executor.isTerminated()) {   } 
	}
}

class FindStringThread implements Runnable {
	
	private String inputString;
	private Node leaf;
	private int index;
	private List<Integer> indices;
	
    public FindStringThread(String inputString, Node leaf, int index, List<Integer> indices){  
        this.inputString = inputString;
    	this.leaf = leaf;
        this.index = index;
        this.indices = indices;
    }  
    
    public void run() {  
    	EditDistance ed = new EditDistance();
    	int i = leaf.getFrom();
        Node parent = leaf.getParent();
        boolean cand = true;
        int p1 = -1;
        while(cand && parent != null) {
            p1 = index - (i - parent.getFrom());
            int p2 = index + (parent.getTo() - i) + 1;
            if(p1 < 0)
                p1 = 0;
            if(p2 > inputString.length())
                p2 = inputString.length();
            int distance = ed.compute(inputString.substring(p1,p2), parent.getPattern());
            if(distance <= parent.getError())
                parent = parent.getParent();
            else {
                p1 -= parent.getError();
                int counter = parent.getError();
                boolean withp1 = false;
                while(counter != 0 && !withp1) {
                    distance = ed.compute(inputString.substring(p1,p2), parent.getPattern());
                    if(distance <= parent.getError()) {
                        parent = parent.getParent();
                        withp1 = true;
                    } else {
                        counter -= 1;
                        p1 += 1;
                    }
                }
                if(!withp1) {
                    p2 += parent.getError();
                    counter = parent.getError();
                    boolean withp2 = false;
                    while(counter != 0 && !withp2) {
                        distance = ed.compute(inputString.substring(p1,p2), parent.getPattern());
                        if(distance <= parent.getError()) {
                            parent = parent.getParent();
                            withp2 = true;
                        } else {
                            counter -= 1;
                            p2 -= 1;
                        }
                    }
                    if(!withp2)
                        cand = false;
                }
            }
		}
        if(cand && !indices.contains(p1))
        	synchronized (indices) {
        		indices.add(p1);
			}
    }
} 

class FindIndicesThread implements Runnable {

	private String inputString;
	private Node leaf;
	
	public FindIndicesThread(String inputString ,Node leaf) {
		this.inputString = inputString;
		this.leaf = leaf;
	}
	
	public void run() {
		String string = inputString;
		int index = string.indexOf(leaf.getPattern());
		while(index != -1) {
			leaf.addIndex(index);
			string = string.substring(0, index) + "$" + string.substring(index+1);
			index = string.indexOf(leaf.getPattern());
		}
	}
	
}