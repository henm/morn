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

import de.henm.morn.core.Constant;

/**
 * @author henm
 */
class MutableConstant implements MutableTerm {

    private final Constant constant;

    public MutableConstant(Constant constant) {
        this.constant = constant;
    }

    @Override
    public boolean contains(MutableVariable x) {
        return false;
    }

    @Override
    public void replace(MutableVariable x, MutableTerm replaceBy) {
        // NOP
    }

    public Constant getOriginalTerm() {
        return constant;
    }

}