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

import java.util.*;

/**
 * @author henm
 */
public class SimpleSubstitutionerTest {

    private final SimpleSubstitutioner simpleSubstitutioner;
    private final CompoundTermFactory compoundTermFactory;

    private Functor p;
    private Functor q;
    private Constant c;
    private FreeVariable x;
    private Term pOfc;
    private Term pOfx;
    private Term qOfx;
    private Map<FreeVariable, Term> substitution;

    public SimpleSubstitutionerTest() {
        this.simpleSubstitutioner = new SimpleSubstitutioner();
        this.compoundTermFactory = new CompoundTermFactory();
    }

    @Before
    public void setUp() {
        this.p = new Functor("p");
        this.q = new Functor("q");
        this.c = new Constant("c");
        this.x = new FreeVariable("X");
        this.pOfc = compoundTermFactory.build(p, c);
        this.pOfx = compoundTermFactory.build(p, x);
        this.qOfx = compoundTermFactory.build(q, x);

        this.substitution = new LinkedHashMap<>();
        this.substitution.put(x, c);
    }

    @Test
    public void getAllPossibleSubstitutionsShouldRegardRules() {
        final Rule rule = qOfx.entailed(pOfx);
        final List<Term> substitutes = Collections.singletonList(c);

        final Collection<Clause> sub = simpleSubstitutioner.getAllPossibleSubstitutions(rule, substitutes);

        Assert.assertEquals(1, sub.size());
    }

    @Test
    public void getAllPossibleSubstitutionsShouldReturnTermForGroundedTerm() {
        final Collection<Term> subs = simpleSubstitutioner.getAllPossibleSubstitutions(pOfc, Collections.emptyList());

        Assert.assertEquals(1, subs.size());
        Assert.assertTrue(subs.contains(pOfc));
    }

    @Test
    public void applySubstitutionShouldNotChangeGroundTerms() {
        final Term substituted = simpleSubstitutioner.applySubstitution(pOfc, new LinkedHashMap());

        Assert.assertEquals(pOfc, substituted);
    }

    @Test(expected = IllegalArgumentException.class)
    public void applySubstitutionShouldThrowAnErrorWhenFreeVariableIsNotInSubstitution() {
        simpleSubstitutioner.applySubstitution(pOfx, new LinkedHashMap<>());
    }

    @Test
    public void resultOfApplySubstitutionShouldBeGround() {
        final Term term = simpleSubstitutioner.applySubstitution(pOfx, substitution);

        Assert.assertTrue(term.isGround());
    }

    @Test
    public void getAllPossibleSubstitutionsShouldReturnEmptyCollectionForEmptyCollectionOfFreeVariables() {
        final Collection<Map<FreeVariable, Term>> subs = simpleSubstitutioner.getAllPossibleSubstitutions(Collections.emptyList(), Collections.singleton(pOfc));

        Assert.assertEquals(0, subs.size());
    }

    @Test
    public void applySubstitutionShouldReplaceAllFreeVariables() {
        final Term term = simpleSubstitutioner.applySubstitution(pOfx, substitution);

        Assert.assertEquals(pOfc, term);
    }

}