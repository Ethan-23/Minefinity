package org.evasive.me.minevolutionCore.enchanting.runicMatrix;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Transformation;
import org.evasive.me.minevolutionCore.MinevolutionCore;
import org.evasive.me.minevolutionCore.mining.PickaxeStatFunctions;
import org.evasive.me.minevolutionCore.enchanting.enchantments.Rarity;
import org.evasive.me.minevolutionCore.enchanting.enchantments.commands.EnchantFunctions;
import org.evasive.me.minevolutionCore.enchanting.enchantments.EnchantType;
import org.evasive.me.minevolutionCore.enchanting.runicMatrix.objects.EnchantOrb;
import org.evasive.me.minevolutionCore.enchanting.runicMatrix.objects.EnchantableItem;
import org.evasive.me.minevolutionCore.enchanting.runicMatrix.objects.RunicMatrix;
import org.evasive.me.minevolutionCore.utils.ComponentUtils;
import org.evasive.me.minevolutionCore.utils.RomanNumeralUtil;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.*;

public class MatrixManager {

    private final Map<UUID, RunicMatrix> matrixMap = new HashMap<>();

    public boolean hasItem(UUID uuid){
        return matrixMap.containsKey(uuid);
    }

    public ItemStack getItem(UUID uuid){
        return matrixMap.get(uuid).getEnchantableItem().getItemStack();
    }

    public void addItem(UUID uuid, ItemStack itemStack, Location location, Player player){
        ItemDisplay itemDisplay = sendDisplayEntity(itemStack, location, player);
        List<EnchantOrb> enchants = new ArrayList<>();
        EnchantableItem enchantableItem = new EnchantableItem(itemStack, itemDisplay, startRotation(itemDisplay));
        RunicMatrix runicMatrix = new RunicMatrix(enchantableItem, enchants);
        matrixMap.put(uuid, runicMatrix);
        spawnEnchants(player, location);
    }

    public void spawnEnchants(Player player, Location location){
        List<Location> locations = new ArrayList<>(Arrays.asList(new Location(location.getWorld(), 4, -0.5 ,0), new Location(location.getWorld(),-4, -0.5, 0), new Location(location.getWorld(),0, -0.5, 4), new Location(location.getWorld(),0, -0.5, -4), new Location(location.getWorld(),3, -0.5, 3),new Location(location.getWorld(),-3, -0.5, 3), new Location(location.getWorld(),-3, -0.5, -3),new Location(location.getWorld(),3, -0.5, -3)));
        List<EnchantType> enchantList = getRandomEnchant(player);
        for (int i = 0; i < enchantList.size(); i++){
            Random random = new Random();
            int rate = random.nextInt(100) + 1;
            EnchantType enchantType = enchantList.get(i);
            Location tempLocation = location.clone().add(locations.get(i));
            Rarity rarity = enchantType.getPickaxeEnchantBuilder().getRarity();
            ItemDisplay itemDisplay = sendDisplayEntity(new ItemStack(rarity.getRarityBuilder().getTierMaterial()), tempLocation, player);
            TextDisplay textDisplay = sendTextDisplayEntity(enchantType, tempLocation, player, rate);
            Interaction interaction = sendInteractionEntity(tempLocation.clone().subtract(0f, 0.5f,0f), player, i);
            EnchantOrb enchantOrb = new EnchantOrb(enchantType, rate, itemDisplay, textDisplay, interaction, startRotation(itemDisplay));
            matrixMap.get(player.getUniqueId()).addEnchant(enchantOrb);
        }
    }

