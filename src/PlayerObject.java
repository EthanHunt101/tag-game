public class PlayerObject {
    //this class handles the actual position of the player in the game
    //the player will be rendered by constantly setting the position of a shape or something to the
    //x-y of the player

    //private - prevents bad cross-communication from multiple instances of the same class
    private double xpos = 0;
    private double ypos = 0;
    //current running total for x and y velocity
    private double xvel = 0;
    private double yvel = 0;
    //current running total for x and y force
    private double xforce = 0;
    private double yforce = 0;
    //bools of player states
    private boolean invincible = false;
    //limits on x and y positions
    private double xmin = 0;
    private double xmax = 0;
    private double ymin = 0;
    private double ymax = 0;

    private double maxSpeed = 15;
    private double minSpeed = 0.1;
    private double mass = 2;
    //private double drag = 0.5;
    private double driveForce = 5;
    private double derivedDrag = this.driveForce / (this.maxSpeed);
    private double bouncefactor = 0.2;
    private double dashVel = maxSpeed * 2;

    public double getXpos(){
        return this.xpos;
    }
    public double getYpos(){
        return this.ypos;
    }
    public void setXpos(double xposin){
            this.xpos = xposin;
        }
    public void setYpos(double yposin){
            this.ypos = yposin;
        }
    public void setXmin(double xminin){
            this.xmin = xminin;
        }
    public void setXmax(double xmaxin){
            this.xmax = xmaxin;
        }
    public void setYmin(double yminin){
            this.ymin = yminin;
        }
    public void setYmax(double ymaxin){
            this.ymax = ymaxin;
        }
    public void setXvel(double xvelin){ this.xvel = xvelin; }
    public void setYvel(double yvelin){ this.yvel = yvelin; }
    public double getXforce() {return this.xforce; }
    public double getYforce() {return this.yforce; }
    public void setXforce(double xforcein){this.xforce = xforcein;}
    public void setYforce(double yforcein){this.yforce = yforcein;}


    public boolean getInvincible(){return this.invincible;}

    private int sign(double value){
        if(value > 0){
            return 1;
        } else if(value < 0){
            return -1;
        } else {
            return 0;
        }
    }

//dashing: 1) add a velocity to dashed player above equilibrium (max speed)
    // 2) set invincible to false
    // 3) make it so when velocity decays back to max speed + a little, not invincible
    // maybe: checking happens in array move

    //dash in direction of move array if both dirs not pressed down
    //if both sides pressed down, dash in direction of velocity
    public void dash(int[] dirs){
        //if not opposing directions held, impulse in dir of holding
        //y direction
        if( !(dirs[0] == dirs[1])){
            this.yvel += ((-1 * dirs[0] * this.dashVel) + (dirs[1] * this.dashVel));
        } else {
            //otherwise, dash in direction of velocity
            this.yvel += sign(this.yvel) * this.dashVel;
        }
        //x direction
        if( !(dirs[2] == dirs[3])){
            this.xvel += ((-1 * dirs[2] * this.dashVel) + (dirs[3] * this.dashVel));
        } else {
            //otherwise, dash in direction of velocity
            this.xvel += sign(this.xvel) * this.dashVel;
        }
        this.invincible = true;

    }

    //forces are added each loop of the main game function
    //they are processed in the

    //add the forces from movement for this frame
    public void addMovementForces(int[] dirs){
        this.xforce += ((-1 * dirs[0] * driveForce) + (dirs[1] * driveForce) + (-1 * derivedDrag * this.yvel ) );
        this.yforce += ((-1 * dirs[2] * driveForce) + (dirs[3] * driveForce) + (-1 * derivedDrag * this.xvel ) );
    }

    public void forceToVel(){
        //accelerate based on force
        this.xvel += (this.xforce * this.mass);
        this.yvel += (this.yforce * this.mass);

    }

    public void advArrayMove(int[] dirs){
        double yac = ((-1 * dirs[0] * driveForce) + (dirs[1] * driveForce) + (-1 * derivedDrag * this.yvel ) ) / this.mass;

        double xac = ((-1 * dirs[2] * driveForce) + (dirs[3] * driveForce) + (-1 * derivedDrag * this.xvel ) ) / this.mass;

        this.yvel += yac;
        this.xvel += xac;
        if(Math.abs(this.yvel) < this.minSpeed){
            this.yvel = 0;
        }
        if(Math.abs(this.xvel) < this.minSpeed){
            this.xvel = 0;
        }

        if(this.xpos < xmin){
            this.xvel = bouncefactor * Math.abs(this.xvel);
        }
        if(this.xpos > xmax){
            this.xvel = -1 * bouncefactor * Math.abs(this.xvel);
        }
        if(this.ypos < ymin){
            this.yvel = bouncefactor * Math.abs(this.yvel);
        }
        if(this.ypos > ymax ){
            this.yvel = -1 * bouncefactor * Math.abs(this.yvel);
        }

        this.ypos += this.yvel;
        this.xpos += this.xvel;
        //check for dashing and if they are still INVINCIBLE (tm)
        if((Math.abs(this.yvel) < this.maxSpeed + 1) || (Math.abs(this.xvel) < this.maxSpeed + 1)){
            this.invincible = false;
        }
    }





}
