/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openbakery

import org.gradle.api.logging.LogLevel
import org.gradle.api.tasks.TaskAction
import org.gradle.logging.ProgressLogger
import org.gradle.logging.ProgressLoggerFactory
import org.gradle.logging.StyledTextOutput
import org.gradle.logging.StyledTextOutputFactory
import org.openbakery.configuration.XcodeConfig
import org.openbakery.output.XcodeBuildOutputAppender

class XcodeBuildTask extends AbstractXcodeBuildTask {

	XcodeBuildTask() {
		super()

		dependsOn(
						XcodePlugin.INFOPLIST_MODIFY_TASK_NAME,
						XcodePlugin.KEYCHAIN_CREATE_TASK_NAME,
						XcodePlugin.PROVISIONING_INSTALL_TASK_NAME,
		)

		finalizedBy(
						XcodePlugin.KEYCHAIN_REMOVE_SEARCH_LIST_TASK_NAME
		)
		this.description = "Builds the Xcode project"
	}

	void executeTask() {
		// apply the missing values from the global build spec

		if (buildSpec.scheme == null && buildSpec.target == null) {
			throw new IllegalArgumentException("No 'scheme' or 'target' specified, so do not know what to build");
		}

		def commandList = createCommandList()

		StyledTextOutput output = getServices().get(StyledTextOutputFactory.class).create(XcodeBuildTask.class, LogLevel.LIFECYCLE);
		Map<String, String> environment = buildSpec.environment

		if (!project.getBuildDir().exists()) {
			project.getBuildDir().mkdirs()
		}
		File outputFile = new File(project.getBuildDir(), "xcodebuild-output.txt")
		commandRunner.setOutputFile(outputFile)
		try {

			ProgressLoggerFactory progressLoggerFactory = getServices().get(ProgressLoggerFactory.class);
			ProgressLogger progressLogger = progressLoggerFactory.newOperation(XcodeBuildTask.class).start("XcodeBuildTask", "XcodeBuildTask");

			commandRunner.run("${project.projectDir.absolutePath}", commandList, environment, new XcodeBuildOutputAppender(progressLogger, output))
		} catch (CommandRunnerException ex) {
			logger.lifecycle(getFailureFromLog(outputFile))
		}

		logger.lifecycle("Done")
	}

}
