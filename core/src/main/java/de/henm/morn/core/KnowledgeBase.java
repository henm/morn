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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

/**
 * 
 * @author henm
 */
public class KnowledgeBase {

    private final SimpleSubstitutioner simpleSubstitutioner;
    private final List<Clause> clauses;

    public KnowledgeBase() {
        this.clauses = new ArrayList<>();
        this.simpleSubstitutioner = new SimpleSubstitutioner();
    }

    /**
     * @param fact The fact to add.
     * @return This KnowledgeBase for builder pattern.
     */
    public KnowledgeBase addFact(Fact fact) {
        this.clauses.add(fact);
        return this;
    }

    /**
     * @param Term A term which is to be added as a fact.
     * @return This KnowledgeBase for builder pattern.
     */
    public KnowledgeBase addFact(Term term) {
        this.clauses.add(new Fact(term));
        return this;
    }

    /**
     * @param facts Multiple facts to add.
     * @return This KnowledgeBase for builder pattern.
     */
    public KnowledgeBase addFacts(Fact... facts) {
        for (Fact fact : facts) {
            this.addFact(fact);
        }
        return this;
    }

    /**
     * Add a rule to the knowledge base.
     * 
     * @param head The head of the rule.
     * @param body The body of the rule.
     * @return This KnowledgeBase for builder pattern.
     */
    public KnowledgeBase addRule(Term head, Term... body) {
        this.clauses.add(new Rule(head, body));
        return this;
    }

    /**
     * Add a rule to the knowledge base.
     * 
     * @param rule The rule to add.
     * @return This KnowledgeBase for builder pattern.
     */
    public KnowledgeBase addRule(Rule rule) {
        this.clauses.add(rule);
        return this;
    }

    /**
     * Query this KnowledgeBase for satisfiability.
     * 
     * @return True iff this KnowledgeBase is satisfiable.
     */
    public boolean query() {
        // TODO
        return true;
    }

    public boolean query(FreeVariable... variables) {
        // TODO
        return true;
    }

    Optional<Clause> findRuleWithHead(Term goal) {
        return getGroundedRules().stream().filter(clause -> clause.getHead().equals(goal)).findFirst();
    }

    Collection<Clause> getGroundedRules() {
        return clauses.stream().map(clause -> {
            if (clause.isGround()) {
                return Collections.singletonList(clause);
            } else {
                final Collection<Term> constants = getAllUsedConstants();
                return simpleSubstitutioner.getAllPossibleSubstitutions(clause, constants);
            }
        }).flatMap(Collection::stream).distinct().collect(Collectors.toList());
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