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
package at.sti2.wsmo4j.translator.rif;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.xml.sax.SAXException;

/**
 * Tests translation of RIF encoded WSML logical expression embedded in example
 * for WSMO-Lite specification.
 */
public class ITTranslateVideoOnDemand extends AbstractITTranslate {

	@Test
	public void testVideoOnDemandCondition() throws SAXException, IOException,
			ParserConfigurationException {
		assertNumberOfExpressionsOfFormula(1, "VideoOnDemand_Condition.xml");
	}

	@Test
	public void testVideoOnDemandEffect() throws SAXException, IOException,
			ParserConfigurationException {
		assertNumberOfExpressionsOfFormula(1, "VideoOnDemand_Effect.xml");
	}

}
