package consulo.maven.java9.moduleGenerator;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

/**
 * @author VISTALL
 * @since 2018-07-24
 */
@Mojo(name = "generate-source-module-info-nocheck", threadSafe = true, defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class GenerateSourceMojoNoCheck extends GenerateSourceMojo
{
	@Override
	protected boolean isIgnored()
	{
		return false;
	}
}
