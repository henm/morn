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
package de.henm.morn.reasoner;

import de.henm.morn.core.FreeVariable;
import de.henm.morn.core.Term;

/**
 * A mutable Term. Useful for efficient unification.
 * 
 * @author henm
 */
interface MutableTerm {

    /**
     * Check if a variable occurs in this term.
     * 
     * @param {x} Variable to check.
     * @return True iff X does occur in this term.
     */
    boolean contains(MutableVariable x);

    /**
     * Replace the occurences of a variable by the given term.
     * 
     * @param {x} The variable to replace.
     * @param {replaceBy} The term to replace the variable by.
     */
    void replace(MutableVariable x, MutableTerm replaceBy);

    /**
     * @return The original term which was used to construct this mutable term.
     */
    Term getOriginalTerm();
}