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
package de.henm.morn.core.reasoner;

import java.util.Iterator;
import java.util.Stack;

import de.henm.morn.core.model.CompoundTerm;
import de.henm.morn.core.model.Constant;
import de.henm.morn.core.model.L;
import de.henm.morn.core.model.Term;
import de.henm.morn.core.model.Variable;

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

        Stack<Terms> stack = new Stack<>();
        stack.add(new Terms(term1, term2));

        while (!stack.isEmpty()) {

            final Terms terms = stack.pop().applySubstitution(substitution);

            if (terms.firstTermVariableCondition()) {
                final Variable variable = (Variable) terms.getTerm1();
                substitution.add(variable, terms.getTerm2());

            } else if (terms.secondTermVariableCondition()) {
                final Variable variable = (Variable) terms.getTerm2();
                substitution.add(variable, terms.getTerm1());

            } else if (terms.sameConstants()) {
                continue;

            } else if (terms.listCondition()) {
                final L list1 = (L) terms.getTerm1();
                final L list2 = (L) terms.getTerm2();

                if (list1.isEmpty() && list2.isEmpty()) {
                    continue;
                }

                if (list1.isEmpty() || list2.isEmpty()) {
                    return new NegativeUnificationResult();
                }

                stack.push(new Terms(list1.getHead(), list2.getHead()));
                stack.push(new Terms(list1.getTail(), list2.getTail()));

            } else if (terms.compoundTermCondition()) {
                final Iterator<Term> arguments1 = ((CompoundTerm) terms.getTerm1()).getArguments()
                        .iterator();
                final Iterator<Term> arguments2 = ((CompoundTerm) terms.getTerm2()).getArguments()
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

    private static class Terms {
        private Term term1;
        private Term term2;

        public Terms(Term term1, Term term2) {
            this.term1 = term1;
            this.term2 = term2;
        }

        public Term getTerm1() {
            return term1;
        }

        public Term getTerm2() {
            return term2;
        }

        public boolean firstTermVariableCondition() {
            return term1 instanceof Variable && !term2.contains((Variable) term1);
        }

        public boolean secondTermVariableCondition() {
            return term2 instanceof Variable && !term1.contains((Variable) term2);
        }

        public boolean sameConstants() {
            return term1 instanceof Constant && term1 instanceof Constant && term1 == term2;
        }

        public boolean compoundTermCondition() {
            return term1 instanceof CompoundTerm && term2 instanceof CompoundTerm
                    && ((CompoundTerm) term1).getFunctor() == ((CompoundTerm) term2).getFunctor();
        }

        public boolean listCondition() {
            return term1 instanceof L && term2 instanceof L;
        }

        public Terms applySubstitution(Substitution substutition) {
            term1 = substutition.apply(term1);
            term2 = substutition.apply(term2);

            return this;
        }
    }
}