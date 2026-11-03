package io.github.GdxDash;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.g2d.Sprite;
import java.util.List;
public class Player {

    private float speed = 500f;
    private float gravity = 351.36f * 2.5f;
    private float Yvelocity;
    private float Xvelocity;
    private float maxVelocity = 900f;
    private Sprite cube;
    private Rectangle cubeBox = new Rectangle();
    public Rectangle getCubeBox() {
        return cubeBox;
    }
    private Rectangle blockPreset = new Rectangle(0,0,64f,64f);
    private boolean hittingBlockSide;
    private boolean grounded;
    public void initialize(Sprite _cube){
        cube = _cube;
        cubeBox.width = 64f;
        cubeBox.height = 64f;
    }
    public void applyPhysics(float deltaTime,List<Tile> tiles){
        cubeBox.x = cube.getX() + 34.5f;
        cubeBox.y = cube.getY() + 34.5f;
        Xvelocity = MathUtils.clamp(Xvelocity,-maxVelocity,maxVelocity);
        Yvelocity = MathUtils.clamp(Yvelocity,Float.NEGATIVE_INFINITY,maxVelocity);
        cube.setY(cube.getY() + Yvelocity * deltaTime);
        Xvelocity += speed * deltaTime;
        cube.setX(cube.getX() + Xvelocity * deltaTime);
        if(cube.getY() < 100.1f){
            cube.setY(100.1f);
            grounded = true;
        }
        else{
            grounded = false;
        }
        for(Tile _tile : tiles){
            if(_tile.type == 0){
                blockPreset.width = 16;
                blockPreset.height = 16;
            }
            if(_tile.type == 1){
                blockPreset.width = 64;
                blockPreset.height = 64;
            }
            if(_tile.type == 2){
                blockPreset.width = 32;
                blockPreset.height = 32;
                blockPreset.x = blockPreset.x + 15f;
            }


            blockPreset.x = _tile.x;
            blockPreset.y = _tile.y;
            if(cubeBox.overlaps(blockPreset) && _tile.type == (short)0){
                doUpwardForce(500f,true);
                grounded = false;
            }
            if(_tile.type != 3){

            
            if(cubeBox.overlaps(blockPreset) && (cubeBox.y + cubeBox.height > blockPreset.y + blockPreset.height)){
                switch(_tile.type){
                case (0):
                    break;
                case(1):
                    hittingBlockSide = false;
                    grounded = true;
                
                    Yvelocity = 0f;
                    cube.setY((blockPreset.y + blockPreset.height) - 34.5f); 
                    break;
                case(2):
                    Main.gameState = 0;
                }

            }

            else{
                if(!grounded){
                   grounded = false; 
                }
            }
            if(cubeBox.overlaps(blockPreset) && ((cubeBox.y   -cubeBox.height) + 80 < blockPreset.y + blockPreset.height) && _tile.type !=0){
                hittingBlockSide = true;
            }
            else{
                hittingBlockSide = false;
            }
            if(hittingBlockSide){
                Main.gameState = 0;
            }
        }
        }
        if(!grounded){
            Yvelocity -= gravity * deltaTime;
            
            cube.setRotation(cube.getRotation() + 100f * deltaTime);
        }
        else{
            if((int)cube.getRotation() % 90 != 0){
                float targetRotation = Math.round(cube.getRotation() / 90) * 90;
                cube.setRotation(Math.round(targetRotation));
                }
            }
        }

    public void doUpwardForce(float upwardForce,boolean ignoreGrounded){
        if(grounded && !ignoreGrounded){
            Yvelocity = upwardForce;
            grounded = false;
        }
        if(ignoreGrounded){
            Yvelocity = upwardForce;
            grounded = false;
        }
    }
    public void resetVariables(){
        Xvelocity = 0f;
        Yvelocity = 0f;
        cube.setX(0f);
        cube.setRotation(0f);
    }
}
