package com.plugin.jar.generator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.maven.archiver.MavenArchiveConfiguration;
import org.apache.maven.archiver.MavenArchiver;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.FileSet;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;
import org.codehaus.plexus.archiver.Archiver;
import org.codehaus.plexus.archiver.jar.JarArchiver;


@Mojo(name="generate",defaultPhase = LifecyclePhase.COMPILE, requiresProject = true, threadSafe = true,
requiresDependencyResolution = ResolutionScope.RUNTIME)
public class GeneratorApplication   extends AbstractMojo{

	private static final String[] DEFAULT_INCLUDES = new String[] { "**/**" };


	@Parameter( defaultValue = "${session}", readonly = true, required = true )
    private MavenSession session;

	@Parameter( defaultValue = "${project}", readonly = true, required = true )
	private MavenProject project;

	@Parameter(property = "contollerPackage" , defaultValue="com.application.project.myapi")
	private String controllerPackage;

	@Parameter(property = "controllerpath",  defaultValue="\\com\\application\\project\\myapi")
	private String controllerPath;

	@Component
	Map<String, Archiver> archivers;

	@Component
    private MavenProjectHelper projectHelper;

	@Parameter( defaultValue = "${project.build.directory}", required = true )
    private File outputDirectory;

	@Parameter( defaultValue = "${project.build.outputDirectory}", required = true )
    private File classesDirectory;



	@Parameter( defaultValue = "${project.build.outputTimestamp}" )
    private String outputTimestamp;


	//target folder where you want to save jar
	private static final String TARGET_FOLDER = "dist/";
	// path of the installed maven
	// private static final String MAVEN_RUNNER_PATH = "D:/workplace/tools/apache-maven-3.8.6/bin/mvn.cmd";

	static Map<String, String> controllers = new HashMap<>();

	@Parameter
	private String classifier;

	@Parameter
    private MavenArchiveConfiguration archive = new MavenArchiveConfiguration();

	@Override
	public void execute(){

		
		String path =project.getBuild().getOutputDirectory()+controllerPath;
		
		;
		File[] files = new File(path).listFiles();
		createTargetFolder();
		filesIteration(files);
	}

	private void filesIteration(File[] files){

		for(File file:files){
			if(file.isDirectory()){
				filesIteration(file.listFiles());
			}else{
				String fileName = getFileName(file.getName());
				getLog().info("controllersName:"+fileName);

				List<String> exclusions = getExlcusions(file.getName(), files);

				getLog().info("Excluded Class:"+exclusions.toString());


				try {
					createArchive(fileName,exclusions);
					File sourceFile= new File(outputDirectory+"/"+fileName+".jar");
					copyFile(sourceFile);
				} catch (Exception e) {
					getLog().info("Exception:"+e.getMessage());
					e.printStackTrace();
				}


			}

		}
	}

	private String getFileName(String filename){
        return FilenameUtils.removeExtension(filename);
    }

	protected List<String> getExlcusions(String controllerName,File[] files) 	
	{
		
		List<String> exc = new ArrayList<>();
		for (File file : files) {
			if(!file.isDirectory()){
				if(!controllerName.equalsIgnoreCase(file.getName())) {
					exc.add(file.getName());
				}
			}
			
		}
		

		return exc;
	}

	protected File getJarFile( File basedir, String resultFinalName, String classifier )
    {
        if ( basedir == null )
        {
            throw new IllegalArgumentException( "basedir is not allowed to be null" );
        }
        if ( resultFinalName == null )
        {
            throw new IllegalArgumentException( "finalName is not allowed to be null" );
        }

        String fileName;
        if ( hasClassifier() )
        {
            fileName = resultFinalName + "-" + classifier + ".jar";
        }
        else
        {
            fileName = resultFinalName + ".jar";
        }

        return new File( basedir, fileName );
    }

