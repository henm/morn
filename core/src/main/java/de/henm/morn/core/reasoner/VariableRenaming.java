/**
 * Copyright 2017-present henm
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.henm.morn.core.reasoner;

import de.henm.morn.core.model.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Generates new, unique variable names used during the reasoning.
 *
 * @author henm
 */
class VariableRenaming {

    private static int variableCounter = 0;

    private final CompoundTermFactory compoundTermfactory;

    public VariableRenaming() {
        this.compoundTermfactory = new CompoundTermFactory();
    }

    public Clause renameVariablesInClause(Clause clause) {
        if (clause instanceof Fact) {
            return renameVariablesInClause((Fact) clause);
        } else if (clause instanceof Rule) {
            return renameVariablesInClause((Rule) clause);
        } else {
            throw new IllegalArgumentException(String.format("Unkown clause %s", clause));
        }
    }

    public Fact renameVariablesInClause(Fact fact) {
        final Map<Variable, Variable> alreadyReplacedVariables = new LinkedHashMap<>();
        final Term renamedTerm = renameVariablesInTerm(fact.getHead(), alreadyReplacedVariables);

        return new Fact(renamedTerm);
    }

    public Rule renameVariablesInClause(Rule rule) {
        final Map<Variable, Variable> alreadyReplacedVariables = new LinkedHashMap<>();
        final Term head = renameVariablesInTerm(rule.getHead(), alreadyReplacedVariables);
        final List<Term> body = rule.getBody().stream()
                .map(t -> renameVariablesInTerm(t, alreadyReplacedVariables))
                .collect(Collectors.toList());

        return new Rule(head, body);
    }

    private Term renameVariablesInTerm(Term term, Map<Variable, Variable> alreadyReplacedVariables) {
        if (term instanceof CompoundTerm) {
            final CompoundTerm compoundTerm = (CompoundTerm) term;
            final List<Term> args = compoundTerm.getArguments().stream()
                    .map(t -> renameVariablesInTerm(t, alreadyReplacedVariables))
                    .collect(Collectors.toList());

            return compoundTermfactory.build(
                    compoundTerm.getFunctor(),
                    args
            );

        } else if (term instanceof PredicateTerm) {
            final PredicateTerm predicateTerm = (PredicateTerm) term;
            final Term term1 = renameVariablesInTerm(predicateTerm.getT1(), alreadyReplacedVariables);
            final Term term2 = renameVariablesInTerm(predicateTerm.getT2(), alreadyReplacedVariables);

            return new PredicateTerm(predicateTerm.getPredicate(), term1, term2);

        } else if (term instanceof L) {
            final L l = (L) term;
            if (l.isEmpty()) {
                return l;
            }

            final Term head = renameVariablesInTerm(l.getHead(), alreadyReplacedVariables);
            final Term rest = renameVariablesInTerm(l.getTail(), alreadyReplacedVariables);

            return new L(head, rest);

        } else if (term instanceof Variable) {
            final Variable variable = (Variable) term;
            if (alreadyReplacedVariables.containsKey(variable)) {
                return alreadyReplacedVariables.get(variable);
            } else {
                final Variable freshVariable = getFreshVariable();
                alreadyReplacedVariables.put(variable, freshVariable);
                return freshVariable;
            }

        } else if (term instanceof Constant) {
            return term;

        } else {
            throw new IllegalArgumentException(String.format("Unknown term %s", term));

        }
    }

    /**
     * @return A fresh, unique variable.
     */
    private Variable getFreshVariable() {
        return new Variable(String.format("_X%d", variableCounter++));
    }
}