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

import org.junit.Assert;
import org.junit.Test;

import de.henm.morn.core.CompoundTermFactory;
import de.henm.morn.core.Constant;
import de.henm.morn.core.Functor;
import de.henm.morn.core.Term;
import de.henm.morn.core.Variable;

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

}