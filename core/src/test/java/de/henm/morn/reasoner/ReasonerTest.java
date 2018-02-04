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
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import de.henm.morn.core.Clause;
import de.henm.morn.core.CompoundTermFactory;
import de.henm.morn.core.Constant;
import de.henm.morn.core.Fact;
import de.henm.morn.core.FreeVariable;
import de.henm.morn.core.Functor;
import de.henm.morn.core.Rule;
import de.henm.morn.core.Term;

public class ReasonerTest {

    private Constant a;
    private Constant b;
    private Constant c;
    private FreeVariable x;
    private Functor p;
    private CompoundTermFactory ctFactory;

    @Before
    public void setUp() {
        this.a = new Constant("a");
        this.b = new Constant("b");
        this.c = new Constant("c");
        this.x = new FreeVariable("X");
        this.p = new Functor("p");
        this.ctFactory = new CompoundTermFactory();
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
        final Constant abraham = new Constant("abraham");
        final Constant isaac = new Constant("isaac");
        final Constant haran = new Constant("haran");
        final Constant lot = new Constant("lot");
        final Constant milcah = new Constant("milcah");
        final Constant yiscah = new Constant("yiscah");

        final Functor father = new Functor("father");
        final Functor mother = new Functor("mother");
        final Functor male = new Functor("male");
        final Functor female = new Functor("female");
        final Functor son = new Functor("son");
        final Functor daughter = new Functor("daughter");

        final CompoundTermFactory ctFactory = new CompoundTermFactory();

        final List<Clause> clauses = new ArrayList<>();
        clauses.add(new Fact(ctFactory.build(father, abraham, isaac)));
        clauses.add(new Fact(ctFactory.build(male, isaac)));
        clauses.add(new Fact(ctFactory.build(father, haran, lot)));
        clauses.add(new Fact(ctFactory.build(male, lot)));
        clauses.add(new Fact(ctFactory.build(father, haran, milcah)));
        clauses.add(new Fact(ctFactory.build(female, milcah)));
        clauses.add(new Fact(ctFactory.build(father, haran, milcah)));
        clauses.add(new Fact(ctFactory.build(female, yiscah)));

        final FreeVariable x = new FreeVariable("X");
        final FreeVariable y = new FreeVariable("Y");
        clauses.add(ctFactory.build(son, x, y).entailed(ctFactory.build(father, y, x), ctFactory.build(male, x)));
        clauses.add(
                ctFactory.build(daughter, x, y).entailed(ctFactory.build(father, y, x), ctFactory.build(female, x)));

        final Reasoner reasoner = new Reasoner(clauses);

        Assert.assertTrue(reasoner.query(ctFactory.build(son, lot, haran)));
    }

    @Test
    public void getAllUsedConstantsShouldReturnAllConstants() {
        final List<Clause> clauses = new ArrayList<>();
        clauses.add(new Fact(c));
        clauses.add(new Fact(ctFactory.build(p, b)));
        clauses.add(new Rule(ctFactory.build(p, b), ctFactory.build(p, c)));

        final Reasoner reasoner = new Reasoner(clauses);
        final Set<Term> usedConstants = reasoner.getAllUsedConstants();

        Assert.assertEquals(2, usedConstants.size());
        Assert.assertTrue(usedConstants.contains(b));
        Assert.assertTrue(usedConstants.contains(c));
    }

    @Test
    public void getGroundedRulesShouldNotContainAnyRuleWithFreeVariables() {
        final List<Clause> clauses = new ArrayList<>();
        final Fact groundenFact = new Fact(ctFactory.build(p, a));

        clauses.add(groundenFact);
        clauses.add(new Fact(ctFactory.build(p, x)));

        final Reasoner reasoner = new Reasoner(clauses);
        final Collection<Clause> groundedRules = reasoner.getGroundedRules();

        Assert.assertEquals(1, groundedRules.size());
        Assert.assertTrue(groundedRules.contains(groundenFact));
    }
}