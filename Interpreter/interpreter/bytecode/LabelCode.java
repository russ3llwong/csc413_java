package interpreter.bytecode;

import interpreter.VirtualMachine;

import java.util.ArrayList;

public class LabelCode extends ByteCode {
    private String label;

    @Override
    public void init(ArrayList<String> args) {
        label = args.get(0);
    }

    @Override
    public void execute(VirtualMachine vm) {

    }

    @Override
    public String dumpPrint() {
        return "LABEL " + label;
    }

    public String getLabel(){
        return this.label;
    }
}
