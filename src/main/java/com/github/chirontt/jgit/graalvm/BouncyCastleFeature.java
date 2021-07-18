package com.github.chirontt.jgit.graalvm;

import org.bouncycastle.crypto.CryptoServicesRegistrar;
import org.bouncycastle.jcajce.provider.drbg.DRBG;
import org.bouncycastle.jcajce.provider.drbg.DRBG.Default;
import org.bouncycastle.jcajce.provider.drbg.DRBG.NonceAndIV;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.graalvm.nativeimage.ImageSingletons;
import org.graalvm.nativeimage.hosted.Feature;
import org.graalvm.nativeimage.hosted.RuntimeClassInitialization;
import org.graalvm.nativeimage.impl.RuntimeClassInitializationSupport;

import com.oracle.svm.core.annotate.AutomaticFeature;

import java.security.Security;

/**
 * derived from
 * https://github.com/micronaut-projects/micronaut-oracle-cloud/blob/master/oraclecloud-sdk/src/main/java/io/micronaut/oraclecloud/clients/BouncyCastleFeature.java
 */
@AutomaticFeature
public class BouncyCastleFeature implements Feature {

    @Override
    public void afterRegistration(AfterRegistrationAccess access) {
        //org.bouncycastle packages need be initialized at build time...
        RuntimeClassInitialization.initializeAtBuildTime("org.bouncycastle");
        //...in order to register its security provider
        Security.addProvider(new BouncyCastleProvider());
    }

    @Override
    public void duringSetup(DuringSetupAccess access) {
        //programmatically re-run class initialization at runtime
        //(equivalent to the (deprecated) --rerun-class-initialization-at-runtime=
        //command-line option for GraalVM's native-image command)
        RuntimeClassInitializationSupport rci = ImageSingletons.lookup(RuntimeClassInitializationSupport.class);
        //all org.bouncycastle packages are initialized at build time,
        //but some specific classes need be re-intialized at runtime
        //due to static SecureRandom seeding
        rci.rerunInitialization(CryptoServicesRegistrar.class,
                                "See https://github.com/micronaut-projects/micronaut-oracle-cloud/pull/17#discussion_r472955378");
        rci.rerunInitialization(DRBG.Default.class,
                                "See https://github.com/micronaut-projects/micronaut-oracle-cloud/pull/17#discussion_r472955378");
        rci.rerunInitialization(DRBG.NonceAndIV.class,
                                "See https://github.com/micronaut-projects/micronaut-oracle-cloud/pull/17#discussion_r472955378");
    }

}
