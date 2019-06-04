package interpreter.bytecode;

import interpreter.VirtualMachine;

import java.util.ArrayList;
import java.util.Scanner;

public class ReadCode extends ByteCode{

    @Override
    public void init(ArrayList<String> args) {

    }

    @Override
    public void execute(VirtualMachine vm) {
        //scanner to read the input
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter an Integer: ");
        //to avoid exceptions
        if (scanner.hasNextInt()) {
            //if there exists an int, value is pushed to stack
            int value = scanner.nextInt();
            vm.pushRunStack(value);
        } else {
            System.out.println("Invalid input.");
        }
    }

    @Override
    public String dumpPrint() {
        return "READ";
    }
}
