/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2018 Riverssen
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.riverssen.core.mpp;

import com.riverssen.core.mpp.compiler.*;
import com.riverssen.core.mpp.exceptions.ParseException;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class MainCompiler
{
    private static String stdlib()
            throws IOException
    {
        String utf_program = "";

        String includes[] = {
                "std", "rivercoin", "types"
        };

        for (String include : includes)
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(MainCompiler.class.getClassLoader().getResource(include + ".mxx").openStream()));

            String line = "";

            while ((line = reader.readLine()) != null)
                utf_program += line + "\n";

            reader.close();
        }

        return utf_program;
    }

    public static void main(String args[]) throws ParseException, IOException {
        final String[] commands = {
                "---commands---",
                "-c <path to main-class> --compiles and exports it to class.o file--",
                "-t <path to main-class> --creates human-readable abstract syntax tree and exports it to class.t file---",
                "-compile <path to main-class>",
                "-tree <path to main-class>",
                "---arguments---",
                "--v_x --appended at end of command to define language version. (e.g --v_1_0a--)."
        };

        if (args == null || args.length == 0)
        {
            System.err.println("no arguments...");
            for (String string : commands)
                System.out.println(string);

            System.exit(0);
        }

        if (args.length >= 2)
        {
            String path = args[1];
            if (path.startsWith("\""))
                path = path.substring(1, args[1].length() - 1);

            File main_class = new File(path);

            String arg = args[0];

            if (!main_class.exists())
            {
                System.err.println("file '" + main_class + "' doesn't exist.");
                System.exit(0);
            }

            if (arg.equals("-t") || arg.equals("-tree"))
            {
                String utf_program = "" + stdlib();

                BufferedReader reader = new BufferedReader(new FileReader(main_class));

                String line = "";

                while ((line = reader.readLine()) != null)
                    utf_program += line + "\n";

                reader.close();

                PreprocessedProgram preprocessedProgram = new PreprocessedProgram(utf_program, main_class.getParentFile(), new DynamicLibraryLoader(main_class.getParentFile()));

                LexedProgram lexedProgram = new LexedProgram(preprocessedProgram.getFinalProgram());

                ParsedProgram parsedProgram = new ParsedProgram(lexedProgram, preprocessedProgram.getFinalProgram().split("\n"));

                String t = (parsedProgram.getRoot().humanReadable(0));

                File out = new File(main_class.getParent() + File.separator + "" + main_class.getName().substring(0, main_class.getName().lastIndexOf(".")) + ".t");

                BufferedWriter writer = new BufferedWriter(new FileWriter(out));

                writer.write(t);

                writer.flush();
                writer.close();
            } else if (arg.equals("-c") || arg.equals("-compile"))
            {
//                List<Ops> opcodes = Arrays.asList(Ops.values());
//
//                for (Ops op : opcodes)
//                    System.out.println(op + ", /**" + op.getDesc() + "**/");
//
//                System.exit(0);
//                String ops = "";
//
//                String split[] = ops.split(",");
//
//                int i = 0;
//
//                for (String op : split)
//                    System.out.println(op + " = " + i ++ + ",");

//                System.exit(0);

                String utf_program = "" + stdlib();

                BufferedReader reader = new BufferedReader(new FileReader(main_class));

                String line = "";

                while ((line = reader.readLine()) != null)
                    utf_program += line + "\n";

                reader.close();

                PreprocessedProgram preprocessedProgram = new PreprocessedProgram(utf_program, main_class.getParentFile(), new DynamicLibraryLoader(main_class.getParentFile()));

                LexedProgram lexedProgram = new LexedProgram(preprocessedProgram.getFinalProgram());

                ParsedProgram parsedProgram = new ParsedProgram(lexedProgram, preprocessedProgram.getFinalProgram().split("\n"));

                System.out.println(parsedProgram.getRoot().humanReadable(0));

                CompiledProgram program = new CompiledProgram(parsedProgram);

                program.spit(new File(main_class.getParent() + File.separator + "" + main_class.getName().substring(0, main_class.getName().lastIndexOf(".")) + ".o"));
            }
        }
    }
}
