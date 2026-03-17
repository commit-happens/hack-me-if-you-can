package cz.hackmeifyoucan.backend.exception;

public class DuplicateNicknameException extends RuntimeException {
    public DuplicateNicknameException(String nickname) {
        super("Přezdívka již existuje: " + nickname);
    }
}