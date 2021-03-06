package CustomOreGen;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.logging.log4j.Logger;

import CustomOreGen.Server.ServerState;
import CustomOreGen.Server.WorldConfig;
import cpw.mods.fml.common.Loader;

public class CustomOreGenBase
{
    public static Logger log;
    
    public static final String OPTIONS_FILENAME = "CustomOreGen_Options.txt";
	public static final String BASE_CONFIG_FILENAME = "CustomOreGen_Config.xml";
	public static final String DEFAULT_BASE_CONFIG_FILENAME = "CustomOreGen_Config_Default.xml";

    private static int _hasMystcraft = 0;

    public static boolean isClassLoaded(String className)
    {
        try
        {
            CustomOreGenBase.class.getClassLoader().loadClass(className);
            return true;
        }
        catch (ClassNotFoundException var2)
        {
            return false;
        }
    }

    public static void onModPostLoad()
    {
        unpackConfigs();
        hasMystcraft();
    }

    private static void unpackConfigs() {
    	File configPath = getConfigDir();
        File modulesDir = new File(configPath, "modules");
        
        File defaultModulesDir = new File(modulesDir, "default");
        if (defaultModulesDir.exists()) {
        	File[] defaultModules = defaultModulesDir.listFiles();
        	for (File defaultModule : defaultModules) {
        		defaultModule.delete();
        	}
        } else {
        	configPath.mkdir();
        	modulesDir.mkdir();
        	defaultModulesDir.mkdir();
        }
        String[] extraModules = new String[] {
            "AppliedEnergistics.xml",
            "ArsMagica2.xml",
            "BiomesOPlenty.xml",
            "Chisel2.xml",
            "DenseOres.xml",
            "ElectriCraft.xml",
            "EtFuturum.xml",
            "Factorization.xml",
            "FlaxbeardsSteamcraft.xml",
            "Forestry.xml",
            "FossilsandArchaeology.xml",
            "Galacticraft.xml",
            "GeoStrata.xml",
            "Gregtech.xml",
            "Gregtech6.xml",
            "ImmersiveEngineering.xml",
            "MagnetiCraft.xml",
            "Mariculture.xml",
            "IndustrialCraft2.xml",
            "Mekanism.xml",
            "Metallurgy4.xml",
            "Mimicry.xml",
            "MineChem.xml",
            "MinecraftComesAlive.xml",
            "MinecraftSpecialRules.xml",
            "NetherOres.xml",
            "Netherrocks.xml",
            "Nuclearcraft.xml",
            "PamsHarvestCraft.xml",
            "ProjectRed.xml",
            "RailCraft.xml",
            "ReactorCraft.xml",
            "SimpleOres.xml",
            "Thaumcraft4.xml",
            "ThermalFoundation.xml",
            "TinkersConstruct.xml",
            "TinkersSteelworks.xml",
            "Vanilla.xml"
        };
        for (String module : extraModules) {
        	unpackConfigFile("modules/" + module, new File(defaultModulesDir, module));
        }
        
        new File(modulesDir, "custom").mkdir();

        unpackConfigFile(DEFAULT_BASE_CONFIG_FILENAME, new File(configPath, DEFAULT_BASE_CONFIG_FILENAME));
        loadWorldConfig();
	}

	private static WorldConfig loadWorldConfig() {
    	WorldConfig config = null;

        while (config == null)
        {
            try
            {
                config = new WorldConfig();
            }
            catch (Exception e)
            {
                if (!ServerState.onConfigError(e))
                {
                    break;
                }

                config = null;
            }
        }
        
        return config;
	}
	
    public static boolean unpackConfigFile(String configName, File destination)
    {
    	String resourceName = "config/" + configName;
        try
            {
        	    InputStream ex = CustomOreGenBase.class.getClassLoader().getResourceAsStream(resourceName);
                BufferedOutputStream streamOut = new BufferedOutputStream(new FileOutputStream(destination));
                byte[] buffer = new byte[1024];
                int len1;

                while ((len1 = ex.read(buffer)) >= 0)
                {
                    streamOut.write(buffer, 0, len1);
                }

                ex.close();
                streamOut.close();
                return true;
            }
            catch (Exception var6)
            {
                throw new RuntimeException("Failed to unpack resource \'" + resourceName + "\'", var6);
            }
    }

    public static File getConfigDir() {
    	return new File(Loader.instance().getConfigDir(), "CustomOreGen");
    }
    
    public static boolean hasMystcraft()
    {
        if (_hasMystcraft == 0)
        {
            try
            {
                _hasMystcraft = -1;

                if (Loader.isModLoaded("Mystcraft"))
                {
                	/* FIXME: re-enable after restoring Mystcraft compatibility
                    MystcraftInterface.init();
                    _hasMystcraft = 1;
                    */
                }
            }
            catch (Throwable var1)
            {
                log.error("COG Mystcraft interface appears to be incompatible with the installed version of Mystcraft.", var1);
            }
        }

        return _hasMystcraft == 1;
    }
    
    public static String getDisplayString() {
    	return FMLInterface.getDisplayString();
    }
}
