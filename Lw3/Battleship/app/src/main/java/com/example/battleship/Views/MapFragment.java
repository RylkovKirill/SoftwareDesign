package com.example.battleship.Views;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;

import com.example.battleship.Enums.ShotType;
import com.example.battleship.GameLogic.CellType;
import com.example.battleship.GameLogic.Coord;
import com.example.battleship.GameLogic.Map;
import com.example.battleship.GameLogic.MapHelpers.MapValidator;
import com.example.battleship.GameLogic.Ship;
import com.example.battleship.Enums.MapType;
import com.example.battleship.GameLogic.ShipState;
import com.example.battleship.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class MapFragment extends Fragment
{

    private ImageButton[][] cells = new ImageButton[10][10];
    public Map map;
    private MapType mapType;
    private ArrayList<Ship> ships;
    private String stringMap = null;

    public MapType getMapType()
    {
        return mapType;
    }

    public void setMapType(MapType mapType)
    {
        this.mapType = mapType;
    }



    public static MapFragment newInstance(String map, MapType mapType)
    {
        MapFragment mapFragment = new MapFragment();
        Bundle bundle = new Bundle();
        bundle.putString("map", map);
        bundle.putSerializable("mapType", mapType);
        mapFragment.setArguments(bundle);
        return mapFragment;
    }

    public static MapFragment newInstance(MapType mapType)
    {
        return newInstance("", mapType);
    }

//    public static MapFragment newInstance(String map)
//    {
//        return newInstance( map, mapType);
//    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        if(getArguments()!= null)
        {
            stringMap = getArguments().getString("map");
            mapType = (MapType) getArguments().getSerializable("mapType");
            ships = (ArrayList<Ship>) getArguments().getSerializable("ships");
        }

        for (int i = 0; i < 10; i++)
        {
            for(int j = 0; j < 10; j++)
            {
               cells[i][j] = view.findViewById(getResources().getIdentifier("button" + i + j, "id", requireContext().getPackageName()));
            }
        }
        for (ImageButton[] imageButtons : cells)
        {
            for (int j = 0; j < 10; j++) {
                imageButtons[j].setOnClickListener(this::MakeShot);
            }
        }

        if (stringMap != null)
        {
            map = new Gson().fromJson(stringMap, new TypeToken<Map>(){}.getType());

        }

        if(map == null)
        {
            map = new Map();
        }
        MapValidator mapValidator = new MapValidator();
        mapValidator.isValid(map);
        ships = mapValidator.getCurrentShips();
        InitMap();
        return view;
    }

    @Override public void onDestroyView()
    {
        super.onDestroyView();
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "ResourceAsColor"})
    private void InitMap()
    {
            for (int i = 0; i < 10; i++)
            {
                for(int j = 0; j < 10; j++)
                {
                    if (mapType == MapType.PLAYER || mapType == MapType.CREATURE)
                    {
                        if(map.getCell(new Coord(i, j)) == CellType.FILLED)
                        {
                            cells[i][j].setBackground(getResources().getDrawable(R.drawable.filled));
                        }
                    }
                    else
                    {
                            if(map.getCell(new Coord(i, j)) == CellType.FILLED)
                            {
                                cells[i][j].setBackground(getResources().getDrawable(R.drawable.empty));
                            }
                    }
//                    if(mapType == MapType.Opponent)
//                    {
//
//                    }
                    if(map.getCell(new Coord(i, j)) == CellType.EMPTY)
                    {
                        cells[i][j].setBackground(getResources().getDrawable(R.drawable.empty));
                    }
                    if(map.getCell(new Coord(i, j)) == CellType.DESTROYED)
                    {
                        cells[i][j].setBackground(getResources().getDrawable(R.drawable.destroyed));
                    }
                    if(map.getCell(new Coord(i, j)) == CellType.MISSED)
                    {
                        cells[i][j].setBackground(getResources().getDrawable(R.drawable.missed));
                    }
                }
            }
//        else if(mapType == MapType.Opponent)
//        {
//            for (int i = 0; i < 10; i++)
//            {
//                for(int j = 0; j < 10; j++)
//                {
//                        cells[i][j].setBackground(getResources().getDrawable(R.drawable.empty));
//                }
//            }
//        }
    }

    @SuppressLint({"ResourceAsColor", "UseCompatLoadingForDrawables"})
    private void MakeShot(View view)
    {
        if (mapType != MapType.NOTACTIVE)
        {
            String u = view.getTag().toString();
            int x = u.charAt(0) - '0';
            int y = u.charAt(1) - '0';
            if(mapType != MapType.NOTACTIVE)
            {
                if(mapType == MapType.CREATURE)
                {
                    if(map.getCell(new Coord(x, y)) == CellType.EMPTY)
                    {
                        cells[x][y].setBackground(getResources().getDrawable(R.drawable.filled));
                        map.setCell(new Coord(x, y), CellType.FILLED);
                    }
                    else if(map.getCell(new Coord(x, y)) == CellType.FILLED)
                    {
                        cells[x][y].setBackground(getResources().getDrawable(R.drawable.empty));
                        map.setCell(new Coord(x, y), CellType.EMPTY);
                    }
                }
                else if(mapType == MapType.ENEMY)
                {
                    if(map.getCell(new Coord(x, y)) == CellType.FILLED)
                    {
                        //cells[x][y].setBackground(getResources().getDrawable(R.drawable.destroyed));
                        map.setCell(new Coord(x, y), CellType.DESTROYED);
                        checkShipDestroyed();
                        ((BattleActivity)getActivity()).UpdateGameState(ShotType.HIT);
                    }
                    else if(map.getCell(new Coord(x, y)) == CellType.EMPTY)
                    {
                        //cells[x][y].setBackground(getResources().getDrawable(R.drawable.missed));

                        map.setCell(new Coord(x, y), CellType.MISSED);
                    ((BattleActivity)getActivity()).UpdateGameState(ShotType.MISS);
                    }
                }
            }
        }
    }

    private void checkShipDestroyed()
    {
        for (int i = 0; i <ships.size(); i++)
        {
            if (ships.get(i).getShipState() == ShipState.ACTIVE)
            {
                ships.get(i).isDestroyed(map);
            }
        }
    }
}
