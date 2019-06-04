package interpreter.bytecode;

import interpreter.VirtualMachine;

import java.util.ArrayList;

public class LoadCode extends ByteCode {
    private int offset; //offset determines how many numbers to be loaded
    private String id; //id or name for the variable if any

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
        vm.loadRunStack(offset);
    }

    @Override
    public String dumpPrint() {
        return "LOAD " + offset + " " + id + "     <load " + id + ">";
    }
}
