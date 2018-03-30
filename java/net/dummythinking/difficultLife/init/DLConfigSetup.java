package net.dummythinking.difficultLife.init;

import cpw.mods.fml.common.FMLLog;
import net.dummythinking.difficultLife.DLCore;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DLConfigSetup {

    public static int PLAYER_HEARTS_GENERIC;
    public static float DIFFICULTY_MAX;
    public static float DIFFICULTY_DEFAULT;
    public static float DIFFICULTY_EACH_TICK;
    public static float DIFFICULTY_MULTIPLIER_OTHER_WORLD;
    public static float DIFFICULTY_GENERIC_HEALTH_MULTIPLIER;
    public static float DIFFICULTY_PEACEFULL_HEALTH_MULTIPLIER;
    public static boolean ENABLE_CUSTOM_HEALTH_REGEN;
    public static double[] DIFFICULTY_BOUNDS;
    public static float BLIGHT_CHANCE_MULTIPLIER;
    public static float HEART_DROP_CHANCE;
    public static int PACKET_DELAY;
    public static boolean CHANGE_HEART_RENDERING;
    public static boolean RENDER_DIFFICULTY_METER;
    public static boolean ADD_GUI_BUTTON;
    public static boolean ENABLE_VANITY_RENDERER;
    public static int MAX_ADDITIONAL_HEARTS;
    public static int DUNGEON_HEART_WEIGHT;
    public static int DUNGEON_HEART_MIN;
    public static int DUNGEON_HEART_MAX;
    public static boolean LOOSE_HEALTH_ON_DEATH;
    public static boolean GAIN_HEARTS_FROM_WITHER;
    public static boolean GAIN_HEARTS_FROM_DRAGON;
    public static boolean OLD_TEXTURES;
    public static boolean DIFFICULTY_GUI_RIGHT;
    public static boolean DIFFICULTY_GUI_TOP;
    public static int DIFFICULTY_GUI_HORISONTAL;
    public static int DIFFICULTY_GUI_VERTICAL;
    public static List<String> PERMITTED_FROM_BLIGHT = new ArrayList<String>();
    public static List<String> PERMITTED_FROM_HP_INCREASEMENT = new ArrayList<String>();
    public static boolean INVISIBLE_BLIGHTS;
    public static boolean INCREASE_DIFFICULTY_ON_EMPTY_SERVER;

    public static boolean ENABLE_COMMAND_SETDIFFICULTY;
    public static boolean ENABLE_COMMAND_SETMAXHEALTH;
    public static boolean ENABLE_COMMAND_HEAL;

    public static boolean ENABLE_DL_HEART_SYSTEM;

    public static boolean FML_LOG;

    public static void setupCFG(Configuration cfg) {
        try {
            cfg.load();
            ENABLE_DL_HEART_SYSTEM = cfg.getBoolean("heartSystem", Configuration.CATEGORY_GENERAL, true, "Should DL HeartSystem be enabled?");

            PLAYER_HEARTS_GENERIC = cfg.getInt("startingHealth", Configuration.CATEGORY_GENERAL, 10, 0, Integer.MAX_VALUE, "Sets the player starting amount of HEARTS, not HEALTH. Values here will get multiplied by 2 automatically!");

            DIFFICULTY_MAX = cfg.getFloat("difficultyValueMax", Configuration.CATEGORY_GENERAL, 250, Float.MIN_VALUE, Float.MAX_VALUE, "Sets the maximum difficulty value. Example: 250 means, that at the max difficulty zombies may have up to 400 health.");
            DIFFICULTY_DEFAULT = cfg.getFloat("difficultyVauleStarting", Configuration.CATEGORY_GENERAL, 0, Float.MIN_VALUE, Float.MAX_VALUE, "Sets the starting difficulty for the world.");
            DIFFICULTY_EACH_TICK = cfg.getFloat("difficultyValueEachTick", Configuration.CATEGORY_GENERAL, 0.00165562913907284768211920529801F, 0, Float.MAX_VALUE, "The amount of Difficulty added EACH TICK (1/20 of a second)");
            DIFFICULTY_MULTIPLIER_OTHER_WORLD = cfg.getFloat("difficultyValueMultiplierOtherworld", Configuration.CATEGORY_GENERAL, 1.5F, 0, Float.MAX_VALUE, "The modifier, that will be applied to the %difficultyValueEachTick% when the player is not in the overworld(example:nether)");
            DIFFICULTY_GENERIC_HEALTH_MULTIPLIER = cfg.getFloat("difficultyValueHealthModifier", Configuration.CATEGORY_GENERAL, 0.5F, 0, Float.MAX_VALUE, "The modifier, that will GUARANTEED get applied to the entity health upon spawn. Example: if the value is 0.5, and the difficulty is 100, the zombie will GUARANTEED have 70 health, and MAY have some on top of that.");
            DIFFICULTY_PEACEFULL_HEALTH_MULTIPLIER = cfg.getFloat("difficultyValuePeacefullHealthMultiplier", Configuration.CATEGORY_GENERAL, 0.25F, 0, Float.MAX_VALUE, "The modifier, that will GUARANTEED get applied to the ANIMAL entity health upon spawn. Example: if the value is 0.25, and the difficulty is 100, the sheep will GUARANTEED have 35 health, and MAY have some on top of that.");
            ENABLE_CUSTOM_HEALTH_REGEN = cfg.getBoolean("enableCustomHealth", Configuration.CATEGORY_GENERAL, true, "If this is set to %true%, then the default MC health regen mechanic will get replaced by the custom one, added by the mod");
            DIFFICULTY_BOUNDS = cfg.get(Configuration.CATEGORY_GENERAL, "difficultyValueBounds", defaultBounds(), "These are the bounds, at which the difficulty text will get changed. Affects NOTHING, pure client field").getDoubleList();
            BLIGHT_CHANCE_MULTIPLIER = cfg.getFloat("blightChance", Configuration.CATEGORY_GENERAL, 0.125F, 0, Float.MAX_VALUE, "This value represents the chance for the mob to become a blight. Formula: One in (%currentDifficulty% / %maxDifficulty% * %blightChance%) will become a blight.");
            PACKET_DELAY = cfg.getInt("packetDelay", Configuration.CATEGORY_GENERAL, 20, 1, Integer.MAX_VALUE, "The amount of request to send packets to be done, before the packet gets sent. The higher the number, the more server-client desync will be in the game. The lower the number, the more packets, the more server lag.");
            HEART_DROP_CHANCE = cfg.getFloat("heartCanisterDropChance", Configuration.CATEGORY_GENERAL, 0.01F, 0, 1, "The chance for the Heart Canister to drop upon death of any mob. 0.01 means a 1% chance, where 0.25 will mean 25%, 1 = 100% and 0.001 = 0.1%");
            CHANGE_HEART_RENDERING = cfg.getBoolean("changeHeartDisplay", Configuration.CATEGORY_GENERAL, true, "Change the way minecraft hearts are displayed to be 1 row of different colored hearts, instead of multiple rows. If TConstruct is detected this feature gets disabled.");
            RENDER_DIFFICULTY_METER = cfg.getBoolean("displayDifficulty", Configuration.CATEGORY_GENERAL, true, "Should the difficulty bar to the right be rendered. Disabling thise feature WILL NOT disable the difficulty itself!");
            ADD_GUI_BUTTON = cfg.getBoolean("addGuiButton", Configuration.CATEGORY_GENERAL, true, "Should there be a button in the player's inventory to access vanity slots. Setting this to false WILL NOT disable the vanity features themselves!");
            ENABLE_VANITY_RENDERER = cfg.getBoolean("vanityRenderer", Configuration.CATEGORY_GENERAL, true, "Should the vanity render override be enabled? Vanity armor renders instead of the regular, but gives no protection/stats");
            MAX_ADDITIONAL_HEARTS = cfg.getInt("maxAdditionalHealth", Configuration.CATEGORY_GENERAL, -1, -1, Integer.MAX_VALUE, "Sets the player max amount of HEARTS, not HEALTH. Values here will get multiplied by 2 automatically! Setting to -1 removes the cap.");
            DUNGEON_HEART_WEIGHT = cfg.getInt("dungeonHeartWeight", Configuration.CATEGORY_GENERAL, 1, -1, Integer.MAX_VALUE, "The WEIGHT of a heart in a dungeon chest. Setting this to -1 disables the generation!");
            DUNGEON_HEART_MIN = cfg.getInt("dungeonHeartMin", Configuration.CATEGORY_GENERAL, 1, 0, Integer.MAX_VALUE, "The minimum stacksize for the hearts in the dungeon chests. If the %dungeonHeartWeight% is set to -1 has no effect!");
            DUNGEON_HEART_MAX = cfg.getInt("dungeonHeartMax", Configuration.CATEGORY_GENERAL, 3, 0, Integer.MAX_VALUE, "The maximum stacksize for the hearts in the dungeon chests. If the %dungeonHeartWeight% is set to -1 has no effect!");
            LOOSE_HEALTH_ON_DEATH = cfg.getBoolean("looseHealthOnDeath", Configuration.CATEGORY_GENERAL, false, "Should the player loose all health upon death? This might make the game impossible if set to true!");
            GAIN_HEARTS_FROM_WITHER = cfg.getBoolean("gainHeartsFromWither", Configuration.CATEGORY_GENERAL, true, "Should the wither drop hearts upon death?");
            GAIN_HEARTS_FROM_DRAGON = cfg.getBoolean("gainHeartsFromDragon", Configuration.CATEGORY_GENERAL, true, "Should the enderdragon drop hearts upon death?");
            OLD_TEXTURES = cfg.getBoolean("useOldTextures", Configuration.CATEGORY_GENERAL, false, "Should use the pre 1.1 textures?");
            DIFFICULTY_GUI_RIGHT = cfg.getBoolean("guiStickToRightSide", Configuration.CATEGORY_GENERAL, true, "Should the difficulty GUI position be calculated as WIDTH - %horisontalOffset%? If set to false will be calculated as %horisontalOffset%");
            DIFFICULTY_GUI_TOP = cfg.getBoolean("guiStickToTop", Configuration.CATEGORY_GENERAL, true, "Should the difficulty GUI position be calculated as HEIGHT/3 - %verticalOffset%? If set to false will be calculated as %verticalOffset%");
            DIFFICULTY_GUI_HORISONTAL = cfg.getInt("horisontalOffset", Configuration.CATEGORY_GENERAL, 10, Integer.MIN_VALUE, Integer.MAX_VALUE, "The amount of offset for the difficuly GUI on the X axis.");
            DIFFICULTY_GUI_VERTICAL = cfg.getInt("verticalOffset", Configuration.CATEGORY_GENERAL, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, "The amount of offset for the difficuly GUI on the Y axis.");
            INCREASE_DIFFICULTY_ON_EMPTY_SERVER = cfg.getBoolean("increaseDifficultyOnEmpty", Configuration.CATEGORY_GENERAL, false, "Should difficulty increase if always = true, only if players are online = false");

            ENABLE_COMMAND_SETMAXHEALTH = cfg.getBoolean("enableCommandSetMaxHealth", Configuration.CATEGORY_GENERAL, true, "Should the Command 'setCommandHealth' be enabled");
            ENABLE_COMMAND_HEAL = cfg.getBoolean("enableCommandHeal", Configuration.CATEGORY_GENERAL, true, "Should the HealCommand be enabled");
            ENABLE_COMMAND_SETDIFFICULTY = cfg.getBoolean("enableCommandSetDifficulty", Configuration.CATEGORY_GENERAL, true, "Should the SetDifficulty be enabled");

            PERMITTED_FROM_BLIGHT = Arrays.asList(cfg.get(Configuration.CATEGORY_GENERAL, "blightBlacklist", new String[]{"witchery.leonard", "sheep"}, "The blacklist for entities to be spawned as blights. Syntaxis is: modid.entityName").getStringList());
            PERMITTED_FROM_HP_INCREASEMENT = Arrays.asList(cfg.get(Configuration.CATEGORY_GENERAL, "affectedBlacklist", new String[]{"witchery.leonard", "essentialcraft3.ec3.common.entity.EntityMRUPresence"}, "The blacklist for entities to be modified by DifficultLife. Syntaxis is: modid.entityName").getStringList());
            INVISIBLE_BLIGHTS = cfg.getBoolean("invisibleBlights", Configuration.CATEGORY_GENERAL, true, "Should Blights be invisible?");

            FML_LOG = cfg.getBoolean("fmlLog", Configuration.CATEGORY_GENERAL, true, "To much log from DifficultLife? Then set this option to false! (This option only disable the notification logs)");
        } catch (Exception e) {
            FMLLog.log(Level.FATAL, e, "[DifficultLife]	*Catched an exception during generating configuration file!", DLCore.nObj());
        } finally {
            cfg.save();
        }
    }

    public static double[] defaultBounds() {
        //25: Very Easy
        //50: Easy
        //75: Medium
        //100: Hard
        //125: Very Hard
        //150: Insane
        //175: Impossible
        //200: I SEE YOU
        //225: I'M COMING FOR YOU
        //250: HAHAHAHA
        return new double[]{25, 50, 75, 100, 125, 150, 175, 200, 225, 250};
    }

}
