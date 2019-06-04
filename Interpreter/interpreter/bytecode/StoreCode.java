package interpreter.bytecode;

import interpreter.VirtualMachine;

import java.util.ArrayList;

public class StoreCode extends ByteCode {
    private int offset; //offset determines how many numbers to be stored
    private String id; //id or name for variable if any
    private int topOfStack; //value from the top of runTimeStack

    @Override
    public void init(ArrayList<String> args) {
        //remove the first element and assign it to offset
        offset = Integer.parseInt(args.remove(0));
        //if args has the id, it will be assigned
        if(args.isEmpty()){
            id = "";
        }else{
            id = args.get(0);
        }
    }

    @Override
    public void execute(VirtualMachine vm) {
        topOfStack = vm.peekRunStack();
        vm.storeRunStack(offset);
    }

    @Override
    public String dumpPrint() {
        return "STORE " + offset + " " + id + "     " + id + " = " + topOfStack;
    }
}
