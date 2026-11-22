package org.sample.codeworkflow.workflow;

import static com.bytechef.workflow.definition.WorkflowDsl.componentTask;
import static com.bytechef.workflow.definition.WorkflowDsl.task;
import static com.bytechef.workflow.definition.WorkflowDsl.trigger;
import static com.bytechef.workflow.definition.WorkflowDsl.workflow;

import com.bytechef.workflow.definition.TaskContext;
import com.bytechef.workflow.definition.WorkflowDsl.ModifiableWorkflowDefinition;
import java.util.List;
import java.util.Map;

public class ListOrdersWorkflow {

    public static final ModifiableWorkflowDefinition WORKFLOW_DEFINITION = workflow("listOrders")
        .label("List Orders")
        .description("Serves an API Platform endpoint that answers with the orders fetchOrders returns.")
        .triggers(
            trigger("request", "apiPlatform/v1/newApiRequest")
                .parameters(Map.of("request", Map.of())))
        .tasks(
            task("fetchOrders")
                .label("Fetch Orders")
                .perform(ListOrdersWorkflow::fetchOrders),
            componentTask("respond", "apiPlatform/v1/responseToAPIRequest")
                .label("Respond")
                .description("Answers the API caller with the fetchOrders output.")
                .parameters(
                    Map.of(
                        "responseType", "SUCCESS",
                        "response", Map.of("success", "${fetchOrders}"))));

    private static Object fetchOrders(Map<String, ?> input, TaskContext context) {
        List<Map<String, Integer>> orders = List.of(Map.of("id", 1, "total", 42), Map.of("id", 2, "total", 17));

        context.log(TaskContext.LogLevel.INFO, "Returning " + orders.size() + " orders");

        return Map.of("orders", orders, "count", orders.size());
    }
}
