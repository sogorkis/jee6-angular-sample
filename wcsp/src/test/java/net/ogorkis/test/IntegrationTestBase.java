package net.ogorkis.test;

import net.ogorkis.util.Resources;
import org.apache.log4j.spi.LoggerFactory;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;

import java.io.File;


public class IntegrationTestBase {

    public static WebArchive createTestArchive() {
        MavenDependencyResolver resolver = DependencyResolvers
                .use(MavenDependencyResolver.class)
                .loadMetadataFromPom("pom.xml");
        File[] libs = resolver
                .artifact("com.google.guava:guava")
                .artifact("joda-time:joda-time")
                .artifact("org.jadira.usertype:usertype.jodatime")
                .artifact("org.hibernate:hibernate-annotations")
                .resolveAsFiles();

        return ShrinkWrap.create(WebArchive.class, "test.war")
                .addAsLibraries(libs)
                .addClasses(LoggerFactory.class, Resources.class)
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    public static WebArchive createTestArchiveWithPersistence() {
        return createTestArchive()
                .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource("test-ds.xml");
    }

}
