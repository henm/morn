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
package de.henm.morn.core;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Get substitutions. The underlying algorithms are not very efficient.
 *
 * @author henm
 */
public final class SimpleSubstitutioner {

    private final CompoundTermFactory compoundTermFactory;

    public SimpleSubstitutioner() {
        this.compoundTermFactory = new CompoundTermFactory();
    }

    public Collection<Clause> getAllPossibleSubstitutions(Clause clause, Collection<Term> toSubstitute) {
        if (clause instanceof Fact) {
            final Term head = clause.getHead();
            return getAllPossibleSubstitutions(head, toSubstitute).stream()
                    .map(term -> new Fact(term))
                    .collect(Collectors.toList());
        }

        final List<Term> terms = new ArrayList<>();
        terms.add(clause.getHead());
        terms.addAll(clause.getBody());

        // Write rule as a term with functor '%'
        final Term asTerm = compoundTermFactory.build(new Functor("%"), terms);
        final Collection<Term> substituted = getAllPossibleSubstitutions(asTerm, toSubstitute);

        return substituted.stream().map(t -> {
            final List<Term> arguments = ((CompoundTerm) t).getArguments();
            final Term head = arguments.get(0);
            final List<Term> body = arguments.subList(1, arguments.size());
            return new Rule(head, body);
        }).collect(Collectors.toList());
    }

    /**
     * Given a list of terms, return all possible substitutions of the term.
     * E.g. for a Term (p(X, Y)) and terms (q(c), c), return (p(q(c), q(c)),
     * p(q(c), c), p(c, q(c)), p(c, c)).
     *
     * @param terms The terms to replace the free variables.
     * @return A collection of all possible terms where the free variables have
     * been replaced.
     */
    public Collection<Term> getAllPossibleSubstitutions(Term term, Collection<Term> terms) {

        // Get all free variables
        final Collection<FreeVariable> allFreeVariables = getAllFreeVariables(term);

        // Build all possible substitutions of this variables
        final Collection<Map<FreeVariable, Term>> allPossibleSubstitutions = getAllPossibleSubstitutions(allFreeVariables, terms);

        if (allPossibleSubstitutions.isEmpty()) {
            return Collections.singletonList(term);
        }

        // Apply those substitutions to the given term
        return allPossibleSubstitutions.stream()
                .map(sub -> applySubstitution(term, sub))
                .collect(Collectors.toList());
    }

    /**
     * Return all free variables of a given term.
     *
     * @param term A term.
     * @return Collection of all free variables.
     * @todo Better in term?
     */
    public Collection<FreeVariable> getAllFreeVariables(Term term) {
        if (term instanceof FreeVariable) {
            return Collections.singletonList((FreeVariable) term);
        } else if (term instanceof CompoundTerm) {
            final CompoundTerm compoundTerm = (CompoundTerm) term;
            return compoundTerm.getArguments().stream()
                    .map(this::getAllFreeVariables)
                    .flatMap(c -> c.stream())
                    .distinct()
                    .collect(Collectors.toList());
        } else if (term instanceof Constant) {
            return Collections.emptyList();
        } else {
            throw new IllegalArgumentException(String.format("Unkown Term '%s'", term));
        }
    }

    /**
     * Get all possible mappings from a collection of variables to a collection
     * of terms.
     *
     * @param freeVariables The free variables.
     * @param terms         The terms.
     * @return A collection of mappings from all the free variables to terms.
     */
    public Collection<Map<FreeVariable, Term>> getAllPossibleSubstitutions(Collection<FreeVariable> freeVariables, Collection<Term> terms) {
        if (freeVariables.isEmpty()) {
            return Collections.emptyList();
        }

        final Queue<FreeVariable> varQueue = new LinkedList<>(freeVariables);
        return getAllPossibleSubstitutionsRecursively(varQueue, terms).collect(Collectors.toList());
    }

    private Stream<Map<FreeVariable, Term>> getAllPossibleSubstitutionsRecursively(Queue<FreeVariable> freeVariables, Collection<Term> terms) {
        if (freeVariables.isEmpty()) {
            return Stream.of(new LinkedHashMap<>());
        } else {
            final FreeVariable freeVariable = freeVariables.poll();
            final Stream<Map<FreeVariable, Term>> subs = getAllPossibleSubstitutionsRecursively(freeVariables, terms);

            return subs.flatMap(sub -> {
                return terms.stream().map(t -> {
                    final Map<FreeVariable, Term> newSub = new LinkedHashMap<>(sub);
                    newSub.put(freeVariable, t);
                    return newSub;
                });
            });
        }
    }

    /**
     * Apply the given SimpleSubstitutioner.
     *
     * @param term         The term to apply the substitution to.
     * @param substitution A map from all occurring free variables to some terms.
     * @return A new ground term with all free variables substituted.
     * @throws IllegalArgumentException      Thrown when a free variable occurs which is not contained in the substitution.
     * @throws UnsupportedOperationException Thrown when an unknown implementation of Term is given.
     */
    public Term applySubstitution(Term term, Map<FreeVariable, Term> substitution) {
        if (term instanceof FreeVariable) {
            final Term sub = substitution.get(term);
            if (sub == null) {
                throw new IllegalArgumentException(String.format("No substitution for free variable 's' found", term));
            }
            return sub;

        } else if (term instanceof CompoundTerm) {
            final CompoundTerm compoundTerm = (CompoundTerm) term;
            final List<Term> substituted = compoundTerm.getArguments().stream()
                    .map(arg -> applySubstitution(arg, substitution))
                    .collect(Collectors.toList());
            return compoundTermFactory.build(compoundTerm.getFunctor(), substituted);

        } else if (term instanceof Constant) {
            return term;

        } else {
            throw new UnsupportedOperationException(String.format("Unkown Implementation of Term '%s'", term.getClass()));
        }
    }
}