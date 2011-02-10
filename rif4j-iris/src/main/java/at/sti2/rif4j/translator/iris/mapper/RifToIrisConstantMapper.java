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
package at.sti2.rif4j.translator.iris.mapper;

import org.deri.iris.api.factory.IConcreteFactory;
import org.deri.iris.api.factory.ITermFactory;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.factory.Factory;
import org.deri.iris.terms.concrete.CanonicalFactory;

import at.sti2.rif4j.condition.Constant;

/**
 * Maps RIF constants to IRIS terms.
 * 
 * @author Adrian Marte
 */
public class RifToIrisConstantMapper {

	private CanonicalFactory canonicalFactory;

	public RifToIrisConstantMapper() {
		this(Factory.TERM, Factory.CONCRETE);
	}

	public RifToIrisConstantMapper(ITermFactory termFactory,
			IConcreteFactory factory) {
		this.canonicalFactory = new CanonicalFactory(termFactory, factory);
	}

	/**
	 * Maps a RIF constant, given its datatype URI and the value, to an IRIS
	 * term.
	 * 
	 * @param constant
	 *            The RIF constant.
	 * @return The corresponding IRIS term for the specific RIF constant, or
	 *         <code>null</code> if there is no corresponding IRIS term.
	 * @throws IllegalArgumentException
	 *             If the value is not compatible with the type.
	 */
	public ITerm toIrisTerm(Constant constant) {
		String type = constant.getType();
		String value = constant.getText();

		return canonicalFactory.createTerm(value, type);
	}

}
