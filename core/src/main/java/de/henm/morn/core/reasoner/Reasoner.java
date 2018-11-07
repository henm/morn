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

import de.henm.morn.core.model.Clause;
import de.henm.morn.core.model.IntegerTerm;
import de.henm.morn.core.model.PredicateTerm;
import de.henm.morn.core.model.Term;
import io.vavr.Tuple;
import io.vavr.Tuple2;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Simple reasoner.
 *
 * @author henm
 */
public class Reasoner {

    private final List<Clause> clauses;
    private final Unification unification;
    private final VariableRenaming variableRenaming;
    private final ClauseOrdering clauseOrdering;

    public Reasoner(List<Clause> clauses) {
        this.unification = new Unification();
        this.clauses = clauses;
        this.variableRenaming = new VariableRenaming();
        this.clauseOrdering = new ClauseOrdering();
    }

    /**
     * Answer a query for program.
     *
     * @param {goal} The goal to query.
     * @return Optional containing a possible substitution iff the goal can be
     * deduced from the program.
     */
    public Optional<Substitution> query(Term goal) {

        if (goal instanceof PredicateTerm) {
            return testPredicate((PredicateTerm) goal);
        } else {
            return handleQueryWithUnification(goal);
        }
    }

    private Optional<Substitution> testPredicate(PredicateTerm predicateTerm) {
        if (predicateTerm.getT1() instanceof IntegerTerm && predicateTerm.getT2() instanceof IntegerTerm) {
            final IntegerTerm nt1 = (IntegerTerm) predicateTerm.getT1();
            final IntegerTerm nt2 = (IntegerTerm) predicateTerm.getT2();
            if (predicateTerm.test(nt1.getValue(), nt2.getValue())) {
                return Optional.of(new Substitution());
            } else {
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
    }

    private Optional<Substitution> handleQueryWithUnification(Term goal) {
        final List<Tuple2<Clause, PositiveUnificationResult>> clauses = getPossibleClauses(goal);

        // This stream contains the outcomes of all the possible clauses
        final Stream<Optional<Substitution>> results = clauses
                .stream()
                .sorted((x, y) -> clauseOrdering.compare(x._1, y._1))
                .map(r -> getQueryResultForUnificationResult(r._1, r._2));

        // Only the successfull branches are relevant
        return results.filter(branch -> branch.isPresent()).map(branch -> branch.get()).findFirst();
    }

    private List<Tuple2<Clause, PositiveUnificationResult>> getPossibleClauses(Term a) {
        return clauses.stream()
                // First rename the variables in the clause
                .map(clause -> variableRenaming.renameVariablesInClause(clause))
                // Then try to unify
                .map(clause -> Tuple.of(clause, unification.unify(a, clause.getHead())))
                .filter(tuple -> tuple._2.termsUnify())
                .map(tuple -> Tuple.of(tuple._1, (PositiveUnificationResult) tuple._2))
                .collect(Collectors.toList());
    }

    private Optional<Substitution> getQueryResultForUnificationResult(Clause clause,
                                                                      PositiveUnificationResult unificationResult) {
        return clause.getBody().stream()
                .map(term -> {
                    Term t = unificationResult.getSubstitution().apply(term);
                    return t;
                })
                // Check if the term (with substitution applied) is satisfiable
                // TODO Performance: After first fail it is not necessary
                // to query the rest
                .map(this::query)
                // Reduced is called to ensure all terms are true in
                // relation to the program
                // If there are no terms to check the result is success
                .reduce(Optional.of(new Substitution()), this::mergeSubstitutions);
    }

    private Optional<Substitution> mergeSubstitutions(Optional<Substitution> s1, Optional<Substitution> s2) {
        return s1.flatMap((s1Present) -> s2.map(s2Present -> s1Present.merge(s2Present)));
    }
}