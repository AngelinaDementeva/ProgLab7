// 
// Decompiled by Procyon v0.5.36
// 

package helpers;

import java.util.concurrent.Executors;
import java.util.logging.Logger;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ThreadPoolExecutor;

public class ThreadSender
{
    public ThreadPoolExecutor executor;
    private SocketChannel client;
    private String toClient;
    private static final Logger logger;
    
    public ThreadSender(final SocketChannel client, final String toClient) {
        this.executor = (ThreadPoolExecutor)Executors.newCachedThreadPool();
        this.client = client;
        this.toClient = toClient;
    }
    
    static {
        logger = Logger.getLogger(ThreadSender.class.getName());
    }
}
