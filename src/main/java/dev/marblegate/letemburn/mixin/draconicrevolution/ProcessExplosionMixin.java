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

import codechicken.lib.vec.Vector3;
import com.brandon3055.draconicevolution.blocks.reactor.ProcessExplosion;
import dev.marblegate.letemburn.waaoh.RememberDatPos;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Restriction(require = {
        @Condition("sable"),
        @Condition("draconicevolution") })
@Mixin(value = ProcessExplosion.class, remap = false)
public abstract class ProcessExplosionMixin implements RememberDatPos {
    @Unique
    BlockPos lETEMBURN$cache;

    @Mutable
    @Shadow
    @Final
    public Vector3 origin;

    @Mutable
    @Shadow
    protected boolean calculationComplete;

    @Shadow
    public abstract void updateCalculation();

    @Inject(method = "detonate", at = @At(value = "HEAD"))
    private void detonate$letsCalculateInSiteOkay(CallbackInfoReturnable<Boolean> cir) {
        if (lETEMBURN$cache != null) {
            origin = Vector3.fromBlockPosCenter(lETEMBURN$cache);
            while (!calculationComplete) {
                updateCalculation();
            }
        }
    }

    @Override
    public void remember(BlockPos pos) {
        this.lETEMBURN$cache = pos;
    }
}
