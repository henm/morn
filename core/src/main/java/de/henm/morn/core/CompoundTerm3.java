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

import java.util.Arrays;
import java.util.List;

/**
 * @author henm
 */
public class CompoundTerm3 implements CompoundTerm {

    private final Functor functor;
    private final Term arg1;
    private final Term arg2;
    private final Term arg3;

    CompoundTerm3(Functor functor, Term arg1, Term arg2, Term arg3) {
        this.functor = functor;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.arg3 = arg3;
    }

    @Override
    public Functor getFunctor() {
        return functor;
    }

    @Override
    public List<Term> getArguments() {
        return Arrays.asList(arg1, arg2, arg3);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CompoundTerm3 that = (CompoundTerm3) o;

        if (!functor.equals(that.functor)) return false;
        if (!arg1.equals(that.arg1)) return false;
        if (!arg2.equals(that.arg2)) return false;
        return arg3.equals(that.arg3);
    }

    @Override
    public int hashCode() {
        int result = functor.hashCode();
        result = 31 * result + arg1.hashCode();
        result = 31 * result + arg2.hashCode();
        result = 31 * result + arg3.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return String.format("%s(%s, %s, %s)", functor, arg1, arg2, arg3);
    }
}
