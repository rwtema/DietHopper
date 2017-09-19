package com.rwtema.diethopper;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.BlockHopper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mod(modid = DietHopperMod.MODID, version = DietHopperMod.VERSION)
public class DietHopperMod {
	public static final String MODID = "diethopper";
	public static final String VERSION = "1.0";

	private static final EnumMap<EnumFacing, List<AxisAlignedBB>> bounds;

	static {
		List<AxisAlignedBB> commonBounds = ImmutableList.of(
				makeAABB(0, 10, 0, 16, 16, 16),
				makeAABB(4, 4, 4, 12, 10, 12)
		);
		bounds = Stream.of(EnumFacing.values())
				.filter(t -> t != EnumFacing.UP)
				.collect(Collectors.toMap(a -> a, a -> new ArrayList<>(commonBounds), (u, v) -> {
					throw new IllegalStateException();
				}, () -> new EnumMap<>(EnumFacing.class)));

		bounds.get(EnumFacing.DOWN).add(makeAABB(6, 0, 6, 10, 4, 10));

		bounds.get(EnumFacing.NORTH).add(makeAABB(6, 4, 0, 10, 8, 4));
		bounds.get(EnumFacing.SOUTH).add(makeAABB(6, 4, 12, 10, 8, 16));

		bounds.get(EnumFacing.WEST).add(makeAABB(0, 4, 6, 4, 8, 10));
		bounds.get(EnumFacing.EAST).add(makeAABB(12, 4, 6, 16, 8, 10));

	}

	private static AxisAlignedBB makeAABB(int fromX, int fromY, int fromZ, int toX, int toY, int toZ) {
		return new AxisAlignedBB(fromX / 16F, fromY / 16F, fromZ / 16F, toX / 16F, toY / 16F, toZ / 16F);
	}

	@SuppressWarnings({"unused", "WeakerAccess"})
	public static RayTraceResult collisionRayTrace(IBlockState blockState, World worldIn, BlockPos pos, Vec3d start, Vec3d end) {
		return bounds.get(blockState.getValue(BlockHopper.FACING)).stream()
				.map(bb -> rayTrace(pos, start, end, bb))
				.filter(Objects::nonNull)
				.min(Comparator.comparingDouble(r -> start.squareDistanceTo(r.hitVec)))
				.orElse(null);
	}


	@Nullable
	private static RayTraceResult rayTrace(BlockPos pos, Vec3d start, Vec3d end, AxisAlignedBB boundingBox) {
		Vec3d vec3d = start.subtract((double) pos.getX(), (double) pos.getY(), (double) pos.getZ());
		Vec3d vec3d1 = end.subtract((double) pos.getX(), (double) pos.getY(), (double) pos.getZ());
		RayTraceResult raytraceresult = boundingBox.calculateIntercept(vec3d, vec3d1);
		return raytraceresult == null ? null : new RayTraceResult(raytraceresult.hitVec.addVector((double) pos.getX(), (double) pos.getY(), (double) pos.getZ()), raytraceresult.sideHit, pos);
	}
}
