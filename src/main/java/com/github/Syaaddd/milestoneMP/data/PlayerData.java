package com.github.Syaaddd.milestoneMP.data;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerData {

    private final UUID uuid;
    private int playtimeSeconds;
    private int blocksBroken;
    private int blocksPlaced;
    private int mobsKilled;
    private int playersKilled;
    private int joinDays;
    private long lastJoinTime;
    private Map<String, String> claimedMilestones;

    public PlayerData(UUID uuid) {
        this.uuid = uuid;
        this.playtimeSeconds = 0;
        this.blocksBroken = 0;
        this.blocksPlaced = 0;
        this.mobsKilled = 0;
        this.playersKilled = 0;
        this.joinDays = 0;
        this.lastJoinTime = System.currentTimeMillis();
        this.claimedMilestones = new HashMap<>();
    }

    public UUID getUuid() { return uuid; }
    public int getPlaytimeSeconds() { return playtimeSeconds; }
    public void setPlaytimeSeconds(int playtimeSeconds) { this.playtimeSeconds = playtimeSeconds; }
    public void addPlaytime(int seconds) { this.playtimeSeconds += seconds; }

    public int getBlocksBroken() { return blocksBroken; }
    public void setBlocksBroken(int blocksBroken) { this.blocksBroken = blocksBroken; }
    public void addBlockBreak(int amount) { this.blocksBroken += amount; }

    public int getBlocksPlaced() { return blocksPlaced; }
    public void setBlocksPlaced(int blocksPlaced) { this.blocksPlaced = blocksPlaced; }
    public void addBlockPlace(int amount) { this.blocksPlaced += amount; }

    public int getMobsKilled() { return mobsKilled; }
    public void setMobsKilled(int mobsKilled) { this.mobsKilled = mobsKilled; }
    public void addMobKill(int amount) { this.mobsKilled += amount; }

    public int getPlayersKilled() { return playersKilled; }
    public void setPlayersKilled(int playersKilled) { this.playersKilled = playersKilled; }
    public void addPlayerKill(int amount) { this.playersKilled += amount; }

    public int getJoinDays() { return joinDays; }
    public void setJoinDays(int joinDays) { this.joinDays = joinDays; }

    public long getLastJoinTime() { return lastJoinTime; }
    public void setLastJoinTime(long lastJoinTime) { this.lastJoinTime = lastJoinTime; }

    public Map<String, String> getClaimedMilestones() { return claimedMilestones; }
    public void setClaimedMilestones(Map<String, String> claimedMilestones) { this.claimedMilestones = claimedMilestones; }
    public void claimMilestone(String milestoneId, String choiceId) { this.claimedMilestones.put(milestoneId, choiceId); }
    public boolean hasClaimed(String milestoneId) { return claimedMilestones.containsKey(milestoneId); }
    public String getClaimedChoice(String milestoneId) { return claimedMilestones.get(milestoneId); }
}
