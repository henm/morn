/*
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

import java.util.function.BiPredicate;

/**
 * Use a predicate as a term. Obviously the term is fulfilled iff the predicate is true.
 * <p>
 * Only numeric predicates are supported.
 *
 * @author henm
 */
public class PredicateTerm implements Term {

    private final BiPredicate<Integer, Integer> predicate;
    private final Term t1;
    private final Term t2;

    public PredicateTerm(BiPredicate<Integer, Integer> predicate, Term t1, Term t2) {
        this.predicate = predicate;
        this.t1 = t1;
        this.t2 = t2;
    }

    public boolean test(int t, int u) {
        return predicate.test(t, u);
    }

    public BiPredicate<Integer, Integer> getPredicate() {
        return predicate;
    }

    public Term getT1() {
        return t1;
    }

    public Term getT2() {
        return t2;
    }

    @Override
    public boolean isGround() {
        return false;
    }

    @Override
    public boolean contains(Variable x) {
        return false;
    }
}
