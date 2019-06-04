package interpreter.bytecode;

import interpreter.VirtualMachine;

import java.util.ArrayList;

public class LitCode extends ByteCode {
    private int n;
    private String id; //name of variable if any

    @Override
    public void init(ArrayList<String> args) {
        n = Integer.parseInt(args.remove(0));

        //checking if there is a name/id
        if(args.isEmpty()){
            id = "";
        }else{
            id = args.get(0);
        }
    }

    @Override
    public void execute(VirtualMachine vm) {
        if(id == ""){
            //if no variable, just push literal value
            vm.pushRunStack(n);
        }else{
            //to reserve space for variable named id
            vm.pushRunStack(0);
        }
    }

    @Override
    public String dumpPrint() {
        //conditions based on id's value
        if(id == ""){
            return "LIT " + n;
        }else {
            return "LIT " + n + " " + id + "     int " + id;
        }
    }
}
