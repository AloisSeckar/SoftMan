package elrh.softman.logic;

public class Player {
    
    private final String name;
    private int number;

    public Player(String player, int number) {
        this.name = player;
        this.number = number;
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

    @Override
    public String toString() {
        return "#" + number + " " + name;
    }
    
}
