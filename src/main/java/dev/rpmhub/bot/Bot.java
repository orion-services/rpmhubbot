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

import java.util.Arrays;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.interaction.SlashCommand;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionType;

import dev.rpmhub.bot.rest.RevisionService;

@ApplicationScoped
public class Bot {

        @Inject
        @ConfigProperty(name = "token")
        protected String token;

        @Inject
        @RestClient
        protected RevisionService service;

        public void start() {

                DiscordApi api = new DiscordApiBuilder().setToken(token).login().join();

                SlashCommand.with("send", "Send assign result to moodle", Arrays.asList(
                                SlashCommandOption.create(SlashCommandOptionType.STRING,
                                                "github-profile-url", "Github Profile URL",
                                                true),
                                SlashCommandOption.create(SlashCommandOptionType.STRING,
                                                "moodle-profile-url", "Moodle Profile URL",
                                                true),
                                SlashCommandOption.create(SlashCommandOptionType.STRING, "moodle-assign-url",
                                                "Moodle Assign URL",
                                                true)))
                                .createGlobal(api)
                                .join();

                api.addSlashCommandCreateListener(event -> {
                        SlashCommandInteraction slashCommandInteraction = event.getSlashCommandInteraction();
                        if (slashCommandInteraction.getCommandName().equals("send")) {

                                String githubProfileURL = slashCommandInteraction
                                                .getOptionStringValueByName("github-profile-url")
                                                .get();

                                String moodleProfileURL = slashCommandInteraction
                                                .getOptionStringValueByName("moodle-profile-url")
                                                .get();

                                String moodleAssignURL = slashCommandInteraction
                                                .getOptionStringValueByName("moodle-assign-url")
                                                .get();

                                String receipt = service.verify(githubProfileURL, moodleProfileURL,
                                                moodleAssignURL);

                                slashCommandInteraction.createImmediateResponder()
                                                .setContent(receipt)
                                                .setFlags(MessageFlag.EPHEMERAL) // Only visible for the user which
                                                                                 // invoked
                                                .respond();
                        }
                });

        }

}
