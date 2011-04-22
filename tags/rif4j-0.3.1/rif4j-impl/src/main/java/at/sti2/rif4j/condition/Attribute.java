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
package at.sti2.rif4j.condition;

import at.sti2.rif4j.Assertions;

/**
 * @author Adrian Marte
 */
public class Attribute {

	private Term name;

	private Term value;

	public Attribute(Term name, Term value) {
		Assertions.notNull("name", name);
		Assertions.notNull("value", value);

		this.name = name;
		this.value = value;
	}

	public Term getName() {
		return name;
	}

	public void setName(Term name) {
		Assertions.notNull("name", name);

		this.name = name;
	}

	public Term getValue() {
		return value;
	}

	public void setValue(Term value) {
		Assertions.notNull("value", value);

		this.value = value;
	}

}
