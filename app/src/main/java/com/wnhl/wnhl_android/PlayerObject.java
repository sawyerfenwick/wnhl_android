package com.wnhl.wnhl_android;

public class PlayerObject {

    int id;
    int num;
    int currTeam;
    int currG;
    int currA;
    int currP;
    int mediaID;
    String name;
    String playerInfo;
    String leagues;
    String seasons;
    String stats;
    String nationality;
    String prevTeams;

    public PlayerObject(int id, String name, String playerInfo, String leagues, String seasons,
                        int num, String prevTeams, int currTeam, String nationality, int currG,
                        int currA, int currP, String stats, int mediaID){
        this.id = id;
        this.name = name;
        this.playerInfo =playerInfo;
        this.leagues = leagues;
        this.seasons = seasons;
        this.num = num;
        this.prevTeams = prevTeams;
        this.currTeam = currTeam;
        this.nationality = nationality;
        this.currG = currG;
        this.currA = currA;
        this.currP = currP;
        this.stats = stats;
        this.mediaID = mediaID;
    }
}
