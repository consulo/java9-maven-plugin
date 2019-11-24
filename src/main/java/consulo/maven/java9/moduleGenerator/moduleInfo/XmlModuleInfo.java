package consulo.maven.java9.moduleGenerator.moduleInfo;

import org.apache.maven.plugins.annotations.Parameter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author VISTALL
 * @since 2018-07-16
 */
public class XmlModuleInfo implements ModuleInfo
{
	public static class Require implements ModuleInfo.Require
	{
		public String module;
		public boolean transitive;

		private boolean _static;

		public void setStatic(boolean value)
		{
			_static = value;
		}

		@Override
		public String getModule()
		{
			return module;
		}

		@Override
		public boolean isStatic()
		{
			return _static;
		}

		@Override
		public boolean isTransitive()
		{
			return transitive;
		}
	}

	public static class Export implements ModuleInfo.Export
	{
		private String _package;

		@Override
		public String getPackage()
		{
			return _package;
		}

		@Override
		public String[] getModules()
		{
			return new String[0];
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

	@Override
	public List<? extends ModuleInfo.Require> getRequires()
	{
		return requires;
	}

	@Override
	public List<? extends ModuleInfo.Export> getExports()
	{
		return exports;
	}

	@Override
	public List<? extends Use> getUses()
	{
		return Collections.emptyList();
	}

	@Override
	public List<? extends Provider> getProviders()
	{
		return Collections.emptyList();
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public boolean isOpen()
	{
		return open;
	}
}
