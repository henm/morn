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

import java.util.List;

import de.henm.morn.core.CompoundTerm;
import de.henm.morn.core.Functor;

/**
 * @author henm
 */
class MutableCompoundTerm implements MutableTerm {

    private final CompoundTerm compoundTerm;
    private List<MutableTerm> arguments;

    public MutableCompoundTerm(CompoundTerm compoundTerm, List<MutableTerm> arguments) {
        this.compoundTerm = compoundTerm;
        this.arguments = arguments;
    }

    @Override
    public boolean contains(MutableVariable x) {
        for (MutableTerm term : arguments) {
            if (term.contains(x)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void replace(MutableVariable x, MutableTerm replaceBy) {
        for (int i = 0; i < arguments.size(); i++) {
            final MutableTerm term = arguments.get(i);
            if (term == x) {
                arguments.set(i, replaceBy);
            } else {
                term.replace(x, replaceBy);
            }
        }
    }

    @Override
    public CompoundTerm getOriginalTerm() {
        return compoundTerm;
    }

    public Functor getFunctor() {
        return compoundTerm.getFunctor();
    }

    public List<MutableTerm> getArguments() {
        return arguments;
    }
}