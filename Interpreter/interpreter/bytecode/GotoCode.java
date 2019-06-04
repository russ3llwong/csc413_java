package interpreter.bytecode;

import interpreter.VirtualMachine;

import java.util.ArrayList;

public class GotoCode extends ByteCode {
    private String label;
    private int address;

    @Override
    public void init(ArrayList<String> args) {
        label = args.get(0);
    }

    @Override
    public void execute(VirtualMachine vm) {
        vm.setPC(address); //set pc to target address
    }

    @Override
    public String dumpPrint() {
        return "GOTO " + label;
    }

    public void setAddress(int targetAddress){
        this.address = targetAddress - 1; //so the label gets printed
    }

    public String getLabel(){
        return this.label;
    }
}
