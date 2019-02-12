/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.  
/* @author: Bicycle                                                             */
/*----------------------------------------------------------------------------*/

package frc.robot;

import frc.controllers.XBoxController;
import frc.drive.DriveControl;
import frc.controllers.JoystickController;
import frc.robot.RobotMap;
import frc.networking.RemoteOutput;
import frc.util.ClockRegulator;
import frc.arm.Arm;
import frc.arm.ArmControl;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Joystick;


/**
 * Make Robo Go
 **/
public class Robot extends TimedRobot {

    public static RemoteOutput nBroadcaster;
    private Arm arm;
    private ArmControl armControl;
    private DriveControl driveControl;
    private JoystickController Joy;
    private XBoxController Xbox;

    @Override
    public void robotInit() {
        nBroadcaster = new RemoteOutput("10.51.99.27", 5800);
        Robot.nBroadcaster.println("Starting up...");
        Xbox = new XBoxController(0);
        Joy = new JoystickController(1);
        arm = new Arm();

        armControl = new ArmControl(arm, Joy);
    }

    @Override
    public void teleopPeriodic() {
        
        ClockRegulator cl = new ClockRegulator(50);
        BigLoop bigLoop = new BigLoop(cl);

        bigLoop.add(armControl);
        bigLoop.add(driveControl);

        bigLoop.init();
        
        while (isEnabled() && isOperatorControl()) {
            bigLoop.update();
        }
        bigLoop.cleanUp();
    }
}
