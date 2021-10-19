/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.scene.Node;
import com.simsilica.mathd.Vec3d;

/**
 *
 * @author Makary
 */
public class GameObject { // to be refactored to be programmer friendly
    
        public  float radius;
    public  Vec3d position;
    Vec3d previousCellVec;
    public int meleeDamage;
    public int rangedDamage;
    public float range;
    public int health;
    public byte type; // 1-melee unit// 2- ranged unit //3 - projectile// 4- static // 5- player
    public byte action; // 0- idle/1 - defensive stance//2-attacking// 3-dead//4 - running
    public AnimControl animControl;
    public AnimChannel animChannel;
    public Vec3d destination;
    public GameObject target;
    public byte allegiance;
    public Node node;
    public byte movementSpeed;
    public byte armor;
    public double hitboxHeight;
   
    public byte getMovementSpeed(){
    return movementSpeed;
    }
    public void setMovementSpeed(byte movementSpeed){
    this.movementSpeed = movementSpeed;
    }
    
    public byte getArmor(){
    return armor;
    }
    
    public void setArmor(byte armor){
    this.armor = armor;
    }
    
    
    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }
    
    public double getHitboxHeight() {
        return hitboxHeight;
    }

    public void setHitboxHeight(double hitboxHeight) {
        this.hitboxHeight = hitboxHeight;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }
    public Vec3d getPosition() {
        return position;
    }

    public void setPosition(Vec3d position) {
        this.position = position;
    }

    public Vec3d getPreviousCellVec() {
        return previousCellVec;
    }

    public void setPreviousCellVec(Vec3d previousCellVec) {
        this.previousCellVec = previousCellVec;
    }

    public int getMeleeDamage() {
        return meleeDamage;
    }

    public void setMeleeDamage(int meleeDamage) {
        this.meleeDamage = meleeDamage;
    }

    public int getRangedDamage() {
        return rangedDamage;
    }

    public void setRangedDamage(int rangedDamage) {
        this.rangedDamage = rangedDamage;
    }

    public float getRange() {
        return range;
    }

    public void setRange(float range) {
        this.range = range;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public byte getAction() {
        return action;
    }

    public void setAction(byte action) {
        this.action = action;
    }

    public AnimControl getAnimControl() {
        return animControl;
    }

    public void setAnimControl(AnimControl animControl) {
        this.animControl = animControl;
    }

    public AnimChannel getAnimChannel() {
        return animChannel;
    }

    public void setAnimChannel(AnimChannel animChannel) {
        this.animChannel = animChannel;
    }

    public Vec3d getDestination() {
        return destination;
    }

    public void setDestination(Vec3d destination) {
        this.destination = destination;
    }

    public GameObject getTarget() {
        return target;
    }

    public void setTarget(GameObject target) {
        this.target = target;
    }

    public byte getAllegiance() {
        return allegiance;
    }

    public void setAllegiance(byte allegiance) {
        this.allegiance = allegiance;
    }
}
