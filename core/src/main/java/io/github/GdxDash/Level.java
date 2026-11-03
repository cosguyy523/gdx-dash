package io.github.GdxDash;

import java.util.ArrayList;
import java.util.List;

public class Level{
    public float levelEnd = 1000f;
    public String pathMusic = "";
    public String levelName = "No level";
        /**
        * Preset for a level
        */
        private List<Tile> levelPreset(){
        levelEnd = 5000f;
        levelName = "Level";
        int distance1 = 512;
        int distance2 = 1512;
        List<Tile> preset = new ArrayList<>();
        for(int i = 0;i <1000;i++){
            preset.add(new Tile(i + 500, i + 80, (short)1));
        }
        for(int i = 0;i<50f;i++){
            distance2 += 200;
            preset.add(new Tile(distance2, 1080, (short)1));
            preset.add(new Tile(distance2,1145, (short)0));
        }
        for(int i = 0;i <100;i++){
            distance1 += 64;
            preset.add(new Tile(distance1,132, (short)2));

        }
        return preset;
    }
        private List<Tile> jumpPadLevelPreset(){
        List<Tile> preset = new ArrayList<Tile>();
        levelName = "Jump pad level";
        levelEnd = 10000f;
        float distanceX = 400f;
        float distanceY = 132f;
        for(int i = 0;i <1000;i++){
            distanceX += 128f;
            distanceY += 10f;
            preset.add(new Tile(distanceX,distanceY,(short)0));
        }
        return preset;
        }

    public List<Tile> load(){
        List<Tile> levelList = new ArrayList<Tile>(){};
        /*Your level here... */
        /*Example:
        levelList.add(new Tile(100f,100f,(short)1));
        Or just set the list as one of the presets:
        levelList = jumpPadLevelPreset();
        You can also automate tile 
        adding by using for loops
        */
       levelList = jumpPadLevelPreset();
        return levelList;
    }
}
