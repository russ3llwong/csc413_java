package interpreter.bytecode;

import interpreter.VirtualMachine;

import java.util.ArrayList;

public class BopCode extends ByteCode {
    //variables for the operands and operator
    private int operand1,operand2;
    private String operator;

    @Override
    public void init(ArrayList<String> args) {
        operator = args.get(0);
    }

    @Override
    public void execute(VirtualMachine vm) {
        //the top level is operand2 because it was pushed after the operand1
        operand2 = vm.popRunStack();
        operand1 = vm.popRunStack();

        //switch-cases for all operators
        switch(operator){
            case "+":
                vm.pushRunStack(operand1 + operand2);
                break;
            case "-":
                vm.pushRunStack(operand1 - operand2);
                break;
            case "*":
                vm.pushRunStack(operand1 * operand2);
                break;
            case "/":
                vm.pushRunStack(operand1 / operand2);
                break;
            case "==":
                if(operand1 == operand2){
                    vm.pushRunStack(1);
                }else{
                    vm.pushRunStack(0);
                }
                break;
            case "!=":
                if(operand1 != operand2){
                    vm.pushRunStack(1);
                }else{
                    vm.pushRunStack(0);
                }
                break;
            case "<=":
                if(operand1 <= operand2){
                    vm.pushRunStack(1);
                }else{
                    vm.pushRunStack(0);
                }
                break;
            case ">":
                if(operand1 > operand2){
                    vm.pushRunStack(1);
                }else{
                    vm.pushRunStack(0);
                }
                break;
            case ">=":
                if(operand1 >= operand2){
                    vm.pushRunStack(1);
                }else{
                    vm.pushRunStack(0);
                }
                break;
            case "<":
                if(operand1 < operand2){
                    vm.pushRunStack(1);
                }else{
                    vm.pushRunStack(0);
                }
                break;
            case "|":
                if(operand1 == 1 || operand2 == 1){
                    vm.pushRunStack(1);
                }else{
                    vm.pushRunStack(0);
                }
                break;
            case "&":
                if(operand1 == 1 && operand2 == 1){
                    vm.pushRunStack(1);
                }else{
                    vm.pushRunStack(0);
                }
                break;
        }
    }

    @Override
    public String dumpPrint() {
        return "BOP " + operator;
    }
}
