package com.nscc.onlinestore2.config;


import com.nscc.onlinestore2.entity.Game;
import com.nscc.onlinestore2.service.GameService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ChatConfig {

    @Bean
    public ChatMemory chatMemory() {
        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(new InMemoryChatMemoryRepository())
                .build();
    }

    @Bean
    public ChatClient chatClient(ChatModel chatModel, ChatMemory chatMemory, GameService gameService) {
        // Start the conversation 'prompt'. Customize the prompt and replace the "whale" conversation below with the prompt variable.
        StringBuilder prompt = new StringBuilder();
        prompt.append("You are a customer service assistant for a video game store service.\n");
        prompt.append("Here is our current game catalog as a list:\n");

        List<Game> games = gameService.getAllGames();
        games.forEach(g -> {
            // Include movies in the catalog. Note: %s is string value, %d is number
            prompt.append(String.format("- Title: %s. Description: %s. ESRB Rating: %s. Price: %d price.\n",
                    g.getName(), g.getDescription(), g.getEsrbRating(), g.getPrice()));
        });

        prompt.append("Answer customer questions based only on this catalog.");

        return ChatClient.builder(chatModel)
                .defaultSystem(prompt.toString())
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }
}
