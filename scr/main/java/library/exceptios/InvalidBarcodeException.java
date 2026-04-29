package library.exceptions;

public class InvalidBarcodeException extends Exception {

    public InvalidBarcodeException(String barcode) {
        super("Invalid barcode: '" + barcode + "'");
    }
}
