package models;
import controllers.modelcontrollers.GameManager;

import java.io.Serializable;
import java.util.ArrayList;

public class Player extends Observable implements Serializable {
    private boolean isInQuarantine;
    private ArrayList<City> cities;
    private String name;
    private int id;
    private String color;
    private String pawn;
    private double money;
    private boolean isBankrupted;
    private int location;
    private boolean isInfected;
    private int quarantineTourCounter;
    private int infectTourCounter;
    private final int INFECTION_TIME = 1;
    private final int QUARANTINE_TIME = 2;

    public Player(String name, String color, String pawn, int id)
    {
        isInQuarantine = false;
        cities = new ArrayList<>();
        this.name = name;
        this.id = id;
        this.color = color;
        this.pawn = pawn;
        money = 200000;
        isBankrupted = false;
        location = 0;
        isInfected = false;
    }

    public void bankrupt() {
        isBankrupted = true;
        this.notifyGameLogObserver("bankrupt", 0);
    }

    public void quarantine(boolean bool)
    {
        if(bool){
            this.setLocation(14);
            if(!isInQuarantine){
                quarantineTourCounter = GameManager.getInstance().getTour();
            }
        }
        isInQuarantine = bool;
        this.notifyGameLogObserver("quarantine", bool ? 1 : 0);
    }

    public boolean checkQuarantine(){
        if(isInQuarantine) {
            int tourToLeaveQuarantine = quarantineTourCounter + QUARANTINE_TIME;
            if (tourToLeaveQuarantine == GameManager.getInstance().getTour()) {
                quarantine(false);
                return true;
            }
        }
        return false;
    }

    public void infect(boolean bool)
    {
        if(bool){
            if(!isInfected){
                infectTourCounter = GameManager.getInstance().getTour();
            }
        }
        isInfected = bool;
        this.notifyAllObservers();
        this.notifyGameLogObserver("infect", bool ? 1 : 0);
    }

    public boolean checkInfection(){
        if(isInfected){
            int tourToDisinfect = infectTourCounter + INFECTION_TIME;
            if(tourToDisinfect == GameManager.getInstance().getTour()){
                infect(false);
                return true;
            }
        }
        return false;
    }

    public void addMoney(double money)
    {
        this.money = this.money + money;
        if(isBankrupted && money > 0){
            isBankrupted = false;
        }
        this.notifyAllObservers();
        this.notifyGameLogObserver("money", money);
    }

    public void removeMoney(double money)
    {
        this.money = this.money - money;
        if(this.money < 0){
            isBankrupted = true;
        }
        this.notifyAllObservers();
        this.notifyGameLogObserver("money", -money);
    }

    public boolean removeCity(City city){
        if (cities.remove(city)){
            this.notifyAllObservers();
            this.notifyGameLogObserver("removeCity" + city.getName(), 0 );
            return true;
        }
        return false;
    }

    public void addCity(City city){
        cities.add(city);
        this.notifyAllObservers();
        this.notifyGameLogObserver("addCity" + city.getName(), 0 );
    }

    //  Getters - Setters

    public ArrayList<City> getCities() {
        return cities;
    }

    public void setLocation(int regionID)
    {
        location = regionID;
        this.notifyAllObservers();
    }

    public int getLocation()
    {
        return location;
    }

    public void setId(int id) { this.id = id; }

    public int getId()
    {
        return id;
    }

    public void setColor(String color) { this.color = color; }

    public String getColor()
    {
        return color;
    }

    public void setPawn(String pawn) { this.pawn = pawn; }

    public String getPawn() { return pawn; }

    public double getMoney()
    {
        return money;
    }

    public boolean isBankrupted()
    {
        return isBankrupted;
    }

    public boolean isInfected()
    {
        return isInfected;
    }

    public void setName(String name) { this.name = name; }

    public String getName()
    {
        return name;
    }

    public boolean isInQuarantine()
    {
        return isInQuarantine;
    }

    @Override
    public String toString() {
        return name;
    }
}
