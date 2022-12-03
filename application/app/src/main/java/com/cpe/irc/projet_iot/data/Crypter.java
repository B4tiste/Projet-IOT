package com.cpe.irc.projet_iot.data;

public class Crypter {

    /**
     *  Crypte le message avec un césar de 3
     */
    private static String cesar(String msg, int decalage) {
        StringBuilder msgEncode = new StringBuilder();
        for (int i = 0; i < msg.length(); i++) {
            int charToInt = (int) msg.charAt(i) + decalage;
            char intToChar = (char) charToInt;
            msgEncode.append(intToChar);
        }
        return msgEncode.toString();
    }


    /**
     *  Encodé le message avec un césar de 3
     *
     * @return Le message encodé
     */
    public static String encode(String msg) {
        return Crypter.cesar(msg, 3);
    }

    /**
     *  Décodé le message avec un césar de 3
     *
     * @return Le message décoder
     */
    public static String decode(String msg) {
        return Crypter.cesar(msg, -3);
    }
}
