/**
 * 
 */
package org.dllearner.algorithms.qtl.operations;

import java.util.Random;

import org.dllearner.algorithms.qtl.datastructures.impl.RDFResourceTree;
import org.dllearner.algorithms.qtl.operations.lgg.LGGGenerator;
import org.dllearner.algorithms.qtl.operations.lgg.LGGGeneratorSimple;
import org.junit.Test;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.NodeFactory;

/**
 * A test class to analyze the performance of the application of different LGG
 * implementations in dependence on the size and complexity of the input trees.
 * 
 * @author Lorenz Buehmann
 *
 */
public class LGGPerformance {
	
//	@Test
	public void testPerformance() {
		LGGGenerator lggGen = new LGGGeneratorSimple();
		
		RDFResourceTree child1Common = new RDFResourceTree(NodeFactory.createURI("ab"));
		RDFResourceTree child2Common = new RDFResourceTree(NodeFactory.createURI("aabb"));
		RDFResourceTree child3Common = new RDFResourceTree(NodeFactory.createURI("aaabbb"));
		Node edge1Common = NodeFactory.createURI("p");
		Node edge2Common = NodeFactory.createURI("q");
		Node edge3Common = NodeFactory.createURI("r");
		
		Random rnd = new Random(123);
		
		int level1 = 20;
		int level2 = 30;
		int level3 = 20;
		
		String var = "a";
		RDFResourceTree tree1 = new RDFResourceTree(NodeFactory.createURI(var));
		for(int i = 0; i < level1; i++) {
			
			RDFResourceTree child1;
			if(rnd.nextBoolean()) {
				child1 = new RDFResourceTree(child1Common);
			} else {
				child1 = new RDFResourceTree(NodeFactory.createURI(var + "_" + i));
			}
			Node edge1 = edge1Common;
			tree1.addChild(child1, edge1);
			
			for (int j = 0; j < level2; j++) {
				
				RDFResourceTree child2;
				if(rnd.nextBoolean()) {
					child2 = new RDFResourceTree(child2Common);
				} else {
					child2 = new RDFResourceTree(NodeFactory.createURI(var + var + "_" + j));
				}
				Node edge2 = rnd.nextBoolean() ? edge2Common : NodeFactory.createURI("q" + j);
				child1.addChild(child2, edge2);
				
				for (int k = 0; k < level3; k++) {
					
					RDFResourceTree child3;
					if(rnd.nextBoolean()) {
						child3 = new RDFResourceTree(child3Common);
					} else {
						child3 = new RDFResourceTree(NodeFactory.createURI(var + var + "_" + k));
					}
					Node edge3 = rnd.nextBoolean() ? edge3Common : NodeFactory.createURI("r" + k);
					child2.addChild(child3, edge3);
				}
			}
		}
		
		var = "b";
		RDFResourceTree tree2 = new RDFResourceTree(NodeFactory.createURI("b"));
			for(int i = 0; i < level1; i++) {
			
			RDFResourceTree child1;
			if(rnd.nextBoolean()) {
				child1 = new RDFResourceTree(child1Common);
			} else {
				child1 = new RDFResourceTree(NodeFactory.createURI(var + "_" + i));
			}
			Node edge1 = edge1Common;
			tree2.addChild(child1, edge1);
			
			for (int j = 0; j < level2; j++) {
				
				RDFResourceTree child2;
				if(rnd.nextBoolean()) {
					child2 = new RDFResourceTree(child2Common);
				} else {
					child2 = new RDFResourceTree(NodeFactory.createURI(var + var + "_" + j));
				}
				Node edge2 = rnd.nextBoolean() ? edge2Common : NodeFactory.createURI("q" + j);
				child1.addChild(child2, edge2);
				
				for (int k = 0; k < level3; k++) {
					
					RDFResourceTree child3;
					if(rnd.nextBoolean()) {
						child3 = new RDFResourceTree(child3Common);
					} else {
						child3 = new RDFResourceTree(NodeFactory.createURI(var + var + "_" + k));
					}
					Node edge3 = rnd.nextBoolean() ? edge3Common : NodeFactory.createURI("r" + k);
					child2.addChild(child3, edge3);
				}
			}
		}
		
		long start = System.currentTimeMillis();
		RDFResourceTree lgg = lggGen.getLGG(tree1, tree2);
		long end = System.currentTimeMillis();
		System.out.println("Operation took " + (end - start) + "ms");
		
		System.out.println(lgg.getStringRepresentation());
	}

}
