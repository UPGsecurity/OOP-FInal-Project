package library.models;

public class Rack {
    private int    number;
    private String locationIdentifier;

    public Rack(int number, String locationIdentifier) {
        this.number             = number;
        this.locationIdentifier = locationIdentifier;
    }

    public int    getNumber()             { return number; }
    public String getLocationIdentifier() { return locationIdentifier; }

    public void setNumber(int number)                         { this.number = number; }
    public void setLocationIdentifier(String locationIdentifier) {
        this.locationIdentifier = locationIdentifier;
    }

    @Override
    public String toString() {
        return "Rack{number=" + number + ", location='" + locationIdentifier + "'}";
    }
}
