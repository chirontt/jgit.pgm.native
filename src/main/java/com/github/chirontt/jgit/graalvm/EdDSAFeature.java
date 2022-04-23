package com.github.chirontt.jgit.graalvm;

import java.security.Security;

import org.graalvm.nativeimage.hosted.Feature;
import org.graalvm.nativeimage.hosted.RuntimeClassInitialization;

import com.oracle.svm.core.annotate.AutomaticFeature;

import net.i2p.crypto.eddsa.EdDSASecurityProvider;

@AutomaticFeature
public class EdDSAFeature implements Feature {

    @Override
    public void afterRegistration(AfterRegistrationAccess access) {
        //net.i2p.crypto.eddsa packages need be initialized at build time...
        RuntimeClassInitialization.initializeAtBuildTime("net.i2p.crypto.eddsa");
        //...in order to register its security provider
        Security.addProvider(new EdDSASecurityProvider());
    }

}
