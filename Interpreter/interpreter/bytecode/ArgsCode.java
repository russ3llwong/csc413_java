package interpreter.bytecode;

import interpreter.VirtualMachine;

import java.util.ArrayList;

public class ArgsCode extends ByteCode {
    private int offset; //the offset determines how many arguments will be used prior to CALL

    @Override
    public void init(ArrayList<String> args) {
        offset = Integer.parseInt(args.get(0));
    }

    @Override
    public void execute(VirtualMachine vm) {
        //starting a new frame before calling a function, values in the latest frame will be the arguments
        vm.newFrameAtRunStack(offset);
    }

    @Override
    public String dumpPrint() {
        return "\nARGS " + offset;
    }
}
