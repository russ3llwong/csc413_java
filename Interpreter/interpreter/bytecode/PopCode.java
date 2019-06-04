package interpreter.bytecode;

import interpreter.VirtualMachine;

import java.util.ArrayList;

public class PopCode extends ByteCode {
    private int n = 0; //n determines how many numbers to be popped

    @Override
    public void init(ArrayList<String> args) {
            n = Integer.parseInt(args.get(0));
    }

    @Override
    public void execute(VirtualMachine vm) {
        //to pop the vm n times
        for(int i=0;i<n; i++){
            vm.popRunStack();
        }
    }

    @Override
    public String dumpPrint() {
        return "POP " + n;
    }
}
