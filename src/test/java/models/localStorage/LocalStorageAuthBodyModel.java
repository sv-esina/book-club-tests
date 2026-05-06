package models.localStorage;

public record  LocalStorageAuthBodyModel (UserDataModel userDataModel, String accessToken,
                                          String refreshToken, boolean isAuthenticated){}
