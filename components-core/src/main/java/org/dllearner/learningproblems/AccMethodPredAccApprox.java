/**
 * Copyright (C) 2007 - 2016, Jens Lehmann
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
package org.dllearner.learningproblems;

import org.apache.log4j.Logger;
import org.dllearner.core.ComponentAnn;
import org.dllearner.core.Reasoner;
import org.dllearner.core.config.ConfigOption;
import org.dllearner.learningproblems.sampling.CELOEPlusSampling;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@ComponentAnn(name = "Predictive Accuracy Approximate", shortName = "approx.prec_acc", version = 0)
public class AccMethodPredAccApprox extends AccMethodPredAcc implements AccMethodTwoValuedApproximate {
	final static Logger logger = Logger.getLogger(AccMethodPredAccApprox.class);
	@Override
	public void init() {
		logger.warn("Approximating predictive accuracy is an experimental feature. USE IT AT YOUR OWN RISK. If you consider to use it for anything serious, please extend the unit tests at org.dllearner.test.junit.HeuristicTests first and verify that it works.");
	}
	// approximation and F-measure
	// (taken from class learning => super class instances corresponds to negative examples
	// and class instances to positive examples)
    @ConfigOption(description = "The Approximate Delta", defaultValue = "0.05", required = false)
	private double approxDelta = 0.05;
	@ConfigOption(description = "(configured by the learning problem)")
	private Reasoner reasoner;
	private boolean doSample = true;
	public void doSamplingTask(String className, Collection<OWLIndividual> pos, Collection<OWLIndividual> neg)
	{ 
		// for testing this, we must use doSample=true
		CELOEPlusSampling.getInstance().sample(reasoner, className, pos, neg);
		
	}
	@Override
	public double getAccApprox2(OWLClassExpression description, Collection<OWLIndividual> positiveExamples, Collection<OWLIndividual> negativeExamples, double noise) {
		int maxNotCovered = (int) Math.ceil(noise*positiveExamples.size());

		int notCoveredPos = 0;
		//			int notCoveredNeg = 0;
		
		int posClassifiedAsPos = 0;
		int negClassifiedAsNeg = 0;

		int nrOfPosChecks = 0;
		int nrOfNegChecks = 0;
		
		OWLIndividual negExample;
		OWLIndividual posExample;
		
		Iterator<OWLIndividual> itPos = positiveExamples.iterator();
		Iterator<OWLIndividual> itNeg = negativeExamples.iterator();
		
		
		if (doSample) {
			doSamplingTask(description.getClassExpressionType().getIRI().toString(), positiveExamples, negativeExamples);

			do {

				if ((posExample = getNextPosInstance()) != null) {
					// System.out.println(posExample);

					if (reasoner.hasType(description, posExample)) {
						posClassifiedAsPos++;
					} else {
						notCoveredPos++;
					}
					nrOfPosChecks++;

					// take noise into account
					if (notCoveredPos > maxNotCovered) {
						return -1;
					}
				}

				if ((negExample = getNextNegInstance()) != null) {

					if (!reasoner.hasType(description, negExample)) {
						negClassifiedAsNeg++;
					}
					nrOfNegChecks++;
				}

				// compute how accurate our current approximation is and return
				// if it is sufficiently accurate
				double approx[] = Heuristics.getPredAccApproximation(positiveExamples.size(), negativeExamples.size(),
						1, nrOfPosChecks, posClassifiedAsPos, nrOfNegChecks, negClassifiedAsNeg);
				if (approx[1] < approxDelta) {
					// System.out.println(approx[0]);
					return approx[0];
				}
			} while (((negExample) != null) || ((posExample) != null));
		} else {

			// special case: we test positive and negative examples in turn

			do {
				// in each loop we pick 0 or 1 positives and 0 or 1 negative
				// and classify it
				if (itPos.hasNext()) {
					posExample = itPos.next();
					// System.out.println(posExample);

					if (reasoner.hasType(description, posExample)) {
						posClassifiedAsPos++;
					} else {
						notCoveredPos++;
					}
					nrOfPosChecks++;

					// take noise into account
					if (notCoveredPos > maxNotCovered) {
						return -1;
					}
				}
				if (itNeg.hasNext()) {
					negExample = itNeg.next();
					if (!reasoner.hasType(description, negExample)) {
						negClassifiedAsNeg++;
					}
					nrOfNegChecks++;
				}

				// compute how accurate our current approximation is and return
				// if it is sufficiently accurate
				double approx2[] = Heuristics.getPredAccApproximation(positiveExamples.size(), negativeExamples.size(),
						1, nrOfPosChecks, posClassifiedAsPos, nrOfNegChecks, negClassifiedAsNeg);
				if (approx2[1] < approxDelta) {
					// System.out.println(approx2[0]);
					return approx2[0];
				}

			} while ((itPos.hasNext() || itNeg.hasNext()));
		}

		return Heuristics.getPredictiveAccuracy(positiveExamples.size(), negativeExamples.size(), posClassifiedAsPos,
				negClassifiedAsNeg, 1);
	}
	public OWLIndividual getNextPosInstance()
	{
		OWLIndividual nextSample = CELOEPlusSampling.getInstance().nextPositive();
		return nextSample;
	}
	public OWLIndividual getNextNegInstance()
	{
		OWLIndividual nextSample = CELOEPlusSampling.getInstance().nextNegative();
		return nextSample;
	}
	@Override
	public double getApproxDelta() {
		return approxDelta;
	}

	@Override
	public void setApproxDelta(double approxDelta) {
		this.approxDelta = approxDelta;
	}

	@Override
	public void setReasoner(Reasoner reasoner) {
		this.reasoner = reasoner;
	}

}
