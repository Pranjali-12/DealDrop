package project.dealdrop.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String messsage){
        super(messsage);
    }
}
