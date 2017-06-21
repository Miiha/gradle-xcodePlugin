package org.openbakery.simulators

import org.openbakery.xcode.Type
import org.openbakery.xcode.Version
import spock.lang.Specification

class SimulatorRuntimeTest extends Specification {


	def "parse iOS"() {

		when:
		SimulatorRuntime runtime = new SimulatorRuntime("iOS 8.4 (8.4 - 12H141) (com.apple.CoreSimulator.SimRuntime.iOS-8-4)")

		then:
		runtime.name == "iOS 8.4"
		runtime.version == new Version("8.4")
		runtime.version.toString() == "8.4"
		runtime.type == Type.iOS
		runtime.identifier == "com.apple.CoreSimulator.SimRuntime.iOS-8-4"
	}

	def "parse tvOS"() {

		when:
		SimulatorRuntime runtime = new SimulatorRuntime("tvOS 9.0 (9.0 - 13T5347l) (com.apple.CoreSimulator.SimRuntime.tvOS-9-0)")

		then:
		runtime.version == new Version("9.0")
		runtime.type == Type.tvOS
		runtime.identifier == "com.apple.CoreSimulator.SimRuntime.tvOS-9-0"
	}

	def "parse watchOS"() {

		when:
		SimulatorRuntime runtime = new SimulatorRuntime("watchOS 2.0 (2.0 - 13S343) (com.apple.CoreSimulator.SimRuntime.watchOS-2-0)")

		then:
		runtime.version == new Version("2.0")
		runtime.type == Type.watchOS
		runtime.identifier == "com.apple.CoreSimulator.SimRuntime.watchOS-2-0"
	}

	def "parse iOS 11"() {
		when:
		SimulatorRuntime runtime = new SimulatorRuntime("iOS 11.0 (11.0 - 15A5278f) - com.apple.CoreSimulator.SimRuntime.iOS-11-0")

		then:
		runtime.version == new Version("11.0")
		runtime.type == Type.iOS
		runtime.identifier == "com.apple.CoreSimulator.SimRuntime.iOS-11-0"
	}
}
