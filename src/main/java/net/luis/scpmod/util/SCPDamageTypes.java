package net.luis.scpmod.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;
import net.luis.scpmod.SCPMod;

public class SCPDamageTypes {
    public static final ResourceKey<DamageType> SCP_207 = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(SCPMod.MOD_ID, "scp_207"));
    public static final ResourceKey<DamageType> SCP_173 = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(SCPMod.MOD_ID, "scp_173"));
    public static final ResourceKey<DamageType> SCP_513_STRANGLE = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(SCPMod.MOD_ID, "scp_513_strangle"));

}
