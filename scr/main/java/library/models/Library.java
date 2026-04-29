package library.models;


public class Library {
    private String  name;
    private Address address;

    private static Library instance;

    private Library(String name, Address address) {
        this.name    = name;
        this.address = address;
    }

    public static Library getInstance(String name, Address address) {
        if (instance == null) {
            instance = new Library(name, address);
        }
        return instance;
    }

    public String  getName()    { return name; }
    public Address getAddress() { return address; }

    public void setName(String name)       { this.name = name; }
    public void setAddress(Address address){ this.address = address; }

    @Override
    public String toString() {
        return "Library{name='" + name + "', address=" + address + "}";
    }
}
