package interpreter.bytecode;

import interpreter.VirtualMachine;

import java.util.ArrayList;

public class WriteCode extends ByteCode {
    //no variables as its only purpose is to print the top of the runTimeStack

    @Override
    public void init(ArrayList<String> args) {

    }

    @Override
    public void execute(VirtualMachine vm) {
        System.out.println("WRITE\n" + vm.peekRunStack());
    }

    @Override
    public String dumpPrint() {
        return "WRITE";
    }
}
