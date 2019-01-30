/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.  
/* @author: Bicycle                                                             */
/*----------------------------------------------------------------------------*/

package frc.robot;

import frc.controllers.XBoxController;
import frc.controllers.JoystickController;
import frc.robot.RobotMap;
import frc.networking.RemoteOutput;
import frc.util.ClockRegulator;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.Encoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

/**
 * Make Robo Go
 **/
public class Robot extends TimedRobot {

    public static RemoteOutput nBroadcaster;
    private CANSparkMax motorWrist;
    private CANSparkMax motorElbow;
    private JoystickController Joy;
    private XBoxController Xbox;

    @Override
    public void robotInit() {
        nBroadcaster = new RemoteOutput("10.51.99.27", 5800);
        Robot.nBroadcaster.println("Starting up...");
        Xbox = new XBoxController(0);
        motorWrist = new CANSparkMax(RobotMap.wristMotor, MotorType.kBrushless);
        motorElbow = new CANSparkMax(RobotMap.elbowMotor, MotorType.kBrushless);
    }

    @Override
    public void teleopPeriodic() {
        //goal: move the arm in a way that is not dumb
        // wrist
        if (Math.abs(Xbox.getStickRY()) > 0) {
            motorWrist.set(Xbox.getStickRY());
        }
        // elbow
        if (Math.abs(Xbox.getStickLY()) > 0) {
            motorElbow.set(Xbox.getStickLY());
        }
    }
}
