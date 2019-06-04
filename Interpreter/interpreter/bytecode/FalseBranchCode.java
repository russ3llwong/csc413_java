package interpreter.bytecode;

import interpreter.VirtualMachine;

import java.util.ArrayList;

public class FalseBranchCode extends ByteCode {
    private String label;
    private int address;

    @Override
    public void init(ArrayList<String> args) {
        label = args.get(0);
    }

    @Override
    public void execute(VirtualMachine vm) {
        //if top of stack if false, then branch to the address
        if(vm.popRunStack() == 0){
            vm.setPC(address);
        }
    }

    @Override
    public String dumpPrint() {
        return "FALSEBRANCH " + label;
    }

    public void setAddress(int targetAddress){
        this.address = targetAddress - 1; //so the labels get printed
    }

    public String getLabel(){
        return this.label;
    }
}
