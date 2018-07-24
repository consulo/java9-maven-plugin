package consulo.maven.java9.moduleGenerator;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

/**
 * @author VISTALL
 * @since 2018-07-16
 */
public abstract class GenerateMojo extends AbstractMojo
{
	@Parameter(alias = "module")
	private ModuleInfo moduleInfo;

	@Parameter(property = "project", defaultValue = "${project}")
	protected MavenProject mavenProject;

	@Override
	public final void execute() throws MojoExecutionException, MojoFailureException
	{
		try
		{
			if(moduleInfo == null)
			{
				throw new MojoFailureException("Module must be set");
			}
			executeImpl(moduleInfo);
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
