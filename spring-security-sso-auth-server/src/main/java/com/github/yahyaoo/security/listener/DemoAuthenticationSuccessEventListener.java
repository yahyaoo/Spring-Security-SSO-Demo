/*
 *  Copyright 2019 Yahyaoo.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.github.yahyaoo.security.listener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;

/**
 * Listen for user authorization success events,see {@link AuthenticationSuccessEvent}.
 * Authorization events related see {@link AbstractAuthenticationEvent} implementation type.
 * <p>
 * See {@link DefaultAuthenticationEventPublisher} for authorized event publish.
 * <br>
 *
 * @author yahyaoo
 * @date 2019/4/6 10:04
 */
public class DemoAuthenticationSuccessEventListener implements SmartApplicationListener {

    private final Log logger = LogFactory.getLog(getClass());

    @Override
    public boolean supportsEventType(Class<? extends ApplicationEvent> eventType) {
        return AuthenticationSuccessEvent.class.isAssignableFrom(eventType);
    }

    @Override
    public boolean supportsSourceType(Class<?> sourceType) {
        return true;
    }

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {

        AuthenticationSuccessEvent successEvent = (AuthenticationSuccessEvent) event;

        if (logger.isDebugEnabled()) {
            logger.debug("We're listening for an authorization event" + ".Principal:"
                    + successEvent.getAuthentication().getPrincipal().toString());
        }

    }

}
