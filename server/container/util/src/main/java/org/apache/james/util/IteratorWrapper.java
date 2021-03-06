/****************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one   *
 * or more contributor license agreements.  See the NOTICE file *
 * distributed with this work for additional information        *
 * regarding copyright ownership.  The ASF licenses this file   *
 * to you under the Apache License, Version 2.0 (the            *
 * "License"); you may not use this file except in compliance   *
 * with the License.  You may obtain a copy of the License at   *
 *                                                              *
 *   http://www.apache.org/licenses/LICENSE-2.0                 *
 *                                                              *
 * Unless required by applicable law or agreed to in writing,   *
 * software distributed under the License is distributed on an  *
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY       *
 * KIND, either express or implied.  See the License for the    *
 * specific language governing permissions and limitations      *
 * under the License.                                           *
 ****************************************************************/

package org.apache.james.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

public class IteratorWrapper<U> implements Iterator<U> {

    private final Iterator<U> underlying;
    private final List<U> entriesSeen;

    public IteratorWrapper(Iterator<U> underlying) {
        Preconditions.checkNotNull(underlying);
        this.underlying = underlying;
        this.entriesSeen = new ArrayList<U>();
    }

    public List<U> getEntriesSeen() {
        return ImmutableList.copyOf(entriesSeen);
    }

    @Override
    public boolean hasNext() {
        return underlying.hasNext();
    }

    @Override
    public U next() {
        U next = underlying.next();
        entriesSeen.add(next);
        return next;
    }

    @Override
    public void remove() {
        underlying.remove();
    }
}
