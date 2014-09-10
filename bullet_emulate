import java.util.*;
import javax.vecmath.*;
import org.uncommons.maths.random.GaussianGenerator;
import org.uncommons.maths.random.MersenneTwisterRNG;
/**
 * @author: Gh00st
 * Aerodynamics emulator
    Copyright (C) 2014  Gh0st

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
public class Bullet_emulate //implements Runnable
{
    ////CONSTANTS////
    final static double dt=0.00001;
    final static double Time=1;
    final static int    WorkerAmount=Runtime.getRuntime().availableProcessors();
          static int    n=100;///WorkerAmount; Splits the particles between threads
    final static double particleMass=5.6E-16;
    final static double molarMass=28.0/1000;
    final static double Avogadro=6.0221413E23;
    final static double altitude=1.76;//m
    final static double g=9.81;//m/s^2
     //Environment//
     /*final static double T=300; //Absolute temperature
      //Altitude: 1.76 m (average canadian standing male)
      //Relative Humidity: 69% in Quebec (St-Lawrence Gulf)
    */
    final static double airDensity=1.161;//kg/m^3
    static Vector3d velocity = new Vector3d(-400,0,0);
    static List<Particle> particles = new ArrayList<>();
    static Bullet bullet = new Bullet("cone","lead"); 
    static Renderer renderer=new Renderer(bullet);
                ////GRAPHICS////
    //static Display display = new Display(bullet);
    public static void main (String[] args)
	{     
            println("Aerodynamics simulation");
                //n=(int)getN(); Real value of n, but takes days to compute
                //Makes the renderer follow the bullet
            println("Adding child to the bullet => renderer");
                bullet.addChild(renderer);
                println("*Done!");
                println("->Populating the box");
                populate();
                println("*Done!");
                println("->Running the simulation. This may take a while. Sit down and watch an episode of Game of Thrones meanwhile...");
                updatePosition();
                println("*Done! Get back to work. You can witness the output");
                if (bullet.velocity.length()>velocity.length())
                    println("Some particles have collided from the back, resulting in a slight speed increase. Retry with a greater amount of particles please");
                else
                    println("Less than the original. Congratulations, seems to work!");
//display.graph(bullet);
	}
	//Populate the piece with air molecules
	public static void populate()
	{
			for (int i=0;i<n;i++)
				{
					particles.add(new Particle(particleMass, renderer,i));
                                        if (bullet.isInside(particles.get(i)))
                                        {
                                        particles.remove(i);
					particles.add(new Particle(particleMass, renderer,i));
                                        }
				}
	}
	//Update position
	public static void updatePosition()
	{
            while (bullet.center.getY()>-altitude)
            {
                    int[] collisions=collisionList(particles);
		for (int i=0;i<particles.size();i++)
			{
                            //Check if still taken care of (inside the box). 
                            //If no, re-populate the box (remove+add). Note that
                            //new particles already spawn inside of the box
                            insideOfBox(particles.get(i));
                            //particle collision. Disable for performance increase
                            particleCollision(i,particles,collisions);
                            //bullet collision
			 if (particles.get(i).hasCollided(bullet.solid))
			 {
                             bullet.setVelocity(particles.get(i).Collision(bullet)); 
			 }
                         //Update particle after each collision (particle-particle && particle-bullet)
                         particles.get(i).move(dt);
                        }//end list
                //Update bullet after each time elapsed
                bullet.gVelocity(dt,g);
                bullet.move(dt);
                println ("-"+bullet.velocity+"-");
                //No param since simply follows the center of the bullet
                renderer.move();
                
            }//end time
	}//end sub
        public static double getCollisionRadius()
        {
         return dt*velocity.length(); 
        }
        ///////////////////////////////////
	//Define print to save some lines//
	///////////////////////////////////
		public static void print(Object item)
		{
			System.out.print(item);
		}
		public static void println(Object item)
		{
				System.out.println(item);
                }
               public static double v()
               {
                   /*
                   95% of data is between [mean-2sigma, mean+2sigma]
                   */
                   //double sigma=Math.sqrt(534.215-Math.pow((2/Math.sqrt(Math.PI)*422),2));
                   //double probability=Math.sqrt(Math.pow(particleMass/(2*Math.PI*kT),2))*4*Math.PI*x*x*Math.pow(Math.E, -particleMass*x*x/(2*kT));
                   double vmin=422;
                   double vmax=Math.sqrt(1.5)*vmin;
                   Random rng = new MersenneTwisterRNG();
                   GaussianGenerator gauss = new GaussianGenerator(vmin, vmax-vmin, rng);
                   return Math.abs(gauss.nextValue());
               }
               public static double getN()
               {
                   return renderer.getVolume()*airDensity/molarMass*Avogadro;
               }
               public static void insideOfBox(Particle particle)
               {
                 if (!particle.isInsideOfBox(renderer))
                            {
                                int i=particle.getIndex();
                                particles.remove(i);
                                particles.add(new Particle(particleMass, renderer,i));
                  }
               }
               public static int[] collisionList(List<Particle> particles)
               {
                   int[] _ = new int[particles.size()];
                   //Initialize values to keep track if they were switched since no -1 particle exist (least is 0)
                   for (int i=0;i<_.length;i++)
                   {
                        _[i]=-1;
                   }
                   for (int i=0;i<particles.size();i++)
                   {
                       for (int j=0;j<particles.size();j++)
                       {
                           //If value is something else than -1 means it was flipped, so skip it
                           if (_[i]!=-1)
                           {
                               continue;
                           }
                           else {
                               //Check if collision occured
                           if (particles.get(i).hasCollided(particles.get(j)))
                               //If yes, store the other particle's list address in both variables (since they interchange and we dont know whether i<j or i>j)
                                _[i]=j;
                                _[j]=i;
                           }
                       }
                   }
                   //Return the array with collision pairs
                   return _;
               }
               public static void particleCollision(int i, List<Particle> particles,int[] collisions)
               {
                   //Index of the complementary particle (collider)
                   int collider=collisions[i];
                   if (collider!=-1)
                   {
                       //Perform the collision
                       particles.get(collider).setVelocity(particles.get(i).Collision(particles.get(collider)));
                       //Prevent from being recalculated until next time dt
                       //We could have only put the first line, since they are inversely defining each other (i points to collider and collider points to i
                       //However, it is better to prevent another calculation and reset both array values to -1
                       collisions[i]=-1;
                       collisions[collider]=-1;
                   }
               }
               public static double time()
               {
                   //Recall x=xi+t^2*vi-1/2 * a * t: dx=-1/2 * g *t => 2dx/g=t // Time taken to reach the ground from a given height
                   return (2*altitude)/g;
               }
}
