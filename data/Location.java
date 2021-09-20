// 
// Decompiled by Procyon v0.5.36
// 

package data;

public class Location
{
    private Integer x;
    private Long y;
    private String name;
    
    public Location(final int x, final long y, final String name) {
        this.x = x;
        this.y = y;
        this.name = name;
    }
    
    public Location() {
    }
    
    public void setX(final Integer x) {
        this.x = x;
    }
    
    public void setY(final Long y) {
        this.y = y;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public Integer getX() {
        return this.x;
    }
    
    public Long getY() {
        return this.y;
    }
    
    public String getName() {
        return this.name;
    }
    
    @Override
    public String toString() {
        return this.x + " " + this.y + " " + this.name;
    }
}
