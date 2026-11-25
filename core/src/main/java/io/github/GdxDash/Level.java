package io.github.GdxDash;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
public class Level{
    public enum blockSequenceType{
        pillar,
        array,
    }
    //Where the level ends
    public float levelEnd = 1000f;

    //Overrides the speed of the cube throughout the level (except the level end speed up)
    //Normal cube speed is around 500
    //If override is smaller than or equal to zero,it is ignored
    public float speedOverRide = 0;

    //Makes the camera only follow the player if grounded
    //Can be used for a brief moment in the level,or constantly
    public boolean dontFollowCameraOnJump = false;
    //The path to the music (if it exists)
    //if not leave this empty
    //The game will try to search the music both as an internal and an external path
    public String pathMusic = "";

    //Your level name
    public String levelName = "No level";

    public List<Tile> presets(blockSequenceType _blockSequenceType,float x,float y,int ammount,short type){
        List<Tile> _preset = new ArrayList<>();
        switch(_blockSequenceType){
            case blockSequenceType.array:
                for(int i = 0;i < ammount;i++){
                    _preset.add(new Tile(x + 64 * i,y ,type));
                }
                break;
            case blockSequenceType.pillar:
                for(int i = 0;i < ammount;i++){
                    _preset.add(new Tile(x, y + 64 * i, type));
                }
                break;
        }
        return _preset;
    }
    public List<Tile>load(int index){
        List<Tile> tiles = new ArrayList<>();
        for(Tile tile : presets(blockSequenceType.pillar,500,132,3,(short)1)){
            tiles.add(tile);
        }
        for(Tile tile : presets(blockSequenceType.array,500,132 + 64 * 2,999,(short) 1)){
            tiles.add(tile);
        }
        for(int i = 0;i < 10;i++){
            for(Tile tile : presets(blockSequenceType.array,500  * i + 132 + 64 * 7,132 + 64 * 3,3,(short) 2)){
                tiles.add(tile);
            }
        }
        tiles.add(new Tile(300,132,(short) 0));
        //All the levels are added and loaded from this list
        List<List<Tile>> levelList = new ArrayList<List<Tile>>();
        //Tiles are added sequentially,first at index 0,second at index 1,third at index 2 and so on
        levelList.add(tiles);
        //Add your level properties in this switch block
        switch (index) {
            case 0:
                speedOverRide = 600;
                dontFollowCameraOnJump = true;
                levelName = "Default level";
                levelEnd = 5500;
                break;
            default:
                break;
        }
        return levelList.get(index);
    }
}
