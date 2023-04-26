package consulo.maven.java9.moduleGenerator;

import consulo.maven.java9.moduleGenerator.moduleInfo.ModuleInfo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.shared.utils.io.IOUtil;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.ModuleVisitor;
import org.objectweb.asm.Opcodes;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;

/**
 * @author VISTALL
 * @since 2018-07-16
 */
@Mojo(name = "generate-binary-module-info", threadSafe = true, defaultPhase = LifecyclePhase.PROCESS_CLASSES)
public class GenerateBinaryMojo extends GenerateMojo
{
	@Override
	protected void executeImpl(ModuleInfo target) throws Exception
	{
		if(isIgnored())
		{
			return;
		}

		getLog().info("Generating module-info.class");

		ClassWriter classWriter = new ClassWriter(0);
		classWriter.visit(Opcodes.V9, Opcodes.ACC_MODULE, "module-info", null, null, null);

		ModuleVisitor moduleVisitor = classWriter.visitModule(target.getName(), target.isOpen() ? Opcodes.ACC_OPEN : 0, null);

		moduleVisitor.visitRequire("java.base", Opcodes.ACC_MANDATED, null);

		for(ModuleInfo.Require require : target.getRequires())
		{
			boolean isTransitive = require.isTransitive();
			boolean isStatic = require.isStatic();

			moduleVisitor.visitRequire(require.getModule(), (isTransitive ? Opcodes.ACC_TRANSITIVE : 0) | (isStatic ? Opcodes.ACC_STATIC_PHASE : 0), null);
		}

		for(ModuleInfo.Export export : target.getExports())
		{
			moduleVisitor.visitExport(export.getPackage().replace(".", "/"), 0, export.getModules());
		}

		for(ModuleInfo.Use use : target.getUses())
		{
			moduleVisitor.visitUse(className(use.getClassName()));
		}

		for(ModuleInfo.Provider provider : target.getProviders())
		{
			moduleVisitor.visitProvide(className(provider.getServiceName()), classNames(provider.getImplNames()));
		}

		moduleVisitor.visitEnd();
		classWriter.visitEnd();

		byte[] bytes = classWriter.toByteArray();

		String outputDirectory = mavenProject.getBuild().getOutputDirectory();

		FileOutputStream out = null;
		try
		{
			out = new FileOutputStream(new File(outputDirectory, "module-info.class"));
			out.write(bytes);
		}
		finally
		{
			IOUtil.close(out);
		}
	}

	private static String className(String name)
	{
		return name.replace(".", "/");
	}

	private static String[] classNames(String... names)
	{
		return Arrays.stream(names).map(GenerateBinaryMojo::className).toArray(String[]::new);
	}

	@Override
	protected boolean isIgnored()
	{
		return isJdk9OrHighter();
	}
}
