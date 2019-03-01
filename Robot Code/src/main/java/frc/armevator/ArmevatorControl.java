package frc.armevator;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.controllers.ButtonPanel;
import frc.controllers.JoystickController;
import frc.interfaces.LoopModule;
import frc.motion.Interpolator;

import frc.util.Vector2;

import com.revrobotics.ControlType;

public class ArmevatorControl implements LoopModule {

    private final JoystickController Joy;
    private final ButtonPanel panel;

    private final Armevator arm;

    //
    private double elePos = 0;
    private double eleTarget = 0;
    private double eleMotorPos;
    // eleTop : -31.053, loss @ bottom: 0.622-0.854
    // The max height of the elevator is in arbitrary units rn, something is wrong
    // with the eleRatio math or the gearing
    private double eleMaxHeight = -31;
    // ele motor is negative cause its reversed
    private double elbowAngle = 0;
    private double elbowTarget = 0;
    private double elbowMinAngle = 0;
    // this number is a guess
    private double elbowMaxAngle = 130;
    private double wristAngle = 0;
    private double wristTarget = 0;
    private double wristMinAngle = 0;
    // measure this to be @ somewhere & then implement a softstop
    private double wristMaxAngle = 0;
    //
    private double hatMod = 0;
    // so = stupid offset
    private double soW = -92.6658478;
    private double soE = 27.047672;
    double epos, wpos, elpos;
    //
    private boolean done;
    // ****
    private boolean stowed = false;
    // **** 
    private Interpolator eleInterpolator, elbowInterpolator, wristInterpolator, elbowDownInterpolator;
    // private double[] cargo1,cargo2,cargo3,intake,hatch1,hatch2,hatch3,travel;

    private long lastTime;
    // private Vector2 armOffset = new Vector2(12.125, 20.5);
    // private Vector2 stow, ground, hatch1, hatch2, hatch3, cargoship, cargo1,
    // cargo2, cargo3, max;

    public ArmevatorControl(Armevator arm, JoystickController Joy, ButtonPanel panel) {
        this.arm = arm;
        this.Joy = Joy;
        this.panel = panel;

        eleInterpolator = new Interpolator(3);
        elbowInterpolator = new Interpolator(10);
        //elbowDownInterpolator = new Interpolator(5);
        //ff solves this instead
        wristInterpolator = new Interpolator(10);

    }

    @Override
    public void init() {
        arm.encoderReset();
        done = false;
        hatMod = 0;
        elbowAngle = arm.getElbowPosition();
        elbowTarget = elbowAngle;
        wristAngle = arm.getWristPosition();
        wristTarget = wristAngle;
        epos = 0;
        wpos = 0;
        elePos = arm.getElePosition();
        lastTime = System.currentTimeMillis();

        arm.enableArmPID();
        // arm.disableArmPID();
        // !!!
        if (!stowed) {
            soE = 0;
            soW = 0;
        }
        if (stowed) {
            this.exitStow();
            soE = -92.6658478;
            soW = 27.047672;
        }

    }

    private void findArmPositions() {
        SmartDashboard.putNumber("Elbow Angle", elbowAngle);
        SmartDashboard.putNumber("Wrist Angle", wristAngle);
        SmartDashboard.putNumber("Elevator Height", elePos);
        SmartDashboard.putNumber("Elevator Motor Rotations", eleMotorPos);

        elePos = arm.getElePosition();
        eleMotorPos = arm.getEleMotorPos();
        elbowAngle = arm.getElbowPosition();
        wristAngle = arm.getWristPosition();
    }

    private void armInterpolator(double e, double w) {
        elbowInterpolator.init(arm.getElbowPosition(), e);
        wristInterpolator.init(arm.getWristPosition(), w);
    }

    private void elbowDownInterpolator(double e) {
        elbowDownInterpolator.init(arm.getElbowPosition(), e);
    }

