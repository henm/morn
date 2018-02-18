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
package de.henm.morn.reasoner;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.henm.morn.core.Clause;
import de.henm.morn.core.CompoundTermFactory;
import de.henm.morn.core.Constant;
import de.henm.morn.core.Fact;
import de.henm.morn.core.Functor;
import de.henm.morn.core.Term;
import de.henm.morn.core.Variable;

public class ReasonerTest {

    final Constant abraham = new Constant("abraham");
    final Constant isaac = new Constant("isaac");
    final Constant haran = new Constant("haran");
    final Constant lot = new Constant("lot");
    final Constant milcah = new Constant("milcah");
    final Constant yiscah = new Constant("yiscah");

    final Functor father = new Functor("father");
    final Functor male = new Functor("male");
    final Functor female = new Functor("female");
    final Functor son = new Functor("son");
    final Functor daughter = new Functor("daughter");

    private CompoundTermFactory ctFactory;
    private Reasoner familyReasoner;

    public ReasonerTest() {
        this.ctFactory = new CompoundTermFactory();
    }

    @Before
    public void setup() {
        final List<Clause> clauses = new ArrayList<>();
        clauses.add(new Fact(ctFactory.build(father, abraham, isaac)));
        clauses.add(new Fact(ctFactory.build(male, isaac)));
        clauses.add(new Fact(ctFactory.build(father, haran, lot)));
        clauses.add(new Fact(ctFactory.build(male, lot)));
        clauses.add(new Fact(ctFactory.build(father, haran, milcah)));
        clauses.add(new Fact(ctFactory.build(female, milcah)));
        clauses.add(new Fact(ctFactory.build(father, haran, milcah)));
        clauses.add(new Fact(ctFactory.build(female, yiscah)));

        final Variable x = new Variable("X");
        final Variable y = new Variable("Y");
        clauses.add(ctFactory.build(son, x, y).entailed(ctFactory.build(father, y, x), ctFactory.build(male, x)));
        clauses.add(
                ctFactory.build(daughter, x, y).entailed(ctFactory.build(father, y, x), ctFactory.build(female, x)));

        this.familyReasoner = new Reasoner(clauses);
    }

    @Test
    public void simpleFactsShouldBeDeduced() {
        final Term a = new Constant("a");
        final List<Clause> clauses = new ArrayList<>();
        clauses.add(new Fact(a));

        final Reasoner reasoner = new Reasoner(clauses);

        Assert.assertTrue(reasoner.query(a));
    }

    @Test
    public void lotIsSonOfHaran() {
        Assert.assertTrue(familyReasoner.query(ctFactory.build(son, lot, haran)));
    }

    @Test
    public void reasonerShouldHandleQueriesWithVariables() {
        final Variable x = new Variable("X");
        Assert.assertTrue(familyReasoner.query(ctFactory.build(son, x, haran)));
        Assert.assertFalse(familyReasoner.query(ctFactory.build(son, x, x)));
    }
}