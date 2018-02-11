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

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Collectors;

import de.henm.morn.core.CompoundTerm;
import de.henm.morn.core.Constant;
import de.henm.morn.core.FreeVariable;
import de.henm.morn.core.Term;

/**
 * Unification-Algorithm based on the algorithm given in "The Art of Prolog"
 * by Leon S. Sterling and Ehud Y. Shapiro, MIT Press, March 1994.
 * 
 * @author henm
 */
public class Unification {

    /**
     * Check if terms term1 and term2 unify.
     * 
     * @param {term1} First term to unify.
     * @param {term2} Second term to unify.
     * @return UnificationResult containing the result of the unification.
     */
    public UnificationResult unify(Term term1, Term term2) {

        final Substitution substitution = new Substitution();

        final Map<Term, MutableTerm> replaced = new LinkedHashMap<>();
        final MutableTerm mt1 = termToMutableTerm(term1, replaced);
        final MutableTerm mt2 = termToMutableTerm(term2, replaced);

        Stack<Terms> stack = new Stack<>();
        stack.add(new Terms(mt1, mt2));

        while (!stack.isEmpty()) {
            final Terms terms = stack.pop();

            if (terms.firstTermVariableCondition()) {
                final MutableVariable variable = (MutableVariable) terms.getTerm1();
                terms.replace(variable, terms.getTerm2());
                substitution.add(variable.getOriginalTerm(), terms.getTerm2().getOriginalTerm());

            } else if (terms.secondTermVariableCondition()) {
                final MutableVariable variable = (MutableVariable) terms.getTerm2();
                terms.replace(variable, terms.getTerm1());
                substitution.add(variable.getOriginalTerm(), terms.getTerm1().getOriginalTerm());

            } else if (terms.sameConstants()) {
                continue;

            } else if (terms.compoundTermCondition()) {
                final Iterator<MutableTerm> arguments1 = ((MutableCompoundTerm) terms.getTerm1()).getArguments()
                        .iterator();
                final Iterator<MutableTerm> arguments2 = ((MutableCompoundTerm) terms.getTerm2()).getArguments()
                        .iterator();

                while (arguments1.hasNext()) {
                    stack.push(new Terms(arguments1.next(), arguments2.next()));
                }

            } else {
                return new NegativeUnificationResult();
            }
        }

        return new PositiveUnificationResult(substitution);
    }

    private MutableTerm termToMutableTerm(Term term, Map<Term, MutableTerm> replaced) {
        if (replaced.containsKey(term)) {
            return replaced.get(term);
        }

        if (term instanceof FreeVariable) {
            final MutableTerm mt = new MutableVariable((FreeVariable) term);
            replaced.put(term, mt);
            return mt;

        } else if (term instanceof Constant) {
            final MutableTerm mt = new MutableConstant((Constant) term);
            replaced.put(term, mt);
            return mt;

        } else if (term instanceof CompoundTerm) {
            final CompoundTerm ct = (CompoundTerm) term;
            final List<MutableTerm> arguments = ct.getArguments().stream().map(t -> this.termToMutableTerm(t, replaced))
                    .collect(Collectors.toList());

            final MutableTerm mt = new MutableCompoundTerm(ct, arguments);
            replaced.put(term, mt);
            return mt;
            
        } else {
            throw new IllegalArgumentException(String.format("Unkown Term-Type of term: %s", term));
        }
    }

    private static class Terms {
        private final MutableTerm term1;
        private final MutableTerm term2;

        public Terms(MutableTerm term1, MutableTerm term2) {
            this.term1 = term1;
            this.term2 = term2;
        }

        public MutableTerm getTerm1() {
            return term1;
        }

        public MutableTerm getTerm2() {
            return term2;
        }

        public boolean firstTermVariableCondition() {
            return term1 instanceof MutableVariable && !term2.contains((MutableVariable) term1);
        }

        public boolean secondTermVariableCondition() {
            return term2 instanceof MutableVariable && !term1.contains((MutableVariable) term2);
        }

        public boolean sameConstants() {
            return term1 instanceof MutableConstant && term1 instanceof MutableConstant && term1 == term2;
        }

        public boolean compoundTermCondition() {
            return term1 instanceof MutableCompoundTerm && term2 instanceof MutableCompoundTerm
                    && ((MutableCompoundTerm) term1).getFunctor() == ((MutableCompoundTerm) term2).getFunctor();
        }

        public void replace(MutableVariable x, MutableTerm replaceBy) {
            term1.replace(x, replaceBy);
            term2.replace(x, replaceBy);
        }
    }
}