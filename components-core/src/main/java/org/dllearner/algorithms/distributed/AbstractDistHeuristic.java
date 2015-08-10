/**
 * Copyright (C) 2007-2011, Jens Lehmann
 *
 * This file is part of DL-Learner.
 *
 * DL-Learner is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * DL-Learner is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.dllearner.algorithms.distributed;

import java.util.Comparator;

import org.dllearner.core.AbstractComponent;
import org.dllearner.core.ComponentAnn;
import org.dllearner.core.ComponentInitException;
import org.dllearner.core.Heuristic;
import org.dllearner.core.config.ConfigOption;

import com.google.common.collect.ComparisonChain;

/**
 * Adapted version of the search algorithm heuristic for the ontology
 * engineering algorithm. The adaptions made here only concern the Comaprable
 * interface which is used with Comparator<DistOENode> here instead of
 * Comparator<OENode>.
 * The heuristic has a strong bias towards short descriptions (i.e. the algorithm is likely to be
 * less suitable for learning complex descriptions).
 *
 * @author Jens Lehmann
 * @author Patrick Westphal
 */
@ComponentAnn(name = "DistOEHeuristicRuntime", shortName = "distceloe_heuristic", version = 0.5)
public abstract class AbstractDistHeuristic extends AbstractComponent implements Heuristic, Comparator<DistOENode>{

	// strong penalty for long descriptions
	private double expansionPenaltyFactor = 0.1;
	// bonus for being better than parent node
	private double gainBonusFactor = 0.3;
	// penalty if a node OWLClassExpression has very many refinements since exploring
	// such a node is computationally very expensive
	private double nodeRefinementPenalty = 0.0001;

	@ConfigOption(name = "startNodeBonus", defaultValue="0.1")
	private double startNodeBonus = 0.1;

	public AbstractDistHeuristic() {

	}

	@Override
	public void init() throws ComponentInitException {

	}

	@Override
	public int compare(DistOENode node1, DistOENode node2) {
		return ComparisonChain.start()
				.compare(getNodeScore(node1), getNodeScore(node2))
				.compare(node1.getDescription(), node2.getDescription())
				.result();
	}

	public abstract double getNodeScore(DistOENode node);

	public double getExpansionPenaltyFactor() {
		return expansionPenaltyFactor;
	}

	public double getGainBonusFactor() {
		return gainBonusFactor;
	}

	public void setGainBonusFactor(double gainBonusFactor) {
		this.gainBonusFactor = gainBonusFactor;
	}

	public double getNodeRefinementPenalty() {
		return nodeRefinementPenalty;
	}

	public void setNodeRefinementPenalty(double nodeRefinementPenalty) {
		this.nodeRefinementPenalty = nodeRefinementPenalty;
	}

	public void setExpansionPenaltyFactor(double expansionPenaltyFactor) {
		this.expansionPenaltyFactor = expansionPenaltyFactor;
	}

	public double getStartNodeBonus() {
		return startNodeBonus;
	}

	public void setStartNodeBonus(double startNodeBonus) {
		this.startNodeBonus = startNodeBonus;
	}
}
