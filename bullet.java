import com.sun.j3d.utils.geometry.Cone;
import javax.media.j3d.*;
import javax.vecmath.*;
/**
 * @author: ArtificialBreeze
*/
public class Bullet
{
//Definitions
static double material;
static double length;
static double diameter;
static double mass;
static double density;
//Objects
static Cone solid;
static Vector3d velocity;
static String materialName;
static Point3d center;
static Transform3D rotate;
static Transform3D translate = new Transform3D();
static TransformGroup tg = new TransformGroup();
//Friction coefficients of the most commonly used metals
final static double copper=0.29;
final static double steel=0.23;
final static double lead=0.43;
  //Construct the bullet
  Bullet (String material,String shape)
	{
            synchronized(this){
        this.tg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        this.tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        this.rotate = new Transform3D();
	//Saving a backup of the name to use it for user interaction
	this.materialName=material;
	//Linking construtor @param speed with the object
	this.velocity=new Vector3d(Bullet_emulate.velocity.x,Bullet_emulate.velocity.y,Bullet_emulate.velocity.z);
	//Setting real bullet dimensions
	this.length=0.022;
	this.diameter=0.0075;
	//Defining the material right away
        switch(material){
	case "copper":
	 this.material=copper;
	 this.density = 8.9;
	break;
	case "steel":
	 this.material=steel;
	 this.density = 7.8;
	break;
	case "lead":
 	 this.material = lead;
 	 this.density = 11.34;
	break;
        default:
	 this.material = steel;
	 this.density = 7.8;
	break;
        }
	//Linking constructor @param mass with the object (M=V*d), density at room temperature
	this.mass=density*length*100*Math.PI*Math.pow((diameter*100/2),2);
        switch(shape)
        {
            //Here we can add other shapes included with the 3D Java API
            case "cone":
            solid=new Cone((float)diameter/2,(float)length);
            break;
            default:
            solid=new Cone((float)diameter/2,(float)length);
            break;
        }
    setChild();
            }
  }
	//Functions definitions//
	public static boolean isInside(Particle particle)
	{
            boolean check=true;
            check =(particle.center.getX()>center.getX()+solid.getHeight()/2)? false :true;
            check =(particle.center.getX()<center.getX()-solid.getHeight()/2)? false :true;
            double theta=Math.atan(solid.getRadius()/solid.getHeight());
            double thetaY=Math.atan(particle.center.getY()/particle.center.getX());
            double thetaZ=Math.atan(particle.center.getZ()/particle.center.getX());
            if (thetaY>theta || thetaZ>theta)
                check=false;
            return check;
            //return !solid.getBounds().isEmpty();
	}
        /*
        tg
        | \
 tgRotate  translate
   /  |   \
solid rotate renderer
       */
        public static void setChild()
        {
            //Hack to rotate the bullet
            rotate.rotZ(Math.PI/2);
            tg.addChild(solid);
            tg.setTransform(translate);
            translate.mul(rotate);
            //Get the center of the shape (position). Initialized by default to 0,0,0
            center = new Point3d(0,0,0);
        }
        public void move(double dt)
        {
            velocity.scale(dt);
            //Log position
            Tuple3d tuple=new Tuple3d() {};
            velocity.get(tuple);
            //Set translation
            Vector3d oldVelocity=new Vector3d();
            translate.get(oldVelocity);
            velocity.add(oldVelocity);
            translate.setTranslation(velocity);
            oldVelocity.negate();
            velocity.add(oldVelocity);
            //Reset velocity back
            velocity.scale(1/dt);
            tg.setTransform(translate);
            center.add(tuple);
            }
        public void addChild(Renderer renderer)
        {
            tg.addChild(renderer.box);
        }
        public TransformGroup getTg()
        {
            return tg;
        }
        public void setVelocity(Vector3d newVelocity)
 {
     if (newVelocity.length() <300000)
	 velocity.set(newVelocity);
     else
     {
         newVelocity.scale(300000/newVelocity.length());
         velocity.set(newVelocity);
     }
 }
        public void gVelocity(double dt,double g)
        {
            Vector3d gravity=new Vector3d(0,-g*dt,0);
            velocity.add(gravity);
        }
}
