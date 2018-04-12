package org.openhab.binding.mqttcontrol.internal;

import org.eclipse.smarthome.core.library.types.DateTimeType;
import org.eclipse.smarthome.core.library.types.DecimalType;
import org.eclipse.smarthome.core.library.types.HSBType;
import org.eclipse.smarthome.core.library.types.IncreaseDecreaseType;
import org.eclipse.smarthome.core.library.types.NextPreviousType;
import org.eclipse.smarthome.core.library.types.OnOffType;
import org.eclipse.smarthome.core.library.types.OpenClosedType;
import org.eclipse.smarthome.core.library.types.PercentType;
import org.eclipse.smarthome.core.library.types.PlayPauseType;
import org.eclipse.smarthome.core.library.types.PointType;
import org.eclipse.smarthome.core.library.types.RewindFastforwardType;
import org.eclipse.smarthome.core.library.types.StopMoveType;
import org.eclipse.smarthome.core.library.types.StringType;
import org.eclipse.smarthome.core.library.types.UpDownType;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.core.types.RefreshType;

public class CommandConverter {

    public static Command convertCommand(String commandtype, String command) {
        commandtype = commandtype.toLowerCase();
        command = command.toLowerCase();
        System.out.println(commandtype);
        if (commandtype.equals("onoff")) {
            return getOnOffType(command);
        } else if (commandtype.equals("percent")) {
            return getPercentType(command);
        } else if (commandtype.equals("decimal")) {
            return getDecimalType(command);
        } else if (commandtype.equals("updown")) {
            return getUpDownType(command);
        } else if (command.equals("datetime")) {
            return getDateTimeType(command);
        } else if (command.equals("hsb")) {
            return getHSBType(command);
        } else if (command.equals("string")) {
            return getStringType(command);
        } else if (command.equals("stopmove")) {
            return getStopMoveType(command);
        } else if (command.equals("rewindfastforward")) {
            return getRewindFastForwardType(command);
        } else if (command.equals("refresh")) {
            getRefreshType(command);
        } else if (command.equals("point")) {
            return getPointType(command);
        } else if (command.equals("playpause")) {
            return getPlayPauseType(command);
        } else if (command.equals("openclosed")) {
            return getOpenedClosedType(command);
        } else if (command.equals("nextprevious")) {
            return getNextPrevoiusType(command);
        } else if (command.equals("increasedecrease")) {
            return getIncreaseDecreaseType(command);
        }
        return null;
    }

    private static StringType getStringType(String command) {
        return new StringType(command);
    }

    private static StopMoveType getStopMoveType(String command) {
        if (command.equals("stop")) {
            return StopMoveType.STOP;
        } else if (command.equals("move")) {
            return StopMoveType.MOVE;
        } else {
            return null;
        }
    }

    private static RewindFastforwardType getRewindFastForwardType(String command) {
        if (command.equals("rewind")) {
            return RewindFastforwardType.REWIND;
        } else if (command.equals("fastforward")) {
            return RewindFastforwardType.FASTFORWARD;
        } else {
            return null;
        }
    }

    private static RefreshType getRefreshType(String command) {
        return RefreshType.REFRESH;
    }

    private static PointType getPointType(String command) {
        return new PointType(command);
    }

    private static PlayPauseType getPlayPauseType(String command) {
        if (command.equals("play")) {
            return PlayPauseType.PLAY;
        } else if (command.equals("pause")) {
            return PlayPauseType.PAUSE;
        } else {
            return null;
        }
    }

    private static OpenClosedType getOpenedClosedType(String command) {
        if (command.equals("open")) {
            return OpenClosedType.OPEN;
        } else if (command.equals("closed")) {
            return OpenClosedType.CLOSED;
        } else {
            return null;
        }
    }

    private static NextPreviousType getNextPrevoiusType(String command) {
        if (command.equals("next")) {
            return NextPreviousType.NEXT;
        } else if (command.equals("previous")) {
            return NextPreviousType.PREVIOUS;
        } else {
            return null;
        }
    }

    private static IncreaseDecreaseType getIncreaseDecreaseType(String command) {
        if (command.equals("increase")) {
            return IncreaseDecreaseType.INCREASE;
        } else if (command.equals("decrease")) {
            return IncreaseDecreaseType.DECREASE;
        } else {
            return null;
        }
    }

    private static HSBType getHSBType(String command) {
        return new HSBType(command);
    }

    private static DateTimeType getDateTimeType(String date) {
        return new DateTimeType(date);
    }

    private static OnOffType getOnOffType(String command) {
        if (command.equals("on")) {
            return OnOffType.ON;
        } else if (command.equals("off")) {
            return OnOffType.OFF;
        } else {
            return null;
        }
    }

    private static PercentType getPercentType(String command) {
        return new PercentType(command);
    }

    private static DecimalType getDecimalType(String command) {
        return new DecimalType(command);
    }

    private static UpDownType getUpDownType(String command) {
        if (command.equals("up")) {
            return UpDownType.UP;
        } else if (command.equals("down")) {
            return UpDownType.DOWN;
        } else {
            return null;
        }
    }

}
