package thetadev.aeriautil.registry;

import net.minecraft.world.storage.loot.LootTable;

public interface ICustomLoot
{
	LootTable.Builder genLoot();
}
