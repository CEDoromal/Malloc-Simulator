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
    
    public MemoryVisualizer(Memory memory, int width, int height) {
        this.memory = memory;
        super.setSize(width, height);
    }
    
    @Override
    protected void paintComponent(Graphics g){
        //super.paintComponent(g);
        int width = this.getWidth();
        int height = this.getHeight();
        int posY = 0;
        g.setColor(Color.WHITE);
        System.out.println(g.getColor());
        g.fillRect(0, 0, width, height);
        for (Process process : memory.getAllocation()) {
            int pHeight = posY + process.getSize()*height/memory.getCapacity();
            g.setColor(process.getColor());
            System.out.println(g.getColor());
            g.fillRect(0, posY, width, pHeight);
            g.setColor(Color.WHITE);
            System.out.println(g.getColor());
            g.drawString(process.getName() + " - Size: " + process.getSize() + (!process.isHole()? " - Time: " + process.getTime() : ""), 0, pHeight);
            posY = pHeight;
        }
    }
}
