package com.github.chirontt.jgit.graalvm;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.jgit.pgm.Main;

public class NativeMain {

    public static void main(String[] args) throws Exception {
        //temp fix for GraalVM's native image for Windows:
        //AWT/Swing in Windows requires the "java.home" property be set.
        //This is to support the 'jgit glog' command which displays the commit log
        //in a graphical window on screen.
        String javaHome = System.getProperty("java.home");
        if (javaHome == null) {
            //set the "java.home" system property to the directory
            //where the native image resides (along with other .dll files)
            try {
                URI executablePath = NativeMain.class.getProtectionDomain().getCodeSource().getLocation().toURI();
                System.setProperty("java.home", (new File(executablePath)).getParent());
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }

        Main.main(args);
    }
}
