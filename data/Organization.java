// 
// Decompiled by Procyon v0.5.36
// 

package data;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Organization
{
    private Integer id;
    private String name;
    private Long annualTurnover;
    private String creationDate;
    private String fullName;
    private OrganizationType type;
    private Address officialAddress;
    private Coordinates coordinates;
    private Integer userId;
    
    public Organization(final Integer id, final String name, final Long annualTurnover, final String creationDate, final String fullName, final OrganizationType type, final Address officialAddress, final Coordinates coordinates, final Integer userId) {
        this.id = id;
        this.name = name;
        this.annualTurnover = annualTurnover;
        this.creationDate = creationDate;
        this.fullName = fullName;
        this.type = type;
        this.officialAddress = officialAddress;
        this.coordinates = coordinates;
        this.userId = userId;
    }
    
    public Organization() {
    }
    
    @Override
    public String toString() {
        return "Organization{id = " + this.id + ", name = '" + this.name + '\'' + ", annualTurnover = " + this.annualTurnover + ", creationDate = " + this.creationDate + ", coordinates = " + this.coordinates + ", fullName = " + this.fullName + ", organizationType = " + this.type + ", address = " + this.officialAddress + '}';
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final int id) {
        this.id = id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public Long getAnnualTurnover() {
        return this.annualTurnover;
    }
    
    public void setAnnualTurnover(final Long annualTurnover) {
        this.annualTurnover = annualTurnover;
    }
    
    public String getFullName() {
        return this.fullName;
    }
    
    public void setFullName(final String fullName) {
        this.fullName = fullName;
    }
    
    public void setCreationDate(final String creationDate) {
        this.creationDate = creationDate;
    }
    
    public OrganizationType getType() {
        return this.type;
    }
    
    public void setType(final OrganizationType type) {
        this.type = type;
    }
    
    public Address getOfficialAddress() {
        return this.officialAddress;
    }
    
    public void setOfficialAddress(final Address officialAddress) {
        this.officialAddress = officialAddress;
    }
    
    public Coordinates getCoordinates() {
        return this.coordinates;
    }
    
    public void setCoordinates(final Coordinates coordinates) {
        this.coordinates = coordinates;
    }
    
    public String getCreationDate() {
        return this.creationDate;
    }
    
    public Integer getUserId() {
        return this.userId;
    }
    
    public void setUserId(final Integer userId) {
        this.userId = userId;
    }
    
    public String getDate() {
        final Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return formatter.format(calendar.getTime());
    }
}
