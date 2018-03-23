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

import de.henm.morn.core.model.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static de.henm.morn.core.model.L.list;
import static de.henm.morn.core.model.L.EMPTY;

/**
 * @author henm
 */
public class SubstitutionTest {

    final Constant a = new Constant("a");
    final Constant b = new Constant("b");
    final Variable x = new Variable("X");
    final Variable y = new Variable("Y");

    final CompoundTermFactory ctFactory;

    Substitution substitution;

    public SubstitutionTest() {
        this.ctFactory = new CompoundTermFactory();
    }

    @Before
    public void setUp() {
        this.substitution = new Substitution();
    }

    @Test
    public void applyShouldrenameVariables() {
        substitution.add(x, y);
        Assert.assertEquals(y, substitution.apply(x));
    }

    @Test
    public void applyShouldReplaceConstants() {
        substitution.add(a, b);
        Assert.assertEquals(b, substitution.apply(a));
    }

    @Test
    public void applyShouldReplaceCompoundTerms() {
        final Functor f = new Functor("f");
        final CompoundTerm fbx = ctFactory.build(f, b, x);

        final Functor g = new Functor("g");
        final CompoundTerm gy = ctFactory.build(g, y);

        substitution.add(x, gy);
        
        final Term toCheck = substitution.apply(fbx);
        Assert.assertTrue(toCheck instanceof CompoundTerm);
        final CompoundTerm ct = (CompoundTerm) toCheck;
        Assert.assertEquals(f, ct.getFunctor());
        Assert.assertEquals(2, ct.getArguments().size());
        Assert.assertEquals(b, ct.getArguments().get(0));
        Assert.assertEquals(gy, ct.getArguments().get(1));
    }

    @Test
    public void resultOfMergeShouldContainAllMappings() {
        substitution.add(a, x);

        final Substitution otherSubstitution = new Substitution();
        otherSubstitution.add(b, y);

        final Substitution mergedSubstitution = substitution.merge(otherSubstitution);

        Assert.assertEquals(x, mergedSubstitution.get(a).get());
        Assert.assertEquals(y, mergedSubstitution.get(b).get());
    }

    @Test(expected = IllegalArgumentException.class)
    public void mergeShouldThrowExceptionIfTermIsMappedInBothArguments() {
        substitution.add(a, x);

        final Substitution otherSubstitution = new Substitution();
        otherSubstitution.add(a, y);

        substitution.merge(otherSubstitution);
    }

    @Test
    public void emptySubstitutionShouldNotChangeLists() {
        final Term list = list(a, b);
        final Term result = substitution.apply(list);

        Assert.assertTrue(result instanceof L);

        final L resultList = (L) result;
        Assert.assertEquals(a, resultList.getHead());

        final L innerList = (L) resultList.getTail();
        Assert.assertEquals(b, innerList.getHead());
        Assert.assertEquals(EMPTY, innerList.getTail());
    }
}