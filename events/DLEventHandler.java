package difficultLife.events;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.world.WorldEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.relauncher.Side;
import difficultLife.init.DLConfigSetup;
import difficultLife.init.DLItems;
import difficultLife.network.DataSyncManager;
import difficultLife.utils.DLSaveStorage;
import difficultLife.utils.DLUtils;

public class DLEventHandler {
	
	public static class DLPotionEntry
	{
		float weight;
		int potionID;
		
		public DLPotionEntry(float f, int i)
		{
			weight = f;
			potionID = i;
		}
	}
	
	public static void initPotions()
	{
		potions.add(new DLPotionEntry(Potion.damageBoost.id, 30));
		potions.add(new DLPotionEntry(Potion.moveSpeed.id, 10));
		potions.add(new DLPotionEntry(Potion.fireResistance.id, 10));
		potions.add(new DLPotionEntry(Potion.invisibility.id, 20));
		potions.add(new DLPotionEntry(Potion.resistance.id, 30));
	}
	
	public static List<DLPotionEntry> potions = new ArrayList<DLPotionEntry>();
	
	public static DLPotionEntry findRandPotion(Random rnd, float maxWeight)
	{
		List<DLPotionEntry> lst = new ArrayList<DLPotionEntry>();
		
		for(int i = 0; i < potions.size(); ++i)
		{
			DLPotionEntry e = potions.get(i);
			if(e.weight <= maxWeight)
			{
				lst.add(e);
			}
		}
		
		if(!lst.isEmpty())
			return lst.get(rnd.nextInt(lst.size()));
		
		return null;
	}
	
