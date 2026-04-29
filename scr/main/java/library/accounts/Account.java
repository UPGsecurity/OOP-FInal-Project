package library.accounts;

import library.enums.AccountStatus;
import library.models.Person;

public abstract class Account {
    protected String        id;
    protected String        password;
    protected AccountStatus status;
    protected Person        person;

    public Account(String id, String password, Person person) {
        this.id       = id;
        this.password = password;
        this.person   = person;
        this.status   = AccountStatus.ACTIVE;
    }

    public boolean resetPassword(String newPassword) {
        if (newPassword == null || newPassword.length() < 4) {
            System.out.println("[Error] Password must be at least 4 characters.");
            return false;
        }
        this.password = newPassword;
        System.out.println("[Info] Parol muvaffaqiyatli yangilandi.");
        return true;
    }

    public boolean login(String inputPassword) {
        if (status != AccountStatus.ACTIVE) {
            System.out.println("[Info] Password successfully updated:" + status);
            return false;
        }
        boolean ok = this.password.equals(inputPassword);
        if (ok) System.out.println("[INFO] Login successful: " + id);
        else    System.out.println("[XATO] Wrong password.");
        return ok;
    }

    public void logout() {
        System.out.println("[INFO] Logged out: " + id);
    }

    public String        getId()       { return id; }
    public AccountStatus getStatus()   { return status; }
    public Person        getPerson()   { return person; }
    public String        getPassword() { return password; }

    public void setStatus(AccountStatus s) { this.status = s; }
    public void setPassword(String p)      { this.password = p; }

    @Override
    public String toString() {
        return "Account{id='" + id + "', status=" + status +
                ", person=" + (person != null ? person.getName() : "null") + "}";
    }
}
