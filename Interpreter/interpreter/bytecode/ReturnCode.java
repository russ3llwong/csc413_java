package interpreter.bytecode;

import interpreter.VirtualMachine;

import java.util.ArrayList;

public class ReturnCode extends ByteCode {
    private String label;
    private int topOfStack;

    @Override
    public void init(ArrayList<String> args) {
        //if there is a label, it will be assigned to String variable label
        if (args.isEmpty()) {
            label = "";
        } else {
            label = args.get(0);
        }
    }

    @Override
    public void execute(VirtualMachine vm) {
        //to allow the vm to return to the caller function
        vm.setPC(vm.popReturnAddrs());
        //a frame is popped to return its value
        vm.popFrameStack();
        topOfStack = vm.peekRunStack();
    }

    @Override
    public String dumpPrint() {
        String str; //string to be returned

        if (label == "") {
            str = "RETURN"; //if no label, just return "RETURN"
        } else {
            //in order to create the baseID
            String baseID;
            int index = label.indexOf("<");

            if (index >= 0) {
                baseID = label.substring(0, index);
            } else {
                baseID = label;
            }

            //final string is constructed if there exists a label
            str = "RETURN " + label + "    exit " + baseID + ": " + topOfStack;
        }
        return str;
    }
}
