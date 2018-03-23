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
import java.util.Collections;
import java.util.List;

/**
 *
 * @author henm
 */
public class CompoundTerm2 implements CompoundTerm {

    private final Functor functor;
    private final Term arg1;
    private final Term arg2;

    CompoundTerm2(Functor functor, Term arg1, Term arg2) {
        this.functor = functor;
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    @Override
    public Functor getFunctor() {
        return functor;
    }

    @Override
    public List<Term> getArguments() {
        return Collections.unmodifiableList(Arrays.asList(arg1, arg2));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CompoundTerm2 that = (CompoundTerm2) o;

        if (!functor.equals(that.functor)) return false;
        if (!arg1.equals(that.arg1)) return false;
        return arg2.equals(that.arg2);
    }

    @Override
    public int hashCode() {
        int result = functor.hashCode();
        result = 31 * result + arg1.hashCode();
        result = 31 * result + arg2.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return String.format("%s(%s, %s)", functor, arg1, arg2);
    }
}
