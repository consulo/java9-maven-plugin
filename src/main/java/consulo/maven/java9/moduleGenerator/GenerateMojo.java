package consulo.maven.java9.moduleGenerator;

import java.io.File;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.utils.SourceRoot;
import consulo.maven.java9.moduleGenerator.moduleInfo.ModuleInfo;
import consulo.maven.java9.moduleGenerator.moduleInfo.SourceModuleInfo;
import consulo.maven.java9.moduleGenerator.moduleInfo.XmlModuleInfo;

/**
 * @author VISTALL
 * @since 2018-07-16
 */
public abstract class GenerateMojo extends AbstractMojo
{
	@Parameter(alias = "module")
	private XmlModuleInfo moduleInfo;

	@Parameter(property = "project", defaultValue = "${project}")
	protected MavenProject mavenProject;

	@Override
	public final void execute() throws MojoExecutionException, MojoFailureException
	{
		try
		{
			ModuleInfo[] target = new ModuleInfo[1];

			// src/main/java
			String sourceDirectory = mavenProject.getBuild().getSourceDirectory();

			File sourceDirFile = new File(sourceDirectory);

			if(sourceDirFile.exists())
			{
				File parentDirectory = sourceDirFile.getParentFile();
				File moduleInfoFile = new File(parentDirectory, "module-info.java");
				if(moduleInfoFile.exists())
				{
					SourceRoot root = new SourceRoot(parentDirectory.toPath(), new ParserConfiguration().setLanguageLevel(ParserConfiguration.LanguageLevel.RAW));

					CompilationUnit unit = root.parse("", moduleInfoFile.getName());
					if(unit == null)
					{
						throw new MojoFailureException("Can't parse module-info.java");
					}

					unit.getModule().ifPresent(moduleDeclaration -> target[0] = new SourceModuleInfo(moduleDeclaration));
				}
			}

			if(target[0] == null)
			{
				target[0] = moduleInfo;
			}

			if(target[0] == null)
			{
				throw new MojoFailureException("Module must be set");
			}

			executeImpl(target[0]);
		}
		catch(Exception e)
		{

			getLog().error(e);
		}
	}

	protected boolean isIgnored()
	{
		return false;
	}

	protected boolean isJdk9OrHighter()
	{
		try
		{
			Class.forName("java.lang.Module");
			return true;
		}
		catch(ClassNotFoundException e)
		{
			return false;
		}
	}

	protected abstract void executeImpl(ModuleInfo target) throws Exception;
}
