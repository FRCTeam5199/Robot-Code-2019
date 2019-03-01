package frc.grabber;

import edu.wpi.first.wpilibj.Solenoid;
import frc.robot.RobotMap;

import java.awt.Panel;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

public class Grabber {

    private VictorSPX intake;
    private Solenoid gripperPiston;
    private Solenoid pokePistons;
    private Solenoid hatchGuidePiston;

    public Grabber() {
        gripperPiston = new Solenoid(RobotMap.gripperPiston);
        pokePistons = new Solenoid(RobotMap.pokePistons);
        intake = new VictorSPX(RobotMap.intakeMotor);
        //hatchGuide = new Solenoid(RobotMap.hatchGuidePiston);

        intake.setNeutralMode(NeutralMode.Brake);
    }

    public void setGrabber(boolean b) {
        gripperPiston.set(b);
    }

    public void setPokers(boolean b) {
        pokePistons.set(b);
    }

    public void setIntake(double n) {
        intake.set(ControlMode.PercentOutput, n);
    }

    // public void setHatchGuide(boolean b) {
    //     hatchGuide.set(b);
    // }    
}