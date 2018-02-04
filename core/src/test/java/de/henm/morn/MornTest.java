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
package de.henm.morn;

import org.junit.Test;
import org.junit.Assert;

import de.henm.morn.core.Functor;
import de.henm.morn.core.Constant;
import de.henm.morn.core.FreeVariable;

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

        final FreeVariable x = new FreeVariable("X");
        final FreeVariable y = new FreeVariable("y");
        final FreeVariable z = new FreeVariable("z");

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

}