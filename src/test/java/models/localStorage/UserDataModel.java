package models.localStorage;

public record UserDataModel(Integer id, String username, String firstName,
                            String lastName, String email, String remoteAddr) {}
