# Native executable for command-line JGit program (PGM)

JGit PGM, part of the Eclipse [JGit](https://github.com/eclipse/jgit) [project](http://www.eclipse.org/jgit/),
is the stand-alone command-line Java program which provides Git commands similar
to the native [git](https://git-scm.com) commands, to access Git repositories.

Usually distributed as a jar package, JGit [PGM](https://github.com/eclipse/jgit/tree/master/org.eclipse.jgit.pgm)
is also distributed as a stand-alone, self-contained (with all dependencies), executable shell archive,
from Maven Central, like
[this](https://repo1.maven.org/maven2/org/eclipse/jgit/org.eclipse.jgit.pgm/5.11.0.202103091610-r/org.eclipse.jgit.pgm-5.11.0.202103091610-r.sh)
of the 5.11.0 version. This shell archive is then normally renamed to `jgit` locally for ease of typing.

On Linux, the shell archive can be run directly.
On Windows, the shell archive can be run with the `java` command, like:
`java -jar org.eclipse.jgit.pgm-5.11.0.202103091610-r.sh --version`

This project aims to produce platform-specific, native executable `jgit` of the JGit PGM
using the [GraalVM native-image](https://www.graalvm.org/reference-manual/native-image) utility,
complementing the shell archive distribution.

Gradle build script is provided for building the project. The JGit version used here is 5.11.0.

## Caveat

The original org.eclipse.jgit.pgm jar package, as distributed from
[Maven Central](https://repo1.maven.org/maven2/org/eclipse/jgit/org.eclipse.jgit.pgm/5.11.0.202103091610-r/org.eclipse.jgit.pgm-5.11.0.202103091610-r.jar),
can't be used by the
GraalVM native-image utility, as it contains references to AWT/Swing classes which are not (yet)
supported for native image generation.

Instead, a modified org.eclipse.jgit.pgm jar file in the `lib` folder is used in this project,
as detailed in [lib/README.md](lib).

## Build pre-requisites

The [GraalVM native-image](https://www.graalvm.org/reference-manual/native-image) page
shows how to set up GraalVM and its native-image utility for common platforms.
[Gluon](https://gluonhq.com/) also provides some setup [details](https://docs.gluonhq.com/#_platforms)
for GraalVM native-image creation.

This project's Gradle build script uses the [client-gradle-plugin](https://github.com/gluonhq/client-gradle-plugin)
from Gluon to build the native executable from Gradle with GraalVM.

The GraalVM native-image utility will use the configuration files in
`src/main/resources/META-INF/native-image` folder to assist in the native image generation.

## Gradle build tasks

To run the JGit PGM in standard JVM with Gradle, execute the `run` task
with relevant parameters as needed:

	gradlew run --args="--version"
	gradlew run --args="ls-remote <some-repo-url>"
	gradlew run --args="clone <some-repo-url>"
	etc.

To produce a native executable, execute the `nativeBuild` task:

	gradlew nativeBuild

The `nativeBuild` task would take a while to compile the application and link into an executable file.
The resulting `jgit` executable file is:

	build/client/x86_64-linux/jgit

(or if building on a Windows machine:

	build\client\x86_64-windows\jgit.exe

)

which can then be run directly (with relevant parameters):

	./build/client/x86_64-linux/jgit --version
	./build/client/x86_64-linux/jgit ls-remote <some-repo-url>
	./build/client/x86_64-linux/jgit clone <some-repo-url>
	etc.

(or if building on a Windows machine:

	build\client\x86_64-windows\jgit.exe --version
	build\client\x86_64-windows\jgit.exe ls-remote <some-repo-url>
	build\client\x86_64-windows\jgit.exe clone <some-repo-url>
	etc.

)

## Compressed executable

The resulting `jgit` native executable can be further reduced in size via compression,
using the [UPX](https://upx.github.io) utility, as described
[here](https://medium.com/graalvm/compressed-graalvm-native-images-4d233766a214).

As an example, the resulting `jgit.exe` native application file produced in Windows is
normally 82MB in size, but is compressed to 22MB with the UPX command: `upx --best jgit.exe`

