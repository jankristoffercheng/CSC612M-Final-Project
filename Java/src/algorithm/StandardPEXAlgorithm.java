package algorithm;

import java.util.ArrayList;

import model.Node;

public class StandardPEXAlgorithm extends PEXAlgorithm{
	
	public void findIndices(String inputString) {
		for(Node leaf : leaves) {
			String string = inputString;
			int index = string.indexOf(leaf.getPattern());
			while(index != -1) {
				leaf.addIndex(index);
				string = string.substring(0, index) + "$" + string.substring(index+1);
				index = string.indexOf(leaf.getPattern());
			}
		}
	}
	
	public void searchCandidates(String inputString) {
		EditDistance ed = new EditDistance();
		for(Node leaf : leaves) {
			for(int index : leaf.getIndices()) {
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
                    indices.add(p1);
			}
		}
	}
}
