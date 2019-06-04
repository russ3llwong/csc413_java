package interpreter.bytecode;

import interpreter.VirtualMachine;

import java.util.ArrayList;

public class DumpCode extends ByteCode {
    String dump; //for the string value of the argument

    @Override
    public void init(ArrayList<String> args) {
        dump = args.get(0);
    }

    @Override
    public void execute(VirtualMachine vm) {
        vm.switchDumpState(dump);
    }

    @Override
    public String dumpPrint() {
        return null;
    }
}
