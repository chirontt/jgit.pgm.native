# Repackaging of org.eclipse.jgit.pgm jar for native image

GraalVM native-image build doesn't (yet?) support AWT/Swing classes for now,
so all references to AWT/Swing classes have to be eliminated from the code base
to support native-image compiling.

The original org.eclipse.jgit.pgm
[package](https://repo1.maven.org/maven2/org/eclipse/jgit/org.eclipse.jgit.pgm/5.10.0.202012080955-r/org.eclipse.jgit.pgm-5.10.0.202012080955-r.jar)
contains 2 classes which are referencing AWT/Swing classes:

	org.eclipse.jgit.pgm.Glog
	org.eclipse.jgit.pgm.Main

For those 2 classes, the entire Glog class can be deleted at image build time dynamically
by GraalVM through substitution, but the
[Main](https://github.com/eclipse/jgit/blob/v5.10.0.202012080955-r/org.eclipse.jgit.pgm/src/org/eclipse/jgit/pgm/Main.java) 
class is more troublesome - it contains calls to the JGit UI classes (which have been excluded from the build)
in lines
[129-130](https://github.com/eclipse/jgit/blob/v5.10.0.202012080955-r/org.eclipse.jgit.pgm/src/org/eclipse/jgit/pgm/Main.java#L129):

	AwtAuthenticator.install();
	AwtCredentialsProvider.install();

It's quite difficult to patch this Main class dynamically, via GraalVM's substitution technique,
to remove just those 2 calls. In the end, the simple solution was: this
[Main](https://github.com/eclipse/jgit/blob/v5.10.0.202012080955-r/org.eclipse.jgit.pgm/src/org/eclipse/jgit/pgm/Main.java) 
source code was copied over to become a new class [NativeMain.java](NativeMain.java),
with those 2 lines commented out, and then compiled.

The resulting package, `org.eclipse.jgit.pgm-5.10.0.202012080955-r-MODIFIED.jar`,
includes this new NativeMain class in place of the original Main class, and without
the original Glog class either, to make it suitable for GraalVM's native image generation.