	public File createArchive(String finalName, List<String> excluded)
        throws MojoExecutionException, IOException
    {
		

        File jarFile = getJarFile( outputDirectory, finalName, classifier );

        // FileSetManager fileSetManager = new FileSetManager();
        FileSet jarContentFileSet = new FileSet();
        jarContentFileSet.setDirectory( classesDirectory.getAbsolutePath() );
        jarContentFileSet.setIncludes( Arrays.asList(DEFAULT_INCLUDES) );
        jarContentFileSet.setExcludes( excluded);

		// JarOutputStream jarOutputStream = jartool.openJar(jarFile.getAbsolutePath());

		// jartool.addFile(jarOutputStream, finalName, finalName);


        // boolean containsModuleDescriptor = false;
        // String[] includedFiles = fileSetManager.getIncludedFiles( jarContentFileSet );
        // for ( String includedFile : includedFiles )
        // {
        //     if ( includedFile.endsWith( MODULE_DESCRIPTOR_FILE_NAME ) )
        //     {
        //         containsModuleDescriptor = true;
        //         break;
        //     }
        // }

        String archiverName ="jar";
        MavenArchiver archiver = new MavenArchiver();
		
        // archiver.setCreatedBy( "Generata Jars Plugin", "com.plugin.jar", "generator-maven-plugin" );
        archiver.setArchiver( (JarArchiver) archivers.get( archiverName ) );
        archiver.setOutputFile( jarFile );

        // configure for Reproducible Builds based on outputTimestamp value
        // archiver.configureReproducibleBuild( outputTimestamp );

        archive.setForced( true );

		

        try
        {
            File contentDirectory = classesDirectory;
            if ( !contentDirectory.exists() )
            {
				getLog().warn( "Not able to create the jar" );
            }
            else
            {
				String[] strarray = new String[excluded.size()];

                archiver.getArchiver().addDirectory( contentDirectory, DEFAULT_INCLUDES, excluded.toArray(strarray ));
            }

            archiver.createArchive( session, project, archive );

            return jarFile;
        }
        catch ( Exception e )
        {
			getLog().warn( "Exception!!! Not able to create the jar" );
            throw new MojoExecutionException( "Error assembling JAR", e );
        }
    }
	

	// public static void build(String targetBuildName, String exclusions)  throws Exception {

	// 	// list of controllers to be exclued
		

	// 	// prepare maven command
	// 	// String command = "mvn.cmd clean install -Dfinal-name=" + targetBuildName + " -Dexttt=" 
	// 	// 		+ exclusions;

	// 	String command ="git status";
		
	// 	System.out.println(command);

	// 	// run maven command
	// 	Process pr = Runtime.getRuntime().exec(command);

	// 	StringBuilder builder = new StringBuilder();

	// 	Executors.newSingleThreadExecutor().submit(()->
	// 	new BufferedReader(new InputStreamReader(pr.getInputStream()))
	// 	.lines().forEach(builder::append));

	// 	System.out.println("Process waitFor: "+pr.waitFor());

	// 	String error =pr.errorReader().readLine();
	// 	System.out.println("Error in generation: "+error);


	// 	if(pr.waitFor()!=0){
	// 		Thread.sleep(5000);
	// 		pr.destroy();
	// 	}

	// 	if(error!=null){
	// 		pr.destroy();
	// 	}



	// 	// int exitCode = pr.waitFor();
	// 	// if (exitCode != 0) {
    //     //     throw new MojoExecutionException("Execution of command '" + command 
    //     //         + "' failed with exit code: " + exitCode);
    //     // }
	// 	System.out.println("Output of the command: "+builder);
	// }
	protected boolean hasClassifier()
    {
        return classifier != null && classifier.trim().length() > 0;
    }

	private static void copyFile(File sourceFile)
	        throws IOException {
		
		String dest= TARGET_FOLDER+ sourceFile.getName();
	    if (!sourceFile.exists()) {
	        return;
	    }
	    sourceFile.renameTo(new File(dest));
	}

	public static void createTargetFolder() {
		File folder = new File(TARGET_FOLDER);
		
		if(folder.exists())
			folder.delete();
		
		folder.mkdir();
	}

}
