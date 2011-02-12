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

import at.sti2.rif4j.condition.AndFormula;
import at.sti2.rif4j.condition.AtomicFormula;
import at.sti2.rif4j.condition.ExistsFormula;
import at.sti2.rif4j.condition.ExternalFormula;
import at.sti2.rif4j.condition.FormulaVisitor;
import at.sti2.rif4j.condition.OrFormula;

/**
 * <p>
 * A visitor that represents an empty implementation of a {@link FormulaVisitor}
 * , where each method does not compute anything.
 * </p>
 * <p>
 * Subclasses of this class should override the methods that need to be
 * implemented.
 * </p>
 * 
 * @author Adrian Marte
 */
public class DefaultFormulaVisitor implements FormulaVisitor {

	@Override
	public void visit(AndFormula andFormula) {
		// Do nothing.
	}

	@Override
	public void visit(OrFormula orFormula) {
		// Do nothing.
	}

	@Override
	public void visit(ExistsFormula existsFormula) {
		// Do nothing.
	}

	@Override
	public void visit(ExternalFormula externalFormula) {
		// Do nothing.
	}

	@Override
	public void visit(AtomicFormula atomicFormula) {
		// Do nothing.
	}

}
