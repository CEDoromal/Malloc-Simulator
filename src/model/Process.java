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

    public Process(String name, int size, int time) {
        this.name = (time>0)? name : "hole";
        this.size = size;
        this.time = time;
        color = (time>0)? Color.getHSBColor((float) Math.random(),(float) 0.7,(float) 0.7) : Color.GRAY;
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
    
    public void decrementTime() {
        time--;
        if (time <= 0) {
            name = "hole";
            color = Color.GRAY;
        }
    }

    public Color getColor() {
        return color;
    }
    
}
