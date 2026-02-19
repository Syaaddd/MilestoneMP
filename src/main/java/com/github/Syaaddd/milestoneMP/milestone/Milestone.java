package com.github.Syaaddd.milestoneMP.milestone;

import java.util.List;

public class Milestone {

    private final String id;
    private final MilestoneType type;
    private final int amount;
    private final List<MilestoneChoice> choices;

    public Milestone(String id, MilestoneType type, int amount, List<MilestoneChoice> choices) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.choices = choices;
    }

    public String getId() { return id; }
    public MilestoneType getType() { return type; }
    public int getAmount() { return amount; }
    public List<MilestoneChoice> getChoices() { return choices; }
    public boolean hasChoices() { return choices != null && !choices.isEmpty(); }
}
