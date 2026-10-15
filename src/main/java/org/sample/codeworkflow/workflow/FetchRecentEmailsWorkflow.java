package org.sample.codeworkflow.workflow;

import static com.bytechef.workflow.definition.WorkflowDsl.connection;
import static com.bytechef.workflow.definition.WorkflowDsl.input;
import static com.bytechef.workflow.definition.WorkflowDsl.output;
import static com.bytechef.workflow.definition.WorkflowDsl.task;
import static com.bytechef.workflow.definition.WorkflowDsl.workflow;

import com.bytechef.workflow.definition.TaskContext;
import com.bytechef.workflow.definition.WorkflowDsl.ModifiableWorkflowDefinition;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FetchRecentEmailsWorkflow {

    public static final ModifiableWorkflowDefinition WORKFLOW_DEFINITION = workflow("fetchRecentEmails")
        .label("Fetch Recent Emails")
        .description("Returns the most recent emails, optionally only those from one sender.")
        .inputs(
            input("from")
                .label("From")
                .type("STRING")
                .required(false))
        .outputs(
            output("emails")
                .task("fetchEmails"))
        .tasks(
            task("fetchEmails")
                .label("Fetch Emails")
                // A code task's perform calls actions itself, so it declares the connections it uses; the name is what
                // perform passes to the action. Pick the Gmail connection for "gmail" in the test configuration or when
                // deploying the project.
                .connections(connection("googleMail", "gmail"))
                .perform(FetchRecentEmailsWorkflow::fetchEmails));

    private static Object fetchEmails(TaskContext context) throws Exception {
        Map<String, Object> searchInput = new HashMap<>();

        Object from = context.input("from");

        if (from != null) {
            searchInput.put("from", from);
        }

        searchInput.put("maxResults", 10);

        Map<?, ?> result = asMap(context.component("googleMail", "searchEmail", searchInput, "gmail"));

        List<?> messages = result.get("messages") instanceof List<?> messageList ? messageList : List.of();

        context.log(TaskContext.LogLevel.INFO, "Found " + messages.size() + " emails");

        return messages;
    }

    private static Map<?, ?> asMap(Object value) {
        return value instanceof Map<?, ?> map ? map : Map.of();
    }
}
