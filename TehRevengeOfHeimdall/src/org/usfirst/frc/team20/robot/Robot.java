package org.usfirst.frc.team20.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.CANTalon.ControlMode;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	public static double lastCycleEncoderPosition;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	public void robotInit() {
		Motors Motor = new Motors();
		Motors.initi();

		Motors.trayMotor.changeControlMode(ControlMode.PercentVbus);
		
		Motors.elevatorSlaveThree.changeControlMode(ControlMode.Follower);
		Motors.elevatorSlaveThree.set(7);
		Motors.elevatorSlaveTwo.changeControlMode(ControlMode.Follower);
		Motors.elevatorSlaveThree.set(7);
		Motors.elevatorSlaveOne.changeControlMode(ControlMode.Follower);
		Motors.elevatorSlaveThree.set(7);
		
		// PID

		Motors.forksMotor.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		Motors.forksMotor.changeControlMode(CANTalon.ControlMode.Position);
		Motors.forksMotor.setPosition(0);
		Motors.forksMotor.setPID(OperatorControls.p, OperatorControls.i,
				OperatorControls.d);
		Motors.forksMotor.setCloseLoopRampRate(OperatorControls.ramp);
		Motors.forksMotor.enableControl();
	}

	/**
	 * This function is called periodically during autonomous
	 */
	public int counter = 0;

	public void autonomousPeriodic() {

		if (counter == 0) {
			double talCur = Motors.forksMotor.getOutputCurrent();
			Motors.forksMotor.set(-85000);
			OperatorControls.talFil = OperatorControls.talFil * .9 + talCur
					* .1;
			if (OperatorControls.talFil > 15) {
				Motors.forksMotor.setPosition(0);
				Motors.forksMotor.set(0);
				counter = 1;
			}
		}
	}

	@Override
	public void teleopInit() {
		lastCycleEncoderPosition = Motors.elevatorMaster.getPosition();
	}

	/**
	 * This function is called periodically during operator control
	 */
	public static boolean fieldCentric = true;
	public static boolean dPad = false;

	public void teleopPeriodic() {
		OperatorControls.opControls();
		
		if (Motors.driver.getRawButton(1)) {
			fieldCentric = !fieldCentric;
		}
		if (fieldCentric) {
			DriverControls.fieldDrive();
		} else {
			DriverControls.robotDrive();
		}
		
		

		counter = 0;

		SmartDashboard.putString("Elevator Master =", ""
				+ Motors.elevatorMaster.getOutputCurrent());
		SmartDashboard.putString("Elevator slave one =", ""
				+ Motors.elevatorSlaveOne.getOutputCurrent());
		SmartDashboard.putString("Elevator slave two =", ""
				+ Motors.elevatorSlaveTwo.getOutputCurrent());
		SmartDashboard.putString("Elevator slave three =", ""
				+ Motors.elevatorSlaveThree.getOutputCurrent());

		SmartDashboard.putString("Elevator Master ==", ""
				+ Motors.elevatorMaster.getOutputVoltage());
		SmartDashboard.putString("Elevator slave one ==", ""
				+ Motors.elevatorSlaveOne.getOutputVoltage());
		SmartDashboard.putString("Elevator slave two ==", ""
				+ Motors.elevatorSlaveTwo.getOutputVoltage());
		SmartDashboard.putString("Elevator slave three ==", ""
				+ Motors.elevatorSlaveThree.getOutputVoltage());

	}

	/**
	 * This function is called periodically during test mode
	 */
	public void testPeriodic() {

	}

}
