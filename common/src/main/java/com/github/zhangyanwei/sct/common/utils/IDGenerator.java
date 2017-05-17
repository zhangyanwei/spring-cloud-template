package com.github.zhangyanwei.sct.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.Integer.parseInt;
import static java.lang.Integer.toUnsignedLong;
import static java.lang.System.getProperty;
import static java.lang.System.getenv;
import static java.time.temporal.ChronoField.INSTANT_SECONDS;

/**
 * SERVER * 16 + SEQ * 16 + TIME * 32
 */
public class IDGenerator {

    private static final Logger logger = LoggerFactory.getLogger(IDGenerator.class);

    private static final IDGenerator instance = new IDGenerator();
    private long defaultTime;
    private int machineId;
    private int sequence = 0;
    private long seconds;

    {
        try {
            defaultTime = new SimpleDateFormat("yyyy-MM-dd").parse("2016-12-01").toInstant().getLong(INSTANT_SECONDS);
            machineId = parseInt(getProperty("machine.id", getenv("machine.id")));
        } catch (ParseException ignore) {}
    }

    private void calculate() {
        long now = currentSeconds();
        if (now == seconds) {
            if (++sequence > 0xFFFF) {
                logger.warn("sequence overflow, waiting.");
                try {
                    wait(100);
                    calculate();
                } catch (InterruptedException ignore) {}
            }
        } else {
            seconds = now;
            sequence = 0;
        }
    }

    private long currentSeconds() {
        return new Date().toInstant().getLong(INSTANT_SECONDS) - defaultTime;
    }

    private synchronized long generate() {
        checkArgument(0xFFFFFF >= machineId, "machine id overflow");
        calculate();
        long result = 0;
        result = result | (toUnsignedLong(machineId) << 48);
        result = result | (toUnsignedLong(sequence) << 32);
        result = result | seconds;
        return result;
    }

    public static long next() {
        return instance.generate();
    }

}
