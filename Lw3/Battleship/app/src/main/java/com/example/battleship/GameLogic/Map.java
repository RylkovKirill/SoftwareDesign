package com.example.battleship.GameLogic;

public class Map
{
    private CellType[][] map;

    public Map()
    {
        map = new CellType[10][10];

        for (int i = 0; i < 10; i++)
        {
            for (int j = 0; j < 10; j++)
            {
                map[i][j] = CellType.EMPTY;
            }
        }
    }

    public CellType[][] getMap()
    {
        return map;
    }

    public void setMap(CellType[][] map)
    {
        this.map = map;
    }

    public CellType getCell(Coord coord)
    {
        return map[coord.getX()][coord.getY()];
    }

    public void setCell(Coord coord, CellType status)
    {
        this.map[coord.getX()][coord.getY()] = status;
    }
}
