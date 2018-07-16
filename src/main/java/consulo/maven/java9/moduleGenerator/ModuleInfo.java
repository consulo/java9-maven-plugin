package consulo.maven.java9.moduleGenerator;

import java.util.ArrayList;
import java.util.List;

import org.apache.maven.plugins.annotations.Parameter;

/**
 * @author VISTALL
 * @since 2018-07-16
 */
public class ModuleInfo
{
	public static class Requires
	{
		public String name;
		public boolean transitive;
		@Parameter(alias = "static")
		public boolean _static;
	}

	public String name;

	public boolean open = false;

	@Parameter(alias = "requires")
	public List<Requires> requires = new ArrayList<>();
}
