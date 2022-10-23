/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package simulator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.Timer;
import display.MemoryVisualizer;
import display.UserInterface;
import java.awt.Container;
import model.Memory;
import model.Process;

/**
 *
 * @author cedor
 */
public class Simulator {
    private UserInterface ui;
    private Memory memory;
    private MemoryVisualizer memoryVisualizer;
    private Container memVisContainer;
    private ArrayList<Process> queueList;
    private ArrayList<Process> inMemoryList;
    private int currentTime = 0;
    private int coalesceTime = 1;
    private int compactTime = 1;
    private int rrCounter = 0;

    public Simulator() {
        queueList = new ArrayList<>();
        inMemoryList = new ArrayList<>();
        ui = new UserInterface(this);
        ui.setVisible(true);
        memVisContainer = ui.getMemVisContainer();
    }
    
    final private Timer simulation = new Timer(1000/2, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent ae) {
            if (inMemoryList.isEmpty() && queueList.isEmpty()) {
                simulation.stop();
                ui.toggleComponents();
                //end the simulation and output total time
                return;
            }

            //compacts memory when every compactTime interval
            if (currentTime%compactTime == 0) {
                int compactMoves = memory.compactPullUp();
                if (compactMoves > 0) {
                    currentTime += compactMoves;
                    memoryVisualizer.repaint();
                    return;
                }
            }
            
            //coalesce memory when every coalesceTime interval
            if (currentTime%coalesceTime == 0 && memory.coalesce()) {
                currentTime++;
                memoryVisualizer.repaint();
                return;
            }
            
            //allocate process and decrement its time after allocation
            for (int i = 0; i < queueList.size(); i++) {
                Process process = queueList.get(i);
                if (!process.isInMemory() && memory.allocateFirstFit(process)) {
                    if(!process.decrementTime()) inMemoryList.add(process);
                    queueList.remove(i);
                    currentTime++;
                    memoryVisualizer.repaint();
                    return;
                }
            }
            
            //run next job
            if (inMemoryList.get(rrCounter).decrementTime()) {
                inMemoryList.remove(rrCounter);
            } else if (rrCounter < inMemoryList.size()-1) {
                rrCounter++;
            } else {
                rrCounter = 0;
            }
            currentTime++;
            memoryVisualizer.repaint();
        }
    });
    
    public void addProcess(String name, int size, int time) {
        queueList.add(new Process(name, size, time));
    }
    
    public void clearProcess() {
        queueList.clear();
    }
    
    public void start(int memorySize, int coalesceTime, int compactTime) {
        ui.toggleComponents();
        memory = new Memory(memorySize);
        memoryVisualizer = new MemoryVisualizer(memory, memVisContainer.getWidth(), memVisContainer.getHeight());
        this.coalesceTime = coalesceTime;
        this.compactTime = compactTime;
        memVisContainer.removeAll();
        memVisContainer.add(memoryVisualizer);
        simulation.start();
    }
    
    
    public static void main(String args[]) {
        new Simulator();
    }
}
