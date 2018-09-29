package consulo.maven.java9.moduleGenerator.moduleInfo;

import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.ast.modules.ModuleDeclaration;
import com.github.javaparser.ast.modules.ModuleExportsStmt;
import com.github.javaparser.ast.modules.ModuleRequiresStmt;
import com.github.javaparser.ast.modules.ModuleStmt;

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
