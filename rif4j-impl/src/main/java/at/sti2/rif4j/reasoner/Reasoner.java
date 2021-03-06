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
package at.sti2.rif4j.reasoner;

import at.sti2.rif4j.condition.Formula;
import at.sti2.rif4j.rule.Document;
import at.sti2.rif4j.rule.Group;
import at.sti2.rif4j.rule.Rule;

/**
 * <p>
 * A {@link Reasoner} is a RIF-BLD reasoner, which is able to check if a RIF-BLD
 * formula entails another formula. Logical entailment in RIF-BLD is defined as
 * follows.
 * </p>
 * <p>
 * <b>Definition (Models).</b> A multi-structure Î is a model of a formula, φ,
 * written as Î |= φ, iff TValÎ(φ) = t. Here φ can be a document or a
 * non-document formula. ☐
 * </p>
 * <p>
 * <b>Definition (Logical entailment).</b> Let φ and ψ be (document or
 * non-document) formulas. We say that φ entails ψ, written as φ |= ψ, if and
 * only if for every multi-structure, Î, Î |= φ implies Î |= ψ. ☐
 * </p>
 * 
 * @author Adrian Marte
 * @see <a href="http://www.w3.org/2005/rules/wiki/BLD#sec-logical-entailment"
 *      >http://www.w3.org/2005/rules/wiki/BLD#sec-logical-entailment</a>
 */
public interface Reasoner {

	public void register(Document document) throws ReasoningException;

	public void register(Group group) throws ReasoningException;

	public void register(Rule rule) throws ReasoningException;

	/**
	 * Returns <code>true</code> if the registered formulas entail the specified
	 * formula, <code>false</code> otherwise.
	 * 
	 * @param formula
	 * @return
	 * @throws ReasoningException
	 */
	public boolean entails(Formula formula) throws ReasoningException;

}
