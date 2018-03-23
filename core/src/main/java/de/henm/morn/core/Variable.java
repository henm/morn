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

/**
 * A free variable.
 * 
 * @author henm
 */
public class Variable implements Term {

    public static final Variable S = new Variable("S");
    public static final Variable T = new Variable("T");
    public static final Variable U = new Variable("U");
    public static final Variable V = new Variable("V");
    public static final Variable W = new Variable("W");
    public static final Variable X = new Variable("X");
    public static final Variable Y = new Variable("Y");
    public static final Variable Z = new Variable("Z");

    private final String name;

    public Variable(String name) {
        this.name = name;
    }

    @Override
    public boolean isGround() {
        return false;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean contains(Variable x) {
        return false;
    }
}