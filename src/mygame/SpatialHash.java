/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

import com.simsilica.mathd.Grid;
import com.simsilica.mathd.GridCell;
import com.simsilica.mathd.Vec3d;
import com.simsilica.mathd.Vec3i;

/**
 *
 * @author Makary
 */
public class SpatialHash {
    public static Multimap<GridCell, GameObject> index = MultimapBuilder.hashKeys().hashSetValues().build();
    public static Grid worldGrid;
    public static GridCell cell;
    public static GridCell previousCell;
    public static int gridSize =1;
    public static Vec3i min;
    public static Vec3i max;
    
    public static void Setup(){
        worldGrid = new Grid(gridSize);
    }
    
    public static void AddObject(GameObject gameObj){  // "puts" gameObj in a cell based on its position
    cell = worldGrid.getContainingCell(gameObj.getPosition());  
    index.put(cell, gameObj);
//    System.out.println("added: " + gameObj);
    if(gameObj.getAllegiance() == UnitUpdate.yourAllegiance && gameObj.getType() !=3 ){
    UnitUpdate.yourUnits.add(gameObj);
    } else if(gameObj.getAllegiance() != UnitUpdate.yourAllegiance && gameObj.getType() !=3) {
    UnitUpdate.enemyUnits.add(gameObj);
    }
    
    }
    
    public static void updateObject(GameObject gameObj){
        if(gameObj.getType() == 3 && gameObj.getAllegiance() == UnitUpdate.yourAllegiance){ // if gameObj is a player made projectile, it terminates itself after travelling 50 meters
// ENEMY MADE PROJECTILES DONT FREE UP THE MEMORY - TO BE FIXED
            if(gameObj.getPosition().toVector3f().distance(UnitUpdate.playerPositionNode.getWorldTranslation()) >= 50){
               gameObj.getNode().removeFromParent();
                index.values().remove(gameObj);
                gameObj = null; // pozwala garbage collectorowi zebrac nullowy obiekt przez co nie ma pokazu slajdow przy tysiacach obiektow

        }
        }
        if(gameObj != null){
        if(gameObj.getAction() != 3 && gameObj.getHealth() <= 0 && gameObj.getType() !=3){
        gameObj.setAction((byte) 3);
        if(gameObj.getType() == 5){ // if the player dies, mark him as dead therefore restricting him from taking actions
        UnitUpdate.playerIsDead = true;
        }
        if(gameObj.getAllegiance() == UnitUpdate.yourAllegiance && gameObj.getType() != 3){
            UnitUpdate.yourUnits.remove(gameObj);
        }
        else if(gameObj.getAllegiance() != UnitUpdate.yourAllegiance && gameObj.getType() != 3){
            UnitUpdate.enemyUnits.remove(gameObj);
        }
        index.values().remove(gameObj);
        }
        if(gameObj.getAction() != 3){
        cell = worldGrid.getContainingCell(new Vec3d(gameObj.getNode().getWorldTranslation().x,gameObj.getNode().getWorldTranslation().y,gameObj.getNode().getWorldTranslation().z));
        if(!index.get(cell).contains(gameObj)){
        previousCell = worldGrid.getContainingCell(gameObj.getPreviousCellVec());
        index.get(previousCell).remove(gameObj); 
        index.put(cell, gameObj);
        gameObj.setPreviousCellVec(cell.getWorldOrigin().toVec3d());
        }
        }
        }
    }
  
    
    public static void checkForCollisionSmall(GameObject gameObj,float tpf){ // this method checks for collision for small (with smaller radius than grid size) objects
    min = worldGrid.worldToCell(new Vec3d(gameObj.getPosition().x-gameObj.getRadius(),gameObj.getPosition().y-gameObj.getRadius(),gameObj.getPosition().z-gameObj.getRadius()));
    max = worldGrid.worldToCell(new Vec3d(gameObj.getPosition().x+gameObj.getRadius(),gameObj.getPosition().y+gameObj.getRadius(),gameObj.getPosition().z+gameObj.getRadius()));

for( int x = min.x; x <= max.x; x++ ) {  
    for( int y = min.y; y <= max.y; y++ ) {
        for( int z = min.z; z <= max.z; z++ ) {
            GridCell testedCell = worldGrid.getContainingCell(x, y, z);
            for(GameObject collisionObject : index.get(testedCell)){
                if(gameObj.getType() ==1 && collisionObject.getType() == 3){
               
 
                }
                if(collisionObject.getType() == 3 &&  gameObj.getAllegiance() != UnitUpdate.yourAllegiance && (gameObj.getPosition().distance(collisionObject.getPosition()) <= gameObj.getRadius() ^ gameObj.getPosition().distance(collisionObject.getPosition()) <= collisionObject.getRadius())){
                    gameObj.setHealth(gameObj.getHealth()-(collisionObject.getMeleeDamage()-gameObj.getArmor()));
                }

            }
        }
    }
}
    
    }

    

    
    
    public static void createGameObject(Vec3d spawnpoint,double hitboxHeight,float radius,Vec3d spawnpointCopy,int meleeDamage,int rangedDamage,float range, int health,byte type,byte action,Node node,AnimControl animControl, AnimChannel animChannel,Vec3d destination, GameObject target,byte allegiance,byte movementSpeed,byte armor){
    GameObject newObject = new GameObject();  // object is a game unit
    newObject.setHitboxHeight(hitboxHeight);
    newObject.setPosition(new Vec3d(spawnpoint.x,spawnpoint.y+hitboxHeight,spawnpoint.z));
    newObject.setRadius(radius);
    newObject.setType(type);
    newObject.setPreviousCellVec(spawnpointCopy);
    newObject.setMeleeDamage(meleeDamage);
    newObject.setRangedDamage(rangedDamage);
    newObject.setRange(range);
    newObject.setHealth(health);
    newObject.setType(type);
    newObject.setNode(node);
    node.setLocalTranslation(spawnpoint.toVector3f());
    UnitUpdate.unitRootNode.attachChild(node);
    newObject.setAction(action);
    newObject.setAnimControl(animControl);
    newObject.setAnimChannel(animChannel);
    if(newObject.getType() !=3 && newObject.getType() != 5){
    animChannel.setAnim("Idle");
    } else if(newObject.getType() ==3 ){
            newObject.getNode().lookAt(destination.toVector3f(), Vector3f.ZERO);
//    newObject.getNode().lookAt(target.getPosition().toVector3f(), Vector3f.ZERO);
    }
    newObject.setTarget(target);
    newObject.setAllegiance(allegiance);
    newObject.setMovementSpeed(movementSpeed);
    newObject.setArmor(armor);
    
           if(newObject.getType() == 5){
   UnitUpdate.playerGameObj = newObject;
   }
    AddObject(newObject);
    }
 

    
}
