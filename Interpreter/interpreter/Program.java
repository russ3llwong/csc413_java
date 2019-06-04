package interpreter;

import interpreter.bytecode.*;

import java.util.ArrayList;
import java.util.HashMap;

public class Program {

    private ArrayList<ByteCode> program;
    //to store the addresses and labels
    private static HashMap<String,Integer> addressMap = new HashMap<String,Integer>();

    public Program() {
        program = new ArrayList<>();
    }

    protected ByteCode getCode(int pc) {
        return this.program.get(pc);
    }

    /**
     * This function should go through the program and resolve all addresses.
     * Currently all labels look like LABEL <<num>>>, these need to be converted into
     * correct addresses so the VirtualMachine knows what to set the Program Counter(PC)
     * HINT: make note what type of data-stucture bytecodes are stored in.
     *
     * @param //program Program object that holds a list of ByteCodes
     */
    public void resolveAddrs() {
        //to store the target address
        Integer address;

        //to iterate through program and resolve their addresses if it is a FalseBranch,GoTo or Call code
        //a temp bytecode of its type is initialized, and the address it set according to the labels in the hashMap
        for(int i = 0; i < program.size(); i++){
            if (program.get(i) instanceof FalseBranchCode){
                FalseBranchCode temp = (FalseBranchCode) getCode(i);
                address = addressMap.get(temp.getLabel()).intValue();
                temp.setAddress(address);
            }else if ( program.get(i) instanceof GotoCode ){
                GotoCode temp = (GotoCode) getCode(i);
                address = addressMap.get(temp.getLabel()).intValue();
                temp.setAddress(address);
            }else if (program.get(i) instanceof CallCode) {
                CallCode temp = (CallCode) getCode(i);
                address = addressMap.get(temp.getLabel()).intValue();
                temp.setAddress(address);
            }
        }

    }

    //To add a byteCode object into the ArrayList
    public void add(ByteCode byteCode){
        //for efficiency,labels and their addresses(indices) are stored into the hashMap before adding into program
        //to avoid iterating through program twice
        if (byteCode instanceof LabelCode){
            LabelCode label = (LabelCode)byteCode;
            addressMap.put(label.getLabel(), program.size());
        }
        program.add(byteCode);
    }


}
