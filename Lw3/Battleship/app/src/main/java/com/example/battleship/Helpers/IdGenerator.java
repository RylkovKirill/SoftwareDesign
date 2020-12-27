package com.example.battleship.Helpers;

import java.util.Random;

public class IdGenerator
{
    private static final String symbols = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + ("ABCDEFGHIJKLMNOPQRSTUVWXYZ").toLowerCase() + "0123456789";

    public static String GenerateId()
    {
        char[] gameId = new char[20];
        Random random = new Random();
        for (int i = 0; i < 20; i++)
        {
            gameId[i] = symbols.charAt(random.nextInt(symbols.length()));
        }
        return new String(gameId);
    }
}
