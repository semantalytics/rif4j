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

import java.util.List;

import at.sti2.rif4j.Describable;

/**
 * @author Adrian Marte
 * @author Daniel Winkler
 */
public interface Uniterm extends Describable {

	public List<Argument> getArguments();

	public void setArguments(List<Argument> arguments);

	public Constant getOperator();

	public void setOperator(Constant operator);

}
