/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package display;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;
import model.Memory;
import model.Process;

/**
 *
 * @author cedor
 */
public class MemoryVisualizer extends JPanel {
    private final Memory memory;
    private final int width, height;
    
    public MemoryVisualizer(Memory memory, int width, int height) {
        this.memory = memory;
        this.setSize(width, height);
        this.width = width;
        this.height = height;
    }
    
    @Override
    protected void paintComponent(Graphics g){
        //super.paintComponent(g);
        int posY = 0;
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        for (Process process : memory.getAllocation()) {
            int pHeight = posY + process.getSize()*height/memory.getCapacity();
            g.setColor(process.getColor());
            g.fillRect(0, posY, width, pHeight);
            g.setColor(Color.BLACK);
            g.drawRect(0, posY, width, pHeight);
            g.setColor(Color.WHITE);
            g.drawString(process.getName() + " - Size: " + process.getSize() + (!process.isHole()? " - Time: " + process.getTime() : ""), 3, pHeight-3);
            posY = pHeight;
        }
    }
}
