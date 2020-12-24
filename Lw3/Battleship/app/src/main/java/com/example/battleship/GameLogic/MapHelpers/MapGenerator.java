package com.example.battleship.GameLogic.MapHelpers;

import com.example.battleship.GameLogic.CellType;
import com.example.battleship.GameLogic.Coord;
import com.example.battleship.GameLogic.Map;
import com.example.battleship.GameLogic.Ship;
import com.example.battleship.GameLogic.ShipOrientation;

import java.util.ArrayList;

public class MapGenerator
{
    private ArrayList<Ship> ships = new ArrayList<>();
    private Map map;

    public MapGenerator()
    {
        map = new Map();
        ships.add(new Ship(ShipOrientation.GetRandomShipOrientation(), 4));
        ships.add(new Ship(ShipOrientation.GetRandomShipOrientation(), 3));
        ships.add(new Ship(ShipOrientation.GetRandomShipOrientation(), 3));
        ships.add(new Ship(ShipOrientation.GetRandomShipOrientation(), 2));
        ships.add(new Ship(ShipOrientation.GetRandomShipOrientation(), 2));
        ships.add(new Ship(ShipOrientation.GetRandomShipOrientation(), 2));
        ships.add(new Ship(ShipOrientation.GetRandomShipOrientation(), 1));
        ships.add(new Ship(ShipOrientation.GetRandomShipOrientation(), 1));
        ships.add(new Ship(ShipOrientation.GetRandomShipOrientation(), 1));
        ships.add(new Ship(ShipOrientation.GetRandomShipOrientation(), 1));
    }

    public Map GenerateRandomMap()
    {
       for (int i = 0; i <ships.size(); i++)
       {
           PlaceShip(ships.get(i));
       }
       return map;
    }

    private void PlaceShip(Ship ship)
    {
        Coord coord = new Coord((int) ( Math.random() * 10 ), (int) (Math.random() * 10 ));
        while (!shipCanBePlaced(ship, coord))
        {
            coord = new Coord((int) (Math.random() * 10), (int) (Math.random() * 10));
        }
    }

    private boolean shipCanBePlaced(Ship ship, Coord coord)
    {
        if(map.getCell(coord) != CellType.EMPTY)
        {
            return false;
        }
        else
        {
//            if(ship.getSize()!=1)
//            {
                if(ship.getOrientation() == ShipOrientation.HORIZONTAL)
                {
                    for(int i = 0; i < ship.getSize(); i++)
                    {
                        Coord next = new Coord(coord.getX() + i, coord.getY() );
                        if((next.getX() < 0 || next.getX() > 9 ||next.getY() < 0 || next.getY() > 9) || checkNeighbors(next))
                        {
                            return false;
                        }
                    }
                    for(int i = 0; i < ship.getSize(); i++)
                    {
                        Coord next = new Coord(coord.getX() + i, coord.getY() );
                        map.setCell(next, CellType.FILLED);
                    }
                }
                else
                {
                    for(int i = 0; i < ship.getSize(); i++)
                    {
                        Coord next = new Coord(coord.getX(), coord.getY() + i );
                        if((next.getX() < 0 || next.getX() > 9 ||next.getY() < 0 || next.getY() > 9) || checkNeighbors(next))
                        {
                            return false;
                        }
                    }
                    for(int i = 0; i < ship.getSize(); i++)
                    {
                        Coord next = new Coord(coord.getX(), coord.getY() + i );
                        map.setCell(next, CellType.FILLED);
                    }
                }
                return true;
//            }
//            else
//            {
//                for (int x = 0; x < 10; x++)
//                {
//                    for(int y = 0; y < 10; y++)
//                    {
//                        Coord next = new Coord(x, y);
//                        if(map.getCell(next) != CellType.FILLED)
//                        {
//                            if(CheckNeighbors(new Coord(x, y)))
//                            {
//                                map.setCell(next, CellType.FILLED);
//                                return true;
//                            }
//                        }
//                    }
//                }
//                return false;
//            }
        }
    }

    private boolean checkNeighbors(Coord coord)
    {
        CellType[] neighbors = Coord.getNeighborsCellType(map, coord);

        for (CellType neighbor : neighbors)
        {
            if (neighbor != CellType.EMPTY && neighbor != null)
            {
                return true;
            }
        }
        return false;
    }
}
