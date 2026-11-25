package io.github.GdxDash;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.List;
/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
/**
 * 
 * Main class.
 */
public class Main extends ApplicationAdapter {
    public static int gameState = 3;
    private OrthographicCamera camera = new OrthographicCamera();
    private Viewport viewport;
    private boolean turnThisOnThisToTrue = false;
    private SpriteBatch batch;
    private Rectangle buttonOne = new Rectangle(0,0,256,64);
    private Rectangle buttonTwo = new Rectangle(0,0,256,64);
    private Rectangle mouseRectangle = new Rectangle(0,0,1,1);
    private Texture cubeTexture;
    private float waitTime = 1f;
    private float distanceFinish = 132f;
    private Texture block;
    private BitmapFont font;
    private Texture jumpPad;
    private Texture logo;
    private Texture button;
    private Texture buttonPressed;
    private Texture line;
    private Texture spikeTexture;
    private Texture finish;
    private Music music;
    private Vector3 mouse = new Vector3();
    private Sprite cube;
    private Player localPlayer;
    private float deltaTime;
    private ShapeRenderer shapeRenderer;
    private final String playMessage = "Play";
    private final String exitMessage = "Exit";
    private final String levelNullMessage = "[WARNING] The level is empty.Modify the level class!";
    private final String noMusicMessage = "[WARNING] The level has no music.";
    private final String levelNameTooLongMessage = "(long level name)";
    private Rectangle tempRectangle = new Rectangle(0,0,64,64);
    private Rectangle cameraRectangle = new Rectangle(0,0,1920,1080);
    private List<Tile> level = new ArrayList<Tile>();
    private Level levelParsed = new Level();
    @Override 
    public void resize(int width,int height){
        viewport.update(width, height,false);
    }
    

