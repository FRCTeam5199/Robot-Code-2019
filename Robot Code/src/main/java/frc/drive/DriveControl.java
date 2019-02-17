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
    //private final PigeonIMU gyro;
    private final XBoxController controller;

    private final double speed = 1;

    public DriveControl(DriveBase base, XBoxController controller) {
        this.base = base;
        this.controller = controller;
        //this.gyro = base.getGyro();

    }

    public void init(){

    }

    @Override
    public void update(long delta){
        base.gearChange(controller.getButton(6));
        tankControl();
        base.getGyro();
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

    public DriveBase getBase() {
        return base;
    }
}