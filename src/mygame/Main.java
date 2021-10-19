package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.renderer.RenderManager;
import com.jme3.system.AppSettings;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;




public class Main extends SimpleApplication {
    private BulletAppState bulletAppState;
    
    
    public static void main(String[] args) {
        Main app = new Main();
        
       AppSettings newSettings = new AppSettings(true);

        newSettings.setFrameRate(2000); // physics dont work properly for fps lower than 100 (to be dependent on tpf)

        app.setSettings(newSettings);
              app.start();

    }
    
    
    @Override
    public void simpleInitApp() {
         flyCam.setEnabled(false);
         stateManager.attach(new MapAppState(this));
         stateManager.attach(new UnitUpdate(this));
         stateManager.attach(new CameraAndMouseControl(this));
         
    }


    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}
