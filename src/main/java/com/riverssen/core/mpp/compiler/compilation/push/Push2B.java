package com.riverssen.core.mpp.compiler.compilation.push;

import com.riverssen.core.mpp.compiler.AbstractSyntaxTree;
import com.riverssen.core.mpp.compiler.Opcode;
import com.riverssen.core.mpp.compiler.Ops;
import com.riverssen.core.mpp.compiler.Token;
import com.riverssen.core.mpp.compiler.compilation.CompilationStep;

public class Push2B implements CompilationStep
{
    @Override
    public void execute(Token token, AbstractSyntaxTree.CompileType type, AbstractSyntaxTree tree, Object... objects)
    {
        switch (token.toString())
        {
            case "0":
                tree.getOpcode().add(new Opcode(Ops.sconst_0));
                break;
            case "1":
                tree.getOpcode().add(new Opcode(Ops.sconst_1));
                break;
            case "2":
                tree.getOpcode().add(new Opcode(Ops.sconst_2));
                break;
            case "3":
                tree.getOpcode().add(new Opcode(Ops.sconst_3));
                break;
                default:
                    tree.getOpcode().add(new Opcode(Ops.sconst).add(Opcode.convertShort(Long.parseLong(token.toString()))));
                    break;
        }
    }
}