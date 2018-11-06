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

import de.henm.morn.core.KnowledgeBase;
import de.henm.morn.core.Morn;
import de.henm.morn.core.model.Constant;
import de.henm.morn.core.model.Functor;
import de.henm.morn.core.model.NumberTerm;
import de.henm.morn.core.model.Variable;
import org.junit.Assert;
import org.junit.Test;

import static de.henm.morn.core.model.Constant.*;
import static de.henm.morn.core.model.L.*;
import static de.henm.morn.core.model.Variable.*;

/**
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
    public void testNumber() {
        final Functor equals = new Functor("equals");

        final KnowledgeBase kb = Morn.buildKB()
                .addFact(equals.apply(X, X));

        Assert.assertTrue(kb.query(equals.apply(new NumberTerm(1), new NumberTerm(1))));
        Assert.assertFalse(kb.query(equals.apply(new NumberTerm(1), new Constant("c"))));
    }
}