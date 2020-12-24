package com.example.battleship.Models;

import java.util.Date;

public class Game
{
    private String firstUser;
    private String firstMap;
    private String firstUserShips;
    private int firstUserResult;
    private String secondUser;
    private String secondMap;
    private String secondUserShips;
    private int secondUserResult;
    private boolean moveCounter;
    private boolean isCompleted;
    private Date gameDate;

    public Game()
    {
    }

    public Game(String firstUser, String secondUser)
    {
        this.firstUser = firstUser;
        this.firstUserResult = 0;
        this.secondUser = secondUser;
        this.secondUserResult = 0;
        this.moveCounter = true;
        this.isCompleted = false;
    }

    public Game(String firstUser, String firstMap, String firstUserShips, String secondUser, String secondMap, String secondUserShips)
    {
        this.firstUser = firstUser;
        this.firstMap = firstMap;
        this.firstUserShips = firstUserShips;
        this.firstUserResult = 0;
        this.secondUser = secondUser;
        this.secondMap = secondMap;
        this.secondUserShips = secondUserShips;
        this.secondUserResult = 0;
        this.moveCounter = true;
        this.isCompleted = false;
    }

    public Game(String firstUser, long firstUserScore, String secondUser, long secondUserScore, Date gameDate)
    {
        this.firstUser = firstUser;
        this.firstUserResult = (int) firstUserScore;
        this.secondUser = secondUser;
        this.secondUserResult = (int) secondUserScore;
        this.isCompleted = true;
        this.gameDate = gameDate;
    }

    public String getFirstUser()
    {
        return firstUser;
    }

    public void setFirstUser(String firstUser)
    {
        this.firstUser = firstUser;
    }

    public String getFirstMap()
    {
        return firstMap;
    }

    public void setFirstMap(String firstMap)
    {
        this.firstMap = firstMap;
    }

    public String getFirstUserShips()
    {
        return firstUserShips;
    }

    public void setFirstUserShips(String firstUserShips)
    {
        this.firstUserShips = firstUserShips;
    }

    public int getFirstUserResult()
    {
        return firstUserResult;
    }

    public void setFirstUserResult(int firstUserResult)
    {
        this.firstUserResult = firstUserResult;
    }

    public String getSecondUser()
    {
        return secondUser;
    }

    public void setSecondUser(String secondUser)
    {
        this.secondUser = secondUser;
    }

    public String getSecondMap()
    {
        return secondMap;
    }

    public void setSecondMap(String secondMap)
    {
        this.secondMap = secondMap;
    }

    public String getSecondUserShips()
    {
        return secondUserShips;
    }

    public void setSecondUserShips(String secondUserShips)
    {
        this.secondUserShips = secondUserShips;
    }

    public int getSecondUserResult()
    {
        return secondUserResult;
    }

    public void setSecondUserResult(int secondUserResult)
    {
        this.secondUserResult = secondUserResult;
    }

    public boolean getMoveCounter() {
        return moveCounter;
    }

    public void setMoveCounter(boolean moveCounter)
    {
        this.moveCounter = moveCounter;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public Date getGameDate() {
        return gameDate;
    }

    public void setGameDate(Date gameDate) {
        this.gameDate = gameDate;
    }
}
