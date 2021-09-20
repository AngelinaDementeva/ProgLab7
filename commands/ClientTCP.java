// 
// Decompiled by Procyon v0.5.36
// 

package commands;

import com.fasterxml.jackson.databind.ObjectWriter;
import java.io.IOException;
import helpers.CommandManager;
import helpers.AunthecationSupervisor;
import java.util.Scanner;
import java.nio.ByteBuffer;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;
import java.net.InetSocketAddress;
import data.Organization;

public class ClientTCP
{
    private String command;
    private Organization org;
    private String filePath;
    public static Integer myUserID;
    private String login;
    private String password;
    private String collectionsIDs;
    private String toServer;
    private InetSocketAddress socket;
    private SocketChannel client;
    
    public String getFilePath() {
        return this.filePath;
    }
    
    public void setFilePath(final String filePath) {
        this.filePath = filePath;
    }
    
    public String getCommand() {
        return this.command;
    }
    
    public void setCommand(final String command) {
        this.command = command;
    }
    
    public Organization getOrg() {
        return this.org;
    }
    
    public void setOrg(final Organization org) {
        this.org = org;
    }
    
    public String getLogin() {
        return this.login;
    }
    
    public String getPassword() {
        return this.password;
    }
    
    public void setLogin(final String login) {
        this.login = login;
    }
    
    public void setPassword(final String password) {
        this.password = password;
    }
    
    public static Integer getMyUserID() {
        return ClientTCP.myUserID;
    }
    
    public static void setMyUserID(final Integer myUserID) {
        ClientTCP.myUserID = myUserID;
    }
    
    public void sending(final ClientTCP sender) throws InterruptedException {
        try {
            this.socket = new InetSocketAddress("localhost", 3852);
            this.client = SocketChannel.open(this.socket);
            while (true) {
                if (this.client.isConnected()) {
                    final ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
                    final String json = ow.writeValueAsString(sender);
                    this.toServer = json.replaceAll("[\\\t|\\\n|\\\r]", " ");
                    final byte[] message = this.toServer.getBytes();
                    final ByteBuffer buffer = ByteBuffer.wrap(message);
                    try {
                        this.client.write(buffer);
                        System.out.println("The command was sent!");
                    }
                    catch (Exception e) {
                        System.out.println("ERROR = " + e.getMessage());
                    }
                    buffer.clear();
                    final ByteBuffer bufferToRead = ByteBuffer.allocate(2048);
                    this.client.read(bufferToRead);
                    String result = new String(bufferToRead.array()).trim();
                    if (result.contains("%%%exit%%%")) {
                        final String res1 = result.split("%%%exit%%%")[0];
                        System.out.println(res1);
                        System.out.println("Thanks for using my program!");
                        System.exit(1);
                    }
                    if (result.indexOf("New user was added successfully!") > -1) {
                        ClientTCP.myUserID = Integer.parseInt(result.split("!")[1]);
                        result = "New user was added successfully!";
                    }
                    if (result.equals("Users with this login/password don't exist")) {
                        System.out.println(result + ". Try again!");
                        final AunthecationSupervisor handle = new AunthecationSupervisor(new Scanner(System.in));
                        handle.handle();
                    }
                    if (result.indexOf("Welcome registered user!") > -1) {
                        ClientTCP.myUserID = Integer.parseInt(result.split("!")[1]);
                        result = "Welcome registered user!";
                    }
                    if (result.indexOf("$CheckId$=") > -1) {
                        final String command = result.split("=")[1];
                        final String s;
                        final String check1 = s = result.split("=")[3];
                        switch (s) {
                            case "true": {
                                final String s2 = command;
                                switch (s2) {
                                    case "update_id": {
                                        final Update_id update_id = new Update_id();
                                        update_id.update(result.split("=")[2]);
                                        break;
                                    }
                                    case "remove_by_id": {
                                        final Remove_id remove_id = new Remove_id();
                                        remove_id.remove_id(result.split("=")[2]);
                                        break;
                                    }
                                }
                                break;
                            }
                            case "false": {
                                result = "Error! Organization with this ID wasn't found!";
                                break;
                            }
                            case "NoAccess": {
                                result = "Error! You have no access to change this record!";
                                break;
                            }
                        }
                    }
                    System.out.println(result);
                    if (result.equals("The minimal element was found! Enter element's values.")) {
                        final Add add = new Add();
                        add.add();
                    }
                    try {
                        final CommandManager commandManager = new CommandManager();
                        commandManager.cmdMode();
                    }
                    catch (Exception e2) {
                        System.out.println(e2.getMessage());
                    }
                }
                this.client.close();
            }
        }
        catch (IOException e3) {
            System.out.println("Can't connect to the server. Check connection and try again!\nWaiting for connection!");
            Thread.sleep(5000L);
            System.out.println("Do you want to try connecting to server 'yes'/'no'?");
            final Scanner scanner = new Scanner(System.in);
            final String answer = scanner.nextLine();
            if (answer.equals("yes")) {
                this.sending(sender);
            }
            else if (answer.equals("no")) {
                System.out.println("Program was stopped");
                System.exit(1);
            }
            else {
                System.out.println("The answer must be 'yes'/'no'");
                this.sending(sender);
            }
        }
    }
    
    public void closing() throws IOException {
        this.client.close();
    }
}
