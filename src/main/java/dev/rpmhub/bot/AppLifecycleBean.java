/**
 * Copyright 2022 RPMHub Bot @ https://github.com/rpmhub/bot
 *
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

package dev.rpmhub.bot;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.logging.Logger;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;

@ApplicationScoped
public class AppLifecycleBean {

    /* Logger */
    private static final Logger LOGGER = Logger.getLogger(AppLifecycleBean.class.getName());

    @Inject
    Bot bot;

    void onStart(@Observes StartupEvent ev) {
        LOGGER.info("The rpm bot is starting...");
        bot.start();

    }

    void onStop(@Observes ShutdownEvent ev) {
        LOGGER.info("The rpm bot is stopping...");
    }

}
