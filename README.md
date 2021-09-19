# Native executable for command-line JGit program (PGM)

JGit PGM, part of the Eclipse [JGit](https://git.eclipse.org/c/jgit/jgit.git/) [project](http://www.eclipse.org/jgit/),
is the stand-alone command-line Java program which provides Git commands similar
to the native [git](https://git-scm.com) commands, to access Git repositories.

Usually distributed as a jar package, JGit [PGM](https://git.eclipse.org/c/jgit/jgit.git/tree/org.eclipse.jgit.pgm)
is also distributed as a stand-alone, self-contained (with all dependencies), executable shell archive,
from Maven Central, like
[this](https://repo1.maven.org/maven2/org/eclipse/jgit/org.eclipse.jgit.pgm/5.13.0.202109080827-r/org.eclipse.jgit.pgm-5.13.0.202109080827-r.sh)
of the 5.13.0 version. This shell archive is then normally renamed to `jgit` locally for ease of typing.

On Linux, the shell archive can be run directly.
On Windows, the shell archive can be run with the `java` command, like:
`java -jar org.eclipse.jgit.pgm-5.13.0.202109080827-r.sh --version`

This project aims to produce platform-specific, native executable `jgit` of the JGit PGM
using the [GraalVM native-image](https://www.graalvm.org/reference-manual/native-image) utility,
complementing the shell archive distribution.

Gradle and Maven build scripts are provided for building the project. The JGit version used here is 5.13.0.

## Build pre-requisites

The [GraalVM native-image](https://www.graalvm.org/reference-manual/native-image) page
shows how to set up GraalVM and its native-image utility for common platforms.
[Gluon](https://gluonhq.com/) also provides some setup [details](https://docs.gluonhq.com/#_platforms)
for GraalVM native-image creation.

The GraalVM native-image utility will use the configuration files in
`src/graal-cfg/<platform>/META-INF/native-image` folder to assist in the native image generation.

## Gradle build tasks

To run the JGit PGM in standard JVM with Gradle, execute the `run` task
with relevant parameters as needed:

	gradlew run --args="--version"
	gradlew run --args="ls-remote <some-repo-url>"
	gradlew run --args="clone <some-repo-url>"
	gradlew run --args="glog"
	etc.

To produce a native executable, execute the `nativeCompile` task:

	gradlew nativeCompile

The `nativeCompile` task would take a while to compile the application and link into an executable file.
The resulting `jgit` executable file is:

	build/native-image-linux/jgit

(or if building on a Windows machine, the executable file is:

	build\native-image-windows\jgit.exe

)

which can then be run directly (with relevant parameters):

	./build/native-image-linux/jgit --version
	./build/native-image-linux/jgit ls-remote <some-repo-url>
	./build/native-image-linux/jgit clone <some-repo-url>
	./build/native-image-linux/jgit glog
	etc.

(or if building on a Windows machine:

	build\native-image-windows\jgit.exe --version
	build\native-image-windows\jgit.exe ls-remote <some-repo-url>
	build\native-image-windows\jgit.exe clone <some-repo-url>
	build\native-image-windows\jgit.exe glog
	etc.

)

## Maven build tasks

To compile and run the JGit PGM in standard JVM with Maven, execute the
`compile` then `exec:exec` task with relevant parameters as needed:

	mvnw compile
	mvnw exec:exec -Djgit.args="--version"
	mvnw exec:exec -Djgit.args="\"ls-remote <some-repo-url>\""
	mvnw exec:exec -Djgit.args="\"clone <some-repo-url>\""
	mvnw exec:exec -Djgit.args="glog"
	etc.

To produce a native executable, execute the `package` task for specific platform
profile (e.g. for Linux):

	mvnw package -Pnative-linux

or if building on a Windows machine:

	mvnw package -Pnative-windows

The `package` task would take a while to compile the application and link into an executable file.
The resulting `jgit` executable file is:

	target/native-image-linux/jgit

(or if building on a Windows machine, the executable file is:

	target\native-image-windows\jgit.exe

)

which can then be run directly (with relevant parameters):

	./target/native-image-linux/jgit --version
	./target/native-image-linux/jgit ls-remote <some-repo-url>
	./target/native-image-linux/jgit clone <some-repo-url>
	./target/native-image-linux/jgit glog
	etc.

(or if building on a Windows machine:

	target\native-image-windows\jgit.exe --version
	target\native-image-windows\jgit.exe ls-remote <some-repo-url>
	target\native-image-windows\jgit.exe clone <some-repo-url>
	target\native-image-windows\jgit.exe glog
	etc.

)

## Compressed executable

The resulting `jgit` native executable, whether produced by Gradle or Maven build script,
can be further reduced in size via compression, using the [UPX](https://upx.github.io) utility,
as described [here](https://medium.com/graalvm/compressed-graalvm-native-images-4d233766a214).

As an example, the resulting `jgit.exe` native application file produced in Windows is
normally 75MB in size, but is compressed to 21MB with the UPX command: `upx --best jgit.exe`

