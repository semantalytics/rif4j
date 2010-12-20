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

import java.util.Properties;

/**
 * A {@link ReasonerFactory} is a point for creating instances of
 * {@link Reasoner} objects. A RIF-BLD reasoner is able to check if a RIF-BLD
 * rule (or document or group) entails another rule (or document or group).
 * 
 * @author Adrian Marte
 */
public interface ReasonerFactory {

	public Reasoner createReasoner();

	public Reasoner createReasoner(Properties configuration);

}
