package AranProblem;

public class Aran {

    private int level;
    private int jobAdvancement;
    private static int[] jobAdvMap={-1, 10, 20, 30, 40, 50};

    //this is default case whenever the main class calls without passing any values this will be the output value
    //empty constructor without paramter = initializing the private instance variables
    public Aran(){
        this.level = 300;
        this.jobAdvancement = -1;
    }

    //Getting the value of jobAdvancement from the user then allocating the value of level
    //contructor with parameter
    public Aran(int jobAdvancement){
        this.jobAdvancement = jobAdvancement;
        this.level = jobAdvMap[jobAdvancement];
    }

    public int getlevel(){
        return level;
    }

    public int setlevel(int level){
        //Sets a range so that the value of level is between 0 and 300
        return this.level = Math.min(Math.max(0,level),300);
    }

    public static boolean isValid(String input) {
    char[] chars = input.toCharArray();
    char prevLetter = '\0';

    for (char currentLetter : chars) {
        if (currentLetter == 'B') {
            if (prevLetter != '\0' && (prevLetter == 'P' || prevLetter == 'M')) {
                return false;
            }
        } else if (currentLetter == 'P') {
            if (prevLetter != '\0' && (prevLetter == 'B' || prevLetter == 'M')) {
                return false;
            }
        } else if (currentLetter == 'T') {
            if (prevLetter != 'P' && prevLetter != 'B') {
                return false;
            }
        } else if (currentLetter == 'M') {
            if (prevLetter != 'T') {
                return false;
            }
        } else {
            return false; // Only allow characters 'B', 'P', 'T', and 'M'
        }

        prevLetter = currentLetter;
    }

    // Check if the last character is 'M' to allow the pattern "BPTMBPTM"
    return prevLetter == 'M';
}

    

    public String toString(){
        return String.format("Aran Info\nLevel : " + level +"\nJob Adv : " + jobAdvancement);
        //return "Aran Info\nLevel : " + level + "\nJob Adv : " + jobAdvancement; 
        //or can do like this 
    }
}
