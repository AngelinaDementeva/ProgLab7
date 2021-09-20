// 
// Decompiled by Procyon v0.5.36
// 

package commands;

import data.Organization;
import data.Address;
import data.Location;
import data.OrganizationType;
import data.Coordinates;
import java.util.NoSuchElementException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Add
{
    public String makerName() {
        while (true) {
            try {
                String name;
                while (true) {
                    final Scanner scanner = new Scanner(System.in);
                    System.out.println("Enter organization's name: ");
                    name = scanner.nextLine().trim();
                    if (!name.isEmpty()) {
                        break;
                    }
                    System.out.println("Name cannot be empty or null");
                }
                return name;
            }
            catch (InputMismatchException inputMismatchException) {
                System.out.println("Name value must be string!");
                continue;
            }
            catch (NoSuchElementException noSuchElementException) {
                System.out.println("Programm was stopped!");
                System.exit(1);
                continue;
            }
            break;
        }
    }
    
    public double makerX() {
        while (true) {
            try {
                double x;
                while (true) {
                    System.out.println("Enter X coordinate. Value cannot be empty.");
                    final Scanner scanner = new Scanner(System.in);
                    x = scanner.nextDouble();
                    final String iX = Double.toString(x);
                    if (!iX.isEmpty()) {
                        break;
                    }
                    System.out.println("Coordinate cannot be empty or null");
                }
                return x;
            }
            catch (InputMismatchException inputMismatchException) {
                System.out.println("Coordinate value must be double number!");
                continue;
            }
            catch (NoSuchElementException noSuchElementException) {
                System.out.println("Programm was stopped!");
                System.exit(1);
                continue;
            }
            break;
        }
    }
    
    public float makerY() {
        while (true) {
            try {
                float y;
                while (true) {
                    System.out.println("Enter Y coordinate. Value cannot be empty.");
                    final Scanner scanner = new Scanner(System.in);
                    y = scanner.nextFloat();
                    final String iY = Float.toString(y);
                    if (!iY.isEmpty()) {
                        break;
                    }
                    System.out.println(" Coordinate cannot be empty or null");
                }
                return y;
            }
            catch (InputMismatchException inputMismatchException) {
                System.out.println("Coordinate value must be float number!");
                continue;
            }
            catch (NoSuchElementException noSuchElementException) {
                System.out.println("Program was stopped!");
                System.exit(1);
                continue;
            }
            break;
        }
    }
    
    public Coordinates makerCoordinates() {
        return new Coordinates(this.makerX(), this.makerY());
    }
    
    public long makerAnnualTurnover() {
        while (true) {
            try {
                long turnover;
                while (true) {
                    System.out.println("Enter organization's annual turnover. Value must be greater than 0.");
                    final Scanner scanner = new Scanner(System.in);
                    turnover = scanner.nextLong();
                    final String ITurnover = String.valueOf(turnover).trim();
                    if (turnover > 0L) {
                        break;
                    }
                    System.out.println("Annual turnover must be greater than 0. Try again!");
                }
                return turnover;
            }
            catch (InputMismatchException inputMismatchException) {
                System.out.println("Annual turnover value must be long number!");
                continue;
            }
            catch (NoSuchElementException noSuchElementException) {
                System.out.println("Program was stopped!");
                System.exit(1);
                continue;
            }
            break;
        }
    }
    
    public String makerFullName() {
        while (true) {
            try {
                final Scanner scanner = new Scanner(System.in);
                System.out.println("Enter organization's FULL name: ");
                final String fullname = scanner.nextLine().trim();
                return fullname;
            }
            catch (InputMismatchException inputMismatchException) {
                System.out.println("Name value must be string!");
                continue;
            }
            catch (NoSuchElementException noSuchElementException) {
                System.out.println("Program was stopped!");
                System.exit(1);
                continue;
            }
            break;
        }
    }
    
    public OrganizationType makerOrganizationType() {
        while (true) {
            try {
                while (true) {
                    System.out.println("Choose type of organization. Enter the number which respond for desired type.");
                    System.out.println("Types: 'TRUST' - 1, 'PRIVATE_LIMITED_COMPANY' - 2, 'OPEN_JOINT_STOCK_COMPANY' - 3");
                    final Scanner scanner = new Scanner(System.in);
                    final int type = scanner.nextInt();
                    switch (type) {
                        case 1: {
                            return OrganizationType.TRUST;
                        }
                        case 2: {
                            return OrganizationType.PRIVATE_LIMITED_COMPANY;
                        }
                        case 3: {
                            return OrganizationType.OPEN_JOINT_STOCK_COMPANY;
                        }
                        default: {
                            continue;
                        }
                    }
                }
            }
            catch (InputMismatchException inputMismatchException) {
                System.out.println("Organziation type must be a number (1, 2, 3). Try again!");
                continue;
            }
            catch (NoSuchElementException noSuchElementException) {
                System.out.println("Program was stopped!");
                System.exit(1);
                continue;
            }
            break;
        }
    }
    
    public String makerAddressStreet() {
        while (true) {
            try {
                String street;
                while (true) {
                    final Scanner scanner = new Scanner(System.in);
                    System.out.println("Enter organization's street address: ");
                    street = scanner.nextLine().trim();
                    if (street.length() > 148) {
                        System.out.println("Street value cannot be greater than 148. Try again!");
                    }
                    else {
                        if (!street.isEmpty()) {
                            break;
                        }
                        System.out.println("Name cannot be empty or null");
                    }
                }
                return street;
            }
            catch (InputMismatchException inputMismatchException) {
                System.out.println("Street value must be string!");
                continue;
            }
            catch (NoSuchElementException noSuchElementException) {
                System.out.println("Program was stopped!");
                System.exit(1);
                continue;
            }
            break;
        }
    }
    
    public int makertownX() {
        while (true) {
            try {
                int x;
                while (true) {
                    System.out.println("Enter town's X coordinate. Value cannot be empty.");
                    final Scanner scanner = new Scanner(System.in);
                    x = scanner.nextInt();
                    final String iX = Integer.toString(x);
                    if (!iX.isEmpty()) {
                        break;
                    }
                    System.out.println("Coordinate cannot be empty or null");
                }
                return x;
            }
            catch (InputMismatchException inputMismatchException) {
                System.out.println("Coordinate value must be integer number!");
                continue;
            }
            catch (NoSuchElementException noSuchElementException) {
                System.out.println("Program was stopped!");
                System.exit(1);
                continue;
            }
            break;
        }
    }
    
    public long makertownY() {
        while (true) {
            try {
                long y;
                while (true) {
                    System.out.println("Enter town's Y coordinate. Value cannot be empty.");
                    final Scanner scanner = new Scanner(System.in);
                    y = scanner.nextLong();
                    final String iY = Long.toString(y);
                    if (!iY.isEmpty()) {
                        break;
                    }
                    System.out.println("Coordinate cannot be empty or null");
                }
                return y;
            }
            catch (InputMismatchException inputMismatchException) {
                System.out.println("Coordinate value must be long number!");
                continue;
            }
            catch (NoSuchElementException noSuchElementException) {
                System.out.println("Program was stopped!");
                System.exit(1);
                continue;
            }
            break;
        }
    }
    
    public String makerTownName() {
        while (true) {
            try {
                String town;
                while (true) {
                    final Scanner scanner = new Scanner(System.in);
                    System.out.println("Enter organization's town: ");
                    town = scanner.nextLine().trim();
                    if (!town.isEmpty()) {
                        break;
                    }
                    System.out.println("Town value cannot be empty or null");
                }
                return town;
            }
            catch (InputMismatchException inputMismatchException) {
                System.out.println("Town value must be string!");
                continue;
            }
            catch (NoSuchElementException noSuchElementException) {
                System.out.println("Program was stopped!");
                System.exit(1);
                continue;
            }
            break;
        }
    }
    
    public Location makerLocation() {
        return new Location(this.makertownX(), this.makertownY(), this.makerTownName());
    }
    
    public Address makerAddress() {
        return new Address(this.makerAddressStreet(), this.makerLocation());
    }
    
    public void add() throws InterruptedException {
        final boolean hand = true;
        Organization newOrg;
        if (hand) {
            newOrg = new Organization(0, this.makerName(), this.makerAnnualTurnover(), "0", this.makerFullName(), this.makerOrganizationType(), this.makerAddress(), this.makerCoordinates(), ClientTCP.myUserID);
        }
        else {
            final long age = 19L;
            final Location location = new Location(12, 22L, "Kazan");
            final Address address = new Address("Zinina", location);
            final Coordinates coordinates = new Coordinates(12.0, 33.0f);
            newOrg = new Organization(0, "dan", age, "", "danil", OrganizationType.TRUST, address, coordinates, ClientTCP.myUserID);
        }
        final ClientTCP sender = new ClientTCP();
        sender.setOrg(newOrg);
        sender.setCommand("add");
        sender.sending(sender);
    }
}
