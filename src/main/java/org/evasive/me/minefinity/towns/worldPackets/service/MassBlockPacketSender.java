package org.evasive.me.minefinity.towns.worldPackets.service;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerMultiBlockChange;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerMultiBlockChange.EncodedBlock;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.world.block.BlockState;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.evasive.me.minefinity.towns.worldPackets.ChunkSection;

import java.io.*;
import java.util.*;

public class MassBlockPacketSender {

    public ProtectedRegion getRegion(World world, String string){
        RegionContainer container = com.sk89q.worldguard.WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regionManager = container.get(BukkitAdapter.adapt(world));
        return regionManager.getRegion(string);
    }

    public Clipboard getClipboard(String schematicName){
        File file = new File(Bukkit.getServer().getPluginsFolder(),
                "FastAsyncWorldEdit/schematics/" + schematicName + ".schem");

        if(!file.exists()){
            Bukkit.getConsoleSender().sendMessage("Schematic not found!");
            return null;
        }

        ClipboardFormat format = ClipboardFormats.findByFile(file);

        if(format == null){
            Bukkit.getConsoleSender().sendMessage("NO FILE FOUND");
            return null;
        }

        Clipboard clipboard;

        try (InputStream input = new FileInputStream(file);
             ClipboardReader reader = format.getReader(input)) {

            clipboard = reader.read();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return clipboard;
    }

    public void showSchematic(Player player, String schematicName, BlockVector3 position){

        Clipboard clipboard = getClipboard(schematicName);
        Region region = clipboard.getRegion();
        BlockVector3 min = region.getMinimumPoint();

        Map<ChunkSection, List<EncodedBlock>> blockLocations = new HashMap<>();

        for (BlockVector3 pos : region) {

            BlockState blockState = clipboard.getBlock(pos);

            if (blockState.getBlockType().getMaterial().isAir()) continue;

            int worldX = position.x() + (pos.x() - min.x());
            int worldY = position.y() + (pos.y() - min.y());
            int worldZ = position.z() + (pos.z() - min.z());

            int chunkX = worldX >> 4;
            int chunkZ = worldZ >> 4;
            int sectionY = worldY >> 4;

            ChunkSection chunkSection = new ChunkSection(chunkX, sectionY, chunkZ);

            blockLocations.computeIfAbsent(chunkSection, k -> new ArrayList<>());

            int localX = worldX & 15;
            int localY = worldY & 15;
            int localZ = worldZ & 15;

            BlockData bukkitData = BukkitAdapter.adapt(blockState);
            WrappedBlockState state = WrappedBlockState.getByString(bukkitData.getAsString());

            if (state == null) continue;

            blockLocations.get(chunkSection).add(
                    new EncodedBlock(state, localX, localY, localZ)
            );
        }

        if(blockLocations.isEmpty()) return;

        processBlocksInBatches(player, blockLocations);
    }

    private void processBlocksInBatches(Player player, Map<ChunkSection, List<EncodedBlock>> blockDataMap) {
        for (Map.Entry<ChunkSection, List<EncodedBlock>> entry : blockDataMap.entrySet()) {
            ChunkSection sectionKey = entry.getKey();
            List<EncodedBlock> blocks = entry.getValue();
            sendBlockPackets(player, sectionKey, createEncodedBlockArray(blocks));
        }
    }

    public void createBlockReplacementMap(Player player, World world, ProtectedRegion region, Map<Material, Material> materialMap) {

        Map<ChunkSection, List<EncodedBlock>> blockLocations = new HashMap<>();

        for (int x = region.getMinimumPoint().x(); x <= region.getMaximumPoint().x(); x++) {
            for (int y = region.getMinimumPoint().y(); y <= region.getMaximumPoint().y(); y++) {
                for (int z = region.getMinimumPoint().z(); z <= region.getMaximumPoint().z(); z++) {

                    Block block = world.getBlockAt(x, y, z);
                    Material blockMaterial = block.getType();

                    if(!materialMap.containsKey(blockMaterial)) continue;

                    int bitMovement = 4;
                    int chunkX = x >> bitMovement;
                    int chunkZ = z >> bitMovement;
                    int sectionY = y >> bitMovement;

                    ChunkSection chunkSection = new ChunkSection(chunkX, sectionY, chunkZ);

                    if(!blockLocations.containsKey(chunkSection))
                        blockLocations.put(chunkSection, new ArrayList<>());

                    Location location = block.getLocation();

                    int chunkSize = 15;
                    int localX = x & chunkSize;
                    int localY = y & chunkSize;
                    int localZ = z & chunkSize;
                    WrappedBlockState state = WrappedBlockState.getByString(getFakeBlockData(location, materialMap.get(blockMaterial)).getAsString());

                    EncodedBlock encodedBlock = new EncodedBlock(
                            state,
                            localX,
                            localY,
                            localZ
                    );

                    blockLocations.get(chunkSection).add(encodedBlock);
                }
            }
        }
        if(blockLocations.isEmpty()) return;
        processBlocksInBatches(player, blockLocations);
    }

    private EncodedBlock[] createEncodedBlockArray(List<EncodedBlock> blocks) {
        EncodedBlock[] encoded =
                new EncodedBlock[blocks.size()];

        for (int i = 0; i < blocks.size(); i++) {
            encoded[i] = blocks.get(i);
        }

        return encoded;
    }

    public BlockData getFakeBlockData(Location location, Material newMaterial) {
        Block block = location.getBlock();

        BlockData originalData = block.getBlockData();

        BlockData newData = Bukkit.createBlockData(newMaterial);

        // Copy matching properties
        if (newData instanceof org.bukkit.block.data.Directional newDir &&
                originalData instanceof org.bukkit.block.data.Directional oldDir) {
            newDir.setFacing(oldDir.getFacing());
        }

        if (newData instanceof org.bukkit.block.data.type.Stairs newStairs &&
                originalData instanceof org.bukkit.block.data.type.Stairs oldStairs) {
            newStairs.setFacing(oldStairs.getFacing());
            newStairs.setHalf(oldStairs.getHalf());
            newStairs.setShape(oldStairs.getShape());
        }

        if (newData instanceof org.bukkit.block.data.type.Slab newSlab &&
                originalData instanceof org.bukkit.block.data.type.Slab oldSlab) {
            newSlab.setType(oldSlab.getType());
        }

        if (newData instanceof org.bukkit.block.data.Orientable newOrient &&
                originalData instanceof org.bukkit.block.data.Orientable oldOrient) {
            newOrient.setAxis(oldOrient.getAxis());
        }



        return newData;
    }

    private void sendBlockPackets(Player player, ChunkSection chunkSection, EncodedBlock[] blockData) {

        if(blockData == null || blockData.length == 0) return;

        WrapperPlayServerMultiBlockChange multiBlockChangePacket = new WrapperPlayServerMultiBlockChange
                (
                        new Vector3i(chunkSection.x(), chunkSection.y(), chunkSection.z()),
                        true,
                        blockData
                );
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, multiBlockChangePacket);

    }
}
