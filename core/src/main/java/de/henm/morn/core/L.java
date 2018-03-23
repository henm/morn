/**
 * Copyright 2017-present henm
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.henm.morn.core;

import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.List;

/**
 * Internal, recursive representation of a list.
 *
 * @author henm
 */
public class L implements Term {

    public static final L EMPTY = new L();
    public static final Variable HEAD = new Variable("H");
    public static final Variable TAIL = new Variable("T");

    private final Term head;
    private final Term tail;

    private L() {
        this.head = new Constant("[]");
        this.tail = null; // Empty list has no tail
    }

    /**
     * Recursive constructor for lists.
     *
     * @param head
     * @param tail
     */
    public L(Term head, Term tail) {
        this.head = head;
        this.tail = tail;
    }

    /**
     * Create a list with variables for matching. The last given variable matches
     * the whole tail of a list.
     *
     * @param variables Variable X_1, ..., X_n
     * @return A list [X_1, [..., X_n]...]
     */
    public static L list(Variable... variables) {
        if (variables.length == 1) {
            return new L(variables[0], EMPTY);
        }

        final List<Variable> reverse = Lists.reverse(Arrays.asList(variables));
        L result = new L(reverse.get(1), reverse.get(0));

        for (Variable var : reverse.subList(2, reverse.size())) {
            result = new L(var, result);
        }

        return result;
    }

    /**
     * Create a list with arbitrary terms.
     *
     * @param terms X_1, ...., X_n
     * @return [X_1, [...., [X_n, []], ... ]]
     */
    public static L list(Term... terms) {
        L result = EMPTY;

        for (Term term : Lists.reverse(Arrays.asList(terms))) {
            result = new L(term, result);
        }

        return result;
    }

    @Override
    public boolean contains(Variable variable) {
        if (this == EMPTY) {
            return false;
        } else {
            return this.head.equals(variable) || this.tail.contains(variable);
        }
    }

    @Override
    public boolean isGround() {
        if (this == EMPTY) {
            return true;
        } else {
            return this.head.isGround() && this.tail.isGround();
        }
    }

    public boolean isEmpty() {
        return this == EMPTY;
    }

    public Term getHead() {
        return head;
    }

    public Term getTail() {
        return tail;
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }
        return String.format("[%s, %s]", head, tail);
    }
}