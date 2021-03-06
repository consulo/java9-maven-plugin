package consulo.maven.java9.moduleGenerator.moduleInfo;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.modules.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author VISTALL
 * @since 2018-09-29
 */
public class SourceModuleInfo implements ModuleInfo
{
	private static class RequireImpl implements Require
	{
		private final ModuleRequiresStmt myModuleRequiresStmt;

		RequireImpl(ModuleRequiresStmt moduleRequiresStmt)
		{
			myModuleRequiresStmt = moduleRequiresStmt;
		}

		@Override
		public String getModule()
		{
			return myModuleRequiresStmt.getNameAsString();
		}

		@Override
		public boolean isStatic()
		{
			return myModuleRequiresStmt.isStatic();
		}

		@Override
		public boolean isTransitive()
		{
			return myModuleRequiresStmt.isTransitive();
		}
	}

	private static class ExportImpl implements Export
	{
		private ModuleExportsStmt myModuleExportsStmt;

		private ExportImpl(ModuleExportsStmt moduleExportsStmt)
		{
			myModuleExportsStmt = moduleExportsStmt;
		}

		@Override
		public String getPackage()
		{
			return myModuleExportsStmt.getNameAsString();
		}

		@Override
		public String[] getModules()
		{
			NodeList<Name> moduleNames = myModuleExportsStmt.getModuleNames();
			String[] modules = new String[moduleNames.size()];
			for(int i = 0; i < modules.length; i++)
			{
				modules[i] = moduleNames.get(i).asString();
			}
			return modules;
		}
	}

	private static class UseImpl implements Use
	{
		private ModuleUsesStmt myModuleUsesStmt;

		private UseImpl(ModuleUsesStmt moduleUsesStmt)
		{
			myModuleUsesStmt = moduleUsesStmt;
		}

		@Override
		public String getClassName()
		{
			return myModuleUsesStmt.getName().asString();
		}
	}

	private static class ProviderImpl implements Provider
	{
		private ModuleProvidesStmt myModuleProvidesStmt;

		private ProviderImpl(ModuleProvidesStmt moduleProvidesStmt)
		{
			myModuleProvidesStmt = moduleProvidesStmt;
		}

		@Override
		public String getServiceName()
		{
			return myModuleProvidesStmt.getName().asString();
		}

		@Override
		public String[] getImplNames()
		{
			NodeList<Name> moduleNames = myModuleProvidesStmt.getWith();
			String[] modules = new String[moduleNames.size()];
			for(int i = 0; i < modules.length; i++)
			{
				modules[i] = moduleNames.get(i).asString();
			}
			return modules;
		}
	}

	private ModuleDeclaration myModuleDeclaration;

	public SourceModuleInfo(ModuleDeclaration moduleDeclaration)
	{
		myModuleDeclaration = moduleDeclaration;
	}

	@Override
	public List<? extends Require> getRequires()
	{
		List<Require> list = new ArrayList<>();
		for(ModuleStmt moduleStmt : myModuleDeclaration.getModuleStmts())
		{
			if(moduleStmt instanceof ModuleRequiresStmt)
			{
				list.add(new RequireImpl((ModuleRequiresStmt) moduleStmt));
			}
		}
		return list;
	}

	@Override
	public List<? extends Export> getExports()
	{
		List<Export> list = new ArrayList<>();
		for(ModuleStmt moduleStmt : myModuleDeclaration.getModuleStmts())
		{
			if(moduleStmt instanceof ModuleExportsStmt)
			{
				list.add(new ExportImpl((ModuleExportsStmt) moduleStmt));
			}
		}
		return list;
	}

	@Override
	public List<? extends Use> getUses()
	{
		List<Use> list = new ArrayList<>();
		for(ModuleStmt moduleStmt : myModuleDeclaration.getModuleStmts())
		{
			if(moduleStmt instanceof ModuleUsesStmt)
			{
				list.add(new UseImpl((ModuleUsesStmt) moduleStmt));
			}
		}
		return list;
	}

	@Override
	public List<? extends Provider> getProviders()
	{
		List<Provider> providers = new ArrayList<>();
		for(ModuleStmt moduleStmt : myModuleDeclaration.getModuleStmts())
		{
			if(moduleStmt instanceof ModuleProvidesStmt)
			{
				providers.add(new ProviderImpl((ModuleProvidesStmt) moduleStmt));
			}
		}
		return providers;
	}

	@Override
	public String getName()
	{
		return myModuleDeclaration.getNameAsString();
	}

	@Override
	public boolean isOpen()
	{
		return myModuleDeclaration.isOpen();
	}
}
