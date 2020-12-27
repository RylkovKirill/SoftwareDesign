package com.example.battleship.GameLogic;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum ShipOrientation
{
    VERTICAL,
    HORIZONTAL;

    private static final List<ShipOrientation> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    public static ShipOrientation  GetRandomShipOrientation()
    {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }
}
