package library;

import java.util.Random;

public class EmailSender {

    public static String generateCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    public static boolean sendResetCode(String email, String code) {
        System.out.println("=========================================");
        System.out.println(" EMAIL SIMULATION ");
        System.out.println("To: " + email);
        System.out.println("Subject: Password Reset Code");
        System.out.println("Message: Your password reset code is: " + code);
        System.out.println("=========================================");

        return true;
    }

    public static boolean sendSmsCode(String phone, String code) {
        System.out.println("=========================================");
        System.out.println(" SMS SIMULATION");
        System.out.println("To: " + phone);
        System.out.println("Message: Your password reset code is: " + code);
        System.out.println("=========================================");

        return true;
    }
}
