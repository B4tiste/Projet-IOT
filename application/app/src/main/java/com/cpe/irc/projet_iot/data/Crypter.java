package com.cpe.irc.projet_iot.data;

/**
 * Classe statique permettant de crypter et décrypter des chaînes de caractères
 */
public class Crypter {

    /**
     *  Crypte le message avec un césar de 3
     *  @param msg le message à crypter
     *  @param decalage le décalage à appliquer
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
     *  Encoder le message avec un césar de 3
     *  @param msg le message à crypter
     *
     *  @return Le message encodé
     */
    public static String encode(String msg) {
        return Crypter.cesar(msg, 3);
    }

    /**
     *  Décodé le message avec un césar de 3
     *  @param msg le message à décrypter
     *
     *  @return Le message décoder
     */
    public static String decode(String msg) {
        return Crypter.cesar(msg, -3);
    }
}
