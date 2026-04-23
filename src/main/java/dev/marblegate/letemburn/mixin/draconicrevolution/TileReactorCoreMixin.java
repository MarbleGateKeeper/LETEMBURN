/*
 * Copyright (C) 2026 MarbleGate
 * SPDX-License-Identifier: LGPL-3.0-or-later
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package dev.marblegate.letemburn.mixin.draconicrevolution;

import com.brandon3055.brandonscore.blocks.TileBCore;
import com.brandon3055.brandonscore.handlers.IProcess;
import com.brandon3055.draconicevolution.blocks.reactor.ProcessExplosion;
import com.brandon3055.draconicevolution.blocks.reactor.tileentity.TileReactorCore;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.marblegate.letemburn.waaoh.RememberDatPos;
import dev.ryanhcode.sable.Sable;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Restriction(require = {
        @Condition("sable"),
        @Condition("draconicevolution") })
@Mixin(value = TileReactorCore.class, remap = false)
public abstract class TileReactorCoreMixin extends TileBCore {
    public TileReactorCoreMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @WrapOperation(method = "updateCriticalState", at = @At(value = "INVOKE", target = "Lcom/brandon3055/brandonscore/handlers/ProcessHandler;addProcess(Lcom/brandon3055/brandonscore/handlers/IProcess;)V"))
    private void updateCriticalState$stopAddThisToProcessingPoll(IProcess process, Operation<Void> original) {
        // Do nothing. Just intercept it. If you have any problem with it, I'll say it's just a mod for fun so ignore the performance OK?
    }

    @ModifyExpressionValue(method = "updateCriticalState", at = @At(value = "INVOKE", target = "Lcom/brandon3055/draconicevolution/blocks/reactor/ProcessExplosion;isCalculationComplete()Z"))
    private boolean updateCriticalState$justStartYourCountDownINeedNoPreCalculation(boolean original) {
        return true;
    }

    @WrapOperation(method = "updateCriticalState", at = @At(value = "INVOKE", target = "Lcom/brandon3055/draconicevolution/blocks/reactor/ProcessExplosion;detonate()Z"))
    private boolean updateCriticalState$giveMeDatPos(ProcessExplosion instance, Operation<Boolean> original) {
        var helper = Sable.HELPER;
        BlockPos pos;
        if (helper.isInPlotGrid(this))
            pos = BlockPos.containing(helper.projectOutOfSubLevel(this.level, new Vec3(getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ())));
        else
            pos = getBlockPos();
        if (instance instanceof RememberDatPos r)
            r.remember(pos);
        return original.call(instance);
    }
}
