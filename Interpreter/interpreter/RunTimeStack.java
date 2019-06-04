package interpreter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

public class RunTimeStack {

    private ArrayList<Integer> runTimeStack;
    private Stack<Integer> framePointer;

    public RunTimeStack() {
        runTimeStack = new ArrayList<Integer>();
        framePointer = new Stack<>();
        // Add initial Frame Pointer, main is the entry
        // point of our language, so its frame pointer is 0.
        framePointer.add(0);
    }

    //function to print the runtime stack and frame pointer stack if "Dump" is turned ON
    public void dump() {
        //iterator enumerator to iterate through the frame pointer
        Iterator iterator = framePointer.iterator();
        //variables to set the frame boundaries
        int endOfFrame, startOfFrame = (Integer) iterator.next();

        //outer loop based on framepointer stack
        for (int i = 0; i < framePointer.size(); i++) {
            //if there are more frames, end of frame will be set to it
            if (iterator.hasNext()) {
                endOfFrame = (Integer) iterator.next();
            } else {
                //otherwise it will be the size of the stack,meaning it's the last frame
                endOfFrame = runTimeStack.size();
            }

            //start by printing the first frame bracket
            System.out.print("[");
            //to print the contents of the current frame
            for (int j = startOfFrame; j < endOfFrame; j++) {
                //to print out the current value
                System.out.print(runTimeStack.get(j));
                //and print "," if it isn't the last value in the frame
                if (j != endOfFrame - 1) {
                    System.out.print(",");
                }
            }

            //ending it by printing the last frame bracket
            System.out.print("]");
            //to move onto the next frame
            startOfFrame = endOfFrame;
        }
        //to add a new line
        System.out.print("\n");
    }

    //function to return the top element of the runtime stack, without removing it
    public int peek() {
        return runTimeStack.get(runTimeStack.size() - 1);
    }

    //function to remove the top element of the runtime stack and return it
    public int pop() {
        //to avoid stack underflow when "POP n" is executed, where n exceeds the size of the runtime stack
        if (runTimeStack.size() != 0) {
            return runTimeStack.remove(runTimeStack.size() - 1);
        } else {
            return 0;
        }
    }

    //function to push a new value to the stack and return it
    public int push(int i) {
        runTimeStack.add(i);
        return i;
    }

    //function to create a new frame at offset, from the current frame
    public void newFrameAt(int offset) {
        //to avoid stack errors
        if (runTimeStack.size() - offset != framePointer.lastElement()) {
            framePointer.push(runTimeStack.size() - offset);
        } else {
            framePointer.add(0);
        }
    }

    //function to pop the current frame and add the return value, which is at the top of the runtime stack back to it
    public void popFrame() {
        //store the return value
        int returnVal = this.peek();
        //to get the starting index of the current frame
        int startOfCurrFrame = framePointer.pop();
        //pop all values in the current frame
        for (int i = runTimeStack.size(); i > startOfCurrFrame; i--) {
            this.pop();
        }
        //store the return value back to the runtime stack
        this.push(returnVal);
    }

    //function to store a value
    public int store(int offset) {
        //pop the top value
        int temp = this.pop();
        //and then replace the value at the offset from the current frame
        runTimeStack.set(framePointer.peek() + offset, temp);
        return runTimeStack.get(offset);
    }

    //function to load a value
    public int load(int offset) {
        //storing the value of the element that is offset from the current frame
        int temp = runTimeStack.get(framePointer.peek() + offset);
        //and then adding it to the stack
        runTimeStack.add(temp);
        return this.peek();
    }

    //function to push an Integer object and returns it
    public Integer push(Integer val) {
        runTimeStack.add(val);
        return val;
    }

    //function to check if the runtime stack is empty
    public boolean runStackIsEmpty() {
        if (runTimeStack.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }
}
