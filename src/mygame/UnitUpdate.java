/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.animation.AnimControl;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;

import com.jme3.scene.Node;
import java.util.ArrayList;
import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.LoopMode;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;

import com.jme3.math.Vector3f;
import com.simsilica.mathd.Vec3d;
import java.util.Random;

/**
 *
 * @author Makary
 */
public class UnitUpdate extends AbstractAppState implements AnimEventListener{
    private final Node rootNode;
    public static final Node unitRootNode = new Node ("unitRootNode");
    public static final Node projectileRootNode = new Node ("projectileRootNode");
    private final AssetManager assetManager;
    private final AppStateManager stateManager;
    
    public static GameObject playerGameObj;
    public static Node playerPositionNode = new Node(); // node do ktorego dolaczona jest kamera
    public static Node playerNode;
    public static Node playerHandsNode;
    public static Node projectileSpawnNode = new Node();

    public static AnimChannel playerAnimChannel;
    public static AnimChannel playerAnimChannelHands;
   
    public static AnimControl playerAnimControlHands;
    public static AnimControl playerAnimControl;
    public static Boolean playerIsMoving = false;
    public static Boolean playerSpawned = false;
    public static Boolean playerIsDead = false;
    public static ArrayList<String> modelList = new ArrayList<>();

    ArrayList<GameObject> TemporaryArrayList = new ArrayList<>();
    public static ArrayList<GameObject> chosenUnits = new ArrayList<>();
    public static ArrayList<GameObject> allUnits = new ArrayList<>();
    public static ArrayList<GameObject> yourUnits = new ArrayList<>();
    public static ArrayList<GameObject> enemyUnits = new ArrayList<>();
    
    
    public static byte yourAllegiance = 1;
    static Vec3d movementVector;    
    public InputManager inputManager;
    

    

    
     public UnitUpdate(SimpleApplication MapAppState){
         rootNode = MapAppState.getRootNode();
          assetManager = MapAppState.getAssetManager();
          stateManager = MapAppState.getStateManager();
          inputManager = MapAppState.getInputManager();
          
    }
     @Override
    public void initialize(AppStateManager stateManager, Application app){
        super.initialize(stateManager, app);
        initPlayer();
        initKeys();
        initUnitModels();
        initUnitSpace();
        SpatialHash.Setup();         

 


    }
    //player
    public void initPlayer(){
       // work in progress gameObject gracza podaza za modelem
       // wip
        playerNode = (Node) assetManager.loadModel("Models/hero/hero.j3o");
        playerHandsNode = (Node) assetManager.loadModel("Models/heroHands/heroHands.j3o");
        playerNode.attachChild(playerHandsNode);
        playerPositionNode.attachChild(playerNode);
        rootNode.attachChild(playerPositionNode);
        playerHandsNode.attachChild(projectileSpawnNode);
        projectileSpawnNode.move(-0.225f,0,0.3f);
        playerAnimControl = playerNode.getChild("Armature").getControl(AnimControl.class);
        playerAnimControlHands = playerHandsNode.getChild("ArmatureHands").getControl(AnimControl.class);
        playerAnimChannel = playerAnimControl.createChannel();
        playerAnimChannelHands = playerAnimControlHands.createChannel();
        playerAnimChannel.setAnim("Idle");
        playerAnimChannelHands.setAnim("Idle");
        

    }
    
