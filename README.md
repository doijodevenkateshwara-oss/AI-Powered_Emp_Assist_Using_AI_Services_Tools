# AI-Powered Employee Assistant (LangChain4j)

A minimal console application that lets employees ask natural-language questions
about their profile, salary and leave balance. The AI understands the request and
invokes Java methods exposed as **AI Tools**.

## Features

- `EmployeeAssistant` interface — the AI service (system instructions + chat method)
- `EmployeeTools` — Java methods exposed as tools:
  - `getEmployeeDetails(employeeId)` — name, department, salary, leave info
  - `calculateAnnualSalary(monthlySalary)` — monthly × 12
  - `checkLeaveBalance(employeeId)` — remaining leave days
  - `calculateRemainingLeaveBalance(employeeId, daysUsed)` — balance after leave usage
- Conversation history via `MessageWindowChatMemory` (last 20 messages)
- Interactive CLI loop in `Main`

Sample employees are defined in-memory in `EmployeeTools` (EMP001–EMP004).

## Prerequisites

- Java 17+ (tested on 21)
- Maven 3.8+
- An OpenAI API key

## Configuration & Run

1. Set your OpenAI API key:

   ```bash
   export OPENAI_API_KEY=sk-your-key-here
   ```

2. Build the project:

   ```bash
   mvn clean package
   ```

   This produces a fat jar: `target/employee-assistant-1.0.0.jar`

3. Run it:

   ```bash
   java -jar target/employee-assistant-1.0.0.jar
   ```

   Or directly with Maven:

   ```bash
   mvn compile exec:java -Dexec.mainClass=com.example.assistant.Main
   ```
   (requires the `exec-maven-plugin`, or just use the jar above)

4. Ask questions, e.g.:

   ```
   What is the annual salary of EMP001?
   How many leave days does EMP002 have left?
   If EMP003 takes 5 days off, how many leaves remain?
   Tell me about employee EMP004.
   ```

   Type `exit` to quit.

## How It Works

1. **User input** — `Main.java` reads a natural-language question from the console
   (e.g. *"What is the annual salary of EMP001?"*) and passes it to the assistant.

2. **AI Service** — `EmployeeAssistant` is a LangChain4j AI service interface.
   Its `@SystemMessage` tells the model it is an employee assistant and which
   operations it supports. `AiServices.builder(...)` wires the interface to the
   `OpenAiChatModel`, the tool objects, and the chat memory.

3. **Tool calling** — LangChain4j inspects the `@Tool`-annotated methods in
   `EmployeeTools` and exposes them to the model as callable functions. When the
   model decides a tool is needed, it returns a tool call; LangChain4j executes
   the Java method locally and feeds the result back to the model.

4. **Response** — the model turns the tool result into a natural-language answer,
   which is printed to the console.

5. **Conversation memory** — `MessageWindowChatMemory` keeps the last 20
   messages, so follow-up questions like *"And what about EMP002?"* work without
   repeating context.

```
User question
   │
   ▼
EmployeeAssistant.chat()  ──(system prompt + history + tool definitions)──►  OpenAI model
   │                                                                        │
   │                                              ◄── tool call (e.g. getEmployeeDetails)
   ▼
LangChain4j executes EmployeeTools method locally
   │
   ▼
Tool result sent back to model ──► natural-language answer ──► console
```

## Project Structure

```
pom.xml
src/main/java/com/example/assistant/
├── EmployeeAssistant.java   # AI service interface (@SystemMessage + chat method)
├── EmployeeTools.java       # @Tool methods + in-memory employee data
└── Main.java                # builds model, wires AiServices, CLI loop
```

## Notes

- The default model is `gpt-4o-mini`; change it in `Main.java`
  (`OpenAiChatModel.builder().modelName(...)`).
- To use a different provider, swap `OpenAiChatModel` for the corresponding
  LangChain4j model (e.g. `OllamaChatModel`) and adjust the dependency in `pom.xml`.
