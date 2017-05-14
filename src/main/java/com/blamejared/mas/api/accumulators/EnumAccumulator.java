package com.blamejared.mas.api.accumulators;

public enum EnumAccumulator {
    REINFROCED_STONE(10000, 1000, 800), IRON(20000, 2000, 1800), STEEL(50000, 5000, 4800), FLUXED(100000, 10000, 9800);
    
    long capacity;
    long input;
    long output;
    
    EnumAccumulator(long capacity, long input, long output) {
        this.capacity = capacity;
        this.input = input;
        this.output = output;
    }
    
    public long getCapacity() {
        return capacity;
    }
    
    public void setCapacity(long capacity) {
        this.capacity = capacity;
    }
    
    public long getInput() {
        return input;
    }
    
    public void setInput(long input) {
        this.input = input;
    }
    
    public long getOutput() {
        return output;
    }
    
    public void setOutput(long output) {
        this.output = output;
    }
}
