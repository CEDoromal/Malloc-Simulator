/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author cedor
 */
public class Memory {
    private int capacity;
    private ArrayList<Process> allocation = new ArrayList<>();
    
    public Memory(int capacity) {
        this.capacity = capacity;
        allocation.add(new Process("hole", capacity, 0));
    }

    public int getCapacity() {
        return capacity;
    }
    
    public ArrayList<Process> getAllocation() {
        return allocation;
    }
    
    //returns true if allocation is successful
    public boolean allocateFirstFit(Process process) {
        for (int i = 0; i < allocation.size(); i++) {
            Process allocated = allocation.get(i);
            if (allocated.isHole() && allocated.getSize() >= process.getSize()) {
                if (allocated.getSize() > process.getSize()) {
                    allocated.setSize(allocated.getSize()-process.getSize());
                } else {
                    allocation.remove(i);
                }
                allocation.add(i, process);
                process.setInMemory(true);
                return true;
            }
        }
        return false;
    }
    
    //returns true if we do coalesce
    public boolean coalesce() {
        boolean coalesced = false;
        for (int i = 0; i < allocation.size(); i++) {
            Process allocated = allocation.get(i);
            if (allocated.isHole()) {
                while (allocation.size() > i+1 && allocation.get(i+1).isHole()) {
                    allocated.setSize(allocated.getSize()+allocation.get(i+1).getSize());
                    allocation.remove(i+1);
                    coalesced = true;
                }
            }
        }
        return coalesced;
    }
    
    //returns number of moves
    public int compactPullUp() {
        int moves = 0;
        int slotIndex = -1;
        for (int i = 0; i < allocation.size(); i++) {
            Process allocated = allocation.get(i);
            if (slotIndex < 0 && allocated.isHole()) {
                slotIndex = i;
            } else if (slotIndex > 0 && !allocated.isHole()) {
                Collections.swap(allocation, slotIndex, i);
                slotIndex++;
                moves++;
            }
        }
        if(coalesce()) {
            moves++;
        }
        return moves;
    }
}
