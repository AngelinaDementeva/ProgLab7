// 
// Decompiled by Procyon v0.5.36
// 

package data;

public class Address
{
    private String street;
    private Location town;
    
    public Address(final String street, final Location town) {
        this.street = street;
        this.town = town;
    }
    
    public Address() {
    }
    
    public void setStreet(final String street) {
        this.street = street;
    }
    
    public void setTown(final Location town) {
        this.town = town;
    }
    
    public Location getTown() {
        return this.town;
    }
    
    public String getStreet() {
        return this.street;
    }
    
    @Override
    public String toString() {
        return this.street + " " + this.town;
    }
}
