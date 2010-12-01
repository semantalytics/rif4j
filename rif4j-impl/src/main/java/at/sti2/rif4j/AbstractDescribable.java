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

import java.util.ArrayList;
import java.util.List;

import at.sti2.rif4j.condition.Constant;
import at.sti2.rif4j.condition.Frame;

/**
 * This class provides a basic implementation of the Describable interface.
 * 
 * @author Adrian Marte
 */
public abstract class AbstractDescribable implements Describable {

	private Constant id;

	private List<Frame> data;

	public AbstractDescribable() {
		data = new ArrayList<Frame>();
	}

	public Constant getId() {
		return id;
	}

	public void setId(Constant id) {
		this.id = id;
	}

	public List<Frame> getMetadata() {
		return data;
	}

	public void setMetadata(List<Frame> data) {
		this.data = data;
	}

}
