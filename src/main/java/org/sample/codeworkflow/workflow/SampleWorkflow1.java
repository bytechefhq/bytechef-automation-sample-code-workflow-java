package org.sample.codeworkflow.workflow;

import com.bytechef.workflow.definition.WorkflowDsl.ModifiableWorkflowDefinition;

import static com.bytechef.workflow.definition.WorkflowDsl.task;
import static com.bytechef.workflow.definition.WorkflowDsl.workflow;

public class SampleWorkflow1 {

    public static final ModifiableWorkflowDefinition WORKFLOW_DEFINITION = workflow("sampleWorkflow1")
        .label("Sample workflow 1")
        .description("Sample workflow 1 description.")
        .tasks(
            task("sampleTask1")
                .description("Sample tas1k description.")
                .perform(SampleWorkflow1::perform)
        );

    private static Object perform() {
        System.out.println("Sample from code workflow java task1!!!");

        return "Sample from code workflow java task1!!!";
    }
}
