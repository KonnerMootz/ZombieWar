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
            survivors[counter] = new Soldier();
            counter++;
        }
        for (int x = 0; x < numTeachers; x++) {
            survivors[counter] = new Teacher();
            counter++;
        }
        for (int x = 0; x < numChildren; x++) {
            survivors[counter] = new Child();
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
            zombies[counter] = new CommonInfected();
            counter++;
        }
        for (int x = 0; x < numTanks; x++) {
            zombies[counter] = new Tank();
            counter++;
        }

        return zombies;
    }

    static int getNumSurvivors(Survivor[] survivors) {
        return survivors.length;
    }

    static int getNumZombies(Zombie[] zombies) {
        return zombies.length;
    }

    static void attackSequence(Survivor[] survivors, Zombie[] zombies) {
        for (Survivor survivor : survivors) {
            if(!survivor.isDead) { // Check if survivor is alive, only attack if not dead
                for (Zombie zombie : zombies) {
                    if (!zombie.isDead) {
                        survivor.attack(zombie, survivor.getAttack()); // Survivors attack zombies first
                        zombie.isDead(zombie.getHealth()); // Check/set if zombie is dead by checking if health is less than zero
                    }
                }
            }
        }

        for (Zombie zombie : zombies) {
            if(!zombie.isDead) { // Check if zombie is alive, only attack if not dead
                for (Survivor survivor : survivors) {
                    if (!survivor.isDead) {
                        zombie.attack(survivor, zombie.getAttack()); // Zombies attack survivors
                        survivor.isDead(survivor.getHealth()); // Check/set if survivor is dead by checking if health is less than zero
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

        System.out.println("We have " + numSurvivors + " survivors trying to make it to safety.");
        System.out.println("But there are " + numZombies + " zombies waiting for them.");
    }

    static void printFinalSurvivorCount(Survivor[] survivors) {
        if(survivors.length == numDeadSurvivors(survivors)) {
            System.out.println("None of the survivors made it.");
        }
        else {
            System.out.println("It seems " + (survivors.length - numDeadSurvivors(survivors)) + " have made it to safety.");
        }
    }
}

abstract class Survivor {
    int health;
    int attack;
    boolean isDead;

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
    int health = 100;
    int attack = 10;

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
    int health = 50;
    int attack = 5;

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
    int health = 20;
    int attack = 2;

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
    int health = 30;
    int attack = 5;

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
    int health = 150;
    int attack = 20;

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

