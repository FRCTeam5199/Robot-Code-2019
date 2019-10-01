package frc.armevator;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.controllers.ButtonPanel;
import frc.controllers.JoystickController;
import frc.interfaces.LoopModule;
import frc.motion.Interpolator;
// import frc.util.Vector2;

public class ArmevatorControl implements LoopModule {

    private final JoystickController Joy;
    private final ButtonPanel panel;

    private final Armevator arm;

    private double eleTarget = 0;
    private double eleMotorPos;
    // The max height of the elevator is in arbitrary units
    private double eleMaxHeight = -32.5; 
    //comp bot ele height ^
    // private double eleMaxHeight = -20.5;
    //practice bot ele height ^
    // ele motor is negative cause its reversed
    private double elbowAngle = 0;
    private double elbowTarget = 0;
    private double elbowMinAngle = 34;
    private double elbowMaxAngle = 170;
    private double wristAngle = 0;
    private double wristTarget = 0;
    private double wristMinAngle = 0;
    private double wristMaxAngle = 0;
    // measure wrist mins and maxs for manual movement
    private double hatMod = 0;
    public double epos, wpos, elpos;
    private Interpolator eleInterpolator, elbowInterpolator, wristInterpolator;
    private long lastTime;
    // private double[] cargo1,cargo2,cargo3,intake,hatch1,hatch2,hatch3,travel;
    // private Vector2 armOffset = new Vector2(12.125, 20.5);
    // private Vector2 stow, ground, hatch1, hatch2, hatch3, cargoship, cargo1,
    // cargo2, cargo3, max;
    // private boolean start;

    public ArmevatorControl(Armevator arm, JoystickController Joy, ButtonPanel panel) {
        this.arm = arm;
        this.Joy = Joy;
        this.panel = panel;

        eleInterpolator = new Interpolator(5);
        elbowInterpolator = new Interpolator(10);
        wristInterpolator = new Interpolator(10);

    }

    @Override
    public void init() {
        arm.setArmBreak();
        // arm.setArmCoast();
        arm.setArmCurrentMax();
        hatMod = 0;
        elbowAngle = arm.getElbowPosition();
        elbowTarget = elbowAngle;
        wristAngle = arm.getWristPosition();
        wristTarget = wristAngle;
        // epos = 0;
        // wpos = 0;
        // elpos = 0;
        lastTime = System.currentTimeMillis();

        arm.enableArmPID();
        //arm.disableArmPID();
        //arm.disableElePID();
        // !!!
    }

    private void findArmPositions() {
        SmartDashboard.putNumber("Elbow Angle", elbowAngle);
        SmartDashboard.putNumber("Wrist Angle", wristAngle);
        SmartDashboard.putNumber("Elevator Height", eleMotorPos);
        SmartDashboard.putNumber("Elevator Motor Rotations", eleMotorPos);
        SmartDashboard.putNumber("Elevator Target", elpos);
        eleMotorPos = arm.getElePosition();
        elbowAngle = arm.getElbowPosition();
        wristAngle = arm.getWristPosition();
    }

    private void armInterpolator(double e, double w) {
        elbowInterpolator.init(arm.getElbowPosition(), e);
        wristInterpolator.init(arm.getWristPosition(), w);
    }

    private void eleInterpolator(double d) {
        eleInterpolator.init(arm.getElePosition(), d);
    }

    private double angleIncrementer(double j) {
        if (System.currentTimeMillis() > lastTime + 10) {
            if (j > 0.1 && elbowTarget < elbowMaxAngle) {
                elbowTarget += 1;
            } else if (j < -0.1 && elbowTarget > elbowMinAngle) {
                elbowTarget -= 1;
            }
            lastTime = System.currentTimeMillis();
            elbowAngle = arm.getElbowPosition();
            wristAngle = arm.getWristPosition();
            return elbowTarget;
        } else {
            elbowAngle = arm.getElbowPosition();
            wristAngle = arm.getWristPosition();
            return arm.getElbowPosition();
        }
    }

    private void manualMove(double d) {
        if (elbowTarget < elbowMaxAngle && elbowTarget >= elbowMinAngle) {
            arm.setElbow(epos + d);
            arm.setWrist(wpos + -d + hatMod);
        }
    }

    private void wristIncrease() {
        if (Joy.getHat() == -1) {
            hatMod += 0;
        } else if (Joy.getHat() == 180) {
            hatMod++;
        } else if (Joy.getHat() == 0) {
            hatMod--;
        }
    }

    private void moveArmTo(double e, double w) {
        armInterpolator(e, w);
        elbowTarget = e;
        wristTarget = w;

    }

    private void moveEleTo(double d) {
        eleInterpolator(d);
        eleTarget = d;
    }

    public void exitStow() {
        moveEleTo(-15.7);
        //comp bot^
        // moveEleTo(-11.0);
        //practice bot ^
        //this lifts the elevator to move the arm out of its kickstand
    }

