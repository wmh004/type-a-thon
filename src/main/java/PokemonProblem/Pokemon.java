package PokemonProblem;

public class Pokemon {
    private String name;
    private String type;
    private double strength;

    public Pokemon(String name, String type, double strength){
        this.name = name;
        this.type = type;
        this.strength = strength;
    }

    //useful in scenarios where you need to sort or order Pokemon objects.
    public int compareTo(Pokemon o){
        return Double.compare(this.strength,o.strength);
    }

    public String getName(){
        return name;
    }
    public String getType(){
        return type;
    }
    public double getStrength(){
        return strength;
    }

    public String toString(){
        return this.name;
    }
    //example if System.out.println(pikachu.toString());
    //the output will be pikachu



    /*
    public String toString() {
        return "Pokemon{name='" + name + "', type='" + type + "', strength=" + strength + "}";
    }
    in the main class:
    Pokemon pikachu = new Pokemon("Pikachu", "Electric", 85.5);
    System.out.println(pikachu.toString());
    Output: Pokemon{name='Pikachu', type='Electric', strength=85.5}*/
}
