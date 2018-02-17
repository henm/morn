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

import java.util.List;

/**
 *
 * @author henm
 */
public interface CompoundTerm extends Term {

    Functor getFunctor();

    @Override
    default boolean isGround() {
        return getArguments().stream().allMatch(t -> t.isGround());
    }

    List<Term> getArguments();

    @Override
    default boolean contains(FreeVariable x) {
        return getArguments().stream().anyMatch(t -> t.equals(x) || t.contains(x));
    }
}
