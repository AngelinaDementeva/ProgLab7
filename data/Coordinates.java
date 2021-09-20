// 
// Decompiled by Procyon v0.5.36
// 

package data;

public class Coordinates
{
    private Double x;
    private Float y;
    
    public Coordinates(final double x, final float y) {
        this.x = x;
        this.y = y;
    }
    
    public Coordinates() {
    }
    
    public void setX(final Double x) {
        this.x = x;
    }
    
    public void setY(final Float y) {
        this.y = y;
    }
    
    public Double getX() {
        return this.x;
    }
    
    public Float getY() {
        return this.y;
    }
    
    @Override
    public String toString() {
        return this.x + " " + this.y;
    }
}
