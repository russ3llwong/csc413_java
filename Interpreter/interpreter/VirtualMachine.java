package interpreter;

import interpreter.bytecode.ByteCode;
import interpreter.bytecode.DumpCode;

import java.util.Stack;

public class VirtualMachine {

    private RunTimeStack runStack;
    private Stack returnAddrs;
    private Program program;
    private int pc;
    private boolean isRunning;
    private boolean dumpState = false;

    protected VirtualMachine(Program program) {
        this.program = program;
    }

    public void executeProgram() {
        pc = 0;
        runStack = new RunTimeStack();
        returnAddrs = new Stack<Integer>();
        isRunning = true;
        while (isRunning) {
            ByteCode code = program.getCode(pc); //creating a bytecode instance
            code.execute(this); //executing the byteCode

            //if dump is ON, dump bytecodes and the stack
            if(dumpState && !(code instanceof DumpCode)){
            //dumpState = true; //for testing
            System.out.println(code.dumpPrint());
            runStack.dump();
            }

            pc++;
        }
    }

    //****the following functions to avoid breaking encapsulation
    //for haltCode to call and switch isRunning
    public void haltProgram() {
        isRunning = false;
    }

    //to peek the runstack
    public int peekRunStack() {
        return runStack.peek();
    }

    //to pop the runstack
    public int popRunStack() {
        return (runStack.pop());
    }

    //to push a value onto the runstack
    public int pushRunStack(int val) {
        return runStack.push(val);
    }

    //to set a new frame
    public void newFrameAtRunStack(int offset) {
        runStack.newFrameAt(offset);
    }

    //to pop the framestack
    public void popFrameStack() {
        runStack.popFrame();
    }

    //to store the runstack with offset/value
    public int storeRunStack(int offset) {
        return runStack.store(offset);
    }

    //to load the runstack with offset/value
    public int loadRunStack(int offset) {
        return runStack.load(offset);
    }

    //to set program counter
    public void setPC(int address) {
        this.pc = address;
    }

    //to get program counter
    public int getPC() {
        int tempPC = this.pc;
        return tempPC;
    }

    //to push an index(value) to returnAddress
    public void pushReturnAddrs(int n) {
        returnAddrs.push(n);
    }

    //to pop an index(value) from returnAddress
    public int popReturnAddrs() {
        return (Integer) returnAddrs.pop();
    }

    //for dumpCode to call and switch dumpState
    public void switchDumpState(String dump) {
        if (dump.equals("ON")) {
            dumpState = true;
        }
        if (dump.equals("OFF")) {
            dumpState = false;
        }
    }

    //to check if runStack is empty
    public boolean emptyRunStack() {
        return runStack.runStackIsEmpty();
    }
}
