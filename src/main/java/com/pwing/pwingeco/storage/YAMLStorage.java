package com.pwing.pwingeco.storage;

import com.pwing.pwingeco.PwingEco;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class YAMLStorage {
    private final PwingEco plugin;
    private final File dataFolder;

    public YAMLStorage(PwingEco plugin) {
        this.plugin = plugin;
        this.dataFolder = new File(plugin.getDataFolder(), "playerdata");
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
    }

    public void savePlayerData(UUID uuid, Map<String, Double> balances) {
        File playerFile = new File(dataFolder, uuid.toString() + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(playerFile);

        for (Map.Entry<String, Double> entry : balances.entrySet()) {
            config.set("balances." + entry.getKey(), entry.getValue());
        }

        try {
            config.save(playerFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, Double> loadPlayerData(UUID uuid) {
        File playerFile = new File(dataFolder, uuid.toString() + ".yml");
        Map<String, Double> balances = new HashMap<>();

        if (!playerFile.exists()) {
            return balances;
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(playerFile);
        ConfigurationSection section = config.getConfigurationSection("balances");
        
        if (section != null) {
            for (String key : section.getKeys(false)) {
                balances.put(key, section.getDouble(key));
            }
        }

        return balances;
    }
}
