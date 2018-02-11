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

import java.util.LinkedHashMap;
import java.util.Map;

import de.henm.morn.core.FreeVariable;
import de.henm.morn.core.Term;

/**
 * Represents a mapping from variables to terms used for unification.
 * 
 * @author henm
 */
class Substitution {

    // TODO Use references instead of hash
    final Map<FreeVariable, Term> substitution;

    /**
     * True iff the substitution-mapping is consistent, i.e. no variable is
     * mapped to multiple, different terms.
     */
    boolean consistent;

    public Substitution() {
        this.substitution = new LinkedHashMap<>();
        this.consistent = true;
    }

    /**
     * @param {var} Variable to add a substitution for.
     * @param {term} The term which is substituted for the variable.
     * @return This substitution for builder-pattern.
     */
    public Substitution add(FreeVariable var, Term term) {
        final Term presentTerm = substitution.get(var);
        if (presentTerm != null && !presentTerm.equals(term)) {
            this.consistent = false;
        }

        this.substitution.put(var, term);
        return this;
    }

    /**
     * @return True iff substitution is consistent.
     */
    public boolean isConsistent() {
        return consistent;
    }

    /**
     * @return This substitution represented as a map from variable to term.
     */
    public Map<FreeVariable, Term> asMap() {
        return substitution;
    }
}