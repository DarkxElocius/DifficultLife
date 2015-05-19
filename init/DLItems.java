package difficultLife.init;

import cpw.mods.fml.common.registry.GameRegistry;
import difficultLife.DLCore;
import difficultLife.item.HeartContainer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.oredict.OreDictionary;
import static difficultLife.init.DLConfigSetup.*;

public class DLItems {

	public static void init()
	{
		heart = new HeartContainer().setTextureName(DLCore.modid+ (OLD_TEXTURES ? ":heart_old" : ":heart")).setUnlocalizedName("dl.heart").setCreativeTab(CreativeTabs.tabMisc);
		GameRegistry.registerItem(heart, "dl.heart");
		if(DUNGEON_HEART_WEIGHT >= 0)
		{
			ChestGenHooks.addItem(ChestGenHooks.DUNGEON_CHEST, new WeightedRandomChestContent(heart, 0, DUNGEON_HEART_MIN, DUNGEON_HEART_MAX, DUNGEON_HEART_WEIGHT));
			ChestGenHooks.addItem(ChestGenHooks.MINESHAFT_CORRIDOR, new WeightedRandomChestContent(heart, 0, DUNGEON_HEART_MIN, DUNGEON_HEART_MAX, DUNGEON_HEART_WEIGHT));
			ChestGenHooks.addItem(ChestGenHooks.PYRAMID_DESERT_CHEST, new WeightedRandomChestContent(heart, 0, DUNGEON_HEART_MIN, DUNGEON_HEART_MAX, DUNGEON_HEART_WEIGHT));
			ChestGenHooks.addItem(ChestGenHooks.PYRAMID_JUNGLE_CHEST, new WeightedRandomChestContent(heart, 0, DUNGEON_HEART_MIN, DUNGEON_HEART_MAX, DUNGEON_HEART_WEIGHT));
			ChestGenHooks.addItem(ChestGenHooks.STRONGHOLD_CORRIDOR, new WeightedRandomChestContent(heart, 0, DUNGEON_HEART_MIN, DUNGEON_HEART_MAX, DUNGEON_HEART_WEIGHT));
			ChestGenHooks.addItem(ChestGenHooks.STRONGHOLD_CROSSING, new WeightedRandomChestContent(heart, 0, DUNGEON_HEART_MIN, DUNGEON_HEART_MAX, DUNGEON_HEART_WEIGHT));
			ChestGenHooks.addItem(ChestGenHooks.STRONGHOLD_LIBRARY, new WeightedRandomChestContent(heart, 0, DUNGEON_HEART_MIN, DUNGEON_HEART_MAX, DUNGEON_HEART_WEIGHT));
			ChestGenHooks.addItem(ChestGenHooks.VILLAGE_BLACKSMITH, new WeightedRandomChestContent(heart, 0, DUNGEON_HEART_MIN, DUNGEON_HEART_MAX, DUNGEON_HEART_WEIGHT));
		}
		OreDictionary.registerOre("heartCanister",heart);
	}
	
	public static Item heart;
}
