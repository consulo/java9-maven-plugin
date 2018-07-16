package consulo.maven.java9.moduleGenerator;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.IOUtil;

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

		if(!isJdk9OrHighter())
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

	private String generateModuleInfo(ModuleInfo target)
	{
		StringBuilder builder = new StringBuilder();

		if(target.open)
		{
			builder.append("open ");
		}

		builder.append("module ").append(target.name).append(" {\n");

		List<ModuleInfo.Requires> requires = target.requires;
		for(ModuleInfo.Requires require : requires)
		{
			builder.append("    ");
			builder.append("requires ");
			if(require.transitive)
			{
				builder.append("transitive ");
			}

			if(require._static)
			{
				builder.append("static ");
			}
			builder.append(require.name);
			builder.append(";\n");
		}

		builder.append("\n}");
		return builder.toString();
	}
}