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

import de.henm.morn.core.KnowledgeBase;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Simple reasoner.
 *
 * @author henm
 */
public class Reasoner {

    private final KnowledgeBase knowledgeBase;

    public Reasoner(KnowledgeBase knowledgeBase) {
        this.knowledgeBase = knowledgeBase;
    }

    /**
     * Answer a ground query for the program.
     *
     */
    public boolean query(Term query) {
        final Queue<Term> resolvent = new LinkedBlockingDeque<>();
        resolvent.add(query);

        while (!resolvent.isEmpty()) {

            final Term goal = resolvent.poll();
            // Find a rule with goal as head
            final Optional<Clause> rule = knowledgeBase.findRuleWithHead(goal);
            if (rule.isPresent()) {
                resolvent.addAll(rule.get().getBody());
            } else {
                return false;
            }
        }

        // Resolvent is empty
        return true;
    }

}