    public void onAnimationDone(AnimControl control, AnimChannel channel, String animName,GameObject attacker,GameObject target,float tpf){
      if(channel.getAnimationName().equals("Attack00") && channel.getAnimMaxTime()-channel.getTime() <= 1*tpf){  // 0.01f, to be replaced with coefficient
            channel.setLoopMode(LoopMode.DontLoop);
           
            if(attacker.getType() == 2 && attacker.getAllegiance() == yourAllegiance){
         
         Node node = (Node) assetManager.loadModel("Models/Arrow/Arrow.j3o");
         SpatialHash.createGameObject(attacker.getPosition(),1.8d, 0.1f, attacker.getPosition(),attacker.getRangedDamage(),0,0,0,(byte) 3,(byte) 0,node,null,null,target.getPosition(),target,yourAllegiance ,(byte)25,(byte) 0);
            } else{
            target.setHealth(target.getHealth()-(attacker.getMeleeDamage() - target.getArmor()));
            }
            
//        animName = "BackToAttack";
//        channel.setAnim(animName);
//        channel.setLoopMode(LoopMode.DontLoop);
        
        } else if(channel.getAnimationName().equals("BackToAttack") && channel.getAnimMaxTime()-channel.getTime() <= 0.01f){
        animName = "Attack00";
        channel.setAnim(animName);
        channel.setLoopMode(LoopMode.DontLoop);
        }
    }
    
    
    

    @Override
    public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {  // niepotrzebne
        if(channel.getAnimationName().equals("Attack01") && channel.getAnimMaxTime()-channel.getTime() <= 0.01f){  // 0.01f, zastapic zmienna
            channel.setLoopMode(LoopMode.DontLoop);
        
        
        animName = "Run";
        channel.setAnim(animName);
        channel.setLoopMode(LoopMode.DontLoop);
        
        }

    }