    @Override
    public void create() {
        level = levelParsed.load(0);
        for(int i = 0;i < 1000;i++){
            distanceFinish += 64f;
            level.add(new Tile(levelParsed.levelEnd, distanceFinish, (short) 3));
        }
        try{
            music = Gdx.audio.newMusic(Gdx.files.internal(levelParsed.pathMusic));
        }
        catch(Exception e){
        }
        try{
            music = Gdx.audio.newMusic(Gdx.files.external(levelParsed.pathMusic));
        }
        catch(Exception e){
        }
        
        shapeRenderer = new ShapeRenderer();
        localPlayer = new Player();
        viewport = new StretchViewport(1920, 1080, camera);
        batch = new SpriteBatch();
        font = new BitmapFont();
        Pixmap pixmap = new Pixmap(10,10,Pixmap.Format.RGB888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        line = new Texture(pixmap);
        block = new Texture("block.png");
        logo = new Texture("logo.png");
        jumpPad = new Texture("jumppad.png");
        finish = new Texture("finish.png");
        cubeTexture = new Texture("cube2.png");
        button = new Texture("button.png");
        buttonPressed = new Texture("buttonPressed.png");
        spikeTexture = new Texture("spike.png");
        cube = new Sprite(cubeTexture);
        cube.setScale(0.5f);
        localPlayer.initialize(cube);
    }

    @Override
    public void render() {

        cameraRectangle.setX(viewport.getCamera().position.x - 960);                                                                          
        cameraRectangle.setY(viewport.getCamera().position.y - 540);
        mouse.set(Gdx.input.getX(),Gdx.input.getY(),0);
        camera.unproject(mouse);
        mouseRectangle.x = mouse.x;
        mouseRectangle.y = mouse.y;
        if(gameState == 1){
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            gameState = 3;
        }
        if(music != null){
            music.play();
        }
        waitTime = 1f;
        localPlayer.applyPhysics(deltaTime,level,levelParsed.levelEnd,levelParsed.speedOverRide);
        if(levelParsed.dontFollowCameraOnJump && localPlayer.getGrounded() && localPlayer.getCubeBox().overlaps(cameraRectangle)){
            viewport.getCamera().position.set(MathUtils.lerp(viewport.getCamera().position.x,cube.getX(),0.2f),MathUtils.lerp(viewport.getCamera().position.y,cube.getY(),0.2f),0);
        }
        else if (!localPlayer.getCubeBox().overlaps(cameraRectangle) || !levelParsed.dontFollowCameraOnJump){
            viewport.getCamera().position.set(cube.getX(),cube.getY(),0);
        }
        viewport.getCamera().update();
        viewport.apply();
        deltaTime = Gdx.graphics.getDeltaTime();
        if(turnThisOnThisToTrue){
            ScreenUtils.clear((float)Math.random(),(float)Math.random(),(float)Math.random(),(float)Math.random());
            cube.setColor((float)Math.random(),(float)Math.random(),(float)Math.random(),(float)Math.random());
        }
        else{
            ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        }
        
        
        batch.setProjectionMatrix(viewport.getCamera().combined);
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
        if(Gdx.input.isButtonPressed(Input.Buttons.LEFT) || Gdx.input.isKeyPressed(Input.Keys.SPACE)){
            localPlayer.doUpwardForce(600,false);
        }
        if(cube.getX() > levelParsed.levelEnd){
            gameState = 3;
            localPlayer.resetVariables();
        }
        batch.begin();
        batch.draw(line,cube.getX() -2500,132,5000,1);

        for(Tile _tile: level){
            tempRectangle.setX(_tile.x);
            tempRectangle.setY(_tile.y);
            if(tempRectangle.overlaps(cameraRectangle)){
            switch(_tile.type){
            case(0):
               batch.draw(jumpPad,_tile.x,_tile.y); 
               break;
            case(1):
               batch.draw(block,_tile.x,_tile.y); 
               break;
            case(2):
               batch.draw(spikeTexture,_tile.x,_tile.y);
               break;
            case(3):
               batch.draw(finish,_tile.x,_tile.y);
               break;
            }
        }
        }

        
        cube.draw(batch);
        batch.end();
    }
    if(gameState == 0){
        if(music != null){
            music.stop();
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            gameState = 3;
        }
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        shapeRenderer.begin(ShapeType.Line);
        shapeRenderer.setColor(Color.GOLD);                                                       
        shapeRenderer.rect(cube.getX() + 34.5f,cube.getY() + 34.5f,64,64);
        for(Tile _tile : level){
           tempRectangle.x = _tile.x;
           tempRectangle.y = _tile.y;
        if(cameraRectangle.overlaps(tempRectangle)){

        
            switch(_tile.type){
            case(0):
               tempRectangle.width = 64;
               tempRectangle.height = 16;
               shapeRenderer.setColor(Color.VIOLET);
               shapeRenderer.rect(tempRectangle.x, tempRectangle.y, tempRectangle.width, tempRectangle.height);
               break;
            case(1):
               tempRectangle.width = 64;
               tempRectangle.height = 64;
               shapeRenderer.setColor(Color.BLUE);
               shapeRenderer.rect(tempRectangle.x, tempRectangle.y, tempRectangle.width, tempRectangle.height);
               break;
            case(2):
               tempRectangle.width = 16;
               tempRectangle.height = 32;
               tempRectangle.x = tempRectangle.x + 15f;
               shapeRenderer.setColor(Color.RED);
               shapeRenderer.rect(tempRectangle.x, tempRectangle.y, tempRectangle.width, tempRectangle.height);
               break;
            }
        }
        }
        shapeRenderer.end();
        batch.begin();
        
        for(Tile _tile: level){
            tempRectangle.x = _tile.x;
            tempRectangle.y = _tile.y;
            if(tempRectangle.overlaps(cameraRectangle)){
            switch(_tile.type){
            case(0):
               batch.draw(jumpPad,_tile.x,_tile.y); 
               break;
            case(1):
               batch.draw(block,_tile.x,_tile.y); 
               break;
            case(2):
               batch.draw(spikeTexture,_tile.x,_tile.y);
               break;
            case(3):
               batch.draw(finish,_tile.x,_tile.y);
               break;
            }
        }
            
        }
        batch.draw(line,cube.getX() - 5000,132,100000,1);
        batch.end();
        waitTime -= deltaTime;
        if(waitTime < 0f){
            gameState = 1;        
            localPlayer.resetVariables();
        }
        
    }
    if(gameState == 3){
        if(music != null){
            music.stop();
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            gameState = 3;
        }
        viewport.getCamera().position.set(600,500,0);
        viewport.getCamera().update();
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        buttonOne.setX(100);
        buttonOne.setY(600);
        buttonTwo.setX(100);
        buttonTwo.setY(400);
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();

        batch.draw(cube, 3, 0.1f); 
        
        if(!mouseRectangle.overlaps(buttonOne)){
            batch.draw(button,100,600);
        }
        else{
            batch.draw(buttonPressed,100,600);
        }
        if(!mouseRectangle.overlaps(buttonTwo)){
            batch.draw(button,100,400);
        }
        else{
            batch.draw(buttonPressed,100,400);
        }
        if(mouseRectangle.overlaps(buttonOne) && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
            localPlayer.resetVariables();
            gameState = 1;
        }
        if(mouseRectangle.overlaps(buttonTwo) && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
            Gdx.app.exit();

        }
        font.draw(batch,playMessage,110,632,200,1,false);
        if(levelParsed.levelName.length() < 22){
            font.draw(batch,levelParsed.levelName,340,632,200,1,false);  
        }
        else{
            font.draw(batch,levelNameTooLongMessage,340,632,200,1,false);  
        }
        
        if (level.size() < 1){
            font.draw(batch,levelNullMessage,130,600,200,1,false);
        }
        if(music == null){
            font.draw(batch,noMusicMessage,130,500,200,1,false);
        }

        font.draw(batch,exitMessage,110,432,200,1,false);
        batch.draw(logo,viewport.getCamera().position.x,viewport.getCamera().position.y );
        batch.end();
    }
}

    @Override
    public void dispose() {
        block.dispose();
        jumpPad.dispose();
        spikeTexture.dispose();
        cubeTexture.dispose();
        button.dispose();
        buttonPressed.dispose();
        finish.dispose();
        line.dispose();
        font.dispose();
        logo.dispose();
        batch.dispose();
    }
}

