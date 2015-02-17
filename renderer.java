import com.sun.j3d.utils.geometry.Box;
import java.util.HashMap;
import javax.media.j3d.Appearance;
import javax.vecmath.Point3d;
/**
 * @author ArtificialBreeze
 */
public class Renderer {
    HashMap<String,Float> coordinates;
    Box box;
    Point3d center;
    Bullet bullet;
    Renderer(Bullet bullet)
    {
     this.bullet=bullet;
     this.coordinates = new HashMap<>();
     this.center=new Point3d (bullet.center);
     coordinates.put("x",0.5f);
     coordinates.put("y",0.5f);
     coordinates.put("z",0.5f);
     box=new Box(coordinates.get("x"),coordinates.get("y"),coordinates.get("z"),new Appearance());
    }
    public double getVolume()
    {
        return box.getZdimension()*box.getYdimension()*box.getXdimension();
    }
    public void move()
    {
        center=bullet.center;
    }
}
