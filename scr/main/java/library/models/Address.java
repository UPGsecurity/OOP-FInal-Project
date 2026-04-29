package library.models;

public class Address {
    private String streetAddress;
    private String city;
    private String state;
    private String zipcode;
    private String country;

    public Address(String streetAddress, String city, String state,
                   String zipcode, String country) {
        this.streetAddress = streetAddress;
        this.city = city;
        this.state = state;
        this.zipcode = zipcode;
        this.country = country;
    }

    public String getStreetAddress() { return streetAddress; }
    public String getCity()          { return city; }
    public String getState()         { return state; }
    public String getZipcode()       { return zipcode; }
    public String getCountry()       { return country; }

    public void setStreetAddress(String streetAddress) { this.streetAddress = streetAddress; }
    public void setCity(String city)                   { this.city = city; }

    @Override
    public String toString() {
        return streetAddress + ", " + city + ", " + state + " " + zipcode + ", " + country;
    }
}
