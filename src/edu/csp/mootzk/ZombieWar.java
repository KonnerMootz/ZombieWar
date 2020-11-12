package edu.csp.mootzk;

import java.util.Random;

public class ZombieWar {
    public static void main(String[] args) {
        Survivor[] survivors = generateSurvivors();
        Zombie[] zombies = generateZombies();

        printInitialMessage(survivors, zombies);

        while(survivors.length > numDeadSurvivors(survivors) && zombies.length > numDeadZombies(zombies)) {
            attackSequence(survivors, zombies);
        }

        printFinalSurvivorCount(survivors);
    }

    static Survivor[] generateSurvivors(){
        Random rand = new Random();
        int randSurvivors = rand.nextInt(9) + 1; // +1 to prevent zero bound (1-10 survivors)
        Survivor[] survivors = new Survivor[randSurvivors];
        int numSoldiers = rand.nextInt(randSurvivors);
        int numTeachers = rand.nextInt(randSurvivors - numSoldiers);
        int numChildren = randSurvivors - numSoldiers - numTeachers;

        int counter = 0;
        for (int x = 0; x < numSoldiers; x++) {
            survivors[counter] = new Soldier(x + 1); // +1 so ID starts at 1 versus zero
            counter++;
        }
        for (int x = 0; x < numTeachers; x++) {
            survivors[counter] = new Teacher(x + 1);
            counter++;
        }
        for (int x = 0; x < numChildren; x++) {
            survivors[counter] = new Child(x + 1);
            counter++;
        }

        return survivors;
    }

    static Zombie[] generateZombies(){
        Random rand = new Random();
        int numZombies = rand.nextInt(9) + 1;
        Zombie[] zombies = new Zombie[numZombies];
        int numCommonInfected = rand.nextInt(numZombies);
        int numTanks = numZombies - numCommonInfected;

        int counter = 0;
        for (int x = 0; x < numCommonInfected; x++) {
            zombies[counter] = new CommonInfected(x + 1); // +1 so ID starts at 1 versus zero
            counter++;
        }
        for (int x = 0; x < numTanks; x++) {
            zombies[counter] = new Tank(x + 1);
            counter++;
        }

        return zombies;
    }

    static int getNumSurvivors(Survivor[] survivors) {
        return survivors.length;
    }

    static int getChildCount(Survivor[] survivors) {
        int numChildren = 0;

        for(Survivor survivor : survivors) {
            if (getSurvivorName(survivor).contains("Child")) {
                numChildren++;
            }
        }

        return numChildren;
    }

    static int getTeacherCount(Survivor[] survivors) {
        int numTeachers = 0;

        for(Survivor survivor : survivors) {
            if (getSurvivorName(survivor).contains("Teacher")) {
                numTeachers++;
            }
        }

        return numTeachers;
    }

    static int getSoldierCount(Survivor[] survivors) {
        int numSoldiers = 0;

        for(Survivor survivor : survivors) {
            if (getSurvivorName(survivor).contains("Soldier")) {
                numSoldiers++;
            }
        }

        return numSoldiers;
    }

    static int getNumZombies(Zombie[] zombies) {
        return zombies.length;
    }

    static int getCommonInfectedCount(Zombie[] zombies) {
        int numCommonInfected = 0;

        for(Zombie zombie : zombies) {
            if (getZombieName(zombie).contains("Common")) {
                numCommonInfected++;
            }
        }

        return numCommonInfected;
    }

    static int getTankCount(Zombie[] zombies) {
        int numTanks = 0;

        for(Zombie zombie : zombies) {
            if (getZombieName(zombie).contains("Tank")) {
                numTanks++;
            }
        }

        return numTanks;
    }

    static String getSurvivorName(Survivor survivor) {
        return survivor.getClass().getSimpleName() + " " + survivor.getID();
    }

    static String getZombieName(Zombie zombie) {
        return zombie.getClass().getSimpleName() + " " + zombie.getID();
    }

    static void attackSequence(Survivor[] survivors, Zombie[] zombies) {
        for (Survivor survivor : survivors) {
            if (!survivor.isDead) { // Check if survivor is alive, only attack if not dead
                for (Zombie zombie : zombies) {
                    if (!zombie.isDead) {
                        survivor.attack(zombie, survivor.getAttack()); // Survivors attack zombies first
                        zombie.isDead(zombie.getHealth()); // Check/set if zombie is dead by checking if health is less than zero

                        if (zombie.isDead) {
                            System.out.println("\t" + getSurvivorName(survivor) + " killed " + getZombieName(zombie));
                        }
                    }
                }
            }
        }

        for (Zombie zombie : zombies) {
            if (!zombie.isDead) { // Check if zombie is alive, only attack if not dead
                for (Survivor survivor : survivors) {
                    if (!survivor.isDead) {
                        zombie.attack(survivor, zombie.getAttack()); // Zombies attack survivors
                        survivor.isDead(survivor.getHealth()); // Check/set if survivor is dead by checking if health is less than zero

                        if (survivor.isDead) {
                            System.out.println("\t" + getZombieName(zombie) + " killed " + getSurvivorName(survivor));
                        }
                    }
                }
            }
        }
    }

    static int numDeadSurvivors(Survivor[] survivors) {
        int numDeadSurvivors = 0;

        for(Survivor survivor : survivors) {
            if (survivor.isDead) {
                numDeadSurvivors++;
            }
        }
        return numDeadSurvivors;
    }

