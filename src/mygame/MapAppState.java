/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import static com.jme3.bullet.PhysicsSpace.getPhysicsSpace;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.input.InputManager;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Node;
import com.jme3.scene.control.CameraControl.ControlDirection;
import com.jme3.terrain.geomipmap.TerrainLodControl;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.heightmap.AbstractHeightMap;
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;

/**
 *
 * @author Makary
 */
public class MapAppState extends AbstractAppState{
    private TerrainQuad terrainQuad;
    private RigidBodyControl terrainRigidBody;
    private final Node rootNode;
    public static final Node terrainRootNode = new Node ("terrainRootNode");
    private final AssetManager assetManager;
    private final InputManager inputManager;
    private Camera camera;
    private Node terrain;
    public BulletAppState bulletAppState;
    
    

     public MapAppState(SimpleApplication Main){
         rootNode = Main.getRootNode();
          assetManager = Main.getAssetManager();
          inputManager = Main.getInputManager();
          camera = Main.getCamera();
    }
     @Override
    public void initialize(AppStateManager stateManager, Application app){
        super.initialize(stateManager, app);
        initTerrain();  

    }
    
    private void initTerrain(){
        DirectionalLight sun = new DirectionalLight();
    sun.setColor(ColorRGBA.White);
    sun.setDirection(new Vector3f(-0.5f,-0.7f,-0.5f).normalizeLocal());
    rootNode.addLight(sun);
        rootNode.attachChild(terrainRootNode);
         

    
    terrain = (Node) assetManager.loadModel("Models/FloorTile/FloorTile.j3o");
    terrain.scale(50);

    terrainRootNode.attachChild(terrain);
    


    }
    

    
    

    
    @Override
    public void cleanup(){
    rootNode.detachChild(terrainRootNode);
    
    super.cleanup();
    }
    @Override
    public void update(float tpf) {

   }
}
