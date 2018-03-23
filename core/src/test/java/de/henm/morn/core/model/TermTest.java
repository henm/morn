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
package de.henm.morn.core.model;

import de.henm.morn.core.model.*;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author henm
 */
public class TermTest {

    final CompoundTermFactory ctFactory = new CompoundTermFactory();

    final Variable x = new Variable("X");
    final Variable y = new Variable("Y");
    final Constant a = new Constant("a");
    final Constant b = new Constant("b");
    final Functor f = new Functor("f");
    final Functor g = new Functor("g");
    final Term fax = ctFactory.build(f, a, x);
    final Term gax = ctFactory.build(g, a, x);

    @Test
    public void variablesShouldBeEqualByReference() {
        Assert.assertEquals(x, x);
        Assert.assertNotEquals(x, y);
    }

    @Test
    public void constantsShouldBeEqualByReference() {
        Assert.assertEquals(a, a);
        Assert.assertNotEquals(a, b);
    }

    @Test
    public void compoundTermsShouldBeEqualByFunctorAndArguments() {
        Assert.assertEquals(fax, fax);
        Assert.assertNotEquals(fax, gax);

        final Term fay = ctFactory.build(f, a, y);

        Assert.assertNotEquals(fax, fay);
    }

    @Test
    public void containsShouldCheckArgumentsOfCompoundTerms() {
        Assert.assertTrue(fax.contains(x));
        Assert.assertFalse(fax.contains(y));
    }

    @Test
    public void variableDoesNotContainAnything() {
        Assert.assertFalse(x.contains(y));
        Assert.assertFalse(x.contains(x));
    }

    @Test
    public void constantDoesNotContainAnything() {
        Assert.assertFalse(a.contains(x));
    }
}