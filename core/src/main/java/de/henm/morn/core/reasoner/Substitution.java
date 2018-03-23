/**
 * Copyright 2017-present henm
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.henm.morn.core.reasoner;

import de.henm.morn.core.model.CompoundTerm;
import de.henm.morn.core.model.CompoundTermFactory;
import de.henm.morn.core.model.L;
import de.henm.morn.core.model.Term;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * A mapping from terms to terms used for unification and query results.
 *
 * @author henm
 */
class Substitution {

    final Map<Term, Term> substitution;
    final CompoundTermFactory compoundTermFactory;

    public Substitution() {
        this.substitution = new LinkedHashMap<>();
        this.compoundTermFactory = new CompoundTermFactory();
    }

    private Substitution(Map<Term, Term> substitution) {
        this();
        this.substitution.putAll(substitution);
    }

    /**
     * @param {replace} Variable to add a substitution for.
     * @param {with}    The term which is substituted for the variable.
     * @return This substitution for builder-pattern.
     */
    public Substitution add(Term replace, Term with) {
        this.substitution.put(replace, with);

        // Replace all occurences of replace in this substitution, too
        for (Map.Entry<Term, Term> entry : substitution.entrySet()) {
            substitution.put(entry.getKey(), this.apply(entry.getValue()));
        }

        return this;
    }

    /**
     * Build a new term, replacing all occurrences of terms according to this
     * substitution.
     *
     * @param {t} The term apply the substitution to.
     * @return A new term with all occurrences replaced.
     */
    public Term apply(Term t) {
        if (t instanceof CompoundTerm) {
            final Optional<Term> substituteWith = get(t);
            if (substituteWith.isPresent()) {
                return substituteWith.get();
            } else {
                final CompoundTerm ct = (CompoundTerm) t;
                final List<Term> replacedArguments = ct.getArguments().stream()
                        .map(arg -> apply(arg))
                        .collect(Collectors.toList());
                return compoundTermFactory.build(ct.getFunctor(), replacedArguments);
            }

        } else if (t instanceof L) {

            final L l = (L) t;
            if (l.isEmpty()) {
                return l;
            }

            final Optional<Term> substituteWith = get(t);
            if (substituteWith.isPresent()) {
                return substituteWith.get();
            } else {
                final Term replacedHead = apply(l.getHead());
                final Term replacedRest = apply(l.getTail());

                return new L(replacedHead, replacedRest);
            }

        } else {
            final Optional<Term> substitutedWith = get(t);
            if (substitutedWith.isPresent()) {
                return substitutedWith.get();
            } else {
                return t;
            }
        }
    }

    /**
     * Check if a term is replaced by this subsitution.
     *
     * @param {t} Term to check.
     * @return The term the variable is subsituted with or null if the variable
     * is not substituted.
     */
    public Optional<Term> get(Term t) {
        return Optional.ofNullable(substitution.get(t));
    }

    /**
     * @param {t} Term to check.
     * @return True iff there is a mapping for the given term.
     */
    public boolean containsMappingFor(Term t) {
        return get(t).isPresent();
    }

    /**
     * Combine the mappings of this substitution with another substitution.
     *
     * @param {otherSubstitution} The other substitution.
     * @return A new substitution containing the mappings from this and the
     * other substitution.
     * @throws IllegalArgumentException Thrown when there are mappings for the
     *                                  same term in both substitutions.
     */
    public Substitution merge(Substitution otherSubstitution) {
        final Map<Term, Term> thisMap = substitution;
        final Map<Term, Term> otherMap = otherSubstitution.substitution;

        final Substitution result = new Substitution(thisMap);
        for (Map.Entry<Term, Term> entry : otherMap.entrySet()) {
            if (this.containsMappingFor(entry.getKey())) {
                throw new IllegalArgumentException(String.format(
                        "Merging of substitutions '%s' and '%s' not possible: Term '%s' is mapped multiple times", this,
                        otherSubstitution, entry.getKey()));
            }
        }

        result.substitution.putAll(otherMap);

        return result;
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