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

import java.util.Collections;
import java.util.List;

/**
 * @author henm
 */
public class CompoundTerm1 implements CompoundTerm {

    private final Functor functor;
    private final Term argument;

    CompoundTerm1(Functor functor, Term argument) {
        this.functor = functor;
        this.argument = argument;
    }

    @Override
    public Functor getFunctor() {
        return functor;
    }

    @Override
    public List<Term> getArguments() {
        return Collections.singletonList(argument);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CompoundTerm1 that = (CompoundTerm1) o;

        if (!functor.equals(that.functor)) return false;
        return argument.equals(that.argument);
    }

    @Override
    public int hashCode() {
        int result = functor.hashCode();
        result = 31 * result + argument.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return String.format("%s(%s)", functor, argument);
    }
}
