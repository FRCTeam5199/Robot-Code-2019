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
import frc.armevator.Armevator;
import frc.armevator.ArmevatorControl;
import frc.vision.Limelight;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


/**
 * Make Robo Go, needs a rework to stop loop overrun
 **/
public class Robot extends TimedRobot {

    public static RemoteOutput nBroadcaster;
    public static Camera camera;
    public static Limelight limey;
    private Armevator arm;
    private Grabber grabber;
    private Lift lift;
    private DriveBase base;
    private ArmevatorControl armControl;
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
        // camera = new Camera();
        //no longer using usb cam plugged into RIO
        Xbox = new XBoxController(0);
        Joy = new JoystickController(1);
        panel = new ButtonPanel(2);
        arm = new Armevator();
        grabber = new Grabber();
        lift = new Lift();
        base = new DriveBase();
        limey = new Limelight();

        armControl = new ArmevatorControl(arm, Joy, panel);
        grabberControl = new GrabberControl(grabber, Joy, panel);
        liftControl = new LiftControl(lift, panel);
        driveControl = new DriveControl(base, Xbox);

        base.aimLightsOff();

    }

    /* @Override
    public void teleopInit() {

    } */

    @Override
    public void teleopPeriodic() {
        
        ClockRegulator cl = new ClockRegulator(50);
        BigLoop bigLoop = new BigLoop(cl);

        bigLoop.add(armControl);
        bigLoop.add(grabberControl);
        bigLoop.add(liftControl);
        bigLoop.add(driveControl);

        bigLoop.init();
        
        while (isEnabled() && (isOperatorControl() || isAutonomous())) {
            bigLoop.update();
            
        }
        bigLoop.cleanUp();
    }

    @Override
    public void autonomousInit() {
        arm.encoderReset();
        grabber.setGrabber(true);
        grabberControl.hasHatch = true;
        armControl.exitStow();
        base.gearChange(false);
    }
    //init 1 time inits from here, iterative robot shouldn't be set up like this to avoid loop overruns, just making do

    @Override
    public void autonomousPeriodic() {
        teleopPeriodic();
    }

    @Override
    public void testInit(){
        arm.encoderReset();
        // grabber.setGrabber(true);
        // grabberControl.hasHatch = true;
        // armControl.exitStow();
    }

    @Override
    public void testPeriodic(){

        ClockRegulator cl = new ClockRegulator(50);
        BigLoop bigLoop = new BigLoop(cl);

        // bigLoop.add(liftControl);
        bigLoop.add(driveControl);
        // bigLoop.add(armControl);
        // bigLoop.add(grabberControl);
        bigLoop.init();
        
        while (isEnabled() && isTest()) {
            bigLoop.update();
            // limey.printValues();
            // camera angles^
        }
        bigLoop.cleanUp();
    }
}
