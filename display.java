import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.universe.SimpleUniverse;
import java.applet.Applet;
import javax.media.j3d.Alpha;
import javax.media.j3d.Appearance;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Material;
import javax.media.j3d.PositionInterpolator;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;
/**
 * @author ArtificialBreeze
 */
public final class Display extends Applet {
SimpleUniverse u = new SimpleUniverse();
BranchGroup bgRoot = new BranchGroup();
Display(Bullet bullet)
{
}
  public BranchGroup createSceneGraph() {
    BranchGroup bg = new BranchGroup();
    Appearance app = new Appearance();
    Color3f objColor = new Color3f(0.8f, 0.2f, 1.0f);
    Color3f black = new Color3f(0.0f, 0.0f, 0.0f);
    app.setMaterial(new Material(objColor, black, objColor, black, 80.0f));
    return bg;
  }
  public void addLights(BranchGroup bg) {
    Color3f color = new Color3f(1.0f, 1.0f, 0.0f);
    Vector3f direction = new Vector3f(-1.0f, -1.0f, -1.0f);
    DirectionalLight light = new DirectionalLight(color, direction);
    light.setInfluencingBounds(getBoundingSphere());
    bg.addChild(light);
  }
  public BranchGroup createBackground() {
    BranchGroup backgroundGroup = new BranchGroup();
    Background back = new Background();
    back.setApplicationBounds(getBoundingSphere());
    BranchGroup bgGeometry = new BranchGroup();
    Appearance app = new Appearance();
    Sphere sphere = new Sphere(1.0f, Primitive.GENERATE_TEXTURE_COORDS
        | Primitive.GENERATE_NORMALS_INWARD, app);
    bgGeometry.addChild(sphere);
    back.setGeometry(bgGeometry);
    backgroundGroup.addChild(back);
    return backgroundGroup;
  }
  public TransformGroup createBehaviors(BranchGroup bg) {
    TransformGroup objTrans = new TransformGroup();
    objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
    Transform3D xAxis = new Transform3D();
    Alpha xAlpha = new Alpha(-1, Alpha.DECREASING_ENABLE
        | Alpha.INCREASING_ENABLE, 1000, 1000, 5000, 1000, 1000, 10000,
        2000, 4000);
    PositionInterpolator posInt = new PositionInterpolator(xAlpha,
        objTrans, xAxis, -0.8f, 0.8f);
    posInt.setSchedulingBounds(getBoundingSphere());
    objTrans.addChild(posInt);
    bg.addChild(objTrans);
    return objTrans;
  }
  BoundingSphere getBoundingSphere() {
    return new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 200.0);
  }
  public void addChild(TransformGroup tg)
  {
         BranchGroup customBg = new BranchGroup();
         customBg.addChild(tg);
         u.addBranchGraph(customBg);
  }
  public void graph(Bullet bullet)
  {
    u.addBranchGraph(createBackground());
    TransformGroup tg = createBehaviors(bgRoot);
    tg.addChild(createSceneGraph());
    u.getViewingPlatform().setNominalViewingTransform();
    addLights(bgRoot);
    u.addBranchGraph(bgRoot);
    addChild(bullet.getTg());
  }
}
