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
 * A predicate as a functor. May be used to build a Predicate Term.
 * <p>
 * Only numeric BiPredicates are supported.
 *
 * @author henm
 */
public class PredicateFunctor {

    private final BiPredicate<Integer, Integer> predicate;

    public PredicateFunctor(BiPredicate<Integer, Integer> predicate) {
        this.predicate = predicate;
    }

    public Term apply(Term t, Term u) {
        return new PredicateTerm(predicate, t, u);
    }
}