    public void deployArm(){ // deploy arm at start
        moveArmTo(ArmConstants.hatch1[0], ArmConstants.hatch1[1]);
    }

    @Override
    public void update(long delta) {
        arm.dumpEleCurrentValue();
        // findArmPositions();

        /* //manual arm movement
        if(Math.abs(Joy.getYAxis()) > 0 || Joy.getHat() != -1){
            manualMove(angleIncrementer(Joy.getYAxis())); 
            wristIncrease();
        }
        if(Joy.getButton(12)){
            if(elpos != -11){
                moveEleTo(-11);
            }
        }
        if(Joy.getButton(11)){
            if(elpos != 0){
                moveEleTo(0);
            }
        } */

        // manual ele movement
        /* if(Math.abs(Joy.getYAxis()) > 0){
            arm.moveEle(Joy.getYAxis());
        } */

        /* the most important code block v */
        epos = elbowInterpolator.get();
        wpos = wristInterpolator.get();
        elpos = eleInterpolator.get();
        arm.setElbow(epos);
        arm.setWrist(wpos);
        arm.setEle(elpos);
        //disabled for manual movement
        //System.out.println(epos);
        //System.out.println(wpos);
        //System.out.println(elpos);
        
        //**** try to move arms out after it exits the startup position */
        
        if (panel.getButton(9)) { //HATCH PORTAL/LEVEL 1 BUTTON
            if (elpos != 0) {
                moveEleTo(0);
            }
            moveArmTo(ArmConstants.hatch1[0], ArmConstants.hatch1[1]);
            panel.lastButton = 9;
        }
        // hatch1 ^
        if (panel.getButton(5)) {
            if (elpos != 0) {
                moveEleTo(0);
            }
            moveArmTo(ArmConstants.cargoIntake[0], ArmConstants.cargoIntake[1]);
            panel.lastButton = 5;
        }
        // ^cargo intake pos
        if (panel.getButton(6)) { //HATCH INTAKE BUTTON
            if (elpos != 0) {
                moveEleTo(0);
            }
            moveArmTo(ArmConstants.hatchIntake[0], ArmConstants.hatchIntake[1]);
            panel.lastButton = 6;
        }
        // ^ the hatch intake position, we arent doing ground pickup anymore so same as hatch1
        if (panel.getButton(3)) {
            if (elpos != 0) {
                moveEleTo(0);
            }
            moveArmTo(ArmConstants.cargo1[0], ArmConstants.cargo1[1]);
            panel.lastButton = 3;
        }
        //cargo 1^

        if (panel.getButton(4)) {
            moveArmTo(ArmConstants.cargoShip[0], ArmConstants.cargoShip[1]);
            if (elpos != 0) {
                moveEleTo(0);
            }
            panel.lastButton = 4;
        }
        //cargo ship ^

        if (panel.getButton(2)) {
            if(elpos != eleMaxHeight){
                moveEleTo(eleMaxHeight);
            }
            moveArmTo(ArmConstants.cargo2[0], ArmConstants.cargo2[1]);
            panel.lastButton = 2;

        }
        // cargo2^
        if (panel.getButton(1)) {
            if (elpos != eleMaxHeight) {
                moveEleTo(eleMaxHeight);
            }
            moveArmTo(ArmConstants.cargo3[0], ArmConstants.cargo3[1]);
            panel.lastButton = 1;
        }
        // cargo3^
        if (panel.getButton(8)) {
            if (elpos != eleMaxHeight) {
                moveEleTo(eleMaxHeight);
            }
            moveArmTo(ArmConstants.hatch2[0], ArmConstants.hatch2[1]);
            panel.lastButton = 8;
        }
        // hatch2^
        if (panel.getButton(7)) {
            moveArmTo(ArmConstants.hatch3[0], ArmConstants.hatch3[1]);
            if (elpos != eleMaxHeight) {
                moveEleTo(eleMaxHeight);
            }
            panel.lastButton = 7;
        }
        // hatch3^
        if(panel.getButton(14)){
            if(elpos != -11.0){
                moveEleTo(-11.0); 
            }
            moveArmTo(0, 0);
            panel.lastButton = 14;

        }
        // "drive" position/ stowed ^
/*
        if (Joy.getButton(7)) { 
            moveArmTo(ArmConstants.oow[0], ArmConstants.oow[1]);
        }

        // fish out of water^
        //SPECIAL POSITION FOR HATCH IN PRACTICE MODE, UNCOMMENT IF NEEDED
        */
/*
//       | v |   CONOR BRAKING JUNK   | v |
        if(Joy.getButtonDown(7)){
            arm.setArmCoast();
        }
        if(Joy.getButtonDown(8)){
            arm.setArmBreak();
        }
//       | ^ | END CONOR BRAKING JUNK | ^ |
*/



        if(Joy.getButton(9)){
            System.out.println("Elbow Pos: " + elbowAngle);
            System.out.println("Wrist Pos: " + wristAngle);
        }
        //print current angles
    }
}