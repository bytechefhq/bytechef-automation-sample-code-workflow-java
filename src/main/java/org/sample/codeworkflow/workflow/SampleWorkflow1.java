package org.sample.codeworkflow.workflow;

import static com.bytechef.workflow.definition.WorkflowDsl.input;
import static com.bytechef.workflow.definition.WorkflowDsl.output;
import static com.bytechef.workflow.definition.WorkflowDsl.task;
import static com.bytechef.workflow.definition.WorkflowDsl.workflow;

import com.bytechef.workflow.definition.TaskContext;
import com.bytechef.workflow.definition.WorkflowDsl.ModifiableWorkflowDefinition;

public class SampleWorkflow1 {

    public static final ModifiableWorkflowDefinition WORKFLOW_DEFINITION = workflow("sampleWorkflow1")
        .label("Sample workflow 1")
        .description("Greets the name it is given.")
        .inputs(
            input("name")
                .label("Name")
                .type("STRING")
                .required(true))
        .outputs(
            output("greeting")
                .task("greet"))
        .tasks(
            task("greet")
                .label("Greet")
                .description("Builds a greeting from the name input.")
                .perform(SampleWorkflow1::greet));

    private static Object greet(TaskContext context) {
        Object name = context.input("name");

        context.log(TaskContext.LogLevel.INFO, "Greeting " + name);

        return "Hello, " + name + "!";
    }
}
