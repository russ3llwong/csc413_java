package interpreter.bytecode;

import interpreter.VirtualMachine;

import java.util.ArrayList;

public class CallCode extends ByteCode {
    private String label;
    private int address;
    private int topOfStack;

    @Override
    public void init(ArrayList<String> args) {
        label = args.get(0);
    }

    @Override
    public void execute(VirtualMachine vm) {
        //to save the current caller function address
        vm.pushReturnAddrs(vm.getPC());
        //to allow the vm to move to the target address/function that is called
        vm.setPC(address);
        if (vm.emptyRunStack()) {
            topOfStack = -1;
        } else {
            topOfStack = vm.peekRunStack();
        }
    }

    @Override
    public String dumpPrint() {
        String str; //string to be returned

        //in order to create the base ID
        String baseID;
        int index = label.indexOf("<");

        if (index >= 0) {
            baseID = label.substring(0, index);
        } else {
            baseID = label;
        }

        //if runstack is empty, no return value is included
        if (topOfStack == -1) {
            str = "CALL " + label + "    " + baseID + "()";
        } else {
            str = "CALL " + label + "    " + baseID + "(" + topOfStack + ")";
        }
        return str;
    }

    public void setAddress(int targetAddress) {
        this.address = targetAddress - 1;
    }

    public String getLabel() {
        return this.label;
    }
}
