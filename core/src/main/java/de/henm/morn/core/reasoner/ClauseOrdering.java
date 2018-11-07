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
package de.henm.morn.core.reasoner;

import de.henm.morn.core.model.Clause;
import de.henm.morn.core.model.Fact;
import de.henm.morn.core.model.Rule;

import java.util.Comparator;

/**
 * Sort possible clauses found by unifying.
 * <p>
 * The current ordering simply favors facts over rules. This is useful for recursive definitions.
 *
 * @author henm
 */
class ClauseOrdering implements Comparator<Clause> {

    @Override
    public int compare(Clause c1, Clause c2) {
        if (c1 instanceof Fact && c2 instanceof Rule) {
            return -1;
        } else if (c1 instanceof Rule && c2 instanceof Fact) {
            return 1;
        } else {
            return 0;
        }
    }
}
