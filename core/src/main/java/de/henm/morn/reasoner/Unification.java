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

import de.henm.morn.core.CompoundTerm;
import de.henm.morn.core.Constant;
import de.henm.morn.core.FreeVariable;
import de.henm.morn.core.Term;

/**
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
        final Substitution startSubstitution = new Substitution();
        return unify(term1, term2, startSubstitution);
    }

    private UnificationResult unify(Term term1, Term term2, Substitution substitution) {

        if (term1 instanceof Constant && term2 instanceof Constant) {
            if (term1 == term2) {
                return new PositiveUnificationResult(substitution);
            } else {
                return new NegativeUnificationResult();
            }

        } else if (term1 instanceof FreeVariable) {
            substitution.add((FreeVariable) term1, term2);
            if (substitution.isConsistent()) {
                return new PositiveUnificationResult(substitution);
            } else {
                return new NegativeUnificationResult();
            }

        } else if (term2 instanceof FreeVariable) {
            substitution.add((FreeVariable) term2, term1);
            if (substitution.isConsistent()) {
                return new PositiveUnificationResult(substitution);
            } else {
                return new NegativeUnificationResult();
            }

        } else if (term1 instanceof CompoundTerm && term2 instanceof CompoundTerm) {
            final CompoundTerm ct1 = (CompoundTerm) term1;
            final CompoundTerm ct2 = (CompoundTerm) term2;

            // Use reference-comparison
            if (ct1.getFunctor() == ct2.getFunctor() && ct1.getArguments().size() == ct2.getArguments().size()) {
                final Iterator<Term> it1 = ct1.getArguments().iterator();
                final Iterator<Term> it2 = ct2.getArguments().iterator();

                while(it1.hasNext()) {
                    final Term t1 = it1.next();
                    final Term t2 = it2.next();

                    final UnificationResult result = unify(t1, t2, substitution);
                    if (!result.termsUnify()) {
                        return result;
                    }
                }
                
                return new PositiveUnificationResult(substitution);

            } else {
                return new NegativeUnificationResult();
            }
        } else {
            return new NegativeUnificationResult();
        }
    }
}