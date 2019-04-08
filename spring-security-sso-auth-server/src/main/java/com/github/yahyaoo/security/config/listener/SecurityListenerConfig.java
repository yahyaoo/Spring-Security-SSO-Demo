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

package com.github.yahyaoo.security.config.listener;

import com.github.yahyaoo.security.listener.DemoAuthenticationSuccessEventListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.context.DelegatingApplicationListener;

/**
 * Listener configuration related to authentication.
 * Here mainly through dependency injection,
 * method {@link WebSecurityConfiguration#delegatingApplicationListener()} injection {@link DelegatingApplicationListener}.
 * <p>
 * Then through {@link DelegatingApplicationListener} agent registered listeners.
 * <p>
 *
 * @author yahyaoo
 * @date 2019/4/6 10:25
 */
@Configuration
public class SecurityListenerConfig {

    private final Log logger = LogFactory.getLog(getClass());

    /**
     * Adds event listener to {@link DelegatingApplicationListener}.
     * listeners you add must implement the {@link SmartApplicationListener}
     * <p>
     * for example:
     * <pre>
     *      &#064;Autowired
     *      &#064;ConditionalOnBean(DelegatingApplicationListener.class)
     *      public void addListener(DelegatingApplicationListener listener) {
     *          listener.addListener(new Your1EventListener());
     *          listener.addListener(new Your2EventListener());
     *          ...
     *      }
     * </pre>
     *
     * @param listener Delegate listener for dependency injection ,see {@link DelegatingApplicationListener}
     */
    @Autowired
    @ConditionalOnBean(DelegatingApplicationListener.class)
    public void addListener(DelegatingApplicationListener listener) {

        if (logger.isDebugEnabled()) {
            logger.debug("Register listener to the delegate container in " + listener.getClass().getName());
        }

        listener.addListener(new DemoAuthenticationSuccessEventListener());

    }

}
