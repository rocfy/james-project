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
package org.apache.james.transport.mailets.jsieve;

import javax.mail.MessagingException;

import org.apache.jsieve.mail.Action;
import org.apache.jsieve.mail.ActionDiscard;
import org.apache.mailet.Mail;
import org.apache.mailet.MailAddress;

import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;

public class DiscardAction extends FileIntoAction implements MailAction {

    public void execute(Action action, Mail mail, final ActionContext context)
            throws MessagingException {
        if (action instanceof ActionDiscard) {
            removeRecipient(mail, context);
        }
    }

    public static void removeRecipient(Mail mail, ActionContext context) {
        mail.setRecipients(FluentIterable.from(mail.getRecipients())
            .filter(isNot(context.getRecipient()))
            .toList());
    }

    private static Predicate<MailAddress> isNot(final MailAddress recipient) {
        return new Predicate<MailAddress>() {
            @Override
            public boolean apply(MailAddress input) {
                return !input.equals(recipient);
            }
        };
    }
}
