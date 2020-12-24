package com.example.battleship.GameLogic;

import java.util.ArrayList;

public class ArtificialIntelligence
{
    private Map map;
    private ArrayList<Ship> ships = new ArrayList<>();

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public ArtificialIntelligence(Map map)
    {
        this.map = map;
    }

    public void takeShot()
    {
        if(checkDamagedShips())
        {

        }
        else
        {
            MakeRandomShot();
        }
    }

    private void MakeRandomShot()
    {
        Coord coord = new Coord((int) ( Math.random() * 10 ), (int) (Math.random() * 10 ));
        while (map.getCell(coord)==CellType.DESTROYED || map.getCell(coord)==CellType.MISSED)
        {
            coord = new Coord((int) (Math.random() * 10), (int) (Math.random() * 10));
        }
        if (map.getCell(coord)==CellType.FILLED)
        {
            map.setCell(coord, CellType.DESTROYED);
            Ship ship = new Ship();
            ship.setShipState(ShipState.DAMAGED);
            ship.setCoord(coord);
            ships.add(ship);
            CheckNeighbors(ship);
            takeShot();
        }
        else if (map.getCell(coord)==CellType.EMPTY)
        {
            map.setCell(coord, CellType.MISSED);
        }
    }

    private boolean checkDamagedShips()
    {
        for (int i = 0; i <ships.size(); i++)
        {
            if (ships.get(i).getShipState() == ShipState.DAMAGED)
            {
                CheckShip(ships.get(i));
                return true;
            }
        }
        return false;
    }

    private void CheckShip(Ship ship)
    {
        if(ship.getOrientation() == null)
        {
            ArrayList<Coord> coords = ship.getCoords();
            for(int i = 0; i < coords.size(); i++)
            {
                Coord[] neighbors = Coord.getNeighborsCoord(map, coords.get(i));
                for(int j = 0; j<neighbors.length; j+=2)
                {
                    if(neighbors[j] == null)
                    {
                        continue;
                    }
                    if(map.getCell(neighbors[j]) == null)
                    {
                        continue;
                    }
                    if(!(map.getCell(neighbors[j]) == CellType.DESTROYED || map.getCell(neighbors[j]) == CellType.MISSED))
                    {
                        if(map.getCell(neighbors[j]) == CellType.EMPTY)
                        {
                            map.setCell(neighbors[j], CellType.MISSED);
                            return;
                        }
                        if(map.getCell(neighbors[j]) == CellType.FILLED)
                        {
                            map.setCell(neighbors[j], CellType.DESTROYED);
                            ship.setCoord(neighbors[j]);
                            CheckNeighbors(ship);
                            takeShot();
                        }
                    }
                }

            }
        }
        else if(ship.getOrientation() == ShipOrientation.HORIZONTAL)
        {
            //бИТЬ по горизатале
        }
        else if(ship.getOrientation() == ShipOrientation.VERTICAL)
        {
            //бИТЬ по вертикале
        }
    }

    private void CheckNeighbors(Ship ship)
    {
        ArrayList<Coord> coords = ship.getCoords();
        for(int i = 0; i < coords.size(); i++)
        {
            Coord[] neighbors = Coord.getNeighborsCoord(map, coords.get(i));
            for(int j = 0; j<neighbors.length; j++)
            {
                if(neighbors[j]!= null && map.getCell(neighbors[j])==CellType.FILLED)
                {
                    return;
                }
            }
        }
        ship.setShipState(ShipState.DESTROYED);
    }
}
