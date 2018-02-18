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

import java.util.List;
import java.util.stream.Collectors;

import de.henm.morn.core.Clause;
import de.henm.morn.core.Term;
import io.vavr.Tuple;
import io.vavr.Tuple2;

/**
 * Simple reasoner.
 *
 * @author henm
 */
public class Reasoner {

    private final List<Clause> clauses;
    private final Unification unification;

    public Reasoner(List<Clause> clauses) {
        this.unification = new Unification();
        this.clauses = clauses;
    }

    /**
     * Answer a query for program.
     * 
     * @param {goal} The goal to query.
     * @return True iff the goal can be deduced from the program.
     */
    public boolean query(Term goal) {
        final List<Tuple2<Clause, PositiveUnificationResult>> clauses = getPossibleClauses(goal);

        return clauses.stream().anyMatch(choose -> {
            final Clause clause = choose._1;
            final Substitution substitution = choose._2.getSubstitution();
            
            return clause.getBody().stream().allMatch(t -> {
                final Term tWithSubstitution = substitution.apply(t);
                return query(tWithSubstitution);
            });
        });
    }

    private List<Tuple2<Clause, PositiveUnificationResult>> getPossibleClauses(Term a) {
        return clauses.stream()
                .map(clause -> Tuple.of(clause, unification.unify(a, clause.getHead())))
                .filter(tuple -> tuple._2.termsUnify())
                .map(tuple -> Tuple.of(tuple._1, (PositiveUnificationResult) tuple._2)).collect(Collectors.toList());
    }
}