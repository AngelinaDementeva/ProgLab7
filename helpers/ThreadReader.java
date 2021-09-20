// 
// Decompiled by Procyon v0.5.36
// 

package helpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.RecursiveTask;

public class ThreadReader extends RecursiveTask<Sender>
{
    SocketChannel client;
    Sender s;
    SelectionKey myKey;
    
    public ThreadReader(final SelectionKey myKey, final SocketChannel client) {
        this.client = client;
    }
    
    @Override
    protected Sender compute() {
        final ByteBuffer buffer = ByteBuffer.allocate(2048);
        try {
            this.client.read(buffer);
        }
        catch (IOException e) {
            try {
                this.client.close();
            }
            catch (IOException ioException) {
                this.myKey.interestOps(0);
            }
            this.myKey.interestOps(0);
        }
        final String result = new String(buffer.array()).trim();
        try {
            this.s = new Sender();
            final ObjectMapper mapper = new ObjectMapper();
            this.s = mapper.readerForUpdating(this.s).readValue(result);
        }
        catch (NullPointerException ex) {}
        catch (JsonProcessingException ex2) {}
        return this.s;
    }
}
