//import com.sun.j3d.utils.geometry.Sphere; //Sphere Class
import com.sun.j3d.utils.geometry.Cone;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.*;
/**
 * @author: ArtificialBreeze
*/
public class Particle
{
int i;
double mass, collisionRadius, theta,phi,r;
double[] position;
//Objects declaration
Point3d center;
BoundingSphere sphere;
Vector3d velocity;
Transform3D translate;
TransformGroup tg;
Renderer renderer;
 Particle (double _mass, Renderer renderer, int i)
 {
         this.i=i;
         this.renderer=renderer;
	 mass=_mass;
         //Splitting speed into random components along random axis
         r=Bullet_emulate.v();
         //2PI since has to be +-
         /*         PI/2
                     |
                   --|--
           PI ___/___|____\____>0
                 |   |    |
                  \ _|_  /
                     |
                     |
                -PI/2 or 3PI/2
         */
         theta=Math.random()*2*Math.PI;
         phi=Math.random()*2*Math.PI;
	 velocity=new Vector3d(r*Math.sin(theta)*Math.cos(phi),r*Math.sin(theta)*Math.sin(phi) ,r*Math.cos(theta) );
	 position=new double[3];
         //Relative to the box center so it renders properly
	 position[0]=(renderer.center.getX()-renderer.box.getXdimension()/2)+Math.random()*renderer.box.getXdimension();
	 position[1]=(renderer.center.getY()-renderer.box.getYdimension()/2)+Math.random()*renderer.box.getYdimension();
	 position[2]=(renderer.center.getZ()-renderer.box.getZdimension()/2)+Math.random()*renderer.box.getZdimension();
	 collisionRadius=Bullet_emulate.getCollisionRadius();
	 center=new Point3d(position);
         sphere=new BoundingSphere(center,collisionRadius);
 }
/////    MODIFIERS     //////
 public void setVelocity(Vector3d velocity)
 {
	 if (velocity.length() <300000)
	 this.velocity.set(velocity);
         else
             this.velocity.set(this.velocity);
 }
 public void move(double dt)
 {      //Log position to always have x(n-1) 
	position[0]=center.getX();
	position[1]=center.getY();
	position[2]=center.getZ();
        velocity.scale(dt);
        Tuple3d tuple=new Tuple3d() {};
        velocity.get(tuple);
        center.add(tuple);
        //Reset velocity back
        velocity.scale(1/dt);
        sphere.setCenter(center);
}
 public boolean hasCollided(Cone cone)
 {
     return sphere.intersect(cone.getBounds());
 }
 //Since this BoundingSphere returns no Bounds, check if one sphere's center is inside another sphere's bounds
 public boolean hasCollided(Particle particle)
 {  Point3d _p = new Point3d();
    particle.sphere.getCenter(_p);
     return sphere.intersect(_p);
 }
 public boolean isInsideOfBox(Renderer renderer)
 {
     boolean check=false;
     if (center.getX()<renderer.center.getX()+renderer.box.getXdimension()/2||center.getX()>renderer.center.getX()-renderer.box.getXdimension()/2)
         check=true;
     if (center.getY()<renderer.center.getY()+renderer.box.getYdimension()/2||center.getY()>renderer.center.getY()-renderer.box.getYdimension()/2)
         check=true;
     if (center.getZ()<renderer.center.getZ()+renderer.box.getZdimension()/2||center.getZ()>renderer.center.getZ()-renderer.box.getZdimension()/2)
         check=true;
     return check;
 }
 public Vector3d Collision(Bullet bullet)
    {
        double bulletMass=bullet.mass;
        Vector3d bulletSpeed=new Vector3d(bullet.velocity.x,bullet.velocity.y,bullet.velocity.z);
        //Speeds X,Y,Z
        Vector3d n=new Vector3d(center.x-bullet.center.x,center.y-bullet.center.y,center.z-bullet.center.z);
        n.normalize();
        double a1 = bulletSpeed.dot(n);
        double a2 = velocity.dot(n);
        double optimizedP = (2.0 * (a1 - a2)) / (bulletMass + mass);
        //Particle velocity
        n.scale(optimizedP * mass);
        Tuple3d tuple=new Tuple3d() {};
        n.get(tuple);
        bulletSpeed.sub(tuple);
        n.scale(1/(optimizedP * mass));
        //Bullet velocity
        n.scale(optimizedP * bulletMass);
        n.get(tuple);
        velocity.add(tuple);
        n.scale(1/(optimizedP * bulletMass));
        return bulletSpeed;
	}
 public Vector3d Collision(Particle particle)
    {
        double particleMass=particle.mass;
        Vector3d particleSpeed=new Vector3d(particle.velocity.x,particle.velocity.y,particle.velocity.z);
        //Speeds X,Y,Z
        Vector3d n=new Vector3d(center.x-particle.center.x,center.y-particle.center.y,center.z-particle.center.z);
        n.normalize();
        double a1 = particleSpeed.dot(n);
        double a2 = velocity.dot(n);
        double optimizedP = (2.0 * (a1 - a2)) / (particleMass + mass);
        //Particle velocity
        n.scale(optimizedP * mass);
        Tuple3d tuple=new Tuple3d() {};
        n.get(tuple);
        particleSpeed.sub(tuple);
        n.scale(1/(optimizedP * mass));
        //Bullet velocity
        n.scale(optimizedP * particleMass);
        n.get(tuple);
        velocity.add(tuple);
        n.scale(1/(optimizedP * particleMass));
        return particleSpeed;
	}
 public int getIndex()
 {
     return i;
 }
}
