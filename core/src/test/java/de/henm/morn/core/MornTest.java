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

import de.henm.morn.core.model.*;
import org.junit.Assert;
import org.junit.Test;

import static de.henm.morn.core.model.Constant.*;
import static de.henm.morn.core.model.L.*;
import static de.henm.morn.core.model.Variable.*;

/**
 * Integration tests.
 *
 * @author henm
 */
public class MornTest {

    @Test
    public void testBiblicalFamily() {
        final Functor father = new Functor("father");
        final Functor mother = new Functor("mother");
        final Functor male = new Functor("male");
        final Functor female = new Functor("female");
        final Functor son = new Functor("son");
        final Functor daughter = new Functor("daughter");
        final Functor grandfather = new Functor("grandfather");

        final Constant terach = new Constant("terach");
        final Constant abraham = new Constant("abraham");
        final Constant nachor = new Constant("nachor");
        final Constant haran = new Constant("haran");
        final Constant isaac = new Constant("isaac");
        final Constant lot = new Constant("lot");
        final Constant milcah = new Constant("milcah");
        final Constant yiscah = new Constant("yiscah");
        final Constant sarah = new Constant("sarah");

        final Variable x = new Variable("X");
        final Variable y = new Variable("Y");
        final Variable z = new Variable("Z");

        final KnowledgeBase kb = Morn.buildKB()
                .addFact(father.apply(terach, abraham))
                .addFact(father.apply(terach, nachor))
                .addFact(father.apply(terach, haran))
                .addFact(father.apply(abraham, isaac))
                .addFact(father.apply(haran, lot))
                .addFact(father.apply(haran, milcah))
                .addFact(father.apply(haran, yiscah))
                .addFact(mother.apply(sarah, isaac))
                .addFact(male.apply(terach))
                .addFact(male.apply(abraham))
                .addFact(male.apply(nachor))
                .addFact(male.apply(haran))
                .addFact(male.apply(isaac))
                .addFact(male.apply(lot))
                .addFact(female.apply(sarah))
                .addFact(female.apply(milcah))
                .addFact(female.apply(yiscah))
                .addRule(son.apply(x, y), father.apply(y, x), male.apply(x))
                .addRule(daughter.apply(x, y), father.apply(y, x), female.apply(x))
                .addRule(grandfather.apply(x, y), father.apply(x, z), father.apply(z, y));

        Assert.assertTrue(kb.query(son.apply(isaac, abraham)));
        Assert.assertFalse(kb.query(daughter.apply(isaac, abraham)));
        Assert.assertTrue(kb.query(daughter.apply(milcah, haran)));
        Assert.assertTrue(kb.query(grandfather.apply(terach, isaac)));
    }

    @Test
    public void testList() {
        final Functor append = new Functor("append");
        final Functor reverse = new Functor("reverse");

        // A simple example computing the reverse of a list
        final KnowledgeBase kb = Morn.buildKB()
                .addFact(append.apply(EMPTY, X, X))
                .addRule(append.apply(list(X, Y), Z, list(X, W)), append.apply(Y, Z, W))

                .addFact(reverse.apply(EMPTY, EMPTY))
                .addRule(reverse.apply(list(HEAD, TAIL), X), reverse.apply(TAIL, Y), append.apply(Y, list(HEAD), X));

        Assert.assertTrue(kb.query(reverse.apply(EMPTY, EMPTY)));
        Assert.assertTrue(kb.query(reverse.apply(list(b, c), list(c, b))));
        Assert.assertFalse(kb.query(reverse.apply(list(a, b, c), list(a, b, c))));
    }

    @Test
    public void testInteger() {
        final Functor equals = new Functor("equals");

        final KnowledgeBase kb = Morn.buildKB()
                .addFact(equals.apply(X, X));

        Assert.assertTrue(kb.query(equals.apply(new IntegerTerm(1), new IntegerTerm(1))));
        Assert.assertFalse(kb.query(equals.apply(new IntegerTerm(1), new Constant("c"))));
    }

    @Test
    public void testSimplePredicate() {
        final PredicateFunctor smaller = new PredicateFunctor((x, y) -> x < y);

        final KnowledgeBase kb = Morn.buildKB();
        Assert.assertTrue(kb.query(smaller.apply(new IntegerTerm(1), new IntegerTerm(2))));
        Assert.assertFalse(kb.query(smaller.apply(new IntegerTerm(2), new IntegerTerm(1))));
    }

    @Test
    public void testQuicksort() {
        final Functor quicksort = new Functor("quicksort");
        final Functor partition = new Functor("partition");
        final Functor append = new Functor("append");

        final Variable left = new Variable("left");
        final Variable right = new Variable("right");
        final Variable ls = new Variable("ls");
        final Variable rs = new Variable("rs");

        final PredicateFunctor leq = new PredicateFunctor((x, y) -> x <= y);
        final PredicateFunctor greater = new PredicateFunctor((x, y) -> x > y);

        final KnowledgeBase kb = Morn.buildKB()
                .addRule(quicksort.apply(list(X, Z), Y),
                        partition.apply(Z, X, left, right),
                        quicksort.apply(left, ls),
                        quicksort.apply(right, rs),
                        append.apply(ls, list(X, rs), Y)
                )
                .addFact(quicksort.apply(EMPTY, EMPTY))
                .addRule(partition.apply(list(X, Z), Y, list(X, ls), rs),
                        leq.apply(X, Y),
                        partition.apply(Z, Y, ls, rs)
                )
                .addRule(partition.apply(list(X, Z), Y, ls, list(X, rs)),
                        greater.apply(X, Y),
                        partition.apply(Z, Y, ls, rs))
                .addFact(partition.apply(EMPTY, Y, EMPTY, EMPTY))
                .addFact(append.apply(EMPTY, X, X))
                .addRule(append.apply(list(X, Z), Y, list(X, U)),
                        append.apply(Z, Y, U));

        Assert.assertTrue(kb.query(quicksort.apply(EMPTY, EMPTY)));

        final IntegerTerm one = new IntegerTerm(1);
        final IntegerTerm two = new IntegerTerm(2);
        final IntegerTerm three = new IntegerTerm(3);
        final IntegerTerm four = new IntegerTerm(4);
        final IntegerTerm five = new IntegerTerm(5);
        Assert.assertTrue(kb.query(quicksort.apply(
                list(one, three, five, four, two),
                list(one, two, three, four, five))
        ));
    }
}