package elrh.softman.logic;

import java.util.Random;

public class Player {
    
    private final String name;
    private int number;
    
    private int battingSkill;
    private int pitchingSkill;
    private int fieldingSkill;
    

    public Player(String player, int number) {
        this.name = player;
        this.number = number;
        
        Random rand = new Random();
        this.battingSkill = rand.nextInt(100) + 1;
        this.pitchingSkill = rand.nextInt(100) + 1;
        this.fieldingSkill = rand.nextInt(100) + 1;
    }
    
    public String getName() {
        return name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getBattingSkill() {
        return battingSkill;
    }

    public void setBattingSkill(int battingSkill) {
        this.battingSkill = battingSkill;
    }

    public int getPitchingSkill() {
        return pitchingSkill;
    }

    public void setPitchingSkill(int pitchingSkill) {
        this.pitchingSkill = pitchingSkill;
    }

    public int getFieldingSkill() {
        return fieldingSkill;
    }

    public void setFieldingSkill(int fieldingSkill) {
        this.fieldingSkill = fieldingSkill;
    }

    @Override
    public String toString() {
        return "#" + number + " " + name;
    }
    
}
