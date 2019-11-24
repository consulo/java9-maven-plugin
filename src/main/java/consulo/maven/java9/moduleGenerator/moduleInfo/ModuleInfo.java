package consulo.maven.java9.moduleGenerator.moduleInfo;

import java.util.List;

/**
 * @author VISTALL
 * @since 2018-09-29
 */
public interface ModuleInfo
{
	interface Require
	{
		String getModule();

		boolean isStatic();

		boolean isTransitive();
	}

	interface Export
	{
		String getPackage();

		String[] getModules();
	}

	interface Use
	{
		String getClassName();
	}

	interface Provider
	{
		String getServiceName();

		String[] getImplNames();
	}

	List<? extends Require> getRequires();

	List<? extends Export> getExports();

	List<? extends Use> getUses();

	List<? extends Provider> getProviders();

	String getName();

	boolean isOpen();
}
