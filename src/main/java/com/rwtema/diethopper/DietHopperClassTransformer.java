package com.rwtema.diethopper;

import com.google.common.collect.ImmutableBiMap;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.ClassNode;

import java.util.Objects;

public class DietHopperClassTransformer implements IClassTransformer {
	private static final ImmutableBiMap<String, String> srgNames = ImmutableBiMap.of(
			"getStateForPlacement", "collisionRayTrace",
			"func_180642_a", "func_180636_a");

	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes) {
		if (bytes == null) return null;
		if (!"net.minecraft.block.BlockHopper".equals(transformedName)) {
			return bytes;
		}

		DietHopperCoreMod.logger.trace("Begin transforming hopper");

		ClassNode classNode = new ClassNode();
		ClassReader classReader = new ClassReader(bytes);
		classReader.accept(classNode, 0);

		String methodName = classNode.methods.stream()
				.map(s -> s.name)
				.map(srgNames::get)
				.filter(Objects::nonNull)
				.findAny()
				.orElseThrow(()->new RuntimeException("Unable to find proper method name."));

		MethodVisitor methodVisitor = classNode.visitMethod(0x1, methodName, "(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;)Lnet/minecraft/util/math/RayTraceResult;", null, null);
		Label l0 = new Label();
		methodVisitor.visitLabel(l0);
		methodVisitor.visitLineNumber(0, l0);
		for (int i = 1; i <= 5; i++) {
			methodVisitor.visitVarInsn(Opcodes.ALOAD, i);
		}
		methodVisitor.visitMethodInsn(
				Opcodes.INVOKESTATIC,
				"com/rwtema/diethopper/HopperDietMod",
				"collisionRayTrace",
				"(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;)Lnet/minecraft/util/math/RayTraceResult;"
				, false
		);
		methodVisitor.visitInsn(Opcodes.ARETURN);
		methodVisitor.visitMaxs(5, 6);
		methodVisitor.visitEnd();


		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		classNode.accept(writer);

		byte[] bytes1 = writer.toByteArray();
		DietHopperCoreMod.logger.trace("Finish transforming hopper class");
		return bytes1;
	}
}