    public List<EnchantType> getRandomEnchant(Player player){
        ItemStack item = matrixMap.get(player.getUniqueId()).getEnchantableItem().getItemStack();
        ItemMeta meta = item.getItemMeta();
        List<EnchantType> enchantTypes = new ArrayList<>();
        for (EnchantType enchantType : EnchantType.values()){
            if(!meta.getPersistentDataContainer().has(enchantType.getPickaxeEnchantBuilder().getKey())){
                enchantTypes.add(enchantType);
            }else if(meta.getPersistentDataContainer().get(enchantType.getPickaxeEnchantBuilder().getKey(), PersistentDataType.INTEGER) < enchantType.getPickaxeEnchantBuilder().getMaxLevel()){
                enchantTypes.add(enchantType);
            }
        }

        List<EnchantType> randomEnchantTypes = new ArrayList<>();

        while (!enchantTypes.isEmpty() && randomEnchantTypes.size() < 8){
            Random random = new Random();
            int randomNumber = 0;
            if(enchantTypes.size() != 1) {
                randomNumber = random.nextInt(enchantTypes.size() - 1);
            }
            randomEnchantTypes.add(enchantTypes.get(randomNumber));
            enchantTypes.remove(randomNumber);
        }
        return randomEnchantTypes;
    }

    public void removeDisplay(UUID uuid){
        matrixMap.get(uuid).getEnchantableItem().getItemDisplay().remove();
        for(int i = 0; i < matrixMap.get(uuid).getEnchants().size(); i++){
            matrixMap.get(uuid).getEnchants().get(i).getItemDisplay().remove();
        }
    }

    public void removeTextDisplay(UUID uuid){
        for(int i = 0; i < matrixMap.get(uuid).getEnchants().size(); i++){
            matrixMap.get(uuid).getEnchants().get(i).getTextDisplay().remove();
        }
    }

    public void removeInteracts(UUID uuid){
        for(int i = 0; i < matrixMap.get(uuid).getEnchants().size(); i++){
            matrixMap.get(uuid).getEnchants().get(i).getInteraction().remove();
        }
    }

    public int startRotation(ItemDisplay itemDisplay){
        final int durationInTicks = 2;  // Run every 2 ticks (10 times per second)
        final float rotationSpeed = 6f; // Slower rotation for smoother effect
        final float bounceHeight = 0.1f; // Maximum bounce height
        final float bounceSpeed = 1f;   // Speed of the bounce

        return Bukkit.getScheduler().runTaskTimer(MinevolutionCore.getCore(), new Runnable() {
            private float angle = 0.0f;       // Current rotation angle
            private float bounceTime = 0.0f;  // Time tracker for bounce effect

            @Override
            public void run() {
                // Update rotation angle
                angle += rotationSpeed; // Increase angle by rotationSpeed
                if (angle >= 360.0f) {
                    angle -= 360.0f; // Wrap the angle back to 0-360
                }

                // Calculate bounce position using sine wave
                bounceTime += bounceSpeed * (durationInTicks * 50 / 1000.0f); // Increment bounceTime
                float bounceOffset = (float) Math.sin(bounceTime) * bounceHeight; // Calculate Y offset

                // Update transformation
                Transformation transformation = itemDisplay.getTransformation();

                // Update rotation (LeftRotation)
                transformation.getLeftRotation().set(new Quaternionf().rotateY((float) Math.toRadians(angle)));

                // Update bounce (translation)
                Vector3f translation = transformation.getTranslation();  // Get current translation (position)
                translation.set(translation.x, bounceOffset, translation.z);  // Modify Y for bounce
                transformation.getTranslation().set(translation);  // Set the updated translation

                // Apply transformation with smoother interpolation
                itemDisplay.setTransformation(transformation);
                itemDisplay.setInterpolationDuration(durationInTicks * 2);  // Adjust to make it smoother
                itemDisplay.setInterpolationDelay(0);
            }
        }, 0L, durationInTicks).getTaskId();
    }

    public Interaction sendInteractionEntity(Location location, Player player, int index) {
        Interaction interaction = location.getWorld().spawn(location, Interaction.class);
        interaction.setInteractionHeight(1f);
        interaction.setInteractionWidth(0.5f);
        NamespacedKey namespacedKey = new NamespacedKey(MinevolutionCore.getCore(), "enchantingID");
        interaction.getPersistentDataContainer().set(namespacedKey, PersistentDataType.INTEGER, index);
        player.showEntity(MinevolutionCore.getCore(), interaction);
        return interaction;
    }

