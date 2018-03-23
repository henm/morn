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

import static de.henm.morn.core.Variable.X;
import static de.henm.morn.core.Variable.Y;

import org.junit.Assert;
import org.junit.Test;

import de.henm.morn.core.Clause;
import de.henm.morn.core.CompoundTerm;
import de.henm.morn.core.Fact;
import de.henm.morn.core.Functor;
import de.henm.morn.core.Rule;

/**
 * @author henm
 */
public class VariableRenamingTest {

    private VariableRenaming variableRenaming;

    public VariableRenamingTest() {
        this.variableRenaming = new VariableRenaming();
    }

    @Test
    public void variablesShouldBeReplacedByFreshVariables() {
        final Functor f = new Functor("f");
        final Fact fact = new Fact(f.apply(X));

        final Clause result = variableRenaming.renameVariablesInClause(fact);
        Assert.assertTrue(result instanceof Fact);

        final Fact resultFact = (Fact) result;
        final CompoundTerm resultTerm = (CompoundTerm) resultFact.getHead();

        Assert.assertFalse(resultTerm.getArguments().get(0).equals(X));
    }

    @Test
    public void sameVariablesShouldBeReplacedBySameFreshVariable() {
        final Functor f = new Functor("f");
        final Functor g = new Functor("g");

        final Rule rule = new Rule(f.apply(X), g.apply(X, Y));

        final Clause result = variableRenaming.renameVariablesInClause(rule);

        Assert.assertTrue(result instanceof Rule);
        final Rule resultRule = (Rule) result;

        Assert.assertTrue(resultRule.getHead() instanceof CompoundTerm);
        Assert.assertTrue(resultRule.getBody().get(0) instanceof CompoundTerm);

        final CompoundTerm head = (CompoundTerm) resultRule.getHead();
        final CompoundTerm body = (CompoundTerm) resultRule.getBody().get(0);

        Assert.assertTrue(head.getArguments().get(0).equals(body.getArguments().get(0)));
    }

}