package com.appsian.aaproject.utils;

public enum CesResponseCode {
    FailedAuthorization("403"),
    MissingProtoBase("460"),
    Unknown("0"),
    NoError(""),
    Success("200");

    private String code;

    public String getCode() {
        return code;
    }

    CesResponseCode(String code) {
        this.code = code;
    }

    public static CesResponseCode fromString(String code) {
        CesResponseCode responseCode = CesResponseCode.Unknown;

        for(CesResponseCode searchCode : CesResponseCode.values()) {
            if(searchCode.code.equals(code)) {
                responseCode = searchCode;
                break;
            }
        }

        if(responseCode == Unknown) {
            responseCode.code = code;
        }

        return responseCode;
    }
}
