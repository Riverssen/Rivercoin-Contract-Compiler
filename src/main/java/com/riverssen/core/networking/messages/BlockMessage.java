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

package com.riverssen.core.networking.messages;

import com.riverssen.core.block.BlockDownloadService;
import com.riverssen.core.block.FullBlock;
import com.riverssen.core.headers.ContextI;
import com.riverssen.core.networking.Client;
import com.riverssen.core.networking.SocketConnection;
import com.riverssen.core.utils.ByteUtil;

import java.io.IOException;

public class BlockMessage extends BasicMessage
{
    private BlockDownloadService   block;
    private boolean     requested;

    public BlockMessage()
    {
        super(ByteUtil.defaultEncoder().encode58(("block message" + System.currentTimeMillis()).getBytes()));
    }

    public BlockMessage(BlockDownloadService block, boolean requested)
    {
        super(ByteUtil.defaultEncoder().encode58(block.getData()));
        this.block = block;
        this.requested = requested;
    }

    @Override
    public void sendMessage(SocketConnection connection, ContextI context) throws IOException
    {
        connection.getOutputStream().writeInt(OP_BLK);
        connection.getOutputStream().writeUTF(getHashCode());
        connection.getOutputStream().writeBoolean(requested);
        connection.getOutputStream().write(block.getData());
    }

    @Override
    public void onReceive(Client client, SocketConnection connection, ContextI context) throws IOException
    {
        String hashCode = null;
        try{
            hashCode = connection.getInputStream().readUTF();
            boolean requested = connection.getInputStream().readBoolean();

            BlockDownloadService block = new BlockDownloadService(connection.getInputStream());

            if(requested)
                context.getBlockChain().download(block);
            else
            {
                context.getBlockChain().queueBlock(block);
                context.getNetworkManager().sendBlock(block, client);
            }

            client.sendMessage(new SuccessMessage(hashCode));
            client.setChainSize(Math.max(client.getChainSize(), block.getBlockID()));
        } catch (Exception e)
        {
            if(hashCode != null)
                client.sendMessage(new FailedMessage(hashCode));
        }
    }
}