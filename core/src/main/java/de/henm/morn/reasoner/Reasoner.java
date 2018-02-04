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

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

import de.henm.morn.core.Clause;
import de.henm.morn.core.CompoundTerm;
import de.henm.morn.core.Constant;
import de.henm.morn.core.Term;

/**
 * Simple reasoner.
 *
 * @author henm
 */
public class Reasoner {

    private final List<Clause> clauses;
    private final SimpleSubstitutioner simpleSubstitutioner;

    public Reasoner(List<Clause> clauses) {
        this.clauses = clauses;
        this.simpleSubstitutioner = new SimpleSubstitutioner();
    }

    /**
     * Answer a ground query for the program.
     *
     */
    public boolean query(Term query) {
        final Stack<BacktrackingState> backtrackingStack = new Stack<>();

        Queue<Term> resolvent = new LinkedBlockingDeque<>();
        resolvent.add(query);

        while (!resolvent.isEmpty()) {

            final Term goal = resolvent.poll();
            // Find rules with goal as head
            List<Clause> rules = this.findRulesWithHead(goal);

            if (rules.isEmpty()) {
                // Backtrack
                if (backtrackingStack.isEmpty()) {
                    // Tried everything
                    return false;
                } else {
                    // Restore
                    final BacktrackingState backtrack = backtrackingStack.pop();
                    resolvent = backtrack.getResolvent();
                    rules = backtrack.getRestOfRules();
                }
            }

            // Choose a backtracking-point
            final Clause rule = rules.get(0);

            final List<Clause> restOfRules = rules.subList(1, rules.size());
            if (!restOfRules.isEmpty()) {
                backtrackingStack.add(new BacktrackingState(
                    copyResolvent(resolvent),
                    restOfRules // already a copy
                ));
            }

            resolvent.addAll(rule.getBody());
        }

        // Resolvent is empty
        return true;
    }

    List<Clause> findRulesWithHead(Term goal) {
        return getGroundedRules().stream().filter(clause -> clause.getHead().equals(goal)).collect(Collectors.toList());
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

    private Queue<Term> copyResolvent(Queue<Term> resolvent) {
        final Iterator<Term> it = resolvent.iterator();
        
        final Queue<Term> result = new LinkedBlockingDeque<>();
        while (it.hasNext()) {
            result.add(it.next());
        }

        return result;
    }

    private static class BacktrackingState {
        final Queue<Term> resolvent;
        final List<Clause> restOfRules;

        public BacktrackingState(Queue<Term> resolvent, List<Clause> restOfRules) {
            this.resolvent = resolvent;
            this.restOfRules = restOfRules;
        }

        public Queue<Term> getResolvent() {
            return resolvent;
        }

        public List<Clause> getRestOfRules() {
            return restOfRules;
        }
    }
}