package com.github.chirontt.jgit.graalvm;

import java.security.Security;

import org.graalvm.nativeimage.hosted.Feature;
import org.graalvm.nativeimage.hosted.RuntimeClassInitialization;

import net.i2p.crypto.eddsa.EdDSASecurityProvider;

public class EdDSAFeature implements Feature {

    @Override
    public void afterRegistration(AfterRegistrationAccess access) {
        //net.i2p.crypto.eddsa packages need be initialized at build time...
        RuntimeClassInitialization.initializeAtBuildTime("net.i2p.crypto.eddsa");
        //...in order to register its security provider
        Security.addProvider(new EdDSASecurityProvider());
    }

}
