package algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import model.Node;

public class ParallelPEXAlgorithm extends PEXAlgorithm{
	
	private int nThreads;
	private ExecutorService executor;
	
	public ParallelPEXAlgorithm(int nThreads) {
		indices = Collections.synchronizedList(new ArrayList<Integer>());
		this.nThreads = nThreads;
	}
	
	public void findIndices(String inputString) {

//		int sLen = inputString.length();
//		for(Node leaf : leaves) {
//			String pattern = leaf.getPattern();
//			int pLen = pattern.length();
//			for(int i = 0;i <= sLen - pLen; i++) {
//				int j=0;
//				while(j<pLen && (inputString.charAt(i+j)==pattern.charAt(j))) {
//	               j=j+1;
//	               if(j==pLen)
//	                   leaf.addIndex(i);
//				}
//			}
//		}
		executor = Executors.newFixedThreadPool(nThreads);
		for(Node leaf : leaves) {
			Runnable worker = new FindIndicesThread(inputString, leaf);  
            executor.execute(worker);
		}
		try {
			executor.shutdown();
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void searchCandidates(String inputString) {
		executor = Executors.newFixedThreadPool(nThreads);
		List<Set<Callable<Object>>> allCallables = new ArrayList<Set<Callable<Object>>>();
		for(Node leaf : leaves) {
			Callable worker = new SearchLeafThread(inputString, leaf, indices);
            Future<Set<Callable<Object>>> future = executor.submit(worker);
            try {
            	Set<Callable<Object>> callables = future.get();
            	executor.invokeAll(callables);
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			executor.shutdown();
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

class SearchLeafThread implements Callable {

	private String inputString;
	public Node leaf;
	private List<Integer> indices;
	
	public SearchLeafThread(String inputString, Node leaf, List<Integer> indices){  
        this.inputString = inputString;
    	this.leaf = leaf;
        this.indices = indices;
    }
	
	public Set<Callable<Object>> call() {
		Set<Callable<Object>> workers = new HashSet<Callable<Object>>();
		for(int index : leaf.getIndices()) {
			Callable worker = new FindStringThread(inputString, leaf, index, indices);  
            workers.add(worker);
		}
		return workers;
	}
}

class FindStringThread implements Callable {
	
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
    
    public Object call() {
    	EditDistance ed = new EditDistance();
    	int i = leaf.getFrom();
        Node parent = leaf.getParent();
        boolean cand = true;
        int p1 = index - i;
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
            	p1 -= 1;
            	if(p1 < 0)
                    p1 = 0;
                int counter = parent.getError();
                boolean withp1 = false;
                while(counter != 0 && !withp1) {
                    distance = ed.compute(inputString.substring(p1,p2), parent.getPattern());
                    if(distance <= parent.getError()) {
                        parent = parent.getParent();
                        withp1 = true;
                    } else {
                        counter -= 1;
                        p1 -= 1;
                        if(p1 < 0)
                            p1 = 0;
                    }
                }
                if(!withp1) {
                	p1 = index - (i - parent.getFrom());
                	p2 += 1;
                    if(p2 > inputString.length())
                        p2 = inputString.length();
                    counter = parent.getError();
                    boolean withp2 = false;
                    while(counter != 0 && !withp2) {
                        distance = ed.compute(inputString.substring(p1,p2), parent.getPattern());
                        if(distance <= parent.getError()) {
                            parent = parent.getParent();
                            withp2 = true;
                        } else {
                            counter -= 1;
                            p2 += 1;
                            if(p2 > inputString.length())
                                p2 = inputString.length();
                        }
                    }
                    if(!withp2)
                        cand = false;
                }
            }
		}
    	synchronized (indices) {
	        if(cand && !indices.contains(p1)){
	        		indices.add(p1);
	        }
		}
    	return null;
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
		int sLen = inputString.length();
		String pattern = leaf.getPattern();
		int pLen = pattern.length();
		for(int i = 0;i <= sLen - pLen; i++) {
			int j=0;
			while(j<pLen && (inputString.charAt(i+j)==pattern.charAt(j))) {
               j=j+1;
               if(j==pLen)
                   leaf.addIndex(i);
			}
		}
	}
	
}