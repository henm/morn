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

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ReasonerTest {

    @Test
    public void simpleFactsShouldBeDeduced() {
        final Term a = new Constant("a");
        final Program program = new Program();
        program.addFact(new Fact(a));

        final Reasoner reasoner = new Reasoner(program);

        assertTrue(reasoner
            .query(a));
    }

    @Test
    public void lotIsSonOfHaran() {
        final Constant abraham = new Constant("abraham");
        final Constant isaac = new Constant("isaac");
        final Constant haran = new Constant("haran");
        final Constant lot = new Constant("lot");
        final Constant milcah = new Constant("milcah");
        final Constant yiscah = new Constant("yiscah");

        final Atom father = new Atom("father");
        final Atom mother = new Atom("mother");
        final Atom male = new Atom("male");
        final Atom female = new Atom("female");
        final Atom son = new Atom("son");
        final Atom daughter = new Atom("daughter");

        final CompoundTermFactory ctFactory = new CompoundTermFactory();

        final Program program = new Program();
        program.addFact(ctFactory.build(father, abraham, isaac))
                .addFact(ctFactory.build(male, isaac))
                .addFact(ctFactory.build(father, haran, lot))
                .addFact(ctFactory.build(male, lot))
                .addFact(ctFactory.build(father, haran, milcah))
                .addFact(ctFactory.build(female, milcah))
                .addFact(ctFactory.build(father, haran, milcah))
                .addFact(ctFactory.build(female, yiscah));

        final FreeVariable x = new FreeVariable("X");
        final FreeVariable y = new FreeVariable("Y");
        program.addRule(ctFactory.build(son, x, y).entailed(ctFactory.build(father, y, x), ctFactory.build(male, x)))
                .addRule(ctFactory.build(daughter, x, y).entailed(ctFactory.build(father, y, x), ctFactory.build(female, x)));

        final Reasoner reasoner = new Reasoner(program);

        assertTrue(reasoner.query(ctFactory.build(son, lot, haran)));
    }

    @Test
    public void reasonerShouldNotBeInitializedWithNull() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Test
    public void queryShouldThrowSensibleExceptionIfQueryIsNull() {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}