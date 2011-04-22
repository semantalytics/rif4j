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
package at.sti2.wsmo4j.reasoner.rif;

import java.util.Properties;

import at.sti2.rif4j.reasoner.Reasoner;
import at.sti2.rif4j.reasoner.ReasonerFactory;

/**
 * A {@link ReasonerFactory} that creates instances of
 * {@link WsmlRifReasoner}.
 * 
 * @author Adrian Marte
 */
public class WsmlRifReasonerFactory implements ReasonerFactory {

	@Override
	public Reasoner createReasoner() {
		return new WsmlRifReasoner();
	}

	@Override
	public Reasoner createReasoner(Properties configuration) {
		return new WsmlRifReasoner();
	}

}
