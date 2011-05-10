//
// HarleyDroid: Harley Davidson J1850 Data Analyser for Android.
//
// Copyright (C) 2010,2011 Stelian Pop <stelian@popies.net>
// Based on various sources, especially:
//	minigpsd by Tom Zerucha <tz@execpc.com>
//	AVR J1850 VPW Interface by Michael Wolf <webmaster@mictronics.de>
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program.  If not, see <http://www.gnu.org/licenses/>.
//

package org.harleydroid;

import java.util.concurrent.CopyOnWriteArrayList;

public class HarleyData {

	// raw values reported in the J1850 stream
	private int mRPM = 0;					// RPM in rotation/minute * 4
	private int mSpeed = 0;					// speed in mph * 200
	private int mEngineTemp = 0;			// XXX engine temperature in Fahrenheit
	private int mFuelGauge = 0;				// fuel gauge: 0 (empty) to 6 (full)
	private int mTurnSignals = 0;			// turn signals bitmap: 0x1=right, 0x2=left
	private boolean mNeutral = false;		// XXX boolean: in neutral
	private boolean mClutch = false;		// XXX boolean: clutch engaged
	private int mGear = 0;					// XXX current gear: 1 to 6
	private boolean mCheckEngine = false;	// XXX boolean: check engine
	private int mOdometer = 0;				// odometer tick (1 tick = 0.4 meters)
	private int mFuel = 0;					// fuel consumption tick (1 tick = 0.000040 liters)

	private int mResetOdometer = 0;

	private CopyOnWriteArrayList<HarleyDataListener> mListeners;

	public HarleyData() {
		mListeners = new CopyOnWriteArrayList<HarleyDataListener>();
	}

	public void addHarleyDataListener(HarleyDataListener l) {
		mListeners.add(l);
	}

	public void removeHarleyDataListener(HarleyDataListener l) {
		mListeners.remove(l);
	}

	// returns the rotations per minute
	public int getRPM() {
		return mRPM / 4;
	}

	public void setRPM(int rpm) {
		if (mRPM != rpm) {
			mRPM = rpm;
			for (HarleyDataListener l : mListeners)
				l.onRPMChanged(mRPM / 4);
		}
	}

	// returns the speed in mph
	public int getSpeedImperial() {
		return (mSpeed * 125) / (16 * 1609);
	}

	// returns the speed in km/h
	public int getSpeedMetric() {
		return mSpeed / 128;
	}

	public void setSpeed(int speed) {
		if (mSpeed != speed) {
			mSpeed = speed;
			for (HarleyDataListener l : mListeners) {
				l.onSpeedImperialChanged((mSpeed * 125) / (16 * 1609));
				l.onSpeedMetricChanged(mSpeed / 128);
			}
		}
	}

	// returns the temperature in F
	public int getEngineTempImperial() {
		return mEngineTemp;
	}

	// returns the temperature in C
	public int getEngineTempMetric() {
		return (mEngineTemp - 32) * 5 / 9;
	}

	public void setEngineTemp(int engineTemp) {
		if (mEngineTemp != engineTemp) {
			mEngineTemp = engineTemp;
			for (HarleyDataListener l : mListeners) {
				l.onEngineTempImperialChanged(mEngineTemp);
				l.onEngineTempMetricChanged((mEngineTemp - 32) * 5 / 9);
			}
		}
	}

	// returns the fuel gauge as 0 (empty) to 6 (full)
	public int getFuelGauge() {
		return mFuelGauge;
	}

	public void setFuelGauge(int fuelGauge) {
		if (mFuelGauge != fuelGauge) {
			mFuelGauge = fuelGauge;
			for (HarleyDataListener l : mListeners)
				l.onFuelGaugeChanged(mFuelGauge);
		}
	}

	// returns the turn signals bitmap: 0x1=right, 0x2=left
	public int getTurnSignals() {
		return mTurnSignals;
	}

	public void setTurnSignals(int turnSignals) {
		if (mTurnSignals != turnSignals) {
			mTurnSignals = turnSignals;
			for (HarleyDataListener l : mListeners)
				l.onTurnSignalsChanged(mTurnSignals);
		}
	}

