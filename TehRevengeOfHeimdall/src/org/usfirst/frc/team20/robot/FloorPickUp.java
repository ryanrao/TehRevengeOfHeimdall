package org.usfirst.frc.team20.robot;

import edu.wpi.first.wpilibj.CANTalon.ControlMode;

public class FloorPickUp {

	// TODO
	public void groundGet(int eleEnc) {

		boolean yep = false;

		Motors.elevatorMaster.changeControlMode(ControlMode.Position);
		Motors.forksMotor.changeControlMode(ControlMode.Position);

		Motors.forksMotor.set(0);
		if (Motors.forksMotor.getOutputCurrent() > 15) {
			Motors.forksMotor.set(0);
		}

		while (Motors.elevatorMaster.get() != 100) {
			Motors.elevatorMaster.set(100);
		}
		if (Motors.elevatorMaster.get() > 20
				|| Motors.elevatorMaster.get() < 110) {

			Motors.elevatorMaster.set(Motors.elevatorMaster.getPosition());
			
			Motors.forksMotor.changeControlMode(ControlMode.PercentVbus);

			if (Motors.forksMotor.getOutputCurrent() < 15) {
				Motors.forksMotor.set(-1);
				Motors.rollersLeft.set(-1);
				Motors.rollersRight.set(1);
			} else if (Motors.forksMotor.getOutputCurrent() > 15) {
				Motors.forksMotor.changeControlMode(ControlMode.Position);
				Motors.forksMotor.set(Motors.forksMotor.getPosition());
				Motors.rollersLeft.set(0);
				Motors.rollersRight.set(0);
				yep = true;
			}
		}
		if (yep) {
			Motors.elevatorMaster.changeControlMode(ControlMode.Position);
			Motors.elevatorMaster.set(eleEnc);
		}
	}
}
