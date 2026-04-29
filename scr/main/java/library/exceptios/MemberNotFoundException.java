package library.exceptions;

public class MemberNotFoundException extends Exception {

    public MemberNotFoundException(String email) {
        super("Member not found: '" + email + "'");
    }
}
