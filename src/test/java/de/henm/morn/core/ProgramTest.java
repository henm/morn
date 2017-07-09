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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.Set;


/**
 *
 * @author henm
 */
public class ProgramTest {

    private Constant a;
    private Constant b;
    private Constant c;
    private FreeVariable x;
    private Atom p;
    private CompoundTermFactory ctFactory;

    @Before
    public void setUp() {
        this.a = new Constant("a");
        this.b = new Constant("b");
        this.c = new Constant("c");
        this.x = new FreeVariable("X");
        this.p = new Atom("p");
        this.ctFactory = new CompoundTermFactory();
    }

    @Test
    public void getAllUsedConstantsShouldReturnAllConstants() {
        final Program program = new Program();
        program.addFact(c);
        program.addFact(ctFactory.build(p, b));
        program.addRule(new Rule(ctFactory.build(p, b), ctFactory.build(p, c)));

        final Set<Term> usedConstants = program.getAllUsedConstants();

        Assert.assertEquals(2, usedConstants.size());
        Assert.assertTrue(usedConstants.contains(b));
        Assert.assertTrue(usedConstants.contains(c));
    }

    @Test
    public void getGroundedRulesShouldNotContainAnyRuleWithFreeVariables() {
        final Program program = new Program();
        final Fact groundenFact = new Fact(ctFactory.build(p, a));

        program.addFact(groundenFact);
        program.addFact(ctFactory.build(p, x));

        final Collection<Clause> groundedRules = program.getGroundedRules();

        Assert.assertEquals(1, groundedRules.size());
        Assert.assertTrue(groundedRules.contains(groundenFact));
    }
}
