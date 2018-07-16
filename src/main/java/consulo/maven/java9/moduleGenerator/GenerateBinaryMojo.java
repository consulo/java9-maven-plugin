package consulo.maven.java9.moduleGenerator;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.codehaus.plexus.util.IOUtil;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.ModuleVisitor;
import org.objectweb.asm.Opcodes;

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
		if(isJdk9OrHighter())
		{
			return;
		}

		getLog().info("Generating module-info.class");

		ClassWriter classWriter = new ClassWriter(0);
		classWriter.visit(Opcodes.V9, Opcodes.ACC_MODULE, "module-info", null, null, null);

		ModuleVisitor moduleVisitor = classWriter.visitModule(target.name, target.open ? Opcodes.ACC_OPEN : 0, null);

		moduleVisitor.visitRequire("java.base", Opcodes.ACC_MANDATED, null);

		for(ModuleInfo.Require require : target.requires)
		{
			boolean isTransitive = require.transitive;
			boolean isStatic = require._static;

			moduleVisitor.visitRequire(require.module, (isTransitive ? Opcodes.ACC_TRANSITIVE : 0) | (isStatic ? Opcodes.ACC_STATIC_PHASE : 0), null);
		}

		for(ModuleInfo.Export export : target.exports)
		{
			moduleVisitor.visitExport(export._package.replace(".", "/"), 0);
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
}
