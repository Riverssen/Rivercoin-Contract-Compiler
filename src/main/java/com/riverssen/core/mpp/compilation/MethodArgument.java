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

package com.riverssen.core.mpp.compilation;

import java.util.Set;

public class MethodArgument
{
    public Struct _this_;
    public Set<Field> _arguments_;

    public MethodArgument(Struct _this_, Set<Field> _arguments_)
    {
        this._this_ = _this_;
        this._arguments_ = _arguments_;
    }

    public boolean contains(String argument)
    {
        for (Field field : _arguments_)
            if (field.getName().equals(argument)) return true;

        return false;
    }

    public int fetchType(String s)
    {
        for (Field field : _arguments_)
            if (field.getName().equals(s)) return field.getValidType();
        return 0;
    }

    public String fetchTypeName(String s)
    {
        for (Field field : _arguments_)
            if (field.getName().equals(s)) return field.getTypeName();
        return "null";
    }
}
