package serviceServer;

import psql.PSQLWorker;
import data.*;
import java.io.*;
import java.net.*;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.*;

public class PSQLServer extends RecursiveTask<Integer> {

    private int port; //порт
    private MemoryDB memoryDB; //база данных
    private PSQLWorker pSQLWorker; //PSQL база данных
    private int startMain = 0; //номер потока запуска
    private Socket clientSocket; //клиентский сокет
    private Cancel cancel; //токен остановки сервера
    private ServerSocket serverSocket; //серверный сокет
    private ThreadPoolExecutor threadPoolExecutor; //пул потоков
    private Logger logger; //логировщик

    class Cancel {
        private ServerSocket serverSocket;

        public void setCancel() {
            try {
                if (Objects.nonNull(serverSocket)) serverSocket.close();
            } catch (IOException ex) {
            }
        }

        public void setServerSocket(ServerSocket serverSocket) {
            this.serverSocket = serverSocket;
        }
    };

    //Создает новый сервер
    private PSQLServer createServer(ThreadPoolExecutor threadPoolExecutor, Socket socketClient, int startMain, Cancel cancel, MemoryDB memoryDB) {
        PSQLServer psqls = new PSQLServer(threadPoolExecutor, port, pSQLWorker.getcString(), pSQLWorker.getUsersTableName(), pSQLWorker.getColnsTableName(),
                pSQLWorker.getUserName(), pSQLWorker.getPassword());
        psqls.clientSocket = socketClient;
        psqls.startMain = startMain;
        psqls.cancel = cancel;
        psqls.memoryDB = memoryDB;
        return psqls;
    }

    //Обработчик соединения с клиентом
    private void execClient() {

        try {
            logger.info("Начало обработки соединения");
            Runnable rps = new RunnerParserServer(clientSocket, memoryDB, pSQLWorker);
            threadPoolExecutor.execute(rps);
        } catch (Exception exception) {
        }
    }

    @Override
    protected Integer compute() {
        switch (startMain) {
            //Главный поток приложения
            case 0: {
                logger.info("Запуск сервера");
                //Читаем базу данных
                if (!pSQLWorker.read(memoryDB.getUsers(), memoryDB.getOrganizations())) {
                    logger.error("Ошибка чтения базы данных");
                    return 0;
                } else {
                    logger.info("Чтение базы данных успешно выполнено");
                }
                PSQLServer pSQLServer = createServer(threadPoolExecutor, null, startMain + 1, cancel, memoryDB);
                pSQLServer.fork();
                Scanner scanner = new Scanner(System.in);
                while (true)
                    if (scanner.next().equals("exit")) {
                        logger.info("Остановка сервера");
                        cancel.setCancel();
                        pSQLServer.join();
                        System.exit(0);
                    }
            }
            //Поток прослушивания
            case 1: {
                List<PSQLServer> servers = new ArrayList<>();
                try {
                    serverSocket = new ServerSocket(port);
                    cancel.setServerSocket(serverSocket);
                    while (true) {
                        logger.info("Ожидание соединения");
                        clientSocket = serverSocket.accept();
                        logger.info("Соединение установлено. Запуск обработки");
                        servers.add(createServer(threadPoolExecutor, clientSocket, startMain + 1, null, memoryDB));
                        servers.get(servers.size() - 1).fork();
                    }
                } catch (IOException exception) {
                }
                for (PSQLServer server : servers)
                    server.join();
                break;
            }
            //Потоки обработчики соединений
            case 2:
                execClient();
        }
        return 0;
    }

    public PSQLServer(ThreadPoolExecutor threadPoolExecutor, int port, String cString, String usersTableName, String colnsTableName, String user, String password) {
        this.port = port;
        pSQLWorker = new PSQLWorker(cString, usersTableName, colnsTableName, user, password, true);
        cancel = new Cancel();
        memoryDB = new MemoryDB();
        this.threadPoolExecutor = threadPoolExecutor;
        logger = LoggerFactory.getLogger(PSQLServer.class);
    }
}
