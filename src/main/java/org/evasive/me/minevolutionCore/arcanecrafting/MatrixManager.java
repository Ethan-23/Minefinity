package org.evasive.me.minevolutionCore.arcanecrafting;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnEntity;
import com.sk89q.worldedit.math.Vector3;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Transformation;
import org.evasive.me.minevolutionCore.MinevolutionCore;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.*;

public class MatrixManager {

    private Map<UUID, RunicMatrix> matrixMap = new HashMap<>();

    public ItemStack getItem(UUID uuid){
        return matrixMap.get(uuid).getPedestalMain();
    }

    public boolean hasItem(UUID uuid){
        return matrixMap.containsKey(uuid);
    }

    public void addItem(UUID uuid, ItemStack itemStack, Location location, Player player){
        ItemDisplay itemDisplay = sendDisplayPacket(itemStack, location, player);
        List<Integer> runnables = new ArrayList<>();
        runnables.add(startRotation(itemDisplay));
        RunicMatrix runicMatrix = new RunicMatrix(itemStack, itemDisplay, runnables);
        matrixMap.put(uuid, runicMatrix);
        spawnEnchants(player, location);
    }

    public void spawnEnchants(Player player, Location location){
        List<Location> locations = new ArrayList<>(Arrays.asList(new Location(location.getWorld(), 4, -0.5 ,0), new Location(location.getWorld(),-4, -0.5, 0), new Location(location.getWorld(),0, -0.5, 4), new Location(location.getWorld(),0, -0.5, -4), new Location(location.getWorld(),3, -0.5, 3),new Location(location.getWorld(),-3, -0.5, 3), new Location(location.getWorld(),-3, -0.5, -3),new Location(location.getWorld(),3, -0.5, -3)));
        List<ItemDisplay> enchantList = new ArrayList<>();
        matrixMap.get(player.getUniqueId()).setEnchants(enchantList);
        List<ItemStack> rarity = new ArrayList<>(Arrays.asList(new ItemStack(Material.GRAY_DYE), new ItemStack(Material.LIME_DYE), new ItemStack(Material.CYAN_DYE), new ItemStack(Material.YELLOW_DYE), new ItemStack(Material.ORANGE_DYE)));
        for (int i = 0; i < 8; i++){
            Random random = new Random();
            int randomIndex = random.nextInt(rarity.size());
            ItemDisplay itemDisplay = sendDisplayPacket(rarity.get(randomIndex), location.clone().add(locations.get(i)), player);
            matrixMap.get(player.getUniqueId()).addEnchants(itemDisplay);
            matrixMap.get(player.getUniqueId()).addScheduler(startRotation(itemDisplay));
        }
    }

    public void removeDisplay(UUID uuid){
        matrixMap.get(uuid).getItemDisplayMain().remove();
        for(int i = 0; i < matrixMap.get(uuid).getEnchants().size(); i++){
            matrixMap.get(uuid).getEnchants().get(i).remove();
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

    public ItemDisplay sendDisplayPacket(ItemStack itemStack, Location location, Player player){
        ItemDisplay itemDisplay = location.getWorld().spawn(location, ItemDisplay.class);
        Transformation transformation = itemDisplay.getTransformation();
        transformation.getScale().set(0.75f);
        itemDisplay.setTransformation(transformation);
        itemDisplay.setVisibleByDefault(false);
        player.showEntity(MinevolutionCore.getCore(), itemDisplay);
        itemDisplay.setItemStack(itemStack);
        itemDisplay.setPersistent(true);
        return itemDisplay;
    }

    public void stopRotation(UUID uuid){
        for (int i = 0; i < matrixMap.get(uuid).getRotationScheduler().size(); i++){
            Bukkit.getScheduler().cancelTask(matrixMap.get(uuid).getRotationScheduler().get(i));
        }
    }

    public void removePlayer(UUID uuid){
        stopRotation(uuid);
        removeDisplay(uuid);
        matrixMap.remove(uuid);
    }

}
