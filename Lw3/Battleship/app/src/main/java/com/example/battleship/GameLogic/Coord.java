package com.example.battleship.GameLogic;

public class Coord
{
    private int x;
    private int y;

    public Coord()
    {
    }

    public Coord(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public int getX()
    {
        return x;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public int getY()
    {
        return y;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public static  CellType[] getNeighborsCellType(Map map, Coord coord)
    {
        CellType[] cellTypes = new CellType[8];
        Coord[] coords = getNeighborsCoord( map,  coord);
        for (int i = 0; i <coords.length; i++)
        {
            if (coords[i] != null)
            {
                cellTypes[i] =  map.getCell(coords[i]);
            }
            else
            {
                cellTypes[i] =  null;
            }
        }
        return cellTypes;
    }

    public static  Coord[] getNeighborsCoord(Map map, Coord coord)
    {
        return new Coord[]{
                coord.getX() < 9 ? (new Coord(coord.getX() + 1, coord.getY())) : null,
                coord.getX() < 9  &&  coord.getY() > 0 ? (new Coord(coord.getX() + 1, coord.getY() - 1)) : null,
                coord.getY() > 0 ? (new Coord(coord.getX(), coord.getY() - 1)) : null,
                coord.getX() > 0  &&  coord.getY() > 0 ? (new Coord(coord.getX() - 1, coord.getY() - 1)) : null,
                coord.getX() > 0 ? (new Coord(coord.getX() - 1, coord.getY())) : null,
                coord.getX() > 0  &&  coord.getY() < 9 ? (new Coord(coord.getX() - 1, coord.getY() + 1)) : null,
                coord.getY() < 9 ? (new Coord(coord.getX(), coord.getY() + 1)) : null,
                coord.getX() < 9 && coord.getY() < 9 ? (new Coord(coord.getX() + 1, coord.getY() + 1)) : null,};
    }
}
