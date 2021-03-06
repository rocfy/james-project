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
package org.apache.james.mailbox.store.user.model.impl;

import org.apache.james.mailbox.store.user.model.Subscription;

import com.google.common.base.Objects;

public class SimpleSubscription implements Subscription {

    private final String user;
    private final String mailbox;
    
    public SimpleSubscription(String user, String mailbox) {
        this.user = user;
        this.mailbox = mailbox;
    }

    public String getMailbox() {
        return mailbox;
    }

    public String getUser() {
        return user;
    }

    @Override
    public final boolean equals(Object o) {
        if (o instanceof SimpleSubscription) {
            SimpleSubscription that = (SimpleSubscription) o;

            return Objects.equal(this.user, that.user)
                && Objects.equal(this.mailbox, that.mailbox);
        }
        return false;
    }

    @Override
    public final int hashCode() {
        return Objects.hashCode(user, mailbox);
    }
}
