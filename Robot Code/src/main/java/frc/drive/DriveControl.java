package frc.drive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.ctre.phoenix.sensors.PigeonIMU;
import frc.drive.DriveBase;
import frc.controllers.XBoxController;
import frc.interfaces.LoopModule;
import edu.wpi.first.wpilibj.RobotDrive;

public class DriveControl implements LoopModule{

    private final DriveBase base;
    private final XBoxController controller;
    //comp bot
    private final double rSpeed = 45;
    private final double kSpeed = .66;
    //practice bot w/ dead motors
    /* private final double rSpeed = 70;
    private final double kSpeed = .9; */

    private double speed;
    private static boolean jesusHasWheel;

    public DriveControl(DriveBase base, XBoxController controller) {
        this.base = base;
        this.controller = controller;

        base.initGyro();
        jesusHasWheel = false;
        speed = kSpeed;
    }

    public void init(){
        base.setDriveCoast();
        //base.setDriveBrake();
        //base.setDriveCurrentMax();

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

    public void jesusTakeTheWheel(){
        base.enableAimPID();
        base.gearChange(false);
        base.aimLightsOn();
    }

    public void returnDriverControl(){
        base.disableAimPID();
        base.aimLightsOff();
    }

/*     public double skim(double v){
        if (v > 1.0) {
            return -((v - 1.0) * rSpeed);
        } else if (v < -1.0) {
            return -((v + 1.0) * rSpeed);
        }else{
            return 0;
        }
    } */

    public void arcadeControl() {
        double targetSpeed = controller.getStickRX() * rSpeed;
		double turnSpeed = targetSpeed * .01;
        base.drive((controller.getStickLY()*(speed)) + turnSpeed, (controller.getStickLY()*(speed)) - turnSpeed);

    }
    
    public void arcadeControlAssisted() {
		double targetSpeed = controller.getStickRX() * rSpeed;
		double currentSpeed = base.getGyroRate();
        double turnSpeed = (currentSpeed - targetSpeed) * 01;
        // double turnSpeed = (targetSpeed - currentSpeed) * .01;
        //not sure which is right
		base.drive((controller.getStickLY()*speed) + turnSpeed, (controller.getStickLY()*speed) - turnSpeed);
    } 
    
    public void printSticks(){
        System.out.println(controller.getStickLX() + " , " + controller.getStickLY());
        System.out.println(controller.getStickRX() + " , " + controller.getStickRY());
    }

    public void printEncoderPos(){
        SmartDashboard.putNumber("Encoder L: ", base.getEncoderLPos());
        SmartDashboard.putNumber("Encoder R: ", base.getEncoderRPos());
    }

    public DriveBase getBase() {
        return base;
    }

    @Override
    public void update(long delta){
        printEncoderPos();
        //base.printGyroVals();
        // printSticks(); 

        if(controller.getButtonDown(6)){
            jesusTakeTheWheel();
            jesusHasWheel = true;
        }
        else if(controller.getButtonUp(6)){
            returnDriverControl();
            jesusHasWheel = false;
        }
        if(!jesusHasWheel){
            base.gearChange(controller.getButton(5));
            if(controller.getButtonDown(5)){
                speed = 1;
            }
            else if(controller.getButtonUp(5)){
                speed = kSpeed;
            }
            //tankControl();
            arcadeControl();
            //arcadeControlAssisted();
        }
        else{
            base.driveAim(controller.getStickLY()*0.5);
        }
    }

}