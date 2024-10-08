/*
 * Copyright 2023-present ByteChef Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.sample.codeworkflow;

import com.bytechef.workflow.ProjectHandler;
import com.bytechef.workflow.definition.ProjectDefinition;
import com.google.auto.service.AutoService;
import org.sample.codeworkflow.workflow.SampleWorkflow1;

import static com.bytechef.workflow.definition.WorkflowDsl.project;
import static org.sample.codeworkflow.constant.SampleConstants.SAMPLE_CODE_PROJECT;

/**
 * @author Ivica Cardic
 */
@AutoService(ProjectHandler.class)
public class SampleProjectHandler implements ProjectHandler {

    private static final ProjectDefinition PROJECT_DEFINITION = project(SAMPLE_CODE_PROJECT)
        .description("Sample code project description.")
        .workflows(SampleWorkflow1.WORKFLOW_DEFINITION);

    @Override
    public ProjectDefinition getDefinition() {
        return PROJECT_DEFINITION;
    }
}
