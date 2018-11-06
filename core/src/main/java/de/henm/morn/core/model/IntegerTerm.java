/*
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
package de.henm.morn.core.model;

/**
 * A term representing an integer.
 *
 * @author henm
 */
public class IntegerTerm implements Term {

    private final int value;

    public IntegerTerm(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean isGround() {
        return false;
    }

    @Override
    public boolean contains(Variable x) {
        return false;
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }
}
