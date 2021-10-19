/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.LoopMode;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.CameraControl;
import com.jme3.scene.shape.Quad;
import com.jme3.math.Ray;
import com.simsilica.mathd.Vec3d;
import java.util.ArrayList;



/**
 *
 * @author Makary
 */
public class CameraAndMouseControl extends AbstractAppState  {
      private final AssetManager assetManager;
    private final InputManager inputManager;
    private final Node cameraControlNode = new Node("cameraControlNode");
    private final Node rootNode;
    private Node guiNode;
    private Node guiNode1 = new Node();
    public CameraNode camNode;
    private Camera camera;
    private boolean mouseLeftIsHeld = false;
    private float initialCursorX;
    private float initialCursorY;
    private Geometry mouseRect;
    Material mat;
    public static Vector3f unitScreenPosition;
    public ArrayList<Node> chosenUnitNodes = new ArrayList<>(); // chosen units 
    public static ArrayList<Vector3f> unitDestination = new ArrayList<>(); // zrobic hash mapa
    public static Vector3f lookingDestination;
    
    public static Vec3d destinationVector;
    static boolean enemyWasChosen;
    
    
    
    Node chosenEnemyUnitNode;
    Spatial selectionBox;
    Spatial enemySelectionBox;
     Node waypoint;
    Spatial waypointSpatial;
    public static Vector3f waypointZero; // waypoint.getWorldTranslation() but with replaced Y with 0


    
    public CameraAndMouseControl(SimpleApplication Main){
          rootNode = Main.getRootNode();
          assetManager = Main.getAssetManager();
          inputManager = Main.getInputManager();
          guiNode = Main.getGuiNode();
          camera = Main.getCamera();

    }
     @Override
    public void initialize(AppStateManager stateManager, Application app){
        super.initialize(stateManager, app);
        initMouseSelection();
        initCamera();
        initKeys();
        
        guiNode.attachChild(guiNode1);
        
     
    }
    
    
    
    public void initCamera(){
    rootNode.attachChild(cameraControlNode);
    camNode = new CameraNode("Camera Node", camera);
    camNode.setControlDir(CameraControl.ControlDirection.SpatialToCamera);
    UnitUpdate.playerPositionNode.attachChild(camNode);
    camNode.setLocalTranslation(new Vector3f(0, 12, 16)); // 0,15,20 // 0,18,24 // 0,12,16
    camNode.lookAt(UnitUpdate.playerNode.getLocalTranslation(), Vector3f.UNIT_Y);

    
    }
   
    
    public void initMouseSelection(){

    }

    

    
    
   private void initKeys() {


    }

    private final ActionListener actionListener = new ActionListener() {
        @Override
        public void onAction(String name, boolean keyPressed, float tpf) {

    
                
            }
            
        
    };
    


    private final AnalogListener analogListener = new AnalogListener() {
        @Override
        public void onAnalog(String name, float value, float tpf) {

        }
    };
    
    @Override
    public void cleanup(){

    super.cleanup();
    }
    @Override
    public void update(float tpf) {           

 
    CollisionResults results = new CollisionResults();
        Vector2f click2d = inputManager.getCursorPosition().clone();
        Vector3f click3d = camera.getWorldCoordinates(click2d, 0f).clone();
        Vector3f dir = camera.getWorldCoordinates(click2d, 1f).subtractLocal(click3d).normalizeLocal();
        Ray ray = new Ray(click3d, dir);
        MapAppState.terrainRootNode.collideWith(ray, results);
        for (int i = 0; i < results.size(); i++) {
          float distance = results.getCollision(i).getDistance();
          Vector3f contactPointPosition = results.getCollision(i).getContactPoint();
          String hit = results.getCollision(i).getGeometry().getName();
        }
        if (results.size() > 0) {
          CollisionResult closest = results.getClosestCollision();
          lookingDestination = closest.getContactPoint().clone();
          lookingDestination.setY(1.2f);
          UnitUpdate.playerNode.lookAt(closest.getContactPoint(), Vector3f.ZERO);
          

        }

   }

}