    public ItemDisplay sendDisplayEntity(ItemStack itemStack, Location location, Player player){
        ItemDisplay itemDisplay = location.getWorld().spawn(location, ItemDisplay.class);
        Transformation transformation = itemDisplay.getTransformation();
        transformation.getScale().set(0.75f);
        itemDisplay.setTransformation(transformation);
        itemDisplay.setVisibleByDefault(false);
        player.showEntity(MinevolutionCore.getCore(), itemDisplay);
        itemDisplay.setItemStack(itemStack);
        itemDisplay.setPersistent(true);
        itemDisplay.setDisplayWidth(0.5f);
        itemDisplay.setDisplayHeight(0.5f);
        return itemDisplay;
    }

    public TextDisplay sendTextDisplayEntity(EnchantType enchantType, Location location, Player player, int rate){
        TextDisplay textDisplay = location.getWorld().spawn(location.clone().add(0f, 0.5f, 0f), TextDisplay.class);
        Transformation transformation = textDisplay.getTransformation();
        transformation.getScale().set(0.75f);
        textDisplay.setTransformation(transformation);
        textDisplay.setVisibleByDefault(false);
        player.showEntity(MinevolutionCore.getCore(), textDisplay);
        ItemMeta meta = matrixMap.get(player.getUniqueId()).getEnchantableItem().getItemStack().getItemMeta();
        int level = 1;
        if(meta.getPersistentDataContainer().has(enchantType.getPickaxeEnchantBuilder().getKey())){
            level = meta.getPersistentDataContainer().get(enchantType.getPickaxeEnchantBuilder().getKey(), PersistentDataType.INTEGER) + 1;
        }
        Rarity rarity = enchantType.getPickaxeEnchantBuilder().getRarity();
        TextColor enchantColor = rarity.getRarityBuilder().getTextColor();
        textDisplay.text(ComponentUtils.makeText(enchantType.getPickaxeEnchantBuilder().getName() + " " + RomanNumeralUtil.intToRoman(level), enchantColor, true).append(ComponentUtils.makeText("\n Success Rate: ", NamedTextColor.GOLD, false).append(ComponentUtils.makeText(rate + "% ", NamedTextColor.GREEN, false))));
        textDisplay.setPersistent(true);
        textDisplay.setBillboard(Display.Billboard.CENTER);
        return textDisplay;
    }

    public void stopRotation(UUID uuid){
        for (int i = 0; i < matrixMap.get(uuid).getEnchants().size(); i++){
            Bukkit.getScheduler().cancelTask(matrixMap.get(uuid).getEnchants().get(i).getRunnable());
        }
    }

    public void removePlayer(UUID uuid){
        stopRotation(uuid);
        removeDisplay(uuid);
        removeTextDisplay(uuid);
        removeInteracts(uuid);
        matrixMap.remove(uuid);
    }

    public EnchantOrb getEnchantOrb(Player player, int index){
        return matrixMap.get(player.getUniqueId()).getEnchants().get(index);
    }

    public void applyEnchant(Player player, int index){
        ItemStack item = matrixMap.get(player.getUniqueId()).getEnchantableItem().getItemStack();
        EnchantFunctions enchantFunctions = new EnchantFunctions();
        Random random = new Random();
        int randomNumber = random.nextInt(100) + 1;
        int chance = getEnchantOrb(player,index).getSuccessRate();
        if(randomNumber <= chance){
            player.sendMessage("Enchant Successfully Applied");
            item = enchantFunctions.addEnchant(item, getEnchantOrb(player, index).getEnchantType(), 1);
        }else{
            player.sendMessage("Enchant Failed");
        }
        player.getInventory().addItem(item);
        removePlayer(player.getUniqueId());
    }

    public void increaseTier(Player player){
        ItemStack itemStack = matrixMap.get(player.getUniqueId()).getEnchantableItem().getItemStack();
        PickaxeStatFunctions pickaxeStatFunctions = new PickaxeStatFunctions();
        matrixMap.get(player.getUniqueId()).getEnchantableItem().setItemStack(pickaxeStatFunctions.tierUp(itemStack));
    }

}
