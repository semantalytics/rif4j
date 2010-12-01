/*
 * Copyright (c) 2009, STI Innsbruck
 * 
 * This program is free software: you can redistribute it and/or modify 
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
