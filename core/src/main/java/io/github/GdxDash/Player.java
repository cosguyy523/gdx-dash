package io.github.GdxDash;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.g2d.Sprite;
import java.util.List;
public class Player {

    private float speed = 430f;
    private float levelEnd;
    private boolean invulnerable = false;
    private float gravity = 351.36f * 5f;
    private float Yvelocity;
    public float getVelocity() {
        return Yvelocity;
    }
    public boolean getGrounded(){
        return grounded;
    }
    private float maxVelocity = 900f;
    private Sprite cube;
    private Rectangle cubeBox = new Rectangle();
    private Rectangle cameraRectangle = new Rectangle(0,0,1920,1080);
    public Rectangle getCubeBox() {
        return cubeBox;
    }
    private Rectangle tempRectangle = new Rectangle(0,0,64f,64f);
    private boolean hittingBlockSide;
    private boolean grounded = true;
    public void initialize(Sprite _cube){
        cube = _cube;
        cubeBox.width = 64f;
        cubeBox.height = 64f;
    }
    public void applyPhysics(float deltaTime,List<Tile> tiles,float levelEnd,float speedOverRide){
        if(speedOverRide > 0 && !(cubeBox.x > levelEnd - 300)){
            speed = speedOverRide;
        }

        if(cubeBox.x > levelEnd - 300){
            invulnerable = true;
            speed += 10000 * deltaTime;
            Yvelocity += 10000 * deltaTime;
        }
        cubeBox.x = cube.getX() + 34.5f;
        cubeBox.y = cube.getY() + 34.5f;
        Yvelocity = MathUtils.clamp(Yvelocity,-maxVelocity,maxVelocity);
        cube.setY(cube.getY() + Yvelocity * deltaTime);
        cube.setX(cube.getX() + speed * deltaTime);
        if(cube.getY() < 100.1f){
            cube.setY(100.1f);
            grounded = true;
        }
        for(Tile _tile : tiles){
            if(_tile.type == 0){
                tempRectangle.width = 16;
                tempRectangle.height = 16;
            }
            if(_tile.type == 1){
                tempRectangle.width = 64;
                tempRectangle.height = 64;
            }
            if(_tile.type == 2){
                tempRectangle.width = 16;
                tempRectangle.height = 32;
                tempRectangle.x = tempRectangle.x + 50f;
            }

            cameraRectangle.x = cube.getX();
            cameraRectangle.y = cube.getX();
            tempRectangle.x = _tile.x;
            tempRectangle.y = _tile.y;
            if(cubeBox.overlaps(tempRectangle) && _tile.type == (short)0){
                doUpwardForce(800f,true);
                grounded = false;
            }
            if(_tile.type != 3){

            
            if(cubeBox.overlaps(tempRectangle) && (cubeBox.y + cubeBox.height > tempRectangle.y + tempRectangle.height) && _tile.type != 2){
                switch(_tile.type){
                case (0):
                    break;
                case(1):
                    grounded = true;
                    hittingBlockSide = false;;
                    Yvelocity = 0f;
                    cube.setY((tempRectangle.y + tempRectangle.height) - 34.5f); 
                    break;
                }

            }
            if(cubeBox.overlaps(tempRectangle) && _tile.type == 2 && !invulnerable){
                Main.gameState = 0;
            }

            if(cubeBox.overlaps(tempRectangle) && ((cubeBox.y   -cubeBox.height) + 80 < tempRectangle.y + tempRectangle.height) && _tile.type !=0 && _tile.type != 2){
                hittingBlockSide = true;
            }
            else{
                hittingBlockSide = false;
            }
            if(hittingBlockSide && !invulnerable){
                Main.gameState = 0;
            }
        }
        }
        if(!grounded){
            Yvelocity -= gravity * deltaTime;
            cube.setRotation(cube.getRotation() - 130f * deltaTime);
        }
        else{
            if(cube.getRotation() % 90 != 0){
                cube.setRotation(Math.round(cube.getRotation() / 90)* 90);
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
        invulnerable = false;
        grounded = true;
        Yvelocity = 0f;
        cube.setX(0f);
        cube.setY(0f);
        cube.setRotation(0f);
    }
}
