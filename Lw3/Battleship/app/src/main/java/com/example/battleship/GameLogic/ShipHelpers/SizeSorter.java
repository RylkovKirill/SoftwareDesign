package com.example.battleship.GameLogic.ShipHelpers;

import com.example.battleship.GameLogic.Ship;

import java.util.Comparator;

public class SizeSorter implements Comparator<Ship>
{
    @Override
    public int compare(Ship first, Ship second)
    {
        return Integer.compare(first.getSize(), second.getSize());
    }
}
