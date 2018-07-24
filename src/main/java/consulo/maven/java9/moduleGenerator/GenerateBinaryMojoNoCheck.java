package consulo.maven.java9.moduleGenerator;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

/**
 * @author VISTALL
 * @since 2018-07-24
 */
@Mojo(name = "generate-binary-module-info-nocheck", threadSafe = true, defaultPhase = LifecyclePhase.PROCESS_CLASSES)
public class GenerateBinaryMojoNoCheck extends GenerateBinaryMojo
{
	@Override
	protected boolean isIgnored()
	{
		return false;
	}
}
