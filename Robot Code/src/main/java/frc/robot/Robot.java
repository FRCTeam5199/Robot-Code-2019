/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.  
/* @author: Bicycle                                                             */
/*----------------------------------------------------------------------------*/

package frc.robot;

import frc.controllers.XBoxController;
import frc.vision.Camera;
import frc.drive.DriveBase;
import frc.drive.DriveControl;
import frc.grabber.Grabber;
import frc.grabber.GrabberControl;
import frc.lift.Lift;
import frc.lift.LiftControl;
import frc.controllers.ButtonPanel;
import frc.controllers.JoystickController;
import frc.robot.RobotMap;
import frc.networking.RemoteOutput;
import frc.util.ClockRegulator;
import frc.arm.Arm;
import frc.arm.ArmControl;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


/**
 * Make Robo Go
 **/
public class Robot extends TimedRobot {

    public static RemoteOutput nBroadcaster;
    public static Camera camera;
    private Arm arm;
    private Grabber grabber;
    private Lift lift;
    private DriveBase base;
    private ArmControl armControl;
    private GrabberControl grabberControl;
    private LiftControl liftControl;
    private DriveControl driveControl;
    private JoystickController Joy;
    private ButtonPanel panel;
    private XBoxController Xbox;

    @Override
    public void robotInit() {
        nBroadcaster = new RemoteOutput("10.51.99.27", 5800);
        Robot.nBroadcaster.println("Starting up...");
        camera = new Camera();
        Xbox = new XBoxController(0);
        Joy = new JoystickController(1);
        panel = new ButtonPanel(2);
        arm = new Arm();
        grabber = new Grabber();
        lift = new Lift();
        base = new DriveBase();

        armControl = new ArmControl(arm, Joy, panel);
        grabberControl = new GrabberControl(grabber, Joy);
        liftControl = new LiftControl(lift, Joy);
        driveControl = new DriveControl(base, Xbox);
    }

    @Override
    public void teleopPeriodic() {
        
        ClockRegulator cl = new ClockRegulator(50);
        BigLoop bigLoop = new BigLoop(cl);

        bigLoop.add(armControl);
        bigLoop.add(grabberControl);
        bigLoop.add(liftControl);
        bigLoop.add(driveControl);

        bigLoop.init();
        
        while (isEnabled() && isOperatorControl()) {
            bigLoop.update();
        }
        bigLoop.cleanUp();
    }
}
