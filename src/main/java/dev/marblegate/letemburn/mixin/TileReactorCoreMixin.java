package dev.marblegate.letemburn.mixin;

import com.brandon3055.brandonscore.blocks.TileBCore;
import com.brandon3055.draconicevolution.blocks.reactor.ProcessExplosion;
import com.brandon3055.draconicevolution.blocks.reactor.tileentity.TileReactorCore;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.ryanhcode.sable.Sable;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;


@Restriction(require = {
                @Condition("sable"),
                @Condition("draconicevolution")})
@Mixin(value = TileReactorCore.class, remap = false)
public abstract class TileReactorCoreMixin extends TileBCore {

    public TileReactorCoreMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @WrapOperation(
            method = "updateCriticalState",
            at = @At(value = "NEW",  args ="class=com/brandon3055/draconicevolution/blocks/reactor/ProcessExplosion")
    )
    private ProcessExplosion yourHandlerMethod(BlockPos origin, int radius, ServerLevel world, int minimumDelayTime, Operation<ProcessExplosion> original) {
        var helper = Sable.HELPER;
        if(helper.isInPlotGrid(this)){
            var pos = helper.projectOutOfSubLevel(world, new Vec3(origin.getX(), origin.getY(), origin.getZ()));
            var realPos = BlockPos.containing(pos);
            return new ProcessExplosion(realPos, radius, world, minimumDelayTime);
        } else {
            return original.call(origin, radius, world, minimumDelayTime);
        }
    }
}
