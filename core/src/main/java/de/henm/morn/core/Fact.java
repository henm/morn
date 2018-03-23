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
 * A rule without body.
 *
 * @author henm
 */
public class Fact implements Clause {

    private final Term head;

    public Fact(Term head) {
        this.head = head;
    }

    @Override
    public Term getHead() {
        return head;
    }

    @Override
    public List<Term> getBody() {
        return Collections.emptyList();
    }

    @Override
    public boolean isGround() {
        return head.isGround();
    }
 
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Fact fact = (Fact) o;

        return head.equals(fact.head);
    }

    @Override
    public int hashCode() {
        return head.hashCode();
    }

    @Override
    public String toString() {
        return String.format("%s.", head);
    }
}
