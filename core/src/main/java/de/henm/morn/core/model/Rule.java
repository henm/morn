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
import java.util.stream.Collectors;

/**
 * @author henm
 */
public class Rule implements Clause {

    private final Term head;
    private final List<Term> body;

    public Rule(Term head, List<Term> body) {
        this.head = head;
        this.body = body;
    }

    public Rule(Term head, Term... body) {
        this.head = head;
        this.body = Arrays.asList(body);
    }

    @Override
    public Term getHead() {
        return head;
    }

    @Override
    public List<Term> getBody() {
        return body;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Rule rule = (Rule) o;

        if (!head.equals(rule.head)) return false;
        return body.equals(rule.body);
    }

    @Override
    public int hashCode() {
        int result = head.hashCode();
        result = 31 * result + body.hashCode();
        return result;
    }

    @Override
    public String toString() {
        final String bodyAsString = String.join(", ", body.stream().map(t -> t.toString()).collect(Collectors.toList()));
        return String.format("%s :- %s.", head, bodyAsString);
    }
}
