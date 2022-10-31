package com.github.chirontt.jgit.graalvm;

import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.graalvm.nativeimage.hosted.Feature;
import org.graalvm.nativeimage.hosted.RuntimeClassInitialization;

/**
 * derived from
 * https://github.com/micronaut-projects/micronaut-oracle-cloud/pull/17
 */
public class BouncyCastleFeature implements Feature {

    @Override
    public void afterRegistration(AfterRegistrationAccess access) {
        //org.bouncycastle packages need be initialized at build time...
        RuntimeClassInitialization.initializeAtBuildTime("org.bouncycastle");
        //...in order to register its security provider
        Security.addProvider(new BouncyCastleProvider());
    }

}
