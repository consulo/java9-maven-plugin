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
	public static class Require
	{
		public String module;
		public boolean transitive;
		@Parameter(alias = "static")
		public boolean _static;
	}

	public static class Export
	{
		@Parameter(alias = "package")
		public String _package;
	}

	public String name;

	public boolean open = false;

	@Parameter(alias = "requires")
	public List<Require> requires = new ArrayList<>();

	@Parameter(alias = "exports")
	public List<Export> exports = new ArrayList<>();
}
