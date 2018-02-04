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

import java.util.ArrayList;
import java.util.List;

import de.henm.morn.core.Clause;
import de.henm.morn.core.Fact;
import de.henm.morn.core.Rule;
import de.henm.morn.core.Term;
import de.henm.morn.reasoner.Reasoner;

/**
 * 
 * @author henm
 */
public class KnowledgeBase {

    private final List<Clause> clauses;

    public KnowledgeBase() {
        this.clauses = new ArrayList<>();
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
     * Query this KnowledgeBase.
     * 
     * @param term The term to query.
     * @return True iff term is true in this KnowledgeBase.
     */
    public boolean query(Term term) {
        final Reasoner reasoner = new Reasoner(this.clauses);
        return reasoner.query(term);
    }
}