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

import java.util.Arrays;
import java.util.List;

/**
 * Building compound terms.
 *
 * @author henm
 */
public class CompoundTermFactory {

    public CompoundTerm build(Functor functor, Term term) {
        return new CompoundTerm1(functor, term);
    }

    public CompoundTerm build(Functor functor, Term term1, Term term2) {
        return new CompoundTerm2(functor, term1, term2);
    }

    public CompoundTerm build(Functor functor, Term term1, Term term2, Term term3) {
        return new CompoundTerm3(functor, term1, term2, term3);
    }

    public CompoundTerm build(Functor functor, Term term1, Term term2, Term term3, Term term4) {
        return new CompoundTerm4(functor, term1, term2, term3, term4);
    }

    public CompoundTerm build(Functor functor, List<Term> terms) {
        final int numOfArgs = terms.size();
        switch (numOfArgs) {
            case 1:
                return build(functor, terms.get(0));
            case 2:
                return build(functor, terms.get(0), terms.get(1));
            case 3:
                return build(functor, terms.get(0), terms.get(1), terms.get(2));
            case 4:
                return build(functor, terms.get(0), terms.get(1), terms.get(2), terms.get(3));
            default:
                throw new UnsupportedOperationException("Not yet supportet");
        }
    }

    public CompoundTerm build(Functor functor, Term... terms) {
        return build(functor, Arrays.asList(terms));
    }
}
