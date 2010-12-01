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
package at.sti2.rif4j;

/**
 * This class defines helper methods which ease the process of asserting certain
 * constraints on values.
 * 
 * @author Adrian Marte
 */
public final class Assertions {

	public static void notNull(String name, Object value)
			throws NullPointerException {
		if (value == null) {
			throw new NullPointerException(name + " must not be null");
		}
	}

}
