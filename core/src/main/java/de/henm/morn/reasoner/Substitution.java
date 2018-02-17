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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.henm.morn.core.CompoundTerm;
import de.henm.morn.core.CompoundTermFactory;
import de.henm.morn.core.Term;

/**
 * Represents a mapping from variables to terms used for unification.
 * 
 * @author henm
 */
class Substitution {

    // TODO Use references instead of hash
    final Map<Term, Term> substitution;
    final CompoundTermFactory compoundTermFactory;

    public Substitution() {
        this.substitution = new LinkedHashMap<>();
        this.compoundTermFactory = new CompoundTermFactory();
    }

    /**
     * @param {var} Variable to add a substitution for.
     * @param {term} The term which is substituted for the variable.
     * @return This substitution for builder-pattern.
     */
    public Substitution add(Term replace, Term with) {
        this.substitution.put(replace, with);
        return this;
    }

    public Term applyTo(Term t) {
        if (t instanceof CompoundTerm) {
            final Term substituteWith = get(t);
            if (substituteWith != null) {
                return substituteWith;
            } else {
                final CompoundTerm ct = (CompoundTerm) t;
                final List<Term> replacedArguments = ct.getArguments().stream().map(arg -> applyTo(arg))
                        .collect(Collectors.toList());
                return compoundTermFactory.build(ct.getFunctor(), replacedArguments);
            }

        } else {
            final Term substitutedWith = get(t);
            if (substitutedWith != null) {
                return substitutedWith;
            } else {
                return t;
            }
        }
    }

    /**
     * @param {t} Variable to check.
     * @return The term the variable is subsituted with or null if the variable
     * is not substituted.
     */
    public Term get(Term t) {
        return substitution.get(t);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        for (Map.Entry<Term, Term> entry : substitution.entrySet()) {
            builder.append(String.format("%s: %s\n", entry.getKey(), entry.getValue()));
        }
        return builder.toString();
    }
}