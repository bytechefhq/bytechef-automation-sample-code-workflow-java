# bytechef-automation-sample-code-workflow-java

A sample ByteChef **automation code project** written in Java. It is a plain jar: a class implementing
`com.bytechef.automation.project.ProjectHandler`, registered through `META-INF/services` by `@AutoService`, that
describes the project and its workflows with `ProjectDsl` and `WorkflowDsl`. The server finds the handler with
`ServiceLoader` and runs each task's `perform` itself.

The project, "Sample Code Project Java", has four workflows, one class each under `org.sample.codeworkflow.workflow`:

- `sampleWorkflow1` (`SampleWorkflow1`) takes a `name` input, runs one task, `greet`, that logs and returns
  `Hello, <name>!`, and exposes that as the `greeting` output.
- `summarizeNewEmail` (`SummarizeNewEmailWorkflow`) starts with the Gmail **New Email** polling trigger
  (`googleMail/v1/newEmailPolling`). It runs once for every new email and returns a one-line summary as the `summary`
  output.
- `fetchRecentEmails` (`FetchRecentEmailsWorkflow`) takes an optional `from` input, calls the Gmail **Search Email**
  action and returns the matching messages as the `emails` output.
- `notifySlackOnNewEmail` (`NotifySlackOnNewEmailWorkflow`) starts with the Gmail **New Email** trigger and takes a
  required `channel` input; its options come from Slack's own `channel` input (`componentName`, `componentVersion` and
  `groupName` on the input), so it is picked from the Slack workspace's channels. Its `postToSlack` task calls the Slack
  **Send Channel Message** action, so it declares a `slack` connection and passes its name: pick a Slack connection for
  it in the test configuration or when deploying the project.

### Connections

A task names the connections it uses and calls a component action through one of them:

```java
task("fetchEmails")
    .connections(connection("googleMail", "gmail"))
    .perform(context -> context.component("googleMail", "searchEmail", Map.of("maxResults", 10), "gmail"));
```

The declared connection, and the Gmail trigger, show up in the workflow's test configuration and in the project
deployment like any other workflow connection: pick a Gmail connection for `gmail` there. Nothing in the source names a
specific account.

Inside `perform`, `context.input()` returns the workflow inputs plus the output of every task that already ran (under
the task's name), `context.input("name")` returns one of them, and `context.log(level, message)` writes to the
execution log.

## Build

Requires JDK 25 (Gradle's toolchain selects it).

```bash
./gradlew build
```

The jar to deploy is `build/libs/sample-code-project-java-1.0.0.jar`.

`libs/` holds the ByteChef SDK jars the code compiles against: `project-api-1.0.jar` and `workflow-api-1.0.jar`. They
are `compileOnly` - the server provides them at runtime, so they must not be packed into your jar. To refresh them, run
`./gradlew :sdks:backend:java:workflow-api:jar :sdks:backend:java:automation:project-api:jar` in the ByteChef repository
and copy the jars from each module's `build/libs/`.

## Deploy

Uploading a jar requires `bytechef.workflow.code-workflow.java-enabled: true` on the server (the default). When it is
off, the import dialog only offers `.js` and `.py` files and the API rejects jars.

Deploying a project with the same name again deploys a new version of that project instead of creating a second one.

### From the UI

Automation → **Projects** → the arrow next to **New Project** → **Import Code Project**, then pick the jar.

### Through the public API

`POST /api/automation/v1/projects/deploy` with a `multipart/form-data` body:

| Part | Value |
|------|-------|
| `workspaceId` (query parameter) | The workspace the project belongs to. Send it - the upload fails without it. |
| `projectFile` (file part) | The jar. The file name must end in `.jar`; the server picks the language from the extension. |

Authenticate with `Authorization: Bearer <API key>`, using a workspace API key (Settings → Current Workspace →
**API Keys**) that belongs to an administrator - deploying requires the admin role. The key is looked up in the
environment named by the `X-ENVIRONMENT` header (`DEVELOPMENT`, `STAGING` or `PRODUCTION`; `PRODUCTION` when the
header is absent), so send the environment the key was created in. A successful deploy returns `204 No Content`.

```bash
curl -X POST "$BYTECHEF_URL/api/automation/v1/projects/deploy?workspaceId=1" \
  -H "Authorization: Bearer $BYTECHEF_API_KEY" \
  -H "X-ENVIRONMENT: DEVELOPMENT" \
  -F "projectFile=@build/libs/sample-code-project-java-1.0.0.jar"
```

`BYTECHEF_URL` is the server's base URL, for example `http://localhost:9555` for a local development server.
