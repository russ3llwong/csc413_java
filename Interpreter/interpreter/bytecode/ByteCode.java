package interpreter.bytecode;

import interpreter.VirtualMachine;

import java.util.ArrayList;

public abstract class ByteCode {
    //abstract method for initializing bytecodes with arguments
    public abstract void init(ArrayList<String> args);

    //abstract method for executing a byte code
    public abstract void execute(VirtualMachine vm );

    //abstract method to return a string is dump is ON
    public abstract String dumpPrint();
}
