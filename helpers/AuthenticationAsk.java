// 
// Decompiled by Procyon v0.5.36
// 

package helpers;

import java.security.NoSuchAlgorithmException;
import java.util.NoSuchElementException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.io.Console;
import exceptions.NotDeclaredPasswordException;
import exceptions.NotDeclaredLoginException;
import java.util.Scanner;

public class AuthenticationAsk
{
    private Scanner scanner;
    String pattern;
    
    public AuthenticationAsk(final Scanner scanner) {
        this.pattern = "(?=\\S+$).{3,}";
        this.scanner = scanner;
    }
    
    public String askLogin() {
        String login;
        while (true) {
            try {
                System.out.println("Enter your login:");
                login = this.scanner.nextLine().trim();
                if (login.equals("")) {
                    throw new NotDeclaredLoginException();
                }
            }
            catch (NotDeclaredLoginException e) {
                System.out.println("Login shouldn't be empty!");
                continue;
            }
            break;
        }
        return login;
    }
    
    public String askPassword() {
        String password;
        while (true) {
            try {
                System.out.println("Enter password:");
                final Console console = System.console();
                if (console == null) {
                    password = this.scanner.nextLine();
                    password = this.encryptPassword(password);
                }
                else {
                    password = String.valueOf(console.readPassword());
                    password = this.encryptPassword(password);
                }
                if (password.equals("")) {
                    throw new NotDeclaredLoginException();
                }
                if (!password.matches(this.pattern)) {
                    throw new NotDeclaredPasswordException();
                }
            }
            catch (NotDeclaredLoginException e) {
                System.out.println("Password shouldn't be empty!");
                continue;
            }
            catch (NotDeclaredPasswordException e2) {
                System.out.println("Password cannot contains spaces and it must be more than 3 characters!");
                continue;
            }
            break;
        }
        return password;
    }
    
    public boolean askQuestion(final String question) {
        final String finalQuestion = question + " (yes/no):";
        while (true) {
            try {
                System.out.println(finalQuestion);
                final String answer = this.scanner.nextLine().trim();
                if (!answer.equals("yes") && !answer.equals("no")) {
                    throw new NotDeclaredLoginException();
                }
                if (answer.equals("no")) {
                    System.out.println("Please register new account!");
                }
                return answer.equals("yes");
            }
            catch (NotDeclaredLoginException e) {
                System.out.println("The answer must be either 'yes' or 'no'!");
                continue;
            }
            break;
        }
    }
    
    public String encryptPassword(final String password) {
        try {
            final MessageDigest messageDigest = MessageDigest.getInstance("SHA-384");
            final byte[] digest = messageDigest.digest(password.getBytes(StandardCharsets.UTF_8));
            final BigInteger numRepr = new BigInteger(1, digest);
            String hashString;
            for (hashString = numRepr.toString(16); hashString.length() < 32; hashString += "0") {}
            return hashString;
        }
        catch (NoSuchElementException | NoSuchAlgorithmException ex2) {
            final Exception ex;
            final Exception e = ex;
            System.out.println("Something wrong! I can feel it.");
            return null;
        }
    }
}
