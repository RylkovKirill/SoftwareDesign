package com.example.battleship.GameLogic;

import java.util.ArrayList;

public class Ship
{
    private ShipOrientation orientation;
    private int size;
    private ArrayList<Coord> coords;
    private ShipState shipState;

    public Ship()
    {
        this.coords = new ArrayList<>();
        shipState = ShipState.ACTIVE;
    }

    public Ship(ShipOrientation orientation, int size)
    {
        this.orientation = orientation;
        this.size = size;
        this.coords = new ArrayList<>();
        shipState = ShipState.ACTIVE;
    }

    public Ship(int size)
    {
        this.size = size;
        this.coords = new ArrayList<>();
        shipState = ShipState.ACTIVE;
    }

    public Ship(ShipOrientation orientation)
    {
        this.orientation = orientation;
        this.coords = new ArrayList<>();
        shipState = ShipState.ACTIVE;
    }

    public ShipOrientation getOrientation()
    {
        return orientation;
    }

    public void setOrientation(ShipOrientation orientation)
    {
        this.orientation = orientation;
    }

    public int getSize()
    {
        return size;
    }

    public void setSize(int size)
    {
        this.size = size;
    }

    public ArrayList<Coord> getCoords()
    {
        return coords;
    }

    public void setCoords(ArrayList<Coord> coords)
    {
        this.coords = coords;
    }

    public void getCoord(int index)
    {
        this.coords.get(index);
    }

    public void setCoord(Coord coord)
    {
        this.coords.add(coord);
    }

    public ShipState getShipState()
    {
        return shipState;
    }

    public void setShipState(ShipState shipState)
    {
        this.shipState = shipState;
    }

    public boolean isDestroyed(Map map)
    {
        for(int i = 0; i < size; i++)
        {
            if(map.getCell(coords.get(i)) != CellType.DESTROYED)
            {
                return false;
            }
        }
        for(int i = 0; i < size; i++)
        {
            FillNeighborsCoord(map, Coord.getNeighborsCoord(map, coords.get(i)), CellType.MISSED);
        }
        shipState = ShipState.DESTROYED;
        return true;
    }

    private void FillNeighborsCoord(Map map, Coord[] coords, CellType type)
    {
        for(int j = 0; j < coords.length; j++)
        {
            if(coords[j]!=null)
            {
                if (map.getCell(coords[j]) != CellType.DESTROYED)
                {
                    map.setCell(coords[j], type);
                }
            }
        }
    }
}
