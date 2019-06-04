
package interpreter;

import interpreter.bytecode.ByteCode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.StringTokenizer;


public class ByteCodeLoader extends Object {

    private BufferedReader byteSource;

    /**
     * Constructor Simply creates a buffered reader.
     * YOU ARE NOT ALLOWED TO READ FILE CONTENTS HERE
     * THIS NEEDS TO HAPPEN IN LOADCODES.
     */
    public ByteCodeLoader(String file) throws IOException {
            this.byteSource = new BufferedReader(new FileReader(file));
    }
    /**
     * This function should read one line of source code at a time.
     * For each line it should:
     *      Tokenize string to break it into parts.
     *      Grab THE correct class name for the given ByteCode from CodeTable
     *      Create an instance of the ByteCode class name returned from code table.
     *      Parse any additional arguments for the given ByteCode and send them to
     *      the newly created ByteCode instance via the init function.
     */
    public Program loadCodes() {

        Program program = new Program();
        //declaring an array list of String to store args, and clearing it just in case
        ArrayList<String> args = new ArrayList<>();
        args.clear();

        //try-catch to handle various exceptions
        try {
            //reading the first line
            String line = byteSource.readLine();
            while (line != null) {
                //tokenizer to tokenize the line into bytecode names and arguments if any
                StringTokenizer tokenizer = new StringTokenizer(line);
                //to ensure args is empty before storing strings into it
                args.clear();

                //getting the className of a byte code from CodeTable, based on the byteCode read
                String byteCodeName = tokenizer.nextToken();
                String className = CodeTable.getClassName(byteCodeName);

                //if there are arguments, they wil be stored
                while (tokenizer.hasMoreTokens()) {
                    args.add(tokenizer.nextToken());
                }

                //creating a generic class object and initialize a byteCode object
                Class c = Class.forName("interpreter.bytecode." + className);
                ByteCode bc = (ByteCode) c.getDeclaredConstructor().newInstance();

                //initializing the byteCode with its arguments and adding it to program
                bc.init(args);
                program.add(bc);

                //to read the next line
                line = byteSource.readLine();
            }
        } catch (IOException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException |
                InvocationTargetException | InstantiationException e) {
            System.out.println("***ERROR: " + e);
        }

        //to resolve the labels/addresses
        program.resolveAddrs();

        return program;
    }
}
