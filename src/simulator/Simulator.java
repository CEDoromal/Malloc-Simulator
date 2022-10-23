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
import javax.swing.JLabel;
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
    private ArrayList<Process> processList;
    private ArrayList<Process> queueList;
    private ArrayList<Process> inMemoryList;
    private int currentTime = 0;
    private int coalesceTime = 1;
    private int compactTime = 1;
    private int rrCounter = 0;

    public Simulator() {
        processList = new ArrayList<>();
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
                return;
            }

            //compacts memory when every compactTime interval
            if (currentTime%compactTime == 0) {
                int compactMoves = memory.compactPullUp();
                if (compactMoves > 0) {
                    currentTime += compactMoves;
                    memoryVisualizer.repaint();
                    ui.setTime(currentTime);
                    return;
                }
            }
            
            //coalesce memory when every coalesceTime interval
            if (currentTime%coalesceTime == 0 && memory.coalesce()) {
                currentTime++;
                memoryVisualizer.repaint();
                ui.setTime(currentTime);
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
                    ui.setTime(currentTime);
                    return;
                }
            }
            
            //run next job
            if (inMemoryList.get(rrCounter).decrementTime()) {
                inMemoryList.remove(rrCounter);
            } else {
                rrCounter++;
            }
            if(rrCounter >= inMemoryList.size()) rrCounter = 0;
            currentTime++;
            memoryVisualizer.repaint();
            ui.setTime(currentTime);
        }
    });
    
    public void addProcess(String name, int size, int time) {
        processList.add(new Process(name, size, time));
    }
    
    public void clearProcess() {
        processList.clear();
        queueList.clear();
        inMemoryList.clear();
    }
    
    public void start(int memorySize, int coalesceTime, int compactTime) {
        ui.toggleComponents();
        currentTime = 0;
        rrCounter = 0;
        this.coalesceTime = coalesceTime;
        this.compactTime = compactTime;
        memory = new Memory(memorySize);
        memoryVisualizer = new MemoryVisualizer(memory, memVisContainer.getWidth(), memVisContainer.getHeight());
        queueList.clear();
        for (Process process : processList) {
            queueList.add(new Process(process.getName(), process.getSize(), process.getTime(), process.getColor()));
        }
        inMemoryList.clear();
        memVisContainer.removeAll();
        memVisContainer.add(memoryVisualizer);
        simulation.start();
    }
    
    
    public static void main(String args[]) {
        new Simulator();
    }
}
