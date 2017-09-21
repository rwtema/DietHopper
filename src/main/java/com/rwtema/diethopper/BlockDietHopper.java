package com.rwtema.diethopper;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.BlockHopper;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BlockDietHopper extends BlockHopper {
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

	public BlockDietHopper() {
		this.setRegistryName(new ResourceLocation("hopper"));
		this.setHardness(3.0F);
		this.setResistance(8.0F);
		this.setSoundType(SoundType.METAL);
		this.setUnlocalizedName("hopper");
	}

	private static AxisAlignedBB makeAABB(int fromX, int fromY, int fromZ, int toX, int toY, int toZ) {
		return new AxisAlignedBB(fromX / 16F, fromY / 16F, fromZ / 16F, toX / 16F, toY / 16F, toZ / 16F);
	}

	@SuppressWarnings("deprecation")
	@Nullable
	@Override
	public RayTraceResult collisionRayTrace(IBlockState blockState, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull Vec3d start, @Nonnull Vec3d end) {
		return bounds.get(blockState.getValue(BlockHopper.FACING)).stream()
				.map(bb -> rayTrace(pos, start, end, bb))
				.anyMatch(Objects::nonNull)
				? super.collisionRayTrace(blockState, worldIn, pos, start, end) : null;
	}
}
