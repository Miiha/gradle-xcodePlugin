package org.openbakery.appstore

import org.apache.commons.io.FileUtils
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.openbakery.CommandRunner
import spock.lang.Specification


/**
 * Created by rene on 08.01.15.
 */
class AppstoreUploadTaskSpecification extends Specification {

	Project project
	AppstoreUploadTask task
	File infoPlist

	CommandRunner commandRunner = Mock(CommandRunner)
	File ipaBundle;

	def setup() {

		File projectDir = new File(System.getProperty("java.io.tmpdir"), "gradle-xcodebuild")

		project = ProjectBuilder.builder().withProjectDir(projectDir).build()
		project.buildDir = new File(projectDir, 'build').absoluteFile
		project.apply plugin: org.openbakery.XcodePlugin

		project.xcodebuild.xcodePath = "/Application/Xcode.app"

		task = project.tasks.findByName('appstoreUpload')
		task.commandRunner = commandRunner

		ipaBundle = new File(project.getBuildDir(), "package/Test.ipa")
		FileUtils.writeStringToFile(ipaBundle, "dummy")

	}

	def cleanup() {
		FileUtils.deleteDirectory(project.projectDir)
	}

	def "ipa missing"() {
		given:
		FileUtils.deleteDirectory(project.projectDir)

		when:
		task.upload()

		then:
		thrown(IllegalStateException.class)

	}


	def "test upload"() {
		given:
		project.appstore.username = "me@example.com"
		project.appstore.password = "1234"

		when:
		task.upload()

		then:
		1 * commandRunner.run(["/Application/Xcode.app/Contents/Applications/Application Loader.app/Contents/Frameworks/ITunesSoftwareService.framework/Support/altool",
													 "--upload-app",
													 "--username",
													 "me@example.com",
													 "--password",
													 "1234",
													 "--file",
													 ipaBundle.absolutePath], _)

	}
}
