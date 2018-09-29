package consulo.maven.java9.moduleGenerator;

import java.io.File;
import java.io.FileWriter;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.IOUtil;
import consulo.maven.java9.moduleGenerator.moduleInfo.ModuleInfo;

/**
 * @author VISTALL
 * @since 2018-07-16
 */
@Mojo(name = "generate-source-module-info", threadSafe = true, defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class GenerateSourceMojo extends GenerateMojo
{
	@Override
	public void executeImpl(ModuleInfo target) throws Exception
	{
		// remove source module info if exists - do not provide compile error if we switched to java 8 from java 9
		String outputDirectory = mavenProject.getBuild().getDirectory();
		File outputSourceDirectory = new File(outputDirectory, "generated-sources/module-info");
		if(outputSourceDirectory.exists())
		{
			FileUtils.deleteDirectory(outputSourceDirectory);
		}

		if(isIgnored())
		{
			return;
		}

		getLog().info("Generating module-info.java");

		String text = generateModuleInfo(target);

		FileWriter out = null;
		try
		{
			outputSourceDirectory.mkdirs();
			out = new FileWriter(new File(outputSourceDirectory, "module-info.java"), false);
			out.write(text);

			mavenProject.addCompileSourceRoot(outputSourceDirectory.getPath());
		}
		finally
		{
			IOUtil.close(out);
		}
	}

	@Override
	protected boolean isIgnored()
	{
		return !isJdk9OrHighter();
	}

	public static String generateModuleInfo(ModuleInfo target)
	{
		StringBuilder builder = new StringBuilder();

		if(target.isOpen())
		{
			builder.append("open ");
		}

		builder.append("module ").append(target.getName()).append(" {\n");

		for(ModuleInfo.Require require : target.getRequires())
		{
			builder.append("    ");
			builder.append("requires ");
			if(require.isTransitive())
			{
				builder.append("transitive ");
			}

			if(require.isStatic())
			{
				builder.append("static ");
			}
			builder.append(require.getModule());
			builder.append(";\n");
		}

		for(ModuleInfo.Export export : target.getExports())
		{
			builder.append("    ");
			builder.append("exports ");
			builder.append(export.getPackage());
			builder.append(";\n");
		}

		builder.append("\n}");
		return builder.toString();
	}
}
