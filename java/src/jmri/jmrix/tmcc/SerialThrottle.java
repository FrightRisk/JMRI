package jmri.jmrix.tmcc;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jmri.DccLocoAddress;
import jmri.LocoAddress;
import jmri.SpeedStepMode;
import jmri.jmrix.AbstractThrottle;

/**
 * An implementation of DccThrottle.
 * <p>
 * Addresses of 99 and below are considered short addresses, and over 100 are
 * considered long addresses.
 *
 * @author	Bob Jacobsen Copyright (C) 2001, 2006
 */
public class SerialThrottle extends AbstractThrottle {

    /**
     * Constructor.
     *
     * @param memo the connected SerialTrafficController
     * @param address Loco ID
     */
    public SerialThrottle(TmccSystemConnectionMemo memo, DccLocoAddress address) {
        super(memo);
        tc = memo.getTrafficController();

        // cache settings. It would be better to read the
        // actual state, but I don't know how to do this
        this.speedSetting = 0;
        // Functions default to false
        this.address = address;
        this.isForward = true;
        this.speedStepMode = SpeedStepMode.TMCC_32;
    }

    private DccLocoAddress address;
    SerialTrafficController tc;

    @Override
    public LocoAddress getLocoAddress() {
        return address;
    }

    @Override
    public void setF0(boolean f0) {
        updateFunction(0, f0);
        // aux 2
        sendToLayout(0x000D + address.getNumber() * 128);
    }

    @Override
    public void setF1(boolean f1) {
        updateFunction(1, f1);
        // bell
        sendToLayout(0x001D + address.getNumber() * 128);
    }

    @Override
    public void setF2(boolean f2) {
        updateFunction(2, f2);
        // horn/whistle 1
        sendToLayout(0x001C + address.getNumber() * 128);
    }

    @Override
    public void setF3(boolean f3) {
        updateFunction(3, f3);
        // front coupler
        sendToLayout(0x0005 + address.getNumber() * 128);
    }

    @Override
    public void setF4(boolean f4) {
        updateFunction(4, f4);
        // back coupler
        sendToLayout(0x0006 + address.getNumber() * 128);
    }

    @Override
    public void setF5(boolean f5) {
        updateFunction(5, f5);
        // 0
        sendToLayout(0x0010 + address.getNumber() * 128);
    }

    @Override
    public void setF6(boolean f6) {
        updateFunction(6, f6);
        // 1
        sendToLayout(0x0011 + address.getNumber() * 128);
    }

    @Override
    public void setF7(boolean f7) {
        updateFunction(7, f7);
        // 2
        sendToLayout(0x0012 + address.getNumber() * 128);
    }

    @Override
    public void setF8(boolean f8) {
        updateFunction(8, f8);
        // 3
        sendToLayout(0x0013 + address.getNumber() * 128);
    }

    @Override
    public void setF9(boolean f9) {
        updateFunction(9, f9);
        // 4
        sendToLayout(0x0014 + address.getNumber() * 128);
    }

    @Override
    public void setF10(boolean f10) {
        updateFunction(10, f10);
        // 5
        sendToLayout(0x0015 + address.getNumber() * 128);
    }

    @Override
    public void setF11(boolean f11) {
        updateFunction(11, f11);
        // 6
        sendToLayout(0x0016 + address.getNumber() * 128);
    }

    @Override
    public void setF12(boolean f12) {
        updateFunction(12, f12);
        // 7
        sendToLayout(0x0017 + address.getNumber() * 128);
    }

    @Override
    public void setF13(boolean f13) {
        updateFunction(13, f13);
        // 8
        sendToLayout(0x0018 + address.getNumber() * 128);
    }

    @Override
    public void setF14(boolean f14) {
        updateFunction(14, f14);
        // 9
        sendToLayout(0x0019 + address.getNumber() * 128);
    }

    @Override
    public void setF15(boolean f15) {
        updateFunction(15, f15);
        // aux 1
        sendToLayout(0x0009 + address.getNumber() * 128);
    }

    @Override
    public void setF16(boolean f16) {
        updateFunction(16, f16);
        // letoff sound
        sendToLayout(0x001E + address.getNumber() * 128);
    }

    @Override
    public void setF17(boolean f17) {
        updateFunction(17, f17);
        // forward direction
        sendToLayout(0x0000 + address.getNumber() * 128);
    }

    @Override
    public void setF18(boolean f18) {
        updateFunction(18, f18);
        // reverse direction
        sendToLayout(0x0003 + address.getNumber() * 128);
    }

    @Override
    public void setF19(boolean f19) {
        updateFunction(19, f19);
        // toggle direction
        sendToLayout(0x0001 + address.getNumber() * 128);
    }

    @Override
    public void setF20(boolean f20) {
        updateFunction(20, f20);
        // boost
        sendToLayout(0x0004 + address.getNumber() * 128);
    }

    @Override
    public void setF21(boolean f21) {
        updateFunction(21, f21);
        // brake
        sendToLayout(0x0007 + address.getNumber() * 128);
    }

    /**
     * Set the speed.
     *
     * @param speed Number from 0 to 1; less than zero is emergency stop
     */
    @SuppressFBWarnings(value = "FE_FLOATING_POINT_EQUALITY") // OK to compare floating point, notify on any change
    @Override
    public void setSpeedSetting(float speed) {
        float oldSpeed = this.speedSetting;
        this.speedSetting = speed;
        int value = (int) (32 * speed);     // -1 for rescale to avoid estop
        if (value > 31) {
            value = 31;    // max possible speed
        }
        SerialMessage m = new SerialMessage();

        if (value < 0) {
            // immediate stop
            m.putAsWord(0x0060 + address.getNumber() * 128 + 0);
        } else {
            // normal speed setting
            m.putAsWord(0x0060 + address.getNumber() * 128 + value);
        }

        tc.sendSerialMessage(m, null);
        tc.sendSerialMessage(m, null);
        tc.sendSerialMessage(m, null);
        tc.sendSerialMessage(m, null);
        firePropertyChange(SPEEDSETTING, oldSpeed, this.speedSetting);
        record(speed);
    }

    @Override
    public void setIsForward(boolean forward) {
        boolean old = isForward;
        isForward = forward;

        // notify layout
        SerialMessage m = new SerialMessage();
        if (forward) {
            m.putAsWord(0x0000 + address.getNumber() * 128);
        } else {
            m.putAsWord(0x0003 + address.getNumber() * 128);
        }
        tc.sendSerialMessage(m, null);
        tc.sendSerialMessage(m, null);
        tc.sendSerialMessage(m, null);
        tc.sendSerialMessage(m, null);
        firePropertyChange(ISFORWARD, old, isForward);
    }

    protected void sendToLayout(int value) {
        tc.sendSerialMessage(new SerialMessage(value), null);
        tc.sendSerialMessage(new SerialMessage(value), null);
        tc.sendSerialMessage(new SerialMessage(value), null);
        tc.sendSerialMessage(new SerialMessage(value), null);
    }

    /*
     * Set the speed step value.
     * <p>
     * Only 32 steps is available
     *
     * @param Mode ignored, as only 32 is valid
     */
    @Override
    public void setSpeedStepMode(jmri.SpeedStepMode Mode) {
    }

    @Override
    protected void throttleDispose() {
        finishRecord();
    }

}