    static int numDeadZombies(Zombie[] zombies) {
        int numDeadZombies = 0;

        for(Zombie zombie : zombies) {
            if (zombie.isDead) {
                numDeadZombies++;
            }
        }
        return numDeadZombies;
    }

    static void printInitialMessage(Survivor[] survivors, Zombie[] zombies) {
        int numSurvivors = getNumSurvivors(survivors);
        int numZombies = getNumZombies(zombies);
        int numChildren = getChildCount(survivors);
        int numTeachers = getTeacherCount(survivors);
        int numSoldiers = getSoldierCount(survivors);
        int numCommonInfected = getCommonInfectedCount(zombies);
        int numTanks = getTankCount(zombies);
        String stringSurvivors = "We have " + numSurvivors + " survivors trying to make it to safety (";
        String stringZombies = "But there are " + numZombies +  " zombies waiting for them (";
        String stringChildren = "Children";
        String stringTeachers = "Teachers";
        String stringSoldiers = "Soldiers";
        String stringTanks = "Tanks";

        // Change strings to singular form if only one object exists in that category
        if (numSurvivors == 1) {
            stringSurvivors = "We have " + numSurvivors + " survivor trying to make it to safety (";
        }
        if (numZombies == 1) {
            stringZombies = "But there are " + numZombies +  " zombies waiting for them (";
        }
        if (numChildren == 1) {
            stringChildren = "Child";
        }
        if (numTeachers == 1) {
            stringTeachers = "Teacher";
        }
        if (numSoldiers == 1) {
            stringSoldiers = "Soldier";
        }
        if (numTanks == 1) {
            stringTanks = "Tank";
        }

        System.out.println(stringSurvivors
                + numChildren + " " + stringChildren + ", "
                + numTeachers + " " + stringTeachers + ", "
                + numSoldiers + " " + stringSoldiers + ")");
        System.out.println(stringZombies
                + numCommonInfected + " Common Infected, "
                + numTanks + " " + stringTanks + ")\n");
    }

    static void printFinalSurvivorCount(Survivor[] survivors) {
        // Print message stating if survivor(s) made it to safety
        if (survivors.length == numDeadSurvivors(survivors)) {
            if (survivors.length == 1) {
                System.out.println("\nThe survivor didn't make it.");
            }
            else {
                System.out.println("\nNone of the survivors made it.");
            }
        }
        // Some survivor(s) made it to safety
        else {
            if (survivors.length == 1) {
                System.out.println("\nIt seems they made it to safety.");
            }
            else {
                System.out.println("\nIt seems " + (survivors.length - numDeadSurvivors(survivors)) + " have made it to safety.");
            }
        }
    }
}

abstract class Survivor {
    int health;
    int attack;
    boolean isDead;

    protected abstract int getID();

    public int getHealth() {
        return this.health;
    }

    public void setHealth(int attack) {
        this.health = health - attack;
    }

    int getAttack(){
        return attack;
    }

    void attack(Zombie zombie, int attack) {
        zombie.setHealth(attack);
    }

    void isDead(int health) {
        if (health <= 0) {
            isDead = true;
        }
    }
}

class Soldier extends Survivor {
    int id;
    int health = 100;
    int attack = 10;

    public Soldier(int id) {
        this.id = id;
    }

    @Override
    protected int getID() {
        return id;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int attack) {
        this.health = health - attack;
    }

    @Override
    public int getAttack() {
        return attack;
    }

    void attack(Zombie zombie, int attack) {
        zombie.setHealth(attack);
    }
}

class Teacher extends Survivor {
    int id;
    int health = 50;
    int attack = 5;

    public Teacher(int id) {
        this.id = id;
    }

    @Override
    protected int getID() {
        return id;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int attack) {
        this.health = health - attack;
    }

    @Override
    public int getAttack() {
        return attack;
    }

    void attack(Zombie zombie, int attack) {
        zombie.setHealth(attack);
    }
}

class Child extends Survivor {
    int id;
    int health = 20;
    int attack = 2;

    public Child(int id) {
        this.id = id;
    }

    @Override
    protected int getID() {
        return id;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int attack) {
        this.health = health - attack;
    }

    @Override
    public int getAttack() {
        return attack;
    }

    void attack(Zombie zombie, int attack) {
        zombie.setHealth(attack);
    }
}

abstract class Zombie {
    int health;
    int attack;
    boolean isDead;

    protected abstract int getID();

    public int getHealth() {
        return this.health;
    }

    public void setHealth(int attack) {
        this.health = health - attack;
    }

    int getAttack(){
        return attack;
    }

    void attack(Survivor survivor, int attack) {
        survivor.setHealth(attack);
    }

    public void isDead(int health) {
        if (health <= 0) {
            isDead = true;
        }
    }
}

class CommonInfected extends Zombie {
    int id;
    int health = 30;
    int attack = 5;

    public CommonInfected(int id) {
        this.id = id;
    }

    @Override
    protected int getID() {
        return id;
    }

    public int getHealth() {
        return this.health;
    }

    public void setHealth(int attack) {
        this.health = health - attack;
    }

    public int getAttack() {
        return attack;
    }

    void attack(Survivor survivor, int attack) {
        survivor.setHealth(attack);
    }
}

class Tank extends Zombie {
    int id;
    int health = 150;
    int attack = 20;

    public Tank(int id) {
        this.id = id;
    }

    protected int getID() {
        return id;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int attack) {
        this.health = health - attack;
    }

    public int getAttack() {
        return attack;
    }

    void attack(Survivor survivor, int attack) {
        survivor.setHealth(attack);
    }
}