	// returns the neutral clutch: true = in neutral
	public boolean getNeutral() {
		return mNeutral;
	}

	public void setNeutral(boolean neutral) {
		if (mNeutral != neutral) {
			mNeutral = neutral;
			for (HarleyDataListener l : mListeners)
				l.onNeutralChanged(mNeutral);
		}
	}

	// returns the clutch position: true = clutch engaged
	public boolean getClutch() {
		return mClutch;
	}

	public void setClutch(boolean clutch) {
		if (mClutch != clutch) {
			mClutch = clutch;
			for (HarleyDataListener l : mListeners)
				l.onClutchChanged(mClutch);
		}
	}

	// returns the current gear: 1 to 6
	public int getGear() {
		return mGear;
	}

	public void setGear(int gear) {
		if (mGear != gear) {
			mGear = gear;
			for (HarleyDataListener l : mListeners)
				l.onGearChanged(mGear);
		}
	}

	// returns the check engine light: true = on
	public boolean getCheckEngine() {
		return mCheckEngine;
	}

	public void setCheckEngine(boolean checkEngine) {
		if (mCheckEngine != checkEngine) {
			mCheckEngine = checkEngine;
			for (HarleyDataListener l : mListeners)
				l.onCheckEngineChanged(mCheckEngine);
		}
	}

	// returns the odometer in miles * 100
	public int getOdometerImperial() {
		return ((mOdometer - mResetOdometer) * 40) / 1609;
	}

	// returns the odometer in km * 100
	public int getOdometerMetric() {
		return (mOdometer - mResetOdometer) / 25;
	}

	public void setOdometer(int odometer) {
		if (mOdometer != odometer) {
			mOdometer = odometer;
			for (HarleyDataListener l : mListeners) {
				int o = mOdometer - mResetOdometer;
				l.onOdometerImperialChanged((o * 40) / 1609);
				l.onOdometerMetricChanged(o / 25);
			}
		}
	}

	public void resetOdometer() {
		mResetOdometer = mOdometer;
		for (HarleyDataListener l : mListeners) {
			l.onOdometerImperialChanged(0);
			l.onOdometerMetricChanged(0);
		}
	}

	// returns the fuel in fl oz
	public int getFuelImperial() {
		return (mFuel * 338) / 250000;
	}

	// returns the fuel in milliliters
	public int getFuelMetric() {
		return mFuel / 25;
	}

	public void setFuel(int fuel) {
		if (mFuel != fuel) {
			mFuel = fuel;
			for (HarleyDataListener l : mListeners) {
				l.onFuelImperialChanged((mFuel * 338) / 250000);
				l.onFuelMetricChanged(mFuel / 25);
			}
		}
	}

	public void setBadCRC(byte[] buffer) {
		for (HarleyDataListener l : mListeners)
			l.onBadCRCChanged(buffer);
	}

	public void setUnknown(byte[] buffer) {
		for (HarleyDataListener l : mListeners)
			l.onUnknownChanged(buffer);
	}

	public String toString() {
		String ret;

		ret = "RPM:" + mRPM / 4;
		ret += " SPD:" + mSpeed / 128;
		ret += " ETP:" + mEngineTemp;
		ret += " FGE:" + mFuelGauge;
		ret += " TRN:";
		if ((mTurnSignals & 0x3) == 0x3)
			ret += "W";
		else if ((mTurnSignals & 0x1) != 0)
			ret += "R";
		else if ((mTurnSignals & 0x2) != 0)
			ret += "L";
		else
			ret += "x";
		ret += " CLU/NTR:";
		if (mNeutral)
			ret += "N";
		else
			ret += "x";
		if (mClutch)
			ret += "C";
		else
			ret += "x";
		if (mGear > 0 && mGear < 7)
			ret += mGear;
		else
			ret += "x";
		ret += " CHK:" + mCheckEngine;
		ret += " ODO:" + mOdometer;
		ret += " FUL:" + mFuel;
		return ret;
	}
};
