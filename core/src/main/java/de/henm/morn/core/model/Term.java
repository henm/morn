/**
 * Copyright 2017-present henm
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.henm.morn.core.model;

/**
 * @author henm
 */
public interface Term {

    /**
     * @return True iff term is ground.
     */
    boolean isGround();

    default Rule entailed(Term... terms) {
        return new Rule(this, terms);
    }

    /**
     * Check if a variable occurs in this term.
     * 
     * @param {x} Variable to check.
     * @return True iff X does occur in this term.
     */
    boolean contains(Variable x);

}