// 
// Decompiled by Procyon v0.5.36
// 

package helpers;

import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.Future;
import java.util.Iterator;
import java.util.Set;
import java.io.IOException;
import java.util.logging.Level;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Callable;
import java.util.concurrent.ForkJoinTask;
import java.nio.channels.SocketChannel;
import java.nio.channels.SelectionKey;
import java.net.SocketAddress;
import java.util.concurrent.Executors;
import java.util.logging.Logger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.Selector;

public class Reciever
{
    private static Selector selector;
    private static Sender s;
    private ServerSocketChannel socket;
    private InetSocketAddress inetAddress;
    private final int numOfThreads;
    private final ForkJoinPool answerGet;
    private final ExecutorService service;
    private static final Logger logger;
    
    public Reciever() {
        this.numOfThreads = Runtime.getRuntime().availableProcessors();
        this.answerGet = new ForkJoinPool(this.numOfThreads);
        this.service = Executors.newFixedThreadPool(this.numOfThreads);
    }
    
    public void run() throws IOException {
        try {
            Reciever.selector = Selector.open();
            this.socket = ServerSocketChannel.open();
            this.inetAddress = new InetSocketAddress("localhost", 3852);
            this.socket.bind(this.inetAddress);
            this.socket.configureBlocking(false);
            this.socket.accept();
            final int ops = this.socket.validOps();
            this.socket.register(Reciever.selector, ops, null);
            while (true) {
                Reciever.selector.select();
                final Set<SelectionKey> keys = Reciever.selector.selectedKeys();
                final Iterator<SelectionKey> iterator = keys.iterator();
                while (iterator.hasNext()) {
                    final SelectionKey myKey = iterator.next();
                    if (myKey.isAcceptable()) {
                        final SocketChannel client = this.socket.accept();
                        client.configureBlocking(false);
                        client.register(Reciever.selector, 1);
                    }
                    else if (myKey.isReadable()) {
                        final SocketChannel client2 = (SocketChannel)myKey.channel();
                        try {
                            Reciever.s = this.answerGet.invoke((ForkJoinTask<Sender>)new ThreadReader(myKey, client2));
                            final Future<String> task = this.service.submit((Callable<String>)new ThreadExecuter(Reciever.s));
                            String toClient = "";
                            while (!task.isDone()) {
                                Thread.sleep(1L);
                            }
                            try {
                                toClient = task.get();
                            }
                            catch (ExecutionException e2) {
                                myKey.interestOps(0);
                                client2.close();
                                break;
                            }
                            final String finalToClient = toClient;
                            final String s;
                            byte[] message;
                            ByteBuffer bufferToSend;
                            final SocketChannel socketChannel;
                            final Runnable sending = () -> {
                                try {
                                    message = s.getBytes();
                                    bufferToSend = ByteBuffer.wrap(message);
                                    socketChannel.write(bufferToSend);
                                    Reciever.logger.log(Level.INFO, "The message was sent to client!");
                                }
                                catch (Exception e3) {
                                    Reciever.logger.log(Level.WARNING, "Error! Something wrong with sending message to client!");
                                }
                                return;
                            };
                            this.service.execute(sending);
                        }
                        catch (NullPointerException ex) {}
                        catch (InterruptedException e) {
                            Reciever.logger.log(Level.WARNING, e.getMessage());
                        }
                    }
                    iterator.remove();
                }
            }
        }
        catch (IOException ex2) {}
    }
    
    public void checkInput() {
        final Scanner scanner = new Scanner(System.in);
        final Scanner scanner2;
        String[] userCommand;
        final Runnable input = () -> {
            try {
                while (true) {
                    userCommand = (scanner2.nextLine().trim() + " ").split(" ", 2);
                    userCommand[1] = userCommand[1].trim();
                    if (userCommand[0].equals("exit")) {
                        Reciever.logger.log(Level.INFO, "Server stopped!");
                        System.exit(0);
                    }
                }
            }
            catch (NoSuchElementException e) {
                Reciever.logger.log(Level.INFO, "Server stopped!");
                System.exit(0);
                return;
            }
        };
        final Thread thread = new Thread(input);
        thread.start();
    }
    
    static {
        logger = Logger.getLogger(Reciever.class.getName());
    }
}