    @Override
    public void onAnimChange(AnimControl control, AnimChannel channel, String animName) {
        
    }
    
    
      private void initKeys() {
       inputManager.addMapping("W",new KeyTrigger(KeyInput.KEY_W));
       inputManager.addMapping("Attack",new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
       inputManager.addMapping("A",new KeyTrigger(KeyInput.KEY_A));
       inputManager.addMapping("B",new KeyTrigger(KeyInput.KEY_B));

       inputManager.addMapping("K",new KeyTrigger(KeyInput.KEY_K));
       
       inputManager.addListener(actionListener, "W");
       inputManager.addListener(actionListener, "Attack");
       inputManager.addListener(actionListener, "K");
       inputManager.addListener(actionListener, "A");
       inputManager.addListener(actionListener, "B");


    }

    private final ActionListener actionListener = new ActionListener() {
        @Override
        public void onAction(String name, boolean keyPressed, float tpf) {
            if(!playerIsDead && name.equals("W") && !keyPressed){
        playerIsMoving = false;
        if(!playerAnimChannel.getAnimationName().equals("Idle")){
            playerAnimChannel.setAnim("Idle");
            }
        } else if(!playerIsDead && name.equals("W")){
            if(!playerAnimChannel.getAnimationName().equals("Run")){
            playerAnimChannel.setAnim("Run");
            }
        playerIsMoving = true;
        } 
          
        if(!playerIsDead && name.equals("Attack") && !keyPressed){
        playerAnimChannelHands.setAnim("Shoot");
            playerAnimChannelHands.setLoopMode(LoopMode.DontLoop);
          Node node = (Node) assetManager.loadModel("Models/bullet/bullet.j3o");
         SpatialHash.createGameObject(new Vec3d((double)projectileSpawnNode.getWorldTranslation().x,(double)projectileSpawnNode.getWorldTranslation().y+1.22d,(double)projectileSpawnNode.getWorldTranslation().z),1.8d, 0.3f, new Vec3d((double)projectileSpawnNode.getWorldTranslation().x,(double)projectileSpawnNode.getWorldTranslation().y,(double)projectileSpawnNode.getWorldTranslation().z),50,0,0,0,(byte) 3,(byte) 0,node,null,null,new Vec3d ((double)CameraAndMouseControl.lookingDestination.x,(double) CameraAndMouseControl.lookingDestination.y,(double) CameraAndMouseControl.lookingDestination.z-0.1f),null,yourAllegiance ,(byte)50,(byte) 0);
         playerAnimChannelHands.setAnim("Idle");
        }
         if(name.equals("K")&& !keyPressed){ // testing 
for (int i=0 ; i<1000; i++){
   Node node = (Node) assetManager.loadModel("Models/bullet/bullet.j3o");
         SpatialHash.createGameObject(new Vec3d((double)projectileSpawnNode.getWorldTranslation().x,(double)projectileSpawnNode.getWorldTranslation().y+1.22d,(double)projectileSpawnNode.getWorldTranslation().z),1.8d, 0.3f, new Vec3d((double)projectileSpawnNode.getWorldTranslation().x,(double)projectileSpawnNode.getWorldTranslation().y,(double)projectileSpawnNode.getWorldTranslation().z),50,0,0,0,(byte) 3,(byte) 0,node,null,null,new Vec3d ((double)CameraAndMouseControl.lookingDestination.x,(double) CameraAndMouseControl.lookingDestination.y,(double) CameraAndMouseControl.lookingDestination.z),null,yourAllegiance ,(byte)30,(byte) 0);
         playerAnimChannelHands.setAnim("Idle");
}
         }
         if(name.equals("B") && !keyPressed){
System.out.println(SpatialHash.index);
         }
        }
    };
    
    public void animUpdates (GameObject gameObj,float tpf){
        if(gameObj.getNode().getWorldTranslation().distance(gameObj.getPosition().toVector3f()) > 0.05f){ // update hitbox position to node coordinates
      movementVector = new Vec3d((double)gameObj.getNode().getWorldTranslation().x,(double)gameObj.getNode().getWorldTranslation().y+gameObj.getHitboxHeight(),(double)gameObj.getNode().getWorldTranslation().z);
      gameObj.setPosition(movementVector);
        }

        if(gameObj.getType() != 5){
    if(gameObj.getAction() == 3){ // if dead
        if(!gameObj.getAnimChannel().getAnimationName().equals("Death")){
     gameObj.getAnimChannel().setAnim("Death");
     gameObj.getAnimChannel().setLoopMode(LoopMode.DontLoop);
        }
    }else if(gameObj.getAction() == 0 && !gameObj.getAnimChannel().getAnimationName().equals("Idle")){ // if idle
    gameObj.getAnimChannel().setAnim("Idle");
    }else if(gameObj.getAction() == 4 && gameObj.getNode().getWorldTranslation().distance(gameObj.getDestination().toVector3f()) < 0.25f){ // if moving and have reached destination
    gameObj.setAction((byte) 0);
    gameObj.getAnimChannel().setAnim("Idle");
    }else if(gameObj.getAction() == 1 && !gameObj.getAnimChannel().getAnimationName().equals("DefensiveStance")){ // if defensive stance
    gameObj.getAnimChannel().setAnim("DefensiveStance");
    }else if(gameObj.getAction() == 2 && gameObj.getTarget().getAction() != 3 && gameObj.getNode().getWorldTranslation().distance(gameObj.getTarget().getNode().getWorldTranslation()) <= gameObj.getRange()){  // if attacking and in range
        if(!gameObj.getAnimChannel().getAnimationName().contains("Attack")){
        gameObj.getAnimChannel().setAnim("Attack00");
        }
        gameObj.getNode().lookAt(gameObj.getTarget().getNode().getWorldTranslation(), Vector3f.ZERO);
        onAnimationDone(gameObj.getAnimControl(),gameObj.getAnimChannel(),"sw",gameObj,gameObj.getTarget(),tpf);
    }else if(gameObj.getAction() == 2  && gameObj.getNode().getWorldTranslation().distance(gameObj.getTarget().getNode().getWorldTranslation()) >= gameObj.getRange()){ // if attacking and out of range
        if(!gameObj.getAnimChannel().getAnimationName().equals("Run")){
                gameObj.getAnimChannel().setAnim("Run");
        }

        gameObj.getNode().lookAt(gameObj.getTarget().getNode().getWorldTranslation(), Vector3f.ZERO);
      gameObj.getNode().move(gameObj.getNode().getLocalRotation().getRotationColumn(2).multLocal(gameObj.getMovementSpeed()*tpf));

    } else if(gameObj.getAction() == 4 && gameObj.getNode().getWorldTranslation().distance(gameObj.getDestination().toVector3f()) >= 0.25f){ // if moving and havent reached destination
      if(!gameObj.getAnimChannel().getAnimationName().equals("Run")){
        gameObj.getAnimChannel().setAnim("Run");
      }
      gameObj.getNode().move(gameObj.getNode().getLocalRotation().getRotationColumn(2).multLocal(gameObj.getMovementSpeed()*tpf));
    
    } 
        }
    
    
    }
    
    public void initUnitSpace(){
    rootNode.attachChild(unitRootNode);
    rootNode.attachChild(projectileRootNode);
    }
    // todo: 1
    public void initUnitModels(){
    modelList.add("Models/Human/Units/SamuraiArcher.j3o");
    modelList.add("Models/Human/Units/SamuraiBanner.j3o");

    }

    @Override
    public void cleanup(){
            rootNode.detachChild(unitRootNode);

    super.cleanup();
    }
    @Override
    public void update(float tpf) { 
        if(!playerSpawned){
     SpatialHash.createGameObject(Vec3d.ZERO,1.3d, 0.40f, Vec3d.ZERO,30,0,1.5f,100,(byte) 5,(byte) 0,playerPositionNode,null,null,null,null,yourAllegiance ,(byte)8,(byte) 10);
     playerSpawned = true;
        }
//        System.out.println(SpatialHash.index);
        //unit update
        if(playerIsMoving && !playerIsDead){
        playerPositionNode.move(playerNode.getLocalRotation().getRotationColumn(2).multLocal(playerGameObj.getMovementSpeed()*tpf));
        } 
        
        Random random = new Random();
        if(random.nextInt(10000) < 2){
         Node node = (Node) assetManager.loadModel("Models/LavaGolem/LavaGolem.j3o");
         AnimControl animControl = node.getChild("Armature").getControl(AnimControl.class);
         AnimChannel animChannel = animControl.createChannel();
         SpatialHash.createGameObject(Vec3d.ZERO,1.3d, 0.40f, Vec3d.ZERO,30,0,1.5f,100,(byte) 1,(byte) 2,node,animControl,animChannel,playerGameObj.getPosition(),playerGameObj,(byte) 0 ,(byte)1,(byte) 10);
         node.setLocalTranslation(playerPositionNode.getWorldTranslation().x+(float)(-4 + Math.random() * (8 + 4)), tpf, playerPositionNode.getWorldTranslation().z+(float)(-4 + Math.random() * (8 + 4)));
        }
       
        
   TemporaryArrayList.clear();
   TemporaryArrayList.addAll(SpatialHash.index.values());
    for (int i=0; i < TemporaryArrayList.size(); i++){
        if(TemporaryArrayList.get(i).getType() ==3){
        TemporaryArrayList.get(i).getNode().move(TemporaryArrayList.get(i).getNode().getLocalRotation().getRotationColumn(2).multLocal(TemporaryArrayList.get(i).getMovementSpeed()*tpf));
      movementVector = new Vec3d((double)TemporaryArrayList.get(i).getNode().getWorldTranslation().x,(double)TemporaryArrayList.get(i).getNode().getWorldTranslation().y,(double)TemporaryArrayList.get(i).getNode().getWorldTranslation().z);
      TemporaryArrayList.get(i).setPosition(movementVector);

        }
        SpatialHash.updateObject(TemporaryArrayList.get(i));
        if(TemporaryArrayList.get(i).getType() != 3){
        SpatialHash.checkForCollisionSmall(TemporaryArrayList.get(i),tpf);   
        animUpdates(TemporaryArrayList.get(i),tpf);
        }

        
        
            }
   // unit update
   
   
   
   
        }     
}
