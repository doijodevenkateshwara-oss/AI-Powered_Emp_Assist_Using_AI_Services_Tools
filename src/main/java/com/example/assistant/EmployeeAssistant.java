package com.example.assistant;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

public interface EmployeeAssistant {

    @SystemMessage("""
            You are a helpful employee assistant for a company.
            Answer employee questions using the provided tools.
            Supported operations:
            - Retrieve employee details by employee ID
            - Calculate annual salary from monthly salary
            - Check an employee's leave balance
            - Calculate remaining leave balance after leave usage
            Always use the tools when needed and answer in a concise, friendly way.
            """)
    String chat(@UserMessage String userMessage);
}
