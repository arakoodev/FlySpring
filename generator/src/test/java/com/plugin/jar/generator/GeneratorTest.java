package com.plugin.jar.generator;

import java.io.File;

import org.apache.maven.plugin.testing.AbstractMojoTestCase;

public class GeneratorTest extends AbstractMojoTestCase{

    private File testPom = new File( getBasedir(), "src/test/resources/unit/jar-basic-test/pom.xml" );

    public void testJarTestEnvironment() throws Exception{
        GeneratorApplication mojo = (GeneratorApplication) lookupMojo( "generate", testPom );

        assertNotNull( mojo );

        assertEquals( "generator-maven-plugin", mojo.getProject().getArtifactId());
    }


    
}
