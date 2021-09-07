package main;

import java.util.concurrent.*;
import serviceServer.*;

public class MainServer {
    public static void main(String[] args) {
        final ForkJoinPool forkJoinPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
        final ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        forkJoinPool.submit(new PSQLServer(executor, 5000, "jdbc:postgresql://127.0.0.1:5432/CollectionsDB",
                "public.\"Users\"", "public.\"Collections\"", "admin", "")).join();
    }
}
