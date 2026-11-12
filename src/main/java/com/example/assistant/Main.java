package com.example.assistant;

import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        String apiKey = System.getenv("OPENAI_API_KEY");
        if (apiKey == null || apiKey.isBlank()) {
            System.err.println("Please set the OPENAI_API_KEY environment variable.");
            System.exit(1);
        }

        ChatModel model = OpenAiChatModel.builder()
                .apiKey(apiKey)
                .modelName("gpt-4o-mini")
                .temperature(0.0)
                .build();

        EmployeeAssistant assistant = AiServices.builder(EmployeeAssistant.class)
                .chatModel(model)
                .chatMemoryProvider(memoryId -> MessageWindowChatMemory.withMaxMessages(20))
                .tools(new EmployeeTools())
                .build();

        System.out.println("Employee Assistant ready. Type your question (or 'exit' to quit).");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("\nYou: ");
            String input = scanner.nextLine();
            if ("exit".equalsIgnoreCase(input.trim())) {
                break;
            }
            System.out.println("Assistant: " + assistant.chat(input));
        }
    }
}
