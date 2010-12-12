/*
 * Copyright 2010 STI Innsbruck
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package at.sti2.rif4j.transformer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import at.sti2.rif4j.condition.AndFormula;
import at.sti2.rif4j.condition.Attribute;
import at.sti2.rif4j.condition.Formula;
import at.sti2.rif4j.condition.Frame;
import at.sti2.rif4j.condition.OrFormula;

/**
 * @author Adrian Marte
 */
public class FormulaNormalizer {

	public Formula normalize(Formula formula) {
		Formula newFormula;

		newFormula = normalizeConjuncts(formula);

		if (newFormula != null) {
			return newFormula;
		}

		newFormula = normalizeDisjuncts(formula);

		if (newFormula != null) {
			return newFormula;
		}

		newFormula = normalizeFrame(formula);

		if (newFormula != null) {
			return newFormula;
		}

		return formula;
	}

	private Formula normalizeConjuncts(Formula formula) {
		if (!(formula instanceof AndFormula)) {
			return null;
		}

		// TODO Handle And(Fx, Or(Fy, Fz)).

		AndFormula andFormula = (AndFormula) formula;
		List<Formula> newConjuncts = new ArrayList<Formula>();

		for (Formula conjunct : andFormula.getFormulas()) {
			Formula newFormula = normalize(conjunct);
			newConjuncts.add(newFormula);
		}

		return new AndFormula(newConjuncts);
	}

	private Formula normalizeDisjuncts(Formula formula) {
		if (!(formula instanceof OrFormula)) {
			return null;
		}

		OrFormula orFormula = (OrFormula) formula;
		List<Formula> newDisjuncts = new ArrayList<Formula>();

		for (Formula conjunct : orFormula.getFormulas()) {
			Formula newFormula = normalize(conjunct);
			newDisjuncts.add(newFormula);
		}

		return new AndFormula(newDisjuncts);
	}

	private Formula normalizeFrame(Formula formula) {
		if (!(formula instanceof Frame)) {
			return null;
		}

		Frame frame = (Frame) formula;
		List<Formula> newFrames = new ArrayList<Formula>();

		for (Attribute attribute : frame.getAttributes()) {
			Frame newFrame = new Frame(frame.getObject(),
					Collections.singletonList(attribute));
			newFrames.add(newFrame);
		}

		return new AndFormula(newFrames);
	}
}
