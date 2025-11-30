package cz.hackmeifyoucan.backend.exception;

/**
 * DuplicateNicknameException - vyhazuje se pokud už přezdívka v systému existuje.
 */
public class DuplicateNicknameException extends RuntimeException {
    public DuplicateNicknameException(String nickname) {
        super("Přezdívka již existuje: " + nickname);
    }
}