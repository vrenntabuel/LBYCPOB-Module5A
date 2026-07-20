package ph.edu.dlsu.lbycpob.pokemonwebapp.model;

// Abstract Pokemon class
public abstract class AbstractPokemon implements PokemonOperations {
    protected int instanceId;
    protected String name;
    protected double weight;
    protected double height;
    protected double attack;
    protected double defense;
    protected double stamina;
    protected String type;

    public AbstractPokemon(int instanceId, String name, double weight, double height,
                           double attack, double defense, double stamina, String type) {
        this.instanceId = instanceId;
        this.name = name;
        this.weight = weight;
        this.height = height;
        this.attack = attack;
        this.defense = defense;
        this.stamina = stamina;
        this.type = type;
    }

    // Abstract method to be implemented by subclasses
    public abstract String getSpecialAbility();

    // Polymorphic method implementation
    @Override
    public double calculatePowerLevel() {
        return (attack + defense + stamina) * 100;
    }

    @Override
    public void displayInfo() {
        IO.println("Pokemon: " + name + " (" + type + ")");
        IO.println("Power Level: " + calculatePowerLevel());
    }

    public int getInstanceId() { return instanceId; }
    public String getName() { return name; }
    public double getWeight() { return weight; }
    public double getHeight() { return height; }
    public double getAttack() { return attack; }
    public double getDefense() { return defense; }
    public double getStamina() { return stamina; }
    public String getType() { return type; }
}