	@SubscribeEvent
	public void livingDropsEvent(LivingDropsEvent event)
	{
		if(!event.entityLiving.worldObj.isRemote)
		{
			if(event.recentlyHit)
			{
				if(event.entityLiving.worldObj.rand.nextFloat() <= DLConfigSetup.HEART_DROP_CHANCE)
				{
					event.entityLiving.dropItem(DLItems.heart, 1);
				}
				
			}
			
			if(event.entityLiving instanceof EntityWither)
			{
				if(DLConfigSetup.GAIN_HEARTS_FROM_WITHER)
					event.entityLiving.dropItem(DLItems.heart, 3+event.entityLiving.worldObj.rand.nextInt(3));
			}
			
			if(event.entityLiving instanceof EntityDragon)
			{
				if(DLConfigSetup.GAIN_HEARTS_FROM_DRAGON)
					event.entityLiving.dropItem(DLItems.heart, 9+event.entityLiving.worldObj.rand.nextInt(12));
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	@SubscribeEvent
	public void livingSpawnEvent(LivingEvent.LivingUpdateEvent event)
	{
		if(!event.entityLiving.worldObj.isRemote && !(event.entityLiving instanceof EntityPlayer))
		{
			if(EntityList.getEntityString(event.entityLiving) != null && !EntityList.getEntityString(event.entityLiving).isEmpty() && !DLConfigSetup.PERMITTED_FROM_HP_INCREASEMENT.contains(EntityList.getEntityString(event.entityLiving)))
				if(event.entityLiving.getAttributeMap() != null)
				{
					if(event.entityLiving.getEntityAttribute(SharedMonsterAttributes.maxHealth).getModifier(DLUtils.modifierID) == null)
					{
						float difficulty = DLSaveStorage.commonGenericTag.getFloat("difficulty");
						
						if(!DLConfigSetup.PERMITTED_FROM_BLIGHT.contains(EntityList.getEntityString(event.entityLiving)))
							if(event.entityLiving.worldObj.rand.nextFloat() < DLSaveStorage.commonGenericTag.getFloat("difficulty") / DLConfigSetup.DIFFICULTY_MAX * DLConfigSetup.BLIGHT_CHANCE_MULTIPLIER)
							{
								event.entityLiving.addPotionEffect(new PotionEffect(Potion.invisibility.id,Integer.MAX_VALUE,0,true));
								event.entityLiving.addPotionEffect(new PotionEffect(Potion.fireResistance.id,Integer.MAX_VALUE,0,true));
								event.entityLiving.addPotionEffect(new PotionEffect(Potion.moveSpeed.id,Integer.MAX_VALUE,8,true));
								event.entityLiving.addPotionEffect(new PotionEffect(Potion.damageBoost.id,Integer.MAX_VALUE,2,true));
								if(event.entityLiving instanceof EntityLiving)
								{
									EntityLiving entity = (EntityLiving) event.entityLiving;
						            int i = event.entity.worldObj.rand.nextInt(2);
						            float f = 0.5F;
		
						            if (event.entity.worldObj.rand.nextFloat() < 0.095F)
						            {
						                ++i;
						            }
		
						            if (event.entity.worldObj.rand.nextFloat() < 0.095F)
						            {
						                ++i;
						            }
		
						            if (event.entity.worldObj.rand.nextFloat() < 0.095F)
						            {
						                ++i;
						            }
		
						            for (int j = 3; j >= 0; --j)
						            {
						                ItemStack itemstack = entity.func_130225_q(j);
		
						                if (j < 3 && event.entity.worldObj.rand.nextFloat() < f)
						                {
						                    break;
						                }
		
						                if (itemstack == null)
						                {
						                    Item item = EntityLiving.getArmorItemForSlot(j + 1, i);
		
						                    if (item != null)
						                    {
						                        entity.setCurrentItemOrArmor(j + 1, new ItemStack(item));
						                    }
						                }
						            }
								}
								for(int i = 0; i < 5; ++i)
								{
									ItemStack is = event.entityLiving.getEquipmentInSlot(i);
									if(is != null)
									{
										is = EnchantmentHelper.addRandomEnchantment(event.entityLiving.worldObj.rand, is, 30);
									}else
									{
										
									}
								}
								event.entityLiving.setFire(Integer.MAX_VALUE/20);
								if(event.entityLiving instanceof EntityCreeper)
								{
									((EntityCreeper)event.entityLiving).onStruckByLightning(new EntityLightningBolt(event.entityLiving.worldObj, event.entityLiving.posX, event.entityLiving.posY, event.entityLiving.posZ));
								}
								difficulty *= 3;
							}
						
						float genAddedHealth = difficulty;
						
						
	
						
						if(event.entityLiving instanceof IMob)
						{
							genAddedHealth *= DLConfigSetup.DIFFICULTY_GENERIC_HEALTH_MULTIPLIER;
						}else
						{
							genAddedHealth *= DLConfigSetup.DIFFICULTY_PEACEFULL_HEALTH_MULTIPLIER;
						}
						
						difficulty -= genAddedHealth;
						
						if(difficulty > 0)
						{
							float randomFlt = event.entityLiving.worldObj.rand.nextFloat();
							float diffIncrease = difficulty * randomFlt;
							difficulty -= diffIncrease;
							genAddedHealth += diffIncrease;
						}
						if(event.entityLiving.getEntityAttribute(SharedMonsterAttributes.attackDamage) != null)
							if(difficulty > 0)
							{
								float randomFlt = event.entityLiving.worldObj.rand.nextFloat();
								float diffIncrease = difficulty * randomFlt;
								difficulty -= diffIncrease;
								if(event.entityLiving.getEntityAttribute(SharedMonsterAttributes.attackDamage).getModifier(DLUtils.modifierID) == null)
									event.entityLiving.getEntityAttribute(SharedMonsterAttributes.attackDamage).applyModifier(new AttributeModifier(DLUtils.modifierID, "DL.damageMod", diffIncrease/10, 0));
							}
							
						if(difficulty > 0)
						{
							DLPotionEntry e = findRandPotion(event.entityLiving.worldObj.rand,difficulty);
							if(e != null)
							{
								difficulty -= e.weight;
							}
						}
						if(event.entityLiving.getEntityAttribute(SharedMonsterAttributes.maxHealth).getModifier(DLUtils.modifierID) == null)
							event.entityLiving.getEntityAttribute(SharedMonsterAttributes.maxHealth).applyModifier(new AttributeModifier(DLUtils.modifierID, "DL.healthMod", genAddedHealth, 0));
						event.entityLiving.setHealth(event.entityLiving.getMaxHealth());
					}
				}
		}
		if(!event.entityLiving.worldObj.isRemote && event.entityLiving instanceof EntityPlayer && DLConfigSetup.ENABLE_CUSTOM_HEALTH_REGEN)
		{
			if(event.entityLiving.worldObj.getWorldTime() % 240 == 0 && ((EntityPlayer)event.entityLiving).getFoodStats().getFoodLevel() >= 10)
			{
				event.entityLiving.heal(1);
			}
		}
		if(!event.entityLiving.worldObj.isRemote && event.entityLiving instanceof EntityPlayer && event.entityLiving.ticksExisted % 20 == 0)
		{
			EntityPlayer p = (EntityPlayer) event.entityLiving;
			List<EntityPlayer> players = p.worldObj.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.getBoundingBox(p.posX-0.5D, p.posY-0.5D, p.posZ-0.5D, p.posX+0.5D, p.posY+0.5D, p.posZ+0.5D).expand(16, 8, 16));
			for(EntityPlayer pl : players)
			{
				NBTTagCompound tag = DLSaveStorage.playerData.get(p.getCommandSenderName());
				if(tag.hasKey("username"))
				{
					//...
				}else
				{
					tag.setString("username", p.getCommandSenderName());
				}
				DataSyncManager.requestServerToClientMessage("playerData", (EntityPlayerMP) pl, tag, true);
			}
		}
	}
	
	@SubscribeEvent
	public void onWorldTick(TickEvent.WorldTickEvent event)
	{
		if(event.side == Side.SERVER && event.world != null && event.world.provider != null && event.world.provider.dimensionId == 0 && event.phase == Phase.START)
		{
			float dIncrease = 0;
			if(event.world.getWorldTime() % 20 == 0)
			{
				dIncrease = DLConfigSetup.DIFFICULTY_EACH_TICK;
			}
			DLSaveStorage.manageDifficultyIncrease(dIncrease);
		}
		if(DLConfigSetup.ENABLE_CUSTOM_HEALTH_REGEN)
		{
			event.world.getGameRules().setOrCreateGameRule("naturalRegeneration", "false");
		}
	}
	
	@SubscribeEvent
	public void onPlayerConstruction(PlayerEvent.PlayerRespawnEvent event)
	{
		if(event.player instanceof EntityPlayerMP)
		{
			EntityPlayerMP player = (EntityPlayerMP) event.player;
			float maxHealth = player.getMaxHealth();
			if(DLConfigSetup.LOOSE_HEALTH_ON_DEATH)
			{
				NBTTagCompound tag = DLSaveStorage.playerData.get(player.getCommandSenderName());
				if(tag != null && !tag.hasNoTags())
				{
					tag.setInteger("health", DLConfigSetup.PLAYER_HEARTS_GENERIC*2);
					DLSaveStorage.playerData.put(player.getCommandSenderName(), tag);
					DataSyncManager.requestServerToClientMessage("playerData", (EntityPlayerMP) player, DLSaveStorage.playerData.get(player.getCommandSenderName()), true);
				}
			}
			float shouldHave = DLSaveStorage.getSuggestedAmmoundOfHealthForPlayer(player);
			float difference = maxHealth-shouldHave;
			
			AttributeModifier mod = player.getEntityAttribute(SharedMonsterAttributes.maxHealth).getModifier(DLUtils.modifierID);
			if(mod == null)
			{
				player.getEntityAttribute(SharedMonsterAttributes.maxHealth).applyModifier(new AttributeModifier(DLUtils.modifierID, "DL.playerhealthDifference", -difference, 0));
			}
			maxHealth = player.getMaxHealth();
			if(player.getHealth() > maxHealth)
			{
				player.setHealth(maxHealth);
			}
		}
	}
	
	@SubscribeEvent
	public void onPlayerReadedFromFile(net.minecraftforge.event.entity.player.PlayerEvent.LoadFromFile event)
	{
		DLSaveStorage.generatePlayerSaveFile(event);
	}
	
	@SubscribeEvent
	public void onPlayerSavedToFile(net.minecraftforge.event.entity.player.PlayerEvent.SaveToFile event)
	{
		DLSaveStorage.saveServerPlayerFile(event);
	}
	
	@SubscribeEvent
	public void onPlayerJoinedServerEvent(PlayerEvent.PlayerLoggedInEvent event)
	{
		if(event.player instanceof EntityPlayerMP)
		{
			DataSyncManager.requestServerToClientMessage("worldData", (EntityPlayerMP) event.player, DLSaveStorage.commonGenericTag, true);
			DataSyncManager.requestServerToClientMessage("playerData", (EntityPlayerMP) event.player, DLSaveStorage.playerData.get(event.player.getCommandSenderName()), true);
			EntityPlayerMP player = (EntityPlayerMP) event.player;
			float maxHealth = player.getMaxHealth();
			float shouldHave = DLSaveStorage.getSuggestedAmmoundOfHealthForPlayer(player);
			float difference = maxHealth-shouldHave;
			
			AttributeModifier mod = player.getEntityAttribute(SharedMonsterAttributes.maxHealth).getModifier(DLUtils.modifierID);
			if(mod == null)
			{
				player.getEntityAttribute(SharedMonsterAttributes.maxHealth).applyModifier(new AttributeModifier(DLUtils.modifierID, "DL.playerhealthDifference", -difference, 0));
			}
			maxHealth = player.getMaxHealth();
			if(player.getHealth() > maxHealth)
			{
				player.setHealth(maxHealth);
			}
		}
	}
	
	@SubscribeEvent
	public void onWorldLoadEvent(WorldEvent.Load event)
	{
		DLSaveStorage.generateServerWorldFile(event);
	}
	
	@SubscribeEvent
	public void onWorldSaveEvent(WorldEvent.Save event)
	{
		DLSaveStorage.saveServerWorldFile(event);
	}

}
