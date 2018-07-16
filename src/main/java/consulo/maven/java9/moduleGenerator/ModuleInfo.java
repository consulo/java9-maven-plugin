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

		private boolean _static;

		public void setStatic(boolean value)
		{
			_static = value;
		}

		public boolean isStatic()
		{
			return _static;
		}
	}

	public static class Export
	{
		private String _package;

		public String getPackage()
		{
			return _package;
		}

		public void setPackage(String value)
		{
			_package = value;
		}
	}

	public String name;

	public boolean open = false;

	@Parameter(alias = "requires")
	public List<Require> requires = new ArrayList<>();

	@Parameter(alias = "exports")
	public List<Export> exports = new ArrayList<>();
}
