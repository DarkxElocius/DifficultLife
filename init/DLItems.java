package difficultLife.init;

import cpw.mods.fml.common.registry.GameRegistry;
import difficultLife.DLCore;
import difficultLife.item.GlassArmor;
import difficultLife.item.HeartContainer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import static difficultLife.init.DLConfigSetup.*;

public class DLItems {

	public static void init()
	{
		heart = new HeartContainer().setTextureName(DLCore.modid+ (OLD_TEXTURES ? ":heart_old" : ":heart")).setUnlocalizedName("dl.heart").setCreativeTab(CreativeTabs.tabMisc);
		glassHelm = new GlassArmor(glass, 0, 0).setTextureName(DLCore.modid+":glass_helmet").setUnlocalizedName("dl.glassHelmet").setCreativeTab(CreativeTabs.tabCombat);
		glassChest = new GlassArmor(glass, 0, 1).setTextureName(DLCore.modid+":glass_chestplate").setUnlocalizedName("dl.glassChestplate").setCreativeTab(CreativeTabs.tabCombat);
		glassLegs = new GlassArmor(glass, 0, 2).setTextureName(DLCore.modid+":glass_leggings").setUnlocalizedName("dl.glassLeggings").setCreativeTab(CreativeTabs.tabCombat);
		glassBoots = new GlassArmor(glass, 0, 3).setTextureName(DLCore.modid+":glass_boots").setUnlocalizedName("dl.glassBoots").setCreativeTab(CreativeTabs.tabCombat);
		GameRegistry.registerItem(heart, "dl.heart");
		GameRegistry.registerItem(glassHelm, "dl.glassHelmet");
		GameRegistry.registerItem(glassChest, "dl.glassChestplate");
		GameRegistry.registerItem(glassLegs, "dl.glassLeggings");
		GameRegistry.registerItem(glassBoots, "dl.glassBoots");
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
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(glassHelm),new Object[]{
			"GDG",
			"G G",
			'G',"blockGlass",
			'D',"gemDiamond"
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(glassChest),new Object[]{
			"G G",
			"GDG",
			"GGG",
			'G',"blockGlass",
			'D',"gemDiamond"
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(glassLegs),new Object[]{
			"GDG",
			"G G",
			"G G",
			'G',"blockGlass",
			'D',"gemDiamond"
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(glassBoots),new Object[]{
			"G G",
			"GDG",
			'G',"blockGlass",
			'D',"gemDiamond"
		}));
	}
	
	public static Item heart;
	public static ArmorMaterial glass = EnumHelper.addArmorMaterial("DL:GLASS", 32, new int[]{1,3,2,1}, 5);
	public static Item glassHelm;
	public static Item glassChest;
	public static Item glassLegs;
	public static Item glassBoots;
}
