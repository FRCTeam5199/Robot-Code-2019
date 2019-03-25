package frc.drive;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.ctre.phoenix.sensors.PigeonIMU;
import frc.drive.DriveBase;
import frc.controllers.XBoxController;
import frc.interfaces.LoopModule;
import edu.wpi.first.wpilibj.RobotDrive;

public class DriveControl implements LoopModule{

    private final DriveBase base;
    //old is 25
    private final double rSpeed = 45;
    //used to be 65 OR 55
    private final XBoxController controller;
    //old is .75 OR 7
    private final double speed = .66;

    //

    public DriveControl(DriveBase base, XBoxController controller) {
        this.base = base;
        this.controller = controller;

    }

    public void init(){
        base.setDriveCoast();
        //base.setDriveBrake();
        base.setDriveCurrentMax();
        base.initGyro();
    }

    public void tankControl() {
		double speedMultiplier = speed;
		double right = controller.getStickRY();
		double left = controller.getStickLY();

		// Right trigger boost
		speedMultiplier += (1 - speed) * controller.getRTrigger();
		right *= speedMultiplier;
        left *= speedMultiplier;

		// Left trigger straighten
		double avg = (right + left) / 2;
		double lTrigger = controller.getLTrigger();
		double notLTrigger = 1 - lTrigger;
		right = notLTrigger * right + avg * lTrigger;
		left = notLTrigger * left + avg * lTrigger;

		base.drive(right, left);
    }

    public double skim(double v){
        if (v > 1.0) {
            return -((v - 1.0) * rSpeed);
        } else if (v < -1.0) {
            return -((v + 1.0) * rSpeed);
        }else{
            return 0;
        }
    }

    public void arcadeControl() {
        double targetSpeed = controller.getStickRX() * rSpeed;
		double turnSpeed = targetSpeed * .01;
        base.drive((controller.getStickLY()*(speed)) + turnSpeed, (controller.getStickLY()*speed) - turnSpeed);
        
       /*  base.drive();
         */
    }
    
    public void arcadeControlAssisted() {
		double targetSpeed = controller.getStickRX() * rSpeed;
		double currentSpeed = base.getGyroRate();
		double turnSpeed = (targetSpeed - currentSpeed) * .01;
		base.drive((controller.getStickLY()*speed) + turnSpeed, (controller.getStickLY()*speed) - turnSpeed);
    } 
    
    public void printSticks(){
        System.out.println(controller.getStickLX() + " , " + controller.getStickLY());
        System.out.println(controller.getStickRX() + " , " + controller.getStickRY());
    }

    public DriveBase getBase() {
        return base;
    }

    @Override
    public void update(long delta){
        base.gearChange(controller.getButton(6));
        //tankControl();
        arcadeControl();
        //arcadeControlAssisted();
        //base.printGyroVals();
        // printSticks();
    }

}