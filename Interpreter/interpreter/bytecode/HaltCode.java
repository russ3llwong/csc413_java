package interpreter.bytecode;

import interpreter.VirtualMachine;

import java.util.ArrayList;

public class HaltCode extends ByteCode {
    //no variables as its only purpose is to stop the program

    @Override
    public void init(ArrayList<String> args) {

    }

    @Override
    public void execute(VirtualMachine vm) {
        vm.haltProgram();
    }

    @Override
    public String dumpPrint() {
        return "HALT";
    }
}
