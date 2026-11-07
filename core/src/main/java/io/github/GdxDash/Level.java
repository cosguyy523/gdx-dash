package io.github.GdxDash;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Level{
    public enum preset{
        pillar,
        array,
    }
    public float levelEnd = 1000f;
    public String pathMusic = "";
    public String levelName = "No level";
    public List<Tile> presets(preset __preset,float x,float y,int ammount,short type){
        List<Tile> _preset = new ArrayList<>();
        switch(__preset){
            case preset.array:
                for(int i = 0;i < ammount;i++){
                    _preset.add(new Tile(x + 64 * i,y ,type));
                }
                break;
            case preset.pillar:
                for(int i = 0;i < ammount;i++){
                    _preset.add(new Tile(x, y + 64 * i, type));
                }
                break;
        }
        return _preset;
    }
    public List<Tile>load(int index){
        levelName = "Hard jump";
        levelEnd = 10000f;
        List<Tile> blocks = presets(preset.array,500,132,5,(short)1);
        List<Tile> spikes = presets(preset.array,400,132,2,(short) 2);
        List<Tile> spikes2 = presets(preset.array,900,132,7,(short) 2);
        List<Tile> tiles = new ArrayList<>();
        for(Tile tile : blocks){
            tiles.add(tile);
        }
        for(Tile tile : spikes){
            tiles.add(tile);
        }
        for(Tile tile : spikes2){
            tiles.add(tile);
        }
        List<List<Tile>> levelList = new ArrayList<List<Tile>>();
        levelList.add(tiles);
        return levelList.get(index);
    }
}

