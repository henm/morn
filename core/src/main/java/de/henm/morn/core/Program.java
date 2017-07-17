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

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

/**
 * @author henm
 */
public class Program {

    private final SimpleSubstitutioner simpleSubstitutioner;
    private final List<Clause> clauses;

    public Program() {
        this.clauses = new ArrayList<>();
        this.simpleSubstitutioner = new SimpleSubstitutioner();
    }

    public Program addFact(Fact fact) {
        clauses.add(fact);
        return this;
    }

    public Program addFact(Term term) {
        clauses.add(new Fact(term));
        return this;
    }

    public Program addRule(Clause clause) {
        clauses.add(clause);
        return this;
    }

    Optional<Clause> findRuleWithHead(Term goal) {
        return getGroundedRules().stream().filter(clause -> clause.getHead().equals(goal))
                .findFirst();
    }

    Collection<Clause> getGroundedRules() {
        return clauses.stream().map(clause -> {
            if (clause.isGround()) {
                return Collections.singletonList(clause);
            } else {
                final Collection<Term> constants = getAllUsedConstants();
                return simpleSubstitutioner.getAllPossibleSubstitutions(clause, constants);
            }
        }).flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());
    }

    Set<Term> getAllUsedConstants() {
        // Use a queue instead of recursion cause this is java...
        final Queue<Term> queue = new LinkedBlockingQueue<>();
        clauses.forEach(clause -> {
            queue.add(clause.getHead());
            queue.addAll(clause.getBody());
        });

        final Set<Term> result = new LinkedHashSet<>();

        while (!queue.isEmpty()) {
            final Term term = queue.poll();
            if (term instanceof Constant) {
                result.add(term);
            } else if (term instanceof CompoundTerm) {
                queue.addAll(((CompoundTerm) term).getArguments());
            }
        }

        return result;
    }
}
