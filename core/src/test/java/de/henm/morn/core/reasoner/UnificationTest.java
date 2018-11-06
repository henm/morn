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
package de.henm.morn.core.reasoner;

import static de.henm.morn.core.model.L.list;
import static de.henm.morn.core.model.L.EMPTY;

import de.henm.morn.core.model.*;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author henm
 */
public class UnificationTest {

    private final Unification unification;
    private final CompoundTermFactory compoundTermFactory;

    public UnificationTest() {
        this.unification = new Unification();
        this.compoundTermFactory = new CompoundTermFactory();
    }

    @Test
    public void unificationShouldHandleConstants() {
        final Constant a = new Constant("a");
        final Constant b = new Constant("b");

        Assert.assertTrue(unification.unify(a, a).termsUnify());
        Assert.assertFalse(unification.unify(a, b).termsUnify());
    }

    @Test
    public void unificationShouldHandleVariables() {
        final Constant a = new Constant("a");
        final Variable x = new Variable("X");

        Assert.assertTrue(unification.unify(x, x).termsUnify());

        final UnificationResult resultax = unification.unify(a, x);
        Assert.assertTrue(resultax.termsUnify());
        Assert.assertEquals(resultax.getSubstitution().get(x).get(), a);

        final UnificationResult resultxa = unification.unify(x, a);
        Assert.assertTrue(resultxa.termsUnify());
        Assert.assertEquals(resultxa.getSubstitution().get(x).get(), a);
    }

    @Test
    public void unificationShouldHanldeComplexTerms() {
        final Functor f = new Functor("f");
        final Functor g = new Functor("g");
        final Constant a = new Constant("a");
        final Constant b = new Constant("b");
        final Variable x = new Variable("X");

        final Term fab = compoundTermFactory.build(f, a, b);
        Assert.assertTrue(unification.unify(fab, fab).termsUnify());

        final Term fax = compoundTermFactory.build(f, a, x);
        final UnificationResult fabfax = unification.unify(fab, fax);
        Assert.assertTrue(fabfax.termsUnify());
        Assert.assertEquals(fabfax.getSubstitution().get(x).get(), b);

        final UnificationResult faxfax = unification.unify(fax, fax);
        Assert.assertTrue(faxfax.termsUnify());

        final Term gxb = compoundTermFactory.build(g, x, b);
        Assert.assertFalse(unification.unify(fax, gxb).termsUnify());
    }

    @Test
    public void unificationShouldHandleLists() {
        final Constant a = new Constant("a");
        final Constant b = new Constant("b");
        final Constant c = new Constant("c");

        final L l1 = list(a, b, c);
        final L l2 = list(a, b, c);

        final UnificationResult resultOfSame = unification.unify(l1, l2);
        Assert.assertTrue(resultOfSame.termsUnify());

        final L l3 = list(c, b, a);
        final UnificationResult resultOfDifferent = unification.unify(l1, l3);
        Assert.assertFalse(resultOfDifferent.termsUnify());

        final Variable x = new Variable("X");
        final Variable y = new Variable("Y");
        final UnificationResult resultVariables = unification.unify(list(EMPTY, l1), list(x, y));
        Assert.assertTrue(resultVariables.termsUnify());
    }

    @Test
    public void unificationShouldHandleIntegerTerms() {
        final IntegerTerm nt1a = new IntegerTerm(1);
        final IntegerTerm nt1b = new IntegerTerm(1);
        final IntegerTerm nt2 = new IntegerTerm(2);

        final UnificationResult resultOfSame = unification.unify(nt1a, nt1b);
        Assert.assertTrue(resultOfSame.termsUnify());

        final UnificationResult resultOfDifferent = unification.unify(nt1a, nt2);
        Assert.assertFalse(resultOfDifferent.termsUnify());

        final Variable x = new Variable("X");
        final UnificationResult resultWithSubstitution = unification.unify(nt2, x);
        Assert.assertTrue(resultWithSubstitution.termsUnify());

        final UnificationResult resultWithList = unification.unify(list(nt1a, nt2), list(x, nt2));
        Assert.assertTrue(resultWithList.termsUnify());
    }

}