    private void eleInterpolator(double d) {
        eleInterpolator.init(arm.getElePosition(), d);
    }
    // combine these two later

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
        /* double epos = elbowInterpolator.get();
        double wpos = wristInterpolator.get();
        arm.setElbow(epos);
        arm.setWrist(wpos); */
    }

    private void moveEleTo(double d) {
        eleInterpolator(d);
        eleTarget = d;
        /* elpos = eleInterpolator.get();
        arm.setEle(elpos); */
    }

    // connect them

    private void exitStow() {
        moveEleTo(-5);
        if (System.currentTimeMillis() > lastTime + 20) {
            moveArmTo(15.85718 + soE, 0.5714277 + soW);
            done = true;
            lastTime = System.currentTimeMillis();
        }
        if (done && System.currentTimeMillis() > lastTime + 20) {
            moveArmTo(15.85718 + soE, 0.5714277 + soW);
            moveEleTo(-1.5);
        }
    }

    @Override
    public void update(long delta) {
        
        /*
         * if(Math.abs(Joy.getYAxis()) > 0 || Joy.getHat() != -1){
         * this.manualMove(angleIncrementer(Joy.getYAxis())); this.wristIncrease(); }
         */
    
        /* //temp manual ele movement
        if(Math.abs(Joy.getYAxis()) > 0){
            arm.moveEle(Joy.getYAxis());
        }
 */
        this.findArmPositions();

        epos = elbowInterpolator.get();
        wpos = wristInterpolator.get();
        elpos = eleInterpolator.get();
        arm.setElbow(epos);
        arm.setWrist(wpos);
        arm.setEle(elpos);
        // System.out.println(epos);
        // System.out.println(wpos);
        
        //FOR THE REAL BOT: ele maxheight is ~-36.5 for some weird reason, gearing must be different?
        if (Joy.getButton(9)) {
            if (!(elePos < -1.75 && elePos > -1.25)) {
                moveEleTo(-1.5);
            }
            
            moveArmTo(47.3806 + soE, 44.1902 + soW);
            panel.lastButton = 9;
        }
        // hatch1 ^
        if (panel.getButton(5)) {
            if (!(elePos < -1.75 && elePos > -1.25)) {
                moveEleTo(-1.5);
            }
            moveArmTo(15.85718 + soE, 0.5714277 + soW);
            panel.lastButton = 5;
        }
        // ^float/cargo intake pos, ~1 in off the ground with a bit of a wrist tilt down
        if (panel.getButton(6)) {
            if (!(elePos < -1.75 && elePos > -1.25)) {
                // moveEleTo(-1.5);
                moveEleTo(0);
            }
            moveArmTo(21.28582 + soE, -7.0476122 + soW);
            panel.lastButton = 6;
        }
        // ^ the hatch intake position <~.5in off the ground ;; also needs to reverse
        // intake rollers [<-- done]
        if (panel.getButton(3)) {
            if (!(elePos < -1.75 && elePos > -1.25)) {
                moveEleTo(-1.5);
            }
            moveArmTo(68.713668 + soE, -18.47626 + soW);
            panel.lastButton = 3;
        }
        // cargo1^
        if (panel.getButton(2)) {
            if (!(elePos < -1.75 && elePos > -1.25)) {
                moveEleTo(-1.5);
            }
            moveArmTo(128.0 + soE, -55.428165 + soW);
            panel.lastButton = 2;
        }
        // cargo2^
        if (panel.getButton(1)) {
            if (!(elePos < -21.5 && elePos > -20.5)) {
                moveEleTo(-21);
            }
            moveArmTo(127.3835 + soE, -71.9 + soW);
            panel.lastButton = 1;
        }
        // cargo3^
        if (panel.getButton(8)) {
            if (!(elePos < -21.5 && elePos > -20.5)) {
                moveEleTo(-21);
            }
            moveArmTo(29.76192 + soE, 61.42806 + soW);
            panel.lastButton = 8;
        }
        // hatch2^
        if (panel.getButton(7)) {
            moveArmTo(120.43 + soE, -17.8572 + soW);
            if (!(elePos < -21.5 && elePos > -20.5)) {
                moveEleTo(-21);
            }
            panel.lastButton = 7;
        }
        // hatch3^

        //
        if (Joy.getButton(11)) {
            System.out.println("Elbow angle: " + elbowAngle);
            System.out.println("Wrist angle" + wristAngle);
        }
        //

    }
}