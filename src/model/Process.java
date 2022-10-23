/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.awt.Color;

/**
 *
 * @author cedor
 */
public class Process {
    private String name;
    private int size;
    private int time;
    private Color color;
    private boolean hole = false;
    private boolean inMemory = false;

    public Process(String name, int size, int time) {
        this.size = size;
        this.time = time;
        hole = time <= 0;
        this.name = (hole)? "hole" : name;
        color = (hole)? Color.GRAY : Color.getHSBColor((float) Math.random(),(float) 0.7,(float) 0.7);
    }

    public Process(String name, int size, int time, Color color) {
        this.name = name;
        this.size = size;
        this.time = time;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }
    
    public void setSize(int size) {
        this.size = size;
    }

    public int getTime() {
        return time;
    }
    
    //returns true if process ended
    public boolean decrementTime() {
        time--;
        if (time <= 0) {
            turnIntoHole();
            return true;
        }
        return false;
    }

    public Color getColor() {
        return color;
    }

    public boolean isHole() {
        return hole;
    }
    
    public void turnIntoHole() {
        hole = true;
        time = 0;
        name = "hole";
        color = Color.gray;
    }

    public boolean isInMemory() {
        return inMemory;
    }

    public void setInMemory(boolean inMemory) {
        this.inMemory = inMemory;
    }
    
    
}
