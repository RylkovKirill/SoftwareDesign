package com.example.battleship.GameLogic.MapHelpers;

import com.example.battleship.GameLogic.CellType;
import com.example.battleship.GameLogic.Coord;
import com.example.battleship.GameLogic.Map;
import com.example.battleship.GameLogic.Ship;
import com.example.battleship.GameLogic.ShipHelpers.SizeSorter;
import com.example.battleship.GameLogic.ShipOrientation;

import java.util.ArrayList;

public class MapValidator
{
    private ArrayList<Ship> expectedShips = new ArrayList<>();
    private ArrayList<Ship> currentShips = new ArrayList<>();
    private Map map;
    private Boolean[][] checkedCells;
    Boolean check;

    public MapValidator()
    {
        map = new Map();
        expectedShips.add(new Ship(1));
        expectedShips.add(new Ship(1));
        expectedShips.add(new Ship(1));
        expectedShips.add(new Ship(1));
        expectedShips.add(new Ship(2));
        expectedShips.add(new Ship(2));
        expectedShips.add(new Ship(2));
        expectedShips.add(new Ship(3));
        expectedShips.add(new Ship(3));
        expectedShips.add(new Ship(4));
        checkedCells = new Boolean[10][10];
        check = true;
    }

    public ArrayList<Ship> getCurrentShips()
    {
        return currentShips;
    }

    public void setCurrentShips(ArrayList<Ship> currentShips)
    {
        this.currentShips = currentShips;
    }

    public boolean isValid(Map map)
    {
        for (int  x = 0; x < 10; x++)
        {
            for (int  y = 0; y < 10; y++)
            {
                checkedCells[x][y] = false;
            }
        }
        this.map = map;
        for (int  x = 0; x < 10; x++)
        {
            for (int  y = 0; y < 10; y++)
            {
                checkNeighbors(new Coord(x, y));
            }
        }
        if(!check){
            return false;
        }
        return isCurrentEqualExpected(currentShips, expectedShips);
    }

    private void checkNeighbors(Coord coord)
    {
        if (checkedCells[coord.getX()][coord.getY()])
        {
            return;
        }

        if(map.getCell(coord) == CellType.EMPTY)
        {
            return;
        }

        CellType[] neighbors = Coord.getNeighborsCellType(this.map, coord);

        //boolean  isShipBow = false;
        int neighborsCounter = 0;
        for (CellType neighbor : neighbors)
        {
            if (neighbor == CellType.FILLED ||neighbor == CellType.DESTROYED)
            {
                neighborsCounter++;
            }
        }
        if(neighborsCounter == 0)
        {
            Ship ship = new Ship(1);
            ship.setCoord(coord);
            currentShips.add(ship);
        }
        else if(neighborsCounter == 1)
        {


            if (neighbors[0] == CellType.FILLED || neighbors[0] == CellType.DESTROYED)
            {
                checkedCells[coord.getX()][coord.getY()] = true;
                int size = 1;
                Ship ship = new Ship(ShipOrientation.HORIZONTAL);
                ship.setCoord(coord);
                //isShipBow = true;
                for(int i = 1; i < 4; i++)
                {
                    Coord next = new Coord(coord.getX() + i, coord.getY() );
                    if((next.getX() < 0 || next.getX() > 9 ||next.getY() < 0 || next.getY() > 9) ||  checkedCells[next.getX()][next.getY()])
                    {
                        ship.setSize(size);
                        currentShips.add(ship);
                        return;
                    }
                    else if (map.getCell(next) == CellType.FILLED || map.getCell(next) == CellType.DESTROYED)
                    {
                        checkedCells[next.getX()][next.getY()] = true;
                        ship.setCoord(next);
                        size++;
                    }
                    else if(!(map.getCell(next) == CellType.FILLED || map.getCell(next) == CellType.DESTROYED))
                    {
                        break;
                    }
                }
                ship.setSize(size);
                currentShips.add(ship);
            }
            else if (neighbors[6] == CellType.FILLED|| neighbors[6] == CellType.DESTROYED)
            {
                checkedCells[coord.getX()][coord.getY()] = true;
                int size = 1;
                Ship ship = new Ship(ShipOrientation.VERTICAL);
                ship.setCoord(coord);
                //isShipBow = true;
                for(int i = 1; i < 4; i++)
                {
                    Coord next = new Coord(coord.getX(), coord.getY() + i );
                    if((next.getX() < 0 || next.getX() > 9 ||next.getY() < 0 || next.getY() > 9) || checkedCells[next.getX()][next.getY()])
                    {
                        ship.setSize(size);
                        currentShips.add(ship);
                        return;
                    }
                    else if (map.getCell(next) == CellType.FILLED || map.getCell(next) == CellType.DESTROYED)
                    {
                        checkedCells[next.getX()][next.getY()] = true;
                        ship.setCoord(next);
                        size++;
                    }
                    else if(!(map.getCell(next) == CellType.FILLED || map.getCell(next) == CellType.DESTROYED))
                    {
                        break;
                    }
                }
                ship.setSize(size);
                currentShips.add(ship);
            }
            else if (neighbors[2] == CellType.FILLED|| neighbors[2] == CellType.DESTROYED)
            {
                return;
            }
            else if (neighbors[4] == CellType.FILLED|| neighbors[4] == CellType.DESTROYED)
            {
                return;
            }
            else
            {
                check = false;
                return;
            }
        }
    }

    private boolean isCurrentEqualExpected(ArrayList<Ship> currentShips, ArrayList<Ship> expectedShips)
    {
        if(currentShips.size()!=expectedShips.size())
        {
            return false;
        }
        else
        {
            currentShips.sort(new SizeSorter());
            for (int i = 0; i <expectedShips.size(); i++)
            {
                if(currentShips.get(i).getSize()!=expectedShips.get(i).getSize())
                {
                    return false;
                }
            }
            return true;
        }
    }